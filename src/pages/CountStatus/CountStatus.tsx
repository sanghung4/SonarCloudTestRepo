import React, { useEffect, useState, useMemo } from "react";
import { Banner } from "../../components/Banner";
import { useHistory, useLocation } from "react-router-dom";
import { getFormattedDate, getFormattedTime } from "../../utils/dates";
import { getEndDate, applyFilter } from "./utils";
import { StatusText } from "./StatusText";
import { CountWithStatus, RemoveBranchCountsInput, useGetCountsLazyQuery, useLoadCountMutation, useRemoveBranchCountsMutation } from "../../graphql";
import { Button } from "../../components/Button";
import { MdAdd } from "react-icons/md";
import { Modal } from "../../components/Modal";
import { Input } from "../../components/Input";
import { Dropdown, DropdownOption } from "../../components/Dropdown";
import { MdDeleteForever } from "react-icons/md";
import { useAuthContext } from "../../store/AuthProvider";
import { toast, Slide } from "react-toastify";

const CountStatus = () => {

  const { userInfo } = useAuthContext();

  const filOptions: DropdownOption[] = [
    { value: "ALL", display: "All Status" },
    { value: "COMPLETE", display: "Loaded" },
    { value: "NOT_LOADED", display: "Not loaded" },
    { value: "IN_PROGRESS", display: "In progress" }
  ]
  // Date Params
  const query = new URLSearchParams(useLocation().search);
  // ----------------------- //
  // ----- Hooks ----- //
  // ----------------------- //
  const history = useHistory();

  // ----------------------- //
  // ----- Memo Values ----- //
  // ----------------------- //
  const today = useMemo(() => {
    const date = new Date();
    date.setHours(0, 0, 0, 0);
    return date.toISOString().slice(0, 10);
  }, []);

  // ----------------- //
  // ----- State ----- //
  // ----------------- //
  const [countList, setCountList] = useState<CountWithStatus[]>([]);
  const [loadCountModel, setLoadCountModel] = useState<boolean>(false);
  const [archiveCountModel, setarchiveCountModel] = useState<boolean>(false);
  const [filteredList, setFilteredList] = useState<CountWithStatus[]>([]);
  const [search, setSearch] = useState<string>("");
  const [range, setRange] = useState({
    startDate: query.get('startDate') || today,
    endDate: query.get('endDate') || today,
    color: "#007ce6",
    key: "selection",
  });
  const [branchNo, setBranchNo] = useState<string>("");
  const [countId, setCountId] = useState<string>("");
  //TODO
  //Archive Counts
  const [archiveData, setArchiveData] = useState<any>([]);
  const [isAdmin, setIsAdmin] = useState<boolean>(false);

  const [selectedFilter, setSelectedFilter] = useState<DropdownOption>(
    filOptions.filter( elem => elem.display == query.get('countStatus'))[0] || filOptions[0]
  );
  const [totalProducts, setTotalProducts] = useState<number>(0);

  // --------------- //
  // ----- API ----- //
  // --------------- //

  //  GetCounts
  const [getCounts, { data, loading }] = useGetCountsLazyQuery();

  useEffect(() => {
    if (userInfo?.groups) {
      setIsAdmin(userInfo?.groups?.includes('WMS Admin - Mincron'))
    }
  }, [userInfo]);

  useEffect(() => {
    if (range.startDate && range.endDate) {
      fnGetCiounts(range)
    }
    if (!(query.get('startDate') == range.startDate) || !(query.get('endDate') == range.endDate)) {
      history.push(`/count?startDate=${range.startDate}&endDate=${range.endDate}&countStatus=${selectedFilter.display}`);
    }

  }, [range]);

  //  LoadCounts
  const [loadCounts, { loading: loadCountsLoading }] = useLoadCountMutation({
    onCompleted: (data) => {
      const { id } = data.loadCount;
      if (data.loadCount.errorMessage) {
        if (data.loadCount.errorMessage?.includes('KGW-500')) {
          toast.error(`This count has no products`, { transition: Slide });
        } else {
          toast.error(`This account has an error status and cannot be Loaded.`, { transition: Slide });
        }
      } else {
        history.push(`count/${id}/total/${totalProducts}`);
      }
    }, onError: (error) => {
      console.log(error)
    }
  });

  //removeBranchCounts
  const [removeBranchCounts, { loading: removeBranchCountsLoading }] = useRemoveBranchCountsMutation({
    onCompleted: (data) => {
      toast.success(`Success! Count ID archived.`, { transition: Slide });
      fnGetCiounts(range)
      setarchiveCountModel(false);
    }
  });

  const fnGetCiounts = (range: any) => {
    getCounts({
      variables: {
        input: {
          startDate: new Date(range.startDate).toISOString(),
          endDate: getEndDate(new Date(range.endDate)).toISOString(),
        },
      },
    });
  }

  // ------------------- //
  // ----- Effects ----- //
  // ------------------- //
  useEffect(() => {
    setCountList(data?.counts ?? []);
  }, [data]);

  useEffect(() => {
    handleResetLoadCountInput();
    console.log(branchNo + " - " + countId);
  }, []);

  useEffect(() => {
    setFilteredList(countList.filter(applyFilter(search)) ?? []);
    filterTableByStatus()
  }, [search, countList]);

  useEffect(() => {
    filterTableByStatus()
    history.push(`/count?startDate=${range.startDate}&endDate=${range.endDate}&countStatus=${selectedFilter.display}`);
  }, [selectedFilter]);

  const filterTableByStatus = () =>{
    if (selectedFilter.value === "ALL") {
      setFilteredList(countList);
    } else {
      setFilteredList(countList.filter(elem => elem.status == selectedFilter.value))
    }
  }
  // ------------------- //
  // ----- Actions ----- //
  // ------------------- //
  const handleRangeUpdate = (ranges: any) => {
    setRange(ranges.selection);
  };

  const handleSearchOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearch(e.target.value);
  };

  const handleBranchNoOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setBranchNo(e.target.value);
  };

  const handleCountIdOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCountId(e.target.value);
  };

  const handleConfirmLoadCountModel = (branchId: string, countId: string) => {
    loadCounts({ variables: { branchId, countId } });
    setLoadCountModel(false);
  }


  const handleCancelLoadCountModel = () => {
    setLoadCountModel(false);
  }
  const handleConfirmArchiveCountModel = () => {
    handleArchiveCount(archiveData)
  }
  const handleCancelArchiveCountModel = () => {
    setarchiveCountModel(false);
  }

  const handleResetLoadCountInput = () => {
    setBranchNo("");
    setCountId("");
  }

  const handleNotLoadedStatusClick = (count: CountWithStatus) => {
    setBranchNo(count.branchId);
    setCountId(count.countId);
    setTotalProducts(count.totalProducts ?? 0);
    setLoadCountModel(true);
  }

  const handleLoadCountButtonClick = () => {
    handleResetLoadCountInput();
    setLoadCountModel(true);
  }

  const handleArchiveCountButtonClick = (data: CountWithStatus) => {
    setArchiveData(data)
    setarchiveCountModel(true);
  }

  const setCalendarDefault = () => {
    const date = new Date(range.endDate);
    date.setDate(1);
    date.setHours(0, 0, 0, 0);
    return date.toISOString().slice(0, 10);
  }

  const handleDatePicker = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.name === 'start-date') {
      if (!e.target.value) {
        setRange({ ...range, startDate: (setCalendarDefault()) })
      } else {
        setRange({ ...range, startDate: (e.target.value) })
      }
    } else {
      if (!e.target.value) {
        setRange({ ...range, endDate: (today) })
      } else {
        setRange({ ...range, endDate: (e.target.value) })
      }
    }
  };

  const handleSelectBranch = (e: React.ChangeEvent<HTMLInputElement>, data: any) => {
    if (e.target.checked) {
      setArchiveData([...archiveData, data])
    } else {
      setArchiveData(archiveData.filter((elem: any) => elem.branchId !== data.branchId))
    }
  };

  const handleChangeStatus = (selection: DropdownOption) => {
    setSelectedFilter(selection);
  };

  const handleArchiveCount = (data: CountWithStatus) => {
    const date = new Date(data.createdAt || new Date()).toISOString().split("T")
    const input: RemoveBranchCountsInput = {
      "branchId": data.branchId,
      "countId": data.countId,
    }
    removeBranchCounts({ variables: { input } });
  };

  return (
    <>
      <Banner header='Count Status' />
      <Modal
        open={loadCountModel}
        title="Load Count"
        onConfirm={() => {
          handleConfirmLoadCountModel(branchNo, countId);
        }}
        confirmDisabled={(branchNo == "" || countId == "") || loadCountsLoading}
        confirmText="Submit"
        onClose={() => {
          handleCancelLoadCountModel();
        }}
      >
        <div className="grid grid-cols-2 my-5 px-2 gap-4">
          <div className="col-span-1">
            <Input
              label='Branch No.'
              placeholder='1158'
              value={branchNo}
              onChange={
                handleBranchNoOnChange
              }
            />
          </div>
          <div className="col-span-1">
            <Input
              label='Count ID'
              placeholder='13272'
              value={countId}
              onChange={
                handleCountIdOnChange
              }
            />
          </div>
        </div>
      </Modal>
      {/* archiveCountModel */}
      <Modal
        open={archiveCountModel}
        title="Are you sure you want to archive?"
        onConfirm={() => {
          handleConfirmArchiveCountModel();
        }}
        confirmText={removeBranchCountsLoading == true ? 'Loading..' : 'Confirm'}
        confirmDisabled={removeBranchCountsLoading}
        onClose={() => {
          handleCancelArchiveCountModel();
        }}
      >
        <div className="grid grid-cols-2 my-5 px-2 gap-4">
          <p> This count ID will no longer be available </p>
        </div>
      </Modal>
      <div className='flex items-center justify-center'>
        <div className='w-full'>
          <div className='grid grid-cols-3 gap-8 mx-5 mb-5 '>
            <div className='col-span-3 md:col-span-6 xl:col-span-8 2xl:col-span-9 xl:mt-10'>
              <div className='w-full mb-5 col-span-3'>
                <div className="grid grid-cols-2 mb-5" >
                  <div className="flex">
                    <div className="">
                      <Dropdown
                        label='Status'
                        textSize='large'
                        options={filOptions}
                        selected={selectedFilter}
                        setSelected={handleChangeStatus}
                      />
                    </div>
                    {/* Start Date */}
                    <div className="ml-5">
                      <Input
                        type={"date"}
                        label='Start Date'
                        name="start-date"
                        value={range.startDate}
                        onClick={(e: any) => { e.target.showPicker() }}
                        onChange={
                          handleDatePicker
                        }
                      />
                    </div>
                    {/* End Date */}
                    <div className="ml-5">
                      <Input
                        type={"date"}
                        label='End Date'
                        name="end-date"
                        min={range.startDate}
                        onClick={(e: any) => { e.target.showPicker() }}
                        value={range.endDate}
                        onChange={
                          handleDatePicker
                        }
                      />
                    </div>
                  </div>
                  {/* Buttons Modal section */}
                  <div className="flex justify-end mt-9">
                    <Button
                      className='bg-reece-500 hover:bg-reece-600 text-red-50 text-xs'
                      onClick={() => {
                        handleLoadCountButtonClick();
                      }}
                      title='Load Count'
                      icon={<MdAdd />}
                      iconPosition="left"
                    />
                  </div>
                </div>
                {/* Name Filter Section */}
                <input
                  className='w-full p-2 lg:pl-4 bg-white rounded shadow-md text-xs lg:text-sm '
                  type='text'
                  placeholder='Search Branch Name, #'
                  onChange={handleSearchOnChange}
                />
              </div>

              {removeBranchCountsLoading == true && (
                <div className="w-full h-full bg-gray-50 fixed opacity-5"></div>
              )}
              {filteredList && (
                <div className='overflow-y-auto rounded shadow-md max-h-[900px]'>
                  <table className='min-w-full max-w-full leading-normal'>
                    <thead>
                      <tr className='top-0 sticky lg:uppercase lg:font-semibold lg:tracking-wider text-left text-xs text-white bg-reece-700 rounded-tr rounded-tl'>
                        <th className='p-2 lg:px-5 lg:py-3 w-1/5 max-w-[20%]'>
                          Branch
                        </th>
                        <th className='p-2 lg:px-5 lg:py-3 w-1/5 max-w-[20%]'>
                          Count ID
                        </th>
                        <th className='p-2 lg:px-5 lg:py-3 w-1/5 max-w-[20%]'>
                          Products
                        </th>
                        <th className='p-2 lg:px-5 lg:py-3 w-1/5 max-w-[20%]'>
                          Status
                        </th>
                        <th className='p-2 lg:px-5 lg:py-3 w-1/5 max-w-[20%]'>
                          Date
                        </th>
                        {isAdmin &&
                          <th className='p-2 lg:px-5 lg:py-3 w-1/5 max-w-[20%]'>
                            Archive
                          </th>}
                      </tr>
                    </thead>
                    <tbody>
                      {filteredList.length ? (
                        filteredList.map((data, sIndex) => (
                          <tr
                            key={sIndex}
                            className='text-xs xl:text-sm bg-white border-b border-gray-200'
                          >
                            {/* Branch Info */}
                            <td className='p-2 lg:p-5'>
                              <p className='text-gray-900 whitespace-no-wrap'>
                                {data.branchId} - {data.branchName}
                              </p>
                            </td>
                            {/* Count ID */}
                            <td className='p-2 lg:p-5'>
                              <p className='text-gray-900 whitespace-no-wrap'>
                                {data.countId}
                              </p>
                            </td>
                            {/* Products */}
                            <td className='p-2 lg:p-5'>{data.totalProducts}</td>
                            {/* Status */}
                            <td className='p-2 lg:p-5 max-w-[30%]'>
                              {
                                (data.status as string === "NOT_LOADED")
                                  ? <button
                                    className="underline text-reece-500 hover:text-reece-600"
                                    onClick={() => {
                                      handleNotLoadedStatusClick(data);
                                    }
                                    }>
                                    {data.status}
                                  </button>
                                  : <StatusText count={data} />
                              }
                            </td>
                            {/* Timestamps */}
                            <td className='p-2 lg:p-5'>
                              <p className='text-gray-900 2xl:inline'>
                                {data.createdAt &&
                                  getFormattedDate(data.createdAt)}
                              </p>
                              <p className='text-gray-900 2xl:inline'>
                                {" "}
                                {data.createdAt &&
                                  getFormattedTime(data.createdAt)}
                              </p>
                            </td>
                            {/* Archive */}
                            {isAdmin &&
                              <td className='p-2 lg:p-5 text-center'>
                                {/* <input className="mr-1" type="checkbox" value={data.branchId} onChange={(e) => handleSelectBranch(e, data)} />*/}
                                {(data.status as string === "COMPLETE" && data.branchId.length === 3 ) &&
                                  <Button
                                    className='bg-none text-center'
                                    onClick={() => {
                                      handleArchiveCountButtonClick(data);
                                    }}
                                    title=''
                                    icon={<MdDeleteForever className="text-red-800" />}
                                    size="lg"
                                    iconPosition="left"
                                  />
                                  
                                }
                              </td>}
                          </tr>
                        ))
                      ) : (
                        <tr>
                          <td
                            className='p-5 bg-white rounded-bl rounded-br text-center text-xs lg:text-sm'
                            colSpan={6}
                          >
                            {loading ? "Loading..." : "No results"}
                          </td>
                        </tr>
                      )}
                    </tbody>
                  </table>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default CountStatus;

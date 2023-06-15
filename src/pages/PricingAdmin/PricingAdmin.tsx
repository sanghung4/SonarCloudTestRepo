import { useEffect, useState } from "react";
import { Banner } from "../../components/Banner";
import { BranchUsersListResponse, useBranchUsersListLazyQuery } from "../../graphql";
import UserBranchTabForm from "./UserBranchTabForm";

const PricingAdmin = () => {

  // ----- State ----- //

  const [filteredList, setFilteredList] = useState<BranchUsersListResponse[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(false);
 
  // ----- API ----- //

  const [getBranchUsersList] = useBranchUsersListLazyQuery();

  const updateUsersData = () => {
    getBranchUsersList().then(({data})=>{
      if(data){
       setFilteredList(data?.branchUsersList as BranchUsersListResponse[])
       setIsLoading(false)
      } 
     }).catch(error =>{
       setIsLoading(false)
       throw(error)
     })
  }

  // ----- Effects ----- //
 
  useEffect(()=>{
    setIsLoading(true)
    updateUsersData()
  },[])

 
  return (
    <>
      <Banner header='Pricing Admin' />
      <div className='flex items-center justify-center'>
        <div className='w-full'>
          <UserBranchTabForm updateUsersData={updateUsersData}/>
          <div className='col-span-3 md:col-span-6 xl:col-span-8 2xl:col-span-9 xl:mt-10 m-8'>
            {filteredList && (
                <div className='overflow-y-auto rounded shadow-md max-h-[900px]'>
                  <table className='min-w-full max-w-full leading-normal '>
                    <thead>
                      <tr className='top-0 sticky lg:uppercase lg:font-semibold lg:tracking-wider text-left text-xs text-white bg-reece-700 rounded-tr rounded-tl'>
                        <th className='p-2 lg:px-5 lg:py-3 w-1/5 max-w-[20%]'>
                          Branch
                        </th>
                        <th className='p-2 lg:px-5 lg:py-3 w-1/5 max-w-[20%]'>
                          Users
                        </th>
                        
                      </tr>
                    </thead>
                    <tbody>
                      {filteredList.length > 0 ? (
                        filteredList
                        .slice()
                        .sort((objOne,objTwo) => Number(objOne.branchId) - Number(objTwo.branchId)) 
                        .map((item, sIndex) => (
                          <tr
                            key={sIndex}
                            className='text-xs xl:text-sm bg-white border-b border-gray-200 divide-x divide-y'
                          >
                            {/* Branch Info */}
                            <td className='p-2 lg:p-5'>
                              <p className='text-gray-900 whitespace-no-wrap'>
                                {item.branchId} 
                              </p>
                            </td>
                            {/* Users */}
                            <td className='p-2 lg:p-5'>
                            {item.assignedUsers?.map((emailId,eIndex)=>(
                              <p  key={eIndex} className='text-gray-900 whitespace-no-wrap'>
                                {emailId}
                              </p>
                            ))}
                            </td>
                           </tr>
                        ))
                      ) : (
                        <tr>
                          <td
                            className='p-5 bg-white rounded-bl rounded-br text-center text-xs lg:text-sm border-black'
                            colSpan={5}
                          >
                            {isLoading ? "Loading..." : "No results"}
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
    </>
  );
};

export default PricingAdmin;

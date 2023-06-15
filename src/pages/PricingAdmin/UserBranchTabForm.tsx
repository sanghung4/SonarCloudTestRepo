import { ChangeEventHandler, useEffect, useState } from "react";
import { Button } from "../../components/Button";
import { Input } from "../../components/Input";
import { toast, Slide } from "react-toastify";
import { Modal } from "../../components/Modal";
import { ADD_USER, DELETE_USER, EDIT_USER, REMOVE_USER_BRANCH_ACCESS, validateDomain, validInputHelpText } from "./utils";
import { useDebouncedCallback } from "use-debounce";
import {
  useAddUserMutation,
  useDeleteUserMutation,
  useRemoveUserBranchMutation,
  useUpdateUserEmailMutation,
} from "../../graphql";

interface updateUsersDataProp {
  updateUsersData: () => void;
}

const UserBranchTabForm = ({ updateUsersData }: updateUsersDataProp) => {
  const tabOptions = [ADD_USER, EDIT_USER, REMOVE_USER_BRANCH_ACCESS, DELETE_USER];

  // ----- State ----- //

  const [toggleState, setToggleState] = useState<string>(tabOptions[0]);
  const [branchId, setBranchId] = useState<string>("");
  const [userEmail, setUserEmail] = useState<string>("");
  const [newUserEmail, setNewUserEmail] = useState<string>("");
  const [confirm, setConfirm] = useState<boolean>(false);
  const [resetState, setResetState] = useState<boolean>(false);
  const [showSubmit, setShowSubmit] = useState<boolean>(false);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [validBranchIdStatus, setValidBranchIdStatus] = useState<"errorNonNumeric" | "empty">(
    "empty"
  );
  const [validEmailStatus, setValidEmailStatus] = useState<"errorInvalidEmail" | "empty">("empty");
  const [validNewEmailStatus, setValidNewEmailStatus] = useState<"errorInvalidEmail" | "empty">(
    "empty"
  );

  // ----- API ----- //

  const [addUser, { loading: addUserLoading }] = useAddUserMutation();
  const [removeUserBranch, { loading: removeUserBranchLoading }] = useRemoveUserBranchMutation();
  const [updateUserEmail, { loading: updateUserEmailLoading }] = useUpdateUserEmailMutation();
  const [deleteUser, { loading: deleteUserLoading }] = useDeleteUserMutation();

  const toggleTab = (index: string) => {
    setToggleState(index);
  };

  const checkValidBranchIdInput = useDebouncedCallback(() => {
    if (branchId) {
      !isNaN(Number(branchId)) ? setShowSubmit(true) : setValidBranchIdStatus("errorNonNumeric");
    } else {
      setValidBranchIdStatus("empty");
    }
  }, 1000);

  const checkValidEmailInput = useDebouncedCallback(() => {
    if (userEmail) {
      validateDomain(userEmail) ? setShowSubmit(true) : setValidEmailStatus("errorInvalidEmail");
    } else {
      setValidEmailStatus("empty");
    }
  }, 1000);

  const checkValidNewEmailInput = useDebouncedCallback(() => {
    if (newUserEmail) {
      validateDomain(newUserEmail)
        ? setShowSubmit(true)
        : setValidNewEmailStatus("errorInvalidEmail");
    } else {
      setValidNewEmailStatus("empty");
    }
  }, 1000);

  const handleBranchIdChange: ChangeEventHandler<HTMLInputElement> = (e) => {
    setBranchId(e.target.value);
  };
  const handleUserEmailChange: ChangeEventHandler<HTMLInputElement> = (e) => {
    setUserEmail(e.target.value);
  };
  const handleNewUserEmailChange: ChangeEventHandler<HTMLInputElement> = (e) => {
    setNewUserEmail(e.target.value);
  };

  const mutationStatus = (
    success: boolean,
    errorMessage: string | undefined,
    mutationType: string
  ) => {
    if (success) {
      toast.success(`The operation ${mutationType} was performed successfully.`, { transition: Slide });
      // Reset state values
      setResetState(true);
      updateUsersData();
    }
    if (errorMessage) {
      toast.error(errorMessage, { transition: Slide });
    }

    setIsLoading(false);
  };

  const confirmDelete = () => {
    setConfirm(false);
    deleteUser({ variables: { userEmail: userEmail } })
      .then(({ data }) => {
        if (data?.deleteUser?.success || data?.deleteUser?.error) {
          mutationStatus(data?.deleteUser?.success, data?.deleteUser?.error?.message, "delete user");
        }
      })
      .catch(() => {
        toast.error("The operation delete user was unsuccessful.");
      });
  };

  const modalClose = () => {
    setConfirm(false);
  };

  const clickHandler = (handlerState: string) => {
    switch (handlerState) {
      case ADD_USER:
        addUser({ variables: { userEmail: userEmail, branchId: branchId } })
          .then(({ data }) => {
            if (data?.addUser?.success || data?.addUser?.error) {
              mutationStatus(data?.addUser?.success, data?.addUser?.error?.message, "add user");
            }
          })
          .catch(() => {
            toast.error("The operation add user was unsuccessful.");
          });
        break;

      case EDIT_USER:
        updateUserEmail({ variables: { oldUserEmail: userEmail, newUserEmail: newUserEmail } })
          .then(({ data }) => {
            if (data?.updateUserEmail?.success || data?.updateUserEmail?.error) {
              mutationStatus(
                data?.updateUserEmail?.success,
                data?.updateUserEmail?.error?.message,
                "update user email"
              );
            }
          })
          .catch(() => {
            toast.error("The operation update user email was unsuccessful.");
          });
        break;

      case REMOVE_USER_BRANCH_ACCESS:
        removeUserBranch({ variables: { userEmail: userEmail, branchId: branchId } })
          .then(({ data }) => {
            if (data?.removeUserBranch?.success || data?.removeUserBranch?.error) {
              mutationStatus(
                data?.removeUserBranch?.success,
                data?.removeUserBranch?.error?.message,
                "remove user branch"
              );
            }
          })
          .catch(() => {
            toast.error("The operation remove user branch was unsuccessful.");
          });
        break;

      case DELETE_USER:
        setConfirm(true);
        break;
    }
  };

  // ----- Effects ----- //

  useEffect(() => {
    // Reset user email
    setUserEmail("");
    // Reset new user email
    setNewUserEmail("");
    // Reset branch id
    setBranchId("");
    // Reset submit button
    setShowSubmit(false);
    // Reset branch id
    setValidBranchIdStatus("empty");
    // Reset email
    setValidEmailStatus("empty");
    setValidNewEmailStatus("empty");
    // Reset when success
    setResetState(false);
    // Reset Loading
    setIsLoading(false)
  }, [toggleState, resetState]);

  useEffect(() => {
    setShowSubmit(false);
    checkValidBranchIdInput();
    setValidBranchIdStatus("empty");
    setIsLoading(false)
  }, [branchId]);

  useEffect(() => {
    setShowSubmit(false);
    checkValidEmailInput();
    setValidEmailStatus("empty");
    setIsLoading(false)
  }, [userEmail]);

  useEffect(() => {
    setShowSubmit(false);
    checkValidNewEmailInput();
    setValidNewEmailStatus("empty");
    setIsLoading(false)
  }, [newUserEmail]);

  useEffect(() => {
    setIsLoading(
      addUserLoading || removeUserBranchLoading || updateUserEmailLoading || deleteUserLoading
    );
  }, [addUserLoading, removeUserBranchLoading, updateUserEmailLoading, deleteUserLoading]);

  return (
    <>
      <div className='col-span-3 md:col-span-6 xl:col-span-8 2xl:col-span-9 rounded border bg-white xl:mt-10 m-8'>
        <Modal open={confirm} onConfirm={confirmDelete} onClose={modalClose}>
          <div className='my-5 px-2 text-m text-gray-500'>
            Review and confirm selected email for delete: <br />
            <span className='text-red-600 font-bold mr-1'>{userEmail}</span>
          </div>
        </Modal>
        <ul className='tabs flex sm:flex-row'>
          {tabOptions.map((tabTitle) => (
            <button
              key={tabTitle}
              className={`tab
                text-gray-400
                font-large
                py-3 px-6 
                hover:text-blue-800
                mb-5
                ${toggleState === tabTitle ? "active  text-blue-800 " : ""}
            `}
              onClick={() => toggleTab(tabTitle)}
            >
              {tabTitle}
            </button>
          ))}
        </ul>

        <div className='grid grid-cols-3 gap-x-8 gap-y-4  ml-10  justify-center '>
          {/* Branch Input */}
          {toggleState === ADD_USER || toggleState === REMOVE_USER_BRANCH_ACCESS ? (
            <Input
              label='Branch ID'
              placeholder='Enter Branch Number'
              onChange={handleBranchIdChange}
              value={branchId}
              status={validBranchIdStatus}
              helperText={validInputHelpText[validBranchIdStatus]}
            />
          ) : null}

          {/* User Input */}
          <Input
            label={toggleState === EDIT_USER ? "Old User Email" : "User Email"}
            placeholder={toggleState === EDIT_USER ? "Enter Old User Email" : "Enter User Email"}
            onChange={handleUserEmailChange}
            value={userEmail}
            status={validEmailStatus}
            helperText={validInputHelpText[validEmailStatus]}
          />

          {/* Edited User Input */}
          {toggleState === EDIT_USER ? (
            <Input
              label='New User Email'
              placeholder='Enter New User Email'
              onChange={handleNewUserEmailChange}
              value={newUserEmail}
              status={validNewEmailStatus}
              helperText={validInputHelpText[validNewEmailStatus]}
            />
          ) : null}

          {/* Actions */}
          <div className='flex col-span-3 justify-end mr-10 mb-5'>
            <Button
              className='bg-reece-500 text-white'
              disabled={
                userEmail &&
                branchId &&
                validBranchIdStatus === "empty" &&
                validEmailStatus === "empty"
                  ? !showSubmit
                  : userEmail &&
                    newUserEmail &&
                    validNewEmailStatus === "empty" &&
                    validEmailStatus === "empty"
                  ? !showSubmit
                  : userEmail && toggleState === DELETE_USER && validEmailStatus === "empty"
                  ? !showSubmit
                  : true
              }
              loading={isLoading}
              onClick={() => clickHandler(toggleState)}
              title={"Submit"}
            />
          </div>
        </div>
      </div>
    </>
  );
};

export default UserBranchTabForm;


 export const validateDomain = (email:string) =>{
  const domainsList = window.location.href.includes("test.ecomm") ? 
    ['morsco.com', 'reece.com', 'dialexa.com'] :
    ['morsco.com', 'reece.com'];
    
  const match = email.split('@')[1];
  return match !== null && domainsList.includes(match)
}

export const validInputHelpText = {
  errorInvalidEmail: "Please enter a valid email",
  errorNonNumeric: "Invalid Branch ID: Entry allows numeric digits only",
  empty: undefined,
};

export const ADD_USER = "Add User";
export const EDIT_USER = "Edit User";
export const REMOVE_USER_BRANCH_ACCESS = "Remove User Branch Access";
export const DELETE_USER = "Delete User";
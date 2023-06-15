import { useState, Fragment, useEffect } from "react";
import { MenuIcon, DotsHorizontalIcon } from "@heroicons/react/solid";
import { LogoutIcon, UserIcon } from "@heroicons/react/outline";
import { NavLink } from "react-router-dom";
import clsx from "clsx";
import { Menu, Transition } from "@headlessui/react";
import { Login } from "../../pages/Login";
import { useOktaAuth } from "@okta/okta-react";
import { SvgList } from "../SvgList";
import { navigation } from "./utils";
import { useAuthContext } from "../../store/AuthProvider";
import { empty } from "@apollo/client";

const Sidebar = () => {
  const { authState, oktaAuth } = useOktaAuth();
  const { userInfo, userBranchId } = useAuthContext();

  const [sideBar, toggleSideBar] = useState(false);
  const [showMore, setShowMore] = useState(false);

  const logOut = () => oktaAuth.signOut();

  if (!authState || !authState.isAuthenticated) {
    return <Login />;
  }

  // BRANCH LISTING CONFIG
  const showMoreVisibility = userBranchId ? userBranchId.length > 3 : false;

  const branchText = showMore || !showMoreVisibility ? userBranchId?.join(', '): userBranchId?.join(', ').substring(0, 20) + "...";

  const handleClick = () => {
    setShowMore ((prevShowMore) => !prevShowMore);
  }

  const menu = (
    <Menu as='div' className='relative inline-block text-left'>
      <div>
        <Menu.Button className='rounded-full flex items-center p-1 text-white hover:bg-reece-500 focus:none'>
          <DotsHorizontalIcon className='h-5 w-5' aria-hidden='true' />
        </Menu.Button>
      </div>
      <Transition
        as={Fragment}
        enter='transition ease-out duration-100'
        enterFrom='transform opacity-0 scale-95'
        enterTo='transform opacity-100 scale-100'
        leave='transition ease-in duration-75'
        leaveFrom='transform opacity-100 scale-100'
        leaveTo='transform opacity-0 scale-95'
      >
        <Menu.Items className='absolute bottom-8 right-0 mt-2 w-40 lg:w-48 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none'>
          <div className='py-1'>
            <Menu.Item>
              {({ active }: { active: boolean }) => (
                <button
                  onClick={logOut}
                  className={clsx(
                    active ? "bg-background text-gray-700" : "text-gray-700",
                    "flex w-full text-left px-4 py-2 text-sm"
                  )}
                >
                  <LogoutIcon className='w-5 h-5 text-gray-700 md:w-6 md:h-6 mr-2' />
                  Logout
                </button>
              )}
            </Menu.Item>
          </div>
        </Menu.Items>
      </Transition>
    </Menu>
  );

  return (
    <div>
      {/* mobile menu bar */}
      <div className='bg-primary-1-100 text-gray-50 flex justify-between lg:hidden'>
        <SvgList
          background='#003766'
          fill='white'
          className='h-14 p-4'
          name='Logo'
        />
        <button
          className='focus:bg-reece-600 p-4'
          onClick={() => toggleSideBar(!sideBar)}
        >
          <MenuIcon className='h-6 w-6' />
        </button>
      </div>

      {/* sidebar */}
      <div
        className={clsx(
          sideBar ? "translate-x-0" : "-translate-x-full",
          "overflow-hidden h-full flex-col flex px-2 bg-primary-1-100 w-44 lg:w-64 z-10 fixed transform inset-y-0 left-0 lg:translate-x-0 transition duration-200 ease-in-out"
        )}
      >
        <div className='w-full flex flex-col lg:flex-row justify-center items-center h-28 lg:h-24'>
          <SvgList
            background='#003766'
            fill='white'
            className='h-6'
            name='Logo'
          />
          <div className='text-white text-sm font-bold text-center pt-2 lg:pl-4 lg:pt-0'>
            COMPANION PORTAL
          </div>
        </div>
        <div className='flex flex-col justify-between h-full'>
          <nav>
            <ul className='mt-8'>
              {navigation.map(
                (item, index) =>
                  userInfo?.groups?.includes(item.group) && (
                    <li key={index}>
                      <NavLink
                        className={(isActive) => {
                          return clsx(
                            "flex",
                            "w-full",
                            "transition",
                            "duration-200",
                            "hover:bg-reece-600",
                            "hover:text-reece-50",
                            "cursor-pointer",
                            "items-center",
                            "text-sm",
                            "lg:text-base",
                            "mb-2",
                            "px-2",
                            "py-2",
                            "lg:mb-4",
                            "lg:px-6",
                            "lg:py-2",
                            "rounded",
                            isActive
                              ? "text-white bg-reece-500"
                              : "text-gray-400"
                          );
                        }}
                        to={item.href}
                      >
                        <item.icon className='w-5 h-5 md:w-6 md:h-6 color-white mr-2' />
                        {item.name}
                      </NavLink>
                    </li>
                  )
              )}
            </ul>
          </nav>
          <div className='flex-shrink-0 w-full group block lg:text-base mb-4 py-2 lg:mb-6 lg:px-6 rounded'>
            <div className='flex items-center justify-between'>
              <div className='flex items-center'>
                <UserIcon className='h-6 w-6 min-w-fit text-white' />
                <p className='text-xs font-small lg:text-base text-white ml-2'>
                  {userInfo?.name}
                </p>
              </div>
              <div>{menu}</div>
            </div>
            <div className='text-xs font-small text-white ml-8'>
              {userBranchId &&<p>{branchText}</p>}
              {showMoreVisibility && <p className="underline hover:cursor-pointer" onClick={() => handleClick()}>{showMore ? "See less" : "See more"}</p>}
            </div>
          </div>
        </div>
      </div>
      <div className='hidden lg:block w-64'></div>
    </div>
  );
};

export default Sidebar;

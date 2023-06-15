import { Button } from "../../components/Button";
import { useOktaAuth } from "@okta/okta-react";
import { SvgList } from "../../components/SvgList";

const LoginPage = () => {
  const { oktaAuth } = useOktaAuth();

  const login = async () => {
    oktaAuth.signInWithRedirect();
  };

  return (
    <div className='absolute z-99 w-full bg-gray-300 dark:bg-slate-900 min-h-screen flex justify-center'>
      <div className='h-full w-full m-10 max-w-md border border-gray-300 rounded bg-white px-12 pt-12 pb-6'>
        <div className='flex flex-col justify-center items-center mt-12'>
          <SvgList
            background='#003766'
            fill='white'
            className='h-14'
            name='Logo'
          />
          <p className='text-center text-lg text-reece-800 font-semibold mt-8'>
            Companion Portal
          </p>
        </div>
        <div className='mt-48 mb-6'>
          <Button
            className='bg-reece-800 text-gray-100 p-4 w-full rounded tracking-wide font-semibold font-display focus:outline-none focus:shadow-outline hover:bg-reece-900 shadow-lg'
            onClick={login}
            title='Sign In With Okta'
          />
        </div>
      </div>
    </div>
  );
};

export default LoginPage;

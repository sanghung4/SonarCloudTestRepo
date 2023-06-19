import { useReactiveVar } from '@apollo/client';
import { Config, configVar } from 'apollo/local';

export const useConfig = (): [Config, (config: Partial<Config>) => void] => {
  const config = useReactiveVar(configVar);

  const setConfig = (newConfig: Partial<Config>) => {
    configVar({ ...config, ...newConfig });
  };

  return [config, setConfig];
};

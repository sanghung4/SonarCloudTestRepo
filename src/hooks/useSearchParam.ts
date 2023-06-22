import { useHistory, useLocation } from 'react-router-dom';

export default function useSearchParam(paramName: string) {
  const location = useLocation();
  const params = new URLSearchParams(location.search);
  return params.get(paramName);
}

type ParamConfig = {
  arrayKeys: string[];
};

/**
 * This will return the search params as an object
 * @param delimiter used to parse arrays if provided
 * @param doNotPushHistory (default: false) true: replace history, false: push history
 * @returns Search params as object and setter
 */
export function useQueryParams<T>(
  config: ParamConfig = { arrayKeys: [] },
  toReplaceHistory: Boolean = false
): [T, (obj: T, path?: string) => void] {
  const history = useHistory();
  const location = useLocation();
  const params = new URLSearchParams(location.search);

  const setUrlSearchParams = (obj: T, path?: string) => {
    const payload = {
      pathname: path ? path : location.pathname,
      search: new URLSearchParams(
        objectToSearchParams(obj as Object)
      ).toString()
    };
    if (toReplaceHistory) {
      history.replace(payload);
    } else {
      history.push(payload);
    }
  };

  // @ts-ignore
  return [groupParamsByKey(params, config.arrayKeys) as T, setUrlSearchParams];
}

export function objectToSearchParams(record: Object) {
  return (
    Object.entries(record)
      .filter(([_, val]) => val)
      // @ts-ignore
      .reduce((agg, tuple) => {
        let [key, val] = tuple;
        if (Array.isArray(val)) {
          let vals = val.filter((v) => v).map((v) => [key, v]);
          return vals.length ? [...agg, ...vals] : agg;
        } else {
          return [...agg, tuple];
        }
      }, [])
  );
}

export function groupParamsByKey(params: URLSearchParams, arrayKeys: string[]) {
  return [...params.entries()].reduce((acc, tuple) => {
    // getting the key and value from each tuple
    const [key, val] = tuple;
    if (arrayKeys.includes(key)) {
      // if the current key is already an array, we'll add the value to it
      if (Array.isArray(acc[key])) {
        acc[key] = [...acc[key], val];
      } else {
        // if it's not an array, but contains a value, we'll convert it into an array
        // and add the current value to it
        acc[key] = [val];
      }
    } else {
      // plain assignment if no special case is present
      acc[key] = val;
    }

    return acc;
  }, {} as Record<string, string | string[]>);
}

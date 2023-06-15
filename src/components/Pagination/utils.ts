export const DOTS = -1;

export const arrayOfRange = (start: number, end: number) => {
  const length = end - start + 1;
    /*
        Create an array of certain length and set the elements within it from
      start value to end value.
    */
    return Array.from({ length }, (_, idx) => idx + start);
  };
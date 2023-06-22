declare module '@reece/global-types' {
  /**
   * General
   */
  export type DataMap<T = any> = { [key: string]: T };

  // Function
  // @param: FN<[...arguments], output>
  // @default: () => void
  // @return: (...arguments) => output
  export type FN<T extends Array<any> = [], O = void> = (...args: T) => O;
  export type AsyncFN<T extends Array<any> = [], O = void> = FN<T, Promise<O>>;

  /**
   * Event based function types
   */
  export type OnClick<T = HTMLButtonElement> = FN<
    [React.MouseEvent<T> | undefined]
  >;
}

declare module '@reece/global-types' {
  /**
   * General
   */
  export type DataMap<T = any> = { [key: string]: T };

  /**
   * Component
   */
  export type WrapperProps = { children: ReactNode };
}

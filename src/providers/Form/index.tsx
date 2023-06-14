import { isFunction, transform } from 'lodash';
import React, {
  Reducer,
  createContext,
  useContext,
  useEffect,
  useReducer,
  useRef,
  ReactNode,
} from 'react';

export type FormValues = {
  [field: string]: any;
};

export type FormErrors<V> = {
  [K in keyof V]?: string;
};

export type FormTouched<V> = {
  [K in keyof V]?: boolean;
};

export type FormMethods<V> = {
  validateField: (key: keyof V) => void;
  setFieldValue: (key: keyof V, value: any) => void;
  setFieldTouched: (key: keyof V, touched: boolean) => void;
  setFieldError: (key: keyof V, error: string) => void;
  resetForm: (values?: Partial<V>) => void;
  submitForm: () => void;
};

export type FormOptions<V> = {
  initialValues: V;
  initialErrors?: FormErrors<V>;
  initialTouched?: FormTouched<V>;
  validateOnBlur?: boolean;
  validateOnMount?: boolean;
  validate?: (values: V) => FormErrors<V>;
  onReset?: (values: V, actions: FormMethods<V>) => void;
  onSubmit: (values: V, actions: FormMethods<V>) => void | Promise<any>;
};

type FormState<V> = {
  values: V;
  errors: FormErrors<V>;
  touched: FormTouched<V>;
  submitting: boolean;
};

type FormAction<V> =
  | { type: 'SUBMITTED' }
  | { type: 'SUBMIT_FAILURE' }
  | { type: 'SUBMITTING' }
  | { type: 'SET_FIELD_VALUE'; payload: { key: keyof V; value: any } }
  | { type: 'SET_FIELD_TOUCHED'; payload: { key: keyof V; touched: boolean } }
  | { type: 'SET_FIELD_ERROR'; payload: { key: keyof V; error?: string } }
  | { type: 'SET_TOUCHED'; payload: FormTouched<V> }
  | { type: 'SET_ERRORS'; payload: FormErrors<V> }
  | { type: 'RESET_FORM'; payload: FormState<V> };

function formReducer<V>(state: FormState<V>, action: FormAction<V>) {
  switch (action.type) {
    case 'SUBMITTING':
      return { ...state, submitting: true };
    case 'SUBMIT_FAILURE':
      return { ...state, submitting: false };
    case 'SUBMITTED':
      return { ...state, submitting: false };
    case 'SET_FIELD_VALUE':
      return {
        ...state,
        values: {
          ...state.values,
          [action.payload.key]: action.payload.value,
        },
      };
    case 'SET_FIELD_TOUCHED':
      return {
        ...state,
        touched: {
          ...state.touched,
          [action.payload.key]: action.payload.touched,
        },
      };
    case 'SET_FIELD_ERROR':
      return {
        ...state,
        errors: {
          ...state.errors,
          [action.payload.key]: action.payload.error,
        },
      };
    case 'SET_TOUCHED':
      return { ...state, touched: action.payload };
    case 'SET_ERRORS':
      return { ...state, errors: action.payload };
    case 'RESET_FORM':
      return { ...state, ...action.payload };
    default:
      return state;
  }
}

const emptyErrors: FormErrors<unknown> = {};
const emptyTouched: FormTouched<unknown> = {};

export function useForm<V extends FormValues = FormValues>({
  initialValues,
  initialErrors,
  initialTouched,
  validateOnMount = false,
  validateOnBlur = false,
  validate,
  onReset,
  onSubmit,
}: FormOptions<V>): FormState<V> & FormMethods<V> {
  const mounted = useRef(false);
  const [state, dispatch] = useReducer<Reducer<FormState<V>, FormAction<V>>>(
    formReducer,
    {
      values: initialValues,
      errors: initialErrors || emptyErrors,
      touched: initialTouched || emptyTouched,
      submitting: false,
    }
  );

  useEffect(() => {
    mounted.current = true;
    return () => {
      mounted.current = false;
    };
  }, []);

  useEffect(() => {
    if (validateOnMount && mounted.current && validate) {
      const validation = validate(state.values);
      dispatch({ type: 'SET_ERRORS', payload: validation });
    }
  }, [validateOnMount, validate, state.values]);

  const getFormMethods = () => ({
    validateField,
    setFieldValue,
    setFieldTouched,
    setFieldError,
    resetForm,
    submitForm,
  });

  const validateField = (key: keyof V) => {
    if (state.values[key] && validate) {
      const validation = validate(state.values);
      const error = validation[key];
      dispatch({ type: 'SET_FIELD_ERROR', payload: { key, error } });
    }
  };

  const setFieldValue = (key: keyof V, value: any) => {
    dispatch({ type: 'SET_FIELD_VALUE', payload: { key, value } });
  };

  const setFieldTouched = (key: keyof V, touched = true) => {
    dispatch({ type: 'SET_FIELD_TOUCHED', payload: { key, touched } });

    if (validateOnBlur) {
      validateField(key);
    }
  };

  const setFieldError = (key: keyof V, error?: string) => {
    dispatch({ type: 'SET_FIELD_ERROR', payload: { key, error } });
  };

  const submitForm = () => {
    dispatch({ type: 'SUBMITTING' });

    const touched = transform(
      state.values,
      (res, _, key) => {
        res[key] = true;
      },
      {} as FormTouched<any>
    );

    dispatch({ type: 'SET_TOUCHED', payload: touched });

    if (validate) {
      const validation = validate(state.values);
      dispatch({ type: 'SET_ERRORS', payload: validation });
      if (Object.values(validation).some(Boolean)) {
        return;
      }
    }

    Promise.resolve(onSubmit(state.values, getFormMethods()))
      .then((result) => {
        if (mounted.current) {
          dispatch({ type: 'SUBMITTED' });
        }
        return result;
      })
      .catch((errors) => {
        if (mounted.current) {
          dispatch({ type: 'SUBMIT_FAILURE' });
          throw errors;
        }
      });
  };

  const resetForm = (values: Partial<V> = {}) => {
    dispatch({
      type: 'RESET_FORM',
      payload: {
        values: { ...initialValues, ...values },
        errors: initialErrors || emptyErrors,
        touched: initialTouched || emptyTouched,
        submitting: false,
      },
    });
    onReset?.(state.values, getFormMethods());
  };

  return {
    ...state,
    ...getFormMethods(),
  };
}

export interface FormProps<V> extends FormOptions<V> {
  children?: ReactNode | ((props: FormState<V> & FormMethods<V>) => ReactNode);
}

export type IFormContext<V> = FormState<V> & FormMethods<V>;

const FormContext = createContext(undefined as any);

function Form<V extends FormValues = FormValues>({
  children,
  ...formProps
}: FormProps<V>): JSX.Element {
  const formBag = useForm(formProps);
  return (
    <FormContext.Provider value={formBag}>
      {isFunction(children) ? children(formBag) : children}
    </FormContext.Provider>
  );
}

export default Form;

export function useFormContext<V>(): FormState<V> & FormMethods<V> {
  return useContext(FormContext);
}

export interface FieldInputProps<T> {
  value: T;
  error: boolean;
  errorMessage?: string;
  onBlur: () => void;
  onChangeText: (value: T) => void;
}

export interface FieldMetaProps<T> {
  setValue: (value: T) => void;
  setError: (value: string) => void;
  setTouched: (value: boolean) => void;
  validate: () => void;
}

export function useField<V extends FormValues, K extends keyof V>(
  fieldName: K
): [FieldInputProps<V[K]>, FieldMetaProps<V[K]>] {
  const {
    validateField,
    setFieldError,
    setFieldTouched,
    setFieldValue,
    values,
    touched,
    errors,
  } = useFormContext<V>();

  return [
    {
      value: values[fieldName],
      error: !!(touched[fieldName] && errors[fieldName]),
      errorMessage: errors[fieldName],
      onBlur: () => setFieldTouched(fieldName, true),
      onChangeText: (text: string) => setFieldValue(fieldName, text),
    },
    {
      setValue: (value: V) => setFieldValue(fieldName, value),
      setError: (value: string) => setFieldError(fieldName, value),
      setTouched: (value: boolean) => setFieldTouched(fieldName, value),
      validate: () => validateField(fieldName),
    },
  ];
}

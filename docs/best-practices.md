# Best Practices

This project contains all of the recommended practices for this project. Please follow these as you develop any new features and ensure that these standards are being followed on code review.

## File Structure

```
portal/
├─ docs/
├─ public/
├─ src/
│  ├─ common/
│  ├─ utils/
│  ├─ Routes.tsx
│  ├─ App.tsx
├─ tests/
├─ .env
```

 - `docs` - contains markdown folders with documentation.
 - `src/common` - should contain all global components such as the headers.
 - `src/utils` - should contain all non component based code such as date parsing or custom hooks.
 - `src/App.tsx` - This is the root app component and should contain all providers.
 - `src/Routes.tsx` - This should contain all root routes.

#### Building Out Pages

Each page should have it's own route in the application, for example the User Management page is in `src/UserManagement`.

The base component for each page should be called `index.tsx` and will contain the full base component. Any child components of that page should live next to this component. If you make any components while working on a page that will be used in other applications please move them to the `src/common` folder.

The structure should be as follows:
```
src/
├─ MyPage/
│  ├─ index.tsx
│  ├─ SubComponentOne.tsx
│  ├─ SubComponentTwo.tsx
```

## Component Structure

To help keep things consistent our components should always follow the same structure. We will first breakdown the overall structure of our component file. Then we will look at individual sections / rules such as imports or laying out hooks. And then last there is an example will all of this together in a single file.

### General Structure

First let's look at an example of the general component structure we are targeting.
```tsx
// imports
import React from 'react';
// ...

// type defs
type Props = {};

function MyComponent(props: Props) {
  // Component API eg hooks
  
  // render / template
  return (
    // ...
  );

  // Method Definitions
}

// Export
export default MyComponent;
```

The general idea of layout out a component like this is that for most components we can keep the API layer at the top level of the component where you can easily see all methods and variables at a glance. This should make components easier to understand and should help prevent cognitive overload from being bogged down in method details. here's a slightly more detailed example:

```tsx
import React, { useCallback, useEffect, useState } from 'react';

type Props = {
  initialCount: number
}

function Counter(props: Props) {
  /**
   * State
   */
  const [count, setCount] = useState(initialCount);

  /**
   * Effects
   */
  useEffect(alertIfTooHigh, [count]);

  /**
   * Callbacks
   */
  const handleAdd = useCallback(handleAddCb, [count, setCount]);

  return (
    <>
      Count: {count}
      <button onClick={handleAdd}>Add</button>
    </>
  )

  function alertIfTooHigh() {
    if (count > 10) {
      alert('COUNT IS TOO HIGH!');
    }
  }

  function handleAddCb() {
    setCount(count + 1);
  }
}
```
### Imports

Generally we want to break imports into three sections:

 - React
 - Third Parties
 - Our Imports (other components utils etc)

Try to alphebetize these and separate each section with a newline.

```tsx
import React, { useContext, useEffect, useState, ReactNode } from 'react';

import {
  Box,
  CircularProgress,
  Container,
  Grid,
  Hidden,
  Typography,
  fade,
  useScreenSize,
  useTheme
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { CartContext } from 'Cart/CartProvider';
import CartDesktop from 'Cart/Cart.desktop';
import CartMobile from 'Cart/Cart.mobile';
import DeliveryMethod from 'Cart/DeliveryMethod';
import OrderSummary from 'Cart/OrderSummary';
import Shipments from 'Cart/Shipments';
import BackToTop from 'common/BackToTop';
import Loader from 'common/Loader';
import useDebouncedCallback from 'hooks/useDebouncedCallback';
```

Also note that we individually import each hook or type from React. This is mostly stylistic, but will help prevent having `React` all over the files.

### Props

I recommend typing your props and just calling it 'Props' then changing the name when you import it elsewhere. This just helps keep the naming consistent across components. Also prefer using type over interface unless you need to combine or extend another type. Last, don't deconstruct the props. Try to keep the `props` variable before each reference to make it clear that you are referencing a prop and not a piece of state.

```tsx
import React from 'react';

type Props = {
  foo: string;
  bar: number;
}

function MyComponent(props: Props) {
 return (
  <div>{props.foo}, {props.bar}</div>
 );
}

export default MyComponent;
```

This way you can always know what type to import later on if necessary:

```tsx
import { Props as MyComponentProps } from 'MyComponent';
```

### Recommended Hook Order

The following is the recommended order for hooks. Please keep in mind that this is not always possible because hooks may be interdependent. But please try to follow this order when possible.

 - Custom Hooks
 - Refs
 - State
 - Context
 - Data (GraphQL hooks)
 - Memos
 - Effects
 - Callbacks

When you lay these out in your components I recommend that each section be prefixed with a block comment to keep them distinct.

```tsx
/**
 * State
 */
const [count, setCount] = useState(0);
const [val, setVal] = useState('foo');
```

Sometimes this order is not possible. For example, you may have a GraphQL query that relies on a memo. In this case try to move entire blocks of hooks, so maybe you will reorg your sections like the following:

- State
- Memos
- Data

If you have dependencies requiring multiple sequence breaks, that may be a sign of increased complexity and you may consider breaking them into a custom hook. This is a rare case and can be discussed when encountered.

#### Hoisting

Memos, Effects and Callbacks should all be hoisted from below the return of your component. As mentioned above, the goal is simply to reduce complexity when first examining a component. 

> ❗Always postfix memo methods with 'memo'.

```tsx
const double = useMemo(doubleMemo, [count]);

// return

function doubleMemo() {
 return count * 2;
}
```

Effects should just have a function name that describes the behavior.

```tsx
useEffect(loadData, []);
useEffect(doSomethingEvery10Secs, []);

// return

function loadData() { /* ... */ }

function doSomethingEvery10Secs() { /* ... */ }
```

> ❗Always postfix callback methods with 'cb'.

```tsx
const handleClick = useCallback(handleClickCb, []);

// return

function handleClickCb() { /* ... */ }
```

### Complete Component Example

```tsx
// React Import
import React, { useEffect, useState } from 'react';

// 3rd Party Imports
// alphabetized 
import { useScreenSize } from '@dialexa/reece-component-library';
import { SomeIcon } from '@material-ui/icons';
import { get } from 'lodash-es';

// Our imports / Project imports
// Alphabetized and absolute
import { useGetStuffQuery } from 'generated/graphql';
import MyComponent from 'MyComponent';

// Always type props as props
type Props = {
  propOne: string;
  propTwo: number;
}

// don't deconstruct props so that it's clear what is being referenced
// Declare as normal function not fat arrow
function MyComponent(props: Props) {

  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();

  /**
   * Refs
   */
  const ref = useRef(initialValue);

  /**
   * State
   */
  const [stateOne, setStateOne] = useState<Interface>();
  const [stateTwo, setStateTwo] = useState(initialValue);
  const [stateThree, setStateThree] = useState(0); // Infered type

  /**
   * Context
   */
  const { user } = createContext(AuthContext);

  /**
   * Data
   */
  const { data, loading } = useGetStuffQuery();
  
  /**
   * Memos
   */
  const myValue = useMemo(myValueMemo, [stateOne, props.propsOne]);
  const doubleMyValue = useMemo(myDoubleValueMemo, [myValue]);
  
  /**
   * Effects
   */
  useEffect(loadData, [stateOne]);
  
  /**
   * Callbacks
   */
  const onClick = useCallback(onClickCb, [prop.propsTwo]);
  
  return (
    <div>
      <h1>Example Component</h1>
      <h3>{props.propOne}</h3>
      <h3>{stateOne}</h3>
      <button onClick={onClick}></button>
    </div>
  );
  
  /**
   * Memo Definitions
   */
  function myValueMemo() {
    // ...
  }

  /**
   * Effect Definitions
   */
  function loadData() {
    // ...
  }
  
  /**
   * Callback Definitions
   */
  function onClickCb() {
    // ...
  }
}

export default MyComponent;
```

## State Management

Currently we do not have a specific solution for state management such as Redux. Where we do need to share global application state such as the cart we are working with React context. The following shows how you can store state in a a context/provider.

```tsx
/**
 * CartProvider.tsx
 */
import React, { createContext, useState, ReactNode } from 'react';

// Declare the type for your context/state first
type CartContextType = {
  cart?: Cart,
  addItemToCard: (productId: string, quantity: number) => void,
}

// Create and export the context for use in child components
export const CartContext = createContext<CartContextType>({
  cart: {},
  addItemToCart: () => {}
})

type Props = {
  children: ReactNode
}

// Create our Provider that will expose our values to the consumer components
function CartProvider(props: Props) {

  /**
   * State
   */
  const [cart, setCart] = useState<Cart>({
    items: []
  });

  /**
   * Callbacks
   */
  const addItemToCart = (productId: string, quantity: number) => {
    setCart({
      items: [
        ...cart.items,
        {
          productId,
          quantity
        }
      ]
    })
  }

  return (
    <CartContext.Provider
      value={{
        cart,
        addItemToCart
      }}
    >
      {props.children}
    </CartContext.Provider>
  );
}

export default CartProvider;
```

Next add this context to our `src/App.tsx` file:
```tsx
/**
 * App.tsx
 */

// ...
import CartProvider from 'CartProvider';
// ...

function App() {
  // ...

  return (
    <CartProvider>
      // ...
    </CartProvider>
  );
}

export default App;
```

Now this context will be available to any child components in our application.
```tsx
/**
 * src/MyComponent/index.tsx
 */
import React, { useContext } from 'react';

import CartContext from 'CartProvider';

function MyComponent() {
  /**
   * Context
   */
  const { cart, addItemToCart } = useContext(CartContext);

  // ...
}

export default MyComponent;
```

## GraphQL and Generated Types

For this project our frontend types should primarily be generated by the graphql layer and the queries we create. The way this works is that you write your queries in this `portal` repo and then you can run `yarn generate` which will fetch and validate the types with the `public-api` by using the [GraphQL Codegen](https://www.graphql-code-generator.com/) library. 

Here's a quick example of how you would add a new query and generate the types.

1. Add a `*.graphql` file with your query
2. Colocate this file with the consuming component
3. Run your `public-api` locally.
4. Run `yarn generate` in your `portal` directory

These steps will generate fully typed `useQuery` and `useLazyQuery` hooks for your type.

For example, if I added the following graphql file:

```graphql
query unapprovedAccountRequests($accountId: String!) {
  unapprovedAccountRequests(accountId: $accountId) {
    id
    email
    firstName
    lastName
    companyName
    phoneNumber
    roleId
    phoneType {
      id
      name
    }
  }
}
```

And then I generate the graphql types the following fully typed hooks will now be available to me:

 - `useGetApprovedAccountRequestsQuery`
 - `useUnapprovedAccountRequestsQuery`

```
src/
├─ generated/
│  ├─ graphql.ts
├─ MyPage/
│  ├─ index.tsx
│  ├─ requests.graphql
```

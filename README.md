# Branch Inventory Application

## **Table of Contents**

- [Branch Inventory Application](#branch-inventory-application)
  - [**Table of Contents**](#table-of-contents)
  - [**Initial Setup**](#initial-setup)
    - [***Install Dependencies***](#install-dependencies)
    - [***iOS App***](#ios-app)
    - [***Android App***](#android-app)
    - [***Add ENV File***](#add-env-file)
    - [***Install Packages***](#install-packages)
    - [***Run the App Locally***](#run-the-app-locally)
  - [**Deployment Process for Beta**](#deployment-process-for-beta)
  - [**Deploying to TestFlight**](#deploying-to-testflight)
  - [**General Project Setup**](#general-project-setup)
    - [***Source Directory Setup***](#source-directory-setup)
    - [***React Native File Setup***](#react-native-file-setup)
    - [***Example Export File***](#example-export-file)
  - [**Helpful React Native Tools and Snippets**](#helpful-react-native-tools-and-snippets)
    - [***Tools & VSCode Extensions***](#tools--vscode-extensions)
    - [***Snippets***](#snippets)
      - [TypeScript - `.ts`](#typescript---ts)
      - [TypeScript React - `.tsx`](#typescript-react---tsx)
  - [**Utility**](#utility)
    - [***GraphQL***](#graphql)
    - [***Testing***](#testing)
      - [Using `getComponentTestingIds`](#using-getcomponenttestingids)
      - [Using `getScreenTestingIds`](#using-getscreentestingids)
      - [Example](#example)
  - [**Refreshing App Store Certificates**](#refreshing-app-store-certificates)

---

## **Initial Setup**
>  For the most up to date instructions, refer to the React Native environment setup instructions under the [React Native CLI Quickstart section](https://reactnative.dev/docs/environment-setup).

### ***Install Dependencies***

Be sure that you install the required dependencies.

1. Node
2. Watchman
3. Xcode 
4. CocoaPods

For Mac users install the above dependencies with homebrew:

Install Homebrew:

`/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/`

Install Dependencies:

`brew install --cask adoptopenjdk/openjdk/adoptopenjdk8`
`brew install node`
`brew install watchman`
`sudo install yarn -g`

### ***iOS App***

Install cocoapods: `brew install cocoapods`
Install Xcode via the Mac App Store

Within the branch-inventory root directory run: `sudo npm install react-native-cli`

### ***Android App***

Install Android studio https://developer.android.com/studio
-  Within Android studio go to the plugins tab and search for "Android SDK Source" and install
-  Within Android studio go to preferences (shortcut: `Command,`)
-  Navigate to System Settings -> Android SDK and make sure Android 10.0 (Q) is checked
-  In the bottom right corner check the box for "Show Package Details"

Check the following:
-  Android SDK Platform 29
-  Sources for Android 29
-  Intel x86 Atom_64 System Image
-  Google APIs Intel x86 Atom System Image
-  Google Play Intel x86 Atom System Image
Note: M1 users will need to select ARM 64 options instead of Intel x86

Within Android Studio select the ellipsis beside the "Get from VCS" button and select "AVD Manager"
-  Create a new Virtual Device
-  Select a Device type and click next (Pixel 5 recommended)
-  Select the Release name (Q API Level:29 recommended)
-  Click "Create"

Set up your bashrc file:
-  Navigate home in the terminal `cd ~`
-  Open `.bashrc` if it doesnt not exist create it
-  It should look like the following, make sure ANDROID_HOME location is correct

```bash
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/emulator
export PATH=$PATH:$ANDROID_HOME/tools
export PATH=$PATH:$ANDROID_HOME/tools/bin
export PATH=$PATH:$ANDROID_HOME/platform-tools
```

Note: M1 users must set variables like so (pending update)
```bash
export ANDROID_SDK=/Users/austintumlinson/Library/Android/sdk
export PATH=/Users/austintumlinson/Library/Android/sdk/platform-tools:$PATH
export JAVA_HOME=/Applications/Android\ Studio.app/Contents/jre/Contents/Home
export ANDROID_HOME=/Users/austintumlinson/Library/Android/sdk
export PATH=$ANDROID_HOME/emulator:$PATH
export PATH=$ANDROID_HOME/platform-tools:$PATH
export PATH=$ANDROID_HOME/tools:$PATH
export PATH=$ANDROID_HOME/tools/bin:$PATH
```

### ***Add ENV File***

A `.env` file with the necessary environmental variable values must be placed in your root directory.


Note: M1 users may need to change some settings for the Simulator to run in Rosetta
-  Navigate to the Applications->Utilites->Terminal
-  Duplicate Terminal Application and call it something similar to "Rosetta Terminal"
-  Right click new Terminal application and check the "Open when Rosetta" box inside of the Get Info Tab

-  Open the BIA ios folder in Xcode
-  Navigate to Build Settings menu
-  Find the Excluded Architechtures menu
-  For the Debug and Release Catagories type in "arm64" in the location next to "Any iOS Simulator SDK"

-  Additionally if you have already done a 'yarn install:full' you may need to clean the project and re-run the command

### ***Install Packages***

Run `yarn install:full` 
- To install all dependencies, a script was added to `package.json` to streamline the installation process. 
- Acts as a shortcut for `yarn install && cd ios && pod install && cd ../`

### ***Run the App Locally***

Run `yarn ios` - to run in iOS emulator
Run `yarn android` - to run in Android emulator 

---

## **Deployment Process for Beta**
> [Fastlane](https://fastlane.tools/) is used on this project to automate bundling and deployments.

To deploy a new version of the application, follow the process below.
1. Create a branch off of the latest `development` branch.
2. Change the version number of the application by running one of the following.
   - `yarn bump:patch` - changes the patch version (x.x.**`1`** -> x.x.**`2`**).
   - `yarn bump:minor` - changes the minor version (x.**`1`**.x -> x.**`2`**.0).
   - `yarn bump:major` - changes the major version (**`1`**.x.x -> **`2`**.0.0).
3. Create a PR to merge the branch into `development`.
4. After the PR has been merged, create a PR to merge `development` into `staging`.
   - When the `development` is merged into staging, the automated deployment process will begin. 

---

## **Deploying to TestFlight**
> Before proceeding request access to the Reese Apple Development team from appropriate admin.

1. Login to [App Store Connect](https://appstoreconnect.apple.com/login)
2. Enter Reese Physical Inventory Test in My Apps
3. Go to the test TestFlight tab 
4. Find the current build 
5. Under status click the `manage` link
6. Answer the questionnaire and submit

---

## **General Project Setup**

### ***Source Directory Setup***

```
🗂 src
│
│
├──🗂 components - Shared Components
│  │                      
│  └──🗂 ExampleComponent              - Unique Component
│     ├──📄 constants.ts               - Constants
│     ├──📄 ExampleComponent.tsx       - Final Component
│     ├──📄 ExampleSubComponent.tsx    - Sub Component 
│     ├──📎 index.ts                   - Directory Exports
│     ├──📄 types.ts                   - Types
│     └──🛠 utils.ts                   - Utility Functions
│
│
├──🗂 hooks - Hooks
│  │
│  └──🗂 useExample        - Unique Hook
│     ├──📄 constants.ts   - Constants
│     ├──📎 index.ts       - Directory Exports
│     ├──📄 types.ts       - Types
│     ├──📄 useExample.tsx - Hook
│     └──🛠 utils.ts       - Utility Functions
│
├──🗂 providers   - Providers
│  ├──📎 index.ts - Application of All Providers
│  │
│  └──🗂 Example         - Unique Provider
│     ├──📄 constants.ts - Constants
│     ├──📎 index.ts     - Provider
│     ├──📄 types.ts     - Types
│     └──📄 utils.ts     - Utility Functions
│
│
│
└──🗂 screens - Application Screen
   │
   └──🗂 ExampleScreen                 - Screen
      ├──📄 constants.ts               - Constants
      ├──📄 ExampleScreen.tsx          - Screen
      ├──📄 ExampleScreen.test.tsx     - Unit Tests
      ├──📄 ExampleScreenComponent.tsx - Component Unique to Screen
      ├──📎 index.ts                   - Directory Exports
      ├──📄 types.ts                   - Types
      └──🛠 utils.ts                   - Utility Functions

```

### ***React Native File Setup***

```ts
// Example.tsx

IMPORTS        │  import React, {[react imports]} from 'react';
               │  import {[item]} from [path]; 
               │
TYPES          │  type ExampleType = {...}
               │
INTERFACES     │  interface ExampleInterface {...}
               │
               │
React FC       │  const ExampleReact = (...) => {...}
               │
DYNAMIC STYLES │  const getExampleStyle = (...): StyleType => ({
               │     ...
               │  });
               │
STYLES         │  const styles = StyleSheet.create({...})
               │
EXPORT         │  export default ExampleReact
```

### ***Example Export File***

```ts
// index.ts

export { default as ExampleReact } from "./ExampleReact"
export * from "./ExampleReact"

export type { ExampleType } from './types';
export * from './types'
```

---

## **Helpful React Native Tools and Snippets**

### ***Tools & VSCode Extensions***

- [React Native Debugger](https://github.com/jhen0409/react-native-debugger)
- [Auto Rename Tag](https://marketplace.visualstudio.com/items?itemName=formulahendry.auto-rename-tag)
- [Jest](https://marketplace.visualstudio.com/items?itemName=Orta.vscode-jest)
- [Prettier ESLint](https://marketplace.visualstudio.com/items?itemName=rvest.vs-code-prettier-eslint)
- [React Native Tools](https://marketplace.visualstudio.com/items?itemName=msjsdiag.vscode-react-native)
- [ES7 React/Redux/GraphQL/React-Native snippets](https://marketplace.visualstudio.com/items?itemName=dsznajder.es7-react-js-snippets)
- [Jest Runner](https://marketplace.visualstudio.com/items?itemName=firsttris.vscode-jest-runner)

### ***Snippets***

#### TypeScript - `.ts`

- Create index export

```json
"Create export file": {
   "prefix": "expfull",
   "body": [
      "export { default as ${1:component} } from './${1:component}';",
      "export * from './${1:component}';"
   ]
}
```

#### TypeScript React - `.tsx`

- Automate StyleSheet.create

```json
"React Native StyleSheet.create": {
  "prefix": "rns",
  "body": [
    "const styles = StyleSheet.create({",
    "  ${1:tag}: {",
    "    $0",
    "  },",
    "});"
  ]
}
```

---

## **Utility**

### ***GraphQL***

To see a breakdown of our GraphQL queries, mutations, and typedefs visit the GraphQL playground in your browser. 
To access the playground, follow the following links:

[TEST PLAYGROUND](api.test.internal.reecedev.us)

[DEVELOPMENT PLAYGROUND](api.internal.reecedev.us)

### ***Testing***
> To help with testing, two helper functions have been added to the `test-utils` directory in the root of the project. Each can be imported into any screen or component needed.

- `getComponentTestingIds` was added to create TestIDs for custom components that take `testID` props. 
- `getScreenTestingIds` was added to create TestIDs for screens that require the app's bundle ID.

#### Using `getComponentTestingIds`
1. In `constants/testIds.ts`, add an object to `ComponentTestIds` with a key matching the component's name.
2. Inside of the object, add any testIDs that are required for the object.
3. In the component's `.tsx` file, import `getComponentTestingIds` and initialize it with the name of the component as the first param, and the component's testID as the second.
4. Use the object returned by `getComponentTestingIds` to select the testIDs for your component. 

#### Using `getScreenTestingIds`
1. In `constants/testIds.ts`, add an object to `ScreenTestIds` with a key matching the screen's name.
2. Inside of the object, add any testIDs that are required for the object.
3. In the screen's `.tsx` file, import `getScreenTestingIds` and initialize it with the name of the screen.
4. Use the object returned by `getScreenTestingIds` to select the testIDs for your screen.


#### Example

```ts
// test-utils/testIds.ts

export const ComponentTestIds = {
  ...
  ComponentExample: {
    component: 'component-example-component',
    title: 'component-example-title'
  }
  ...
}

export const ScreenTestIds = {
  ...
  ScreenExample: {
    screenLayout: 'screen-example-screen-layout',
    button: 'screen-example-button',
  }
  ...
}
```

```ts
// components/ComponentExample/ComponentExample.tsx

import { getComponentTestingIds } from 'test-utils/testIds';

...

export const ComponentExample = ({ ... , testID }: ComponentExampleProps ) => {

  const testIds = getComponentTestingIds('ComponentExample', testID);

  return (
    <View testID={testIds.component}>
      ...
      <Text testID={testIds.title}>Component Title</Text>
      ...
    </View>
  )
}
```

```ts
// screens/ScreenExample/ScreenExample.tsx

import { getScreenTestingIds } from 'test-utils/testIds';

...

export const ScreenExample = ({ ... }: ScreenExampleProps ) => {

  const testIds = getScreenTestingIds('ScreenExample');

  return (
    <ScreenLayout testID={testIds.screenLayout}>
      ...
      <Button testID={testIds.button} />
      ...
    </ScreenLayout>
  )
}
```
---

## **Refreshing App Store Certificates**

To refresh expired app signing certificates, follow the following instructions:

1. Login to the [Apple Development Portal](https://developer.apple.com/account).
2. Select the Certificates, Identifiers, & Profiles option.
3. Go to the `Profiles` tab.
4. Remove any expired provision profiles for branch inventory test application.
5. Clone the [Morsco Certificates Repo](https://github.com/morsco-reece/certificates)
6. Remove the old certificates from the appropriate `certs` directory.
7. Commit and push changes.
8. Go back to the `branch-inventory` directory.
9. Run the `fastlane match distribution` command.

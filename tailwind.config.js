// below to resolve type error with tailwinds resolveConfig method
/** @type {import("@types/tailwindcss/tailwind-config").TailwindConfig } */

module.exports = {
  // darkMode: 'class',
  content: [
    "./src/App.tsx",
    "./src/pages/**/*.{js,jsx,ts,tsx}",
    "./src/components/**/*.{js,jsx,ts,tsx}",
    "./src/components/*.{js,jsx,ts,tsx}",
    "./public/index.html",
  ],
  theme: {
    screens: {
      sm: "640px",
      // => @media (min-width: 640px) { ... }

      md: "768px",
      // => @media (min-width: 768px) { ... }

      lg: "1024px",
      // => @media (min-width: 1024px) { ... }

      xl: "1380px",
      // => @media (min-width: 1280px) { ... }

      "2xl": "1800px",
      // => @media (min-width: 1536px) { ... }

      "3xl": "2560px",
      // => @media (min-width: 2560px) { ... }
    },
    extend: {
      colors: {
        reece: {
          50: "#e5f3ff",
          100: "#b3dcff",
          200: "#80c4ff",
          300: "#4dadff",
          400: "#1a95ff",
          500: "#007ce6",
          600: "#0060b3",
          700: "#004580",
          800: "#003766", // reece blue
          900: "#00294d",
        },
        background: "#ececef",
        // PRIMARY 1 - Dark Blue
        "primary-1": {
          100: "#003766",
          90: "#1A4B76",
          80: "#335F85",
          70: "#4D7394",
          60: "#6687A3",
          50: "#7F9AB2",
          40: "#99AFC2",
          30: "#B3C3D2",
          20: "#CCD7E0",
          10: "#E6EBF0",
        },
        // PRIMARY 2 - Light Blue
        "primary-2": {
          100: "#0B66EC",
          90: "#2476EE",
          80: "#3C85F0",
          70: "#5594F2",
          60: "#6DA3F4",
          50: "#84B2F5",
          40: "#9DC2F7",
          30: "#B6D2FA",
          20: "#CEE0FB",
          10: "#E7F0FE",
          5: "#F4F9FF",
        },
        // PRIMARY 3 - Dark Text Gray
        "primary-3": {
          100: "#404040",
          90: "#545454",
          80: "#666666",
          70: "#7A7A7A",
          60: "#8C8C8C",
          50: "#9F9F9F",
          40: "#B3B3B3",
          30: "#C6C6C6",
          20: "#D9D9D9",
          10: "#ECECEC",
        },
        // SECONDARY 1 - Orange
        "secondary-1": {
          100: "#FFA500",
          90: "#FFAE1A",
          80: "#FFB733",
          70: "#FFC04D",
          60: "#FFC966",
          50: "#FFD17F",
          40: "#FFDB99",
          30: "#FFE4B3",
          20: "#FFEDCC",
          10: "#FFF6E6",
        },
        // SECONDARY 2 - Text Gray
        "secondary-2": {
          100: "#606060",
          90: "#717171",
          80: "#808080",
          70: "#909090",
          60: "#A0A0A0",
          50: "#AFAFAF",
          40: "#C0C0C0",
          30: "#D0D0D0",
          20: "#DFDFDF",
          10: "#F0F0F0",
        },
        // SECONDARY 3 - Medium Gray
        "secondary-3": {
          100: "#CBCBCB",
          90: "#D1D1D1",
          80: "#D5D5D5",
          70: "#DBDBDB",
          60: "#E0E0E0",
          50: "#E4E4E4",
          40: "#EAEAEA",
          30: "#F0F0F0",
          20: "#F5F5F5",
          10: "#FAFAFA",
        },
        // SECONDARY 4 - Light Gray
        "secondary-4": {
          100: "#F2F2F2",
          90: "#F4F4F4",
          80: "#F5F5F5",
          70: "#F6F6F6",
          60: "#F7F7F7",
          50: "#F8F8F8",
          40: "#FAFAFA",
          30: "#FCFCFC",
          20: "#FDFDFD",
          10: "#FEFEFE",
        },
        // SECONDARY 6 - Off White
        "secondary-6": { 100: "#F5F1ED" },
        // SUPPORT 1 - Green
        "support-1": {
          100: "#407A26",
          90: "#54883C",
          80: "#669551",
          70: "#7AA268",
          60: "#8CAF7D",
          50: "#9FBC92",
          40: "#B3CAA8",
          30: "#C6D8BE",
          20: "#D9E4D4",
          10: "#ECF2E9",
        },
        // SUPPORT 2 - Orangey Red
        "support-2": {
          100: "#C82D15",
          90: "#CE422D",
          80: "#D35744",
          70: "#D96C5C",
          60: "#DE8173",
          50: "#E39589",
          40: "#E9ABA1",
          30: "#EFC0B9",
          20: "#F4D5D0",
          10: "#FAEAE8",
        },
        // SUPPORT 3 - Yellow
        "support-3": {
          100: "#9D6601",
          90: "#A7761B",
          80: "#B18534",
          70: "#BB944E",
          60: "#C4A367",
          50: "#CDB27F",
          40: "#D8C299",
          30: "#E2D2B3",
          20: "#EBE0CC",
          10: "#F6F0E6",
        },
        // SUPPORT 4 - Teal
        "support-4": {
          100: "#3DDBCC",
          90: "#51DFD2",
          80: "#64E2D6",
          70: "#78E6DC",
          60: "#8BE9E0",
          50: "#9DECE5",
          40: "#B1F1EB",
          30: "#C5F5F0",
          20: "#D8F8F5",
          10: "#ECFCFA",
        },
        // SUPPORT 5 - Sky Blue
        "support-5": {
          100: "#44C7F4",
          90: "#57CDF6",
          80: "#69D2F6",
          70: "#7DD8F8",
          60: "#8FDDF8",
          50: "#A1E2F9",
          40: "#B4E9FB",
          30: "#C7EFFC",
          20: "#DAF4FD",
          10: "#EDFAFE",
        },
        // SUPPORT 6 - Purple
        "support-6": {
          100: "#8C44EF",
          90: "#9857F1",
          80: "#A369F2",
          70: "#AF7DF4",
          60: "#BA8FF5",
          50: "#C5A1F6",
          40: "#D1B4F9",
          30: "#DDC7FB",
          20: "#E8DAFC",
          10: "#F4EDFE",
        },
      },
    },
  },
};

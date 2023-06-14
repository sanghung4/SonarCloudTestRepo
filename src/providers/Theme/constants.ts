import { Colors, fontFamily, fontSize, FontWeight } from 'constants/style';
import { Theme } from 'react-native-elements';

export const theme: Theme = {
  Button: {
    titleStyle: {
      fontSize: fontSize.BASE,
    },
  },
  Text: {
    h1Style: {
      fontSize: fontSize.H1,
      fontWeight: FontWeight.BOLD,
      lineHeight: 58.8,
    },
    h2Style: {
      fontSize: fontSize.H2,
      fontWeight: FontWeight.BOLD,
      lineHeight: 46.8,
    },
    h3Style: {
      fontSize: fontSize.H3,
      fontWeight: FontWeight.BOLD,
      lineHeight: 37.2,
    },
    h4Style: {
      fontSize: fontSize.H4,
      fontWeight: FontWeight.BOLD,
      lineHeight: 30,
    },
    h5Style: {
      fontSize: fontSize.H5,
      fontWeight: FontWeight.MEDIUM,
      lineHeight: 24,
    },
    smallStyle: {
      color: Colors.SECONDARY_2100,
      fontSize: fontSize.SMALL,
      lineHeight: 16.8,
    },
    captionStyle: {
      color: Colors.SECONDARY_2100,
      fontSize: fontSize.CAPTION,
      lineHeight: 14.4,
    },
    style: {
      color: Colors.PRIMARY_3100,
      fontSize: fontSize.BASE,
      fontFamily: fontFamily.ROBOTO,
      fontWeight: FontWeight.REGULAR,
      lineHeight: 24,
    },
  },
  colors: {
    primary: Colors.PRIMARY_1100,
    secondary: Colors.PRIMARY_2100,
    white: Colors.WHITE,
    black: Colors.BLACK,
    grey0: Colors.PRIMARY_3100,
    grey1: Colors.SECONDARY_2100,
    grey2: Colors.SECONDARY_270,
    grey3: Colors.SECONDARY_3100,
    grey4: Colors.SECONDARY_4100,
    grey5: Colors.SECONDARY_460,
    searchBg: Colors.SECONDARY_460,
    divider: Colors.SECONDARY_3100,
  },
};

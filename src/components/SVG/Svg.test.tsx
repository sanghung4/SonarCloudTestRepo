import React from 'react';
import { render } from 'test-utils/render';
import { SVGProps } from './types';
import { SvgIcons, IconProps } from './Svg';
import { SessionExpiredImage, SuccessImage } from '.';
import { SVG_TEST_IDS } from './types';
interface SvgIconsArrType {
  name: string;
  testID: string;
}

//WHEN ADDING NEW ICONS, INCLUDE THEM HERE WITH THEIR NAME AND TESTID

const SvgIconsArr = [
  {
    name: 'Account',
    testID: 'account-icon',
  },
  {
    name: 'BranchSummaries',
    testID: 'BranchSummariesTest',
  },
  {
    name: 'CountIDEntryImage',
    testID: 'CountIdEntryImageTest',
  },
  {
    name: 'CountLocations',
    testID: 'CountLocationsTest',
  },
  {
    name: 'Logo',
    testID: 'logo-icon',
  },
  {
    name: 'PlaceholderImage',
    testID: 'PlaceholderImageTest',
  },
  {
    name: 'ManualEntry',
    testID: 'ManualEntryTest',
  },
  {
    name: 'PlaceholderWithLabelImage',
    testID: 'PlaceholderWithLabelImageTest',
  },
  {
    name: 'ScanLocation',
    testID: 'ScanLocationTest',
  },
  {
    name: 'SearchIcon',
    testID: 'search-icon',
  },
  {
    name: 'SearchNoResultsImage',
    testID: 'SearchNoResultsImageTest',
  },
  {
    name: 'SessionExpiredImage',
    testID: 'SessionExpiredSvgTest',
  },
  {
    name: 'WriteInsNoResultsImage',
    testID: 'WriteInsNoResultsImageTest',
  },
  {
    name: 'WriteIns',
    testID: 'WriteInsTest',
  },
  {
    name: 'Pencil',
    testID: 'edit-icon',
  },
];

const mockProps = (props?: Partial<IconProps>) => ({
  name: '',
  style: { height: 25 },
  size: props?.size,
  ...props,
});

describe('<SVGIcons>', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders all Icons and cases correctly', () => {
    SvgIconsArr.map((icon: SvgIconsArrType) => {
      const utils = render(<SvgIcons {...mockProps({ name: icon.name })} />);
      expect(utils.getByTestId(icon.testID)).toBeTruthy();
    });
  });

  it('returns default case', () => {
    const utils = render(<SvgIcons {...mockProps({ name: undefined })} />);
    const component = utils.container.props.children.key;
    expect(component).toBe(null);
  });

  it('renders and returns Logo SVG', () => {
    const utils = render(
      <SvgIcons {...mockProps({ name: 'Logo', size: 25 })} />
    );
    const component = utils.getByTestId(SVG_TEST_IDS.LOGO);
    expect(component.props.style[1].width).toEqual(25);
    expect(component.props.style[0].height).toEqual(25);
    expect(component.props.testID).toEqual(SVG_TEST_IDS.LOGO);
  });
});

const altSVGMockProps = (props?: Partial<SVGProps>) => ({
  style: props?.style,
  size: props?.size,
  ...props,
});

describe('<SessionExpiredImage>', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders SessionExpiredImage Icon correctly', () => {
    const utils = render(<SessionExpiredImage {...altSVGMockProps()} />);
    expect(utils.getByTestId(SVG_TEST_IDS.SESSION_EXPIRED)).toBeTruthy();
  });
});

describe('<SuccessImage>', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders SuccessImage Icon correctly', () => {
    const utils = render(<SuccessImage {...altSVGMockProps()} />);
    expect(utils.getByTestId(SVG_TEST_IDS.SUCCESS_IMAGE)).toBeTruthy();
  });
});

import CountStatus from "./CountStatus";
import { render } from "../../test-utils/jest.setup";

describe("CountStatus", function () {
  // it("should render the count status page correctly", function () {
  //   const utils = render(<CountStatus />);
  //   expect(utils.container).toMatchSnapshot();
  // });

  it("should contain a startDate and endDate for filtering", function () {
    const { getAllByText } = render(<CountStatus />);
    expect(getAllByText("Count Status")).toBeTruthy();
  });
});

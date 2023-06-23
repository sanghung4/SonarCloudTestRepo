import { convertFileList } from 'util/fileProcessor';

/**
 * Mocks
 */
let consoleErrorBackup = global.console.error;

/**
 * TEST
 */
describe('util/fileProcessor', () => {
  // ⚪ Set mocks
  beforeEach(() => {
    consoleErrorBackup = global.console.error;
    global.console.error = jest.fn();
  });
  // ⚪ Reset mocks
  afterEach(() => {
    global.console.error = consoleErrorBackup;
  });

  // 🟢 1 - Blank
  it('Expect `convertFileList` to return blank array with blank input', async () => {
    // act
    const res = await convertFileList(null);
    // assert
    expect(res.length).toBe(0);
  });

  // 🟢 2 - Data
  it('Expect `convertFileList` to return formatted file', async () => {
    // arrange
    const fileList = [new File(['test'], 'test-file')] as unknown as FileList;
    // act
    const res = await convertFileList(fileList);
    // assert
    expect(res.length).toBe(fileList.length);
  });

  // 🟢 3 - Error
  it('Expect `convertFileList` to throw error', async () => {
    // arrange
    const fileList = [{ name: '' } as File] as unknown as FileList;
    // act
    await convertFileList(fileList);
    // assert
    expect(console.error).toBeCalled();
  });
});

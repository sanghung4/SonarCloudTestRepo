import { sortByMemo } from 'Contracts/util/useContractsData';

describe('Contracts - util/useContractsData', () => {
  it('expect `sortByMemo` to return "lastRelease"', () => {
    const [{ id }] = sortByMemo(['lastReleaseDate']);
    expect(id).toBe('lastRelease');
  });

  it('expect `sortByMemo` to return "firstRelease"', () => {
    const [{ id }] = sortByMemo(['firstReleaseDate']);
    expect(id).toBe('firstRelease');
  });

  it('expect `sortByMemo` to return "other"', () => {
    const [{ id }] = sortByMemo(['!other']);
    expect(id).toBe('other');
  });
});

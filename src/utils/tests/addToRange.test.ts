import { DateRange } from 'react-day-picker';
import { addToRange } from 'utils/addToRange';

const today = new Date();
const dayAfterTomorrow = new Date(today);
const dayBeforeYesterday = new Date(today);
const weekAfterTomorrow = new Date(today);
dayAfterTomorrow.setDate(dayAfterTomorrow.getDate() + 2);
weekAfterTomorrow.setDate(dayAfterTomorrow.getDate() + 7);
dayBeforeYesterday.setDate(dayBeforeYesterday.getDate() - 2);

describe('Utils - Add to Range', () => {
  it('expect `undefined` range set both to today', () => {
    const result = addToRange(today);
    expect(result?.from).toBe(today);
    expect(result?.to).toBe(today);
  });

  it('expect when "from = today & to = null" to return both undefined', () => {
    const range: DateRange = { from: today };
    const result = addToRange(today, range);
    expect(result?.from).toBe(undefined);
    expect(result?.to).toBe(undefined);
  });

  it('expect when "from = today+2 & to = null" to return `from` as today and `to` as today+2', () => {
    const range: DateRange = { from: dayAfterTomorrow };
    const result = addToRange(today, range);
    expect(result?.from).toBe(today);
    expect(result?.to).toBe(dayAfterTomorrow);
  });

  it('expect when "from = today-2 & to = null" to return `from` as today-2 and `to` as today', () => {
    const range: DateRange = { from: dayBeforeYesterday };
    const result = addToRange(today, range);
    expect(result?.from).toBe(dayBeforeYesterday);
    expect(result?.to).toBe(today);
  });

  it('expect when both `to` and `from` is today to return both undefined', () => {
    const range: DateRange = { from: today, to: today };
    const result = addToRange(today, range);
    expect(result?.from).toBe(undefined);
    expect(result?.to).toBe(undefined);
  });

  it('expect when "from = today-2 & to = today" to return both as `to`', () => {
    const range: DateRange = { from: dayBeforeYesterday, to: today };
    const result = addToRange(today, range);
    expect(result?.from).toBe(today);
    expect(result?.to).toBe(today);
  });

  it('expect when "from = today & to = today+2" to return both as undefined', () => {
    const range: DateRange = { from: today, to: dayAfterTomorrow };
    const result = addToRange(today, range);
    expect(result?.from).toBe(undefined);
    expect(result?.to).toBe(undefined);
  });

  it('expect when "from = today+2 & to = today+7" to return `from` as today and `to` as today+7', () => {
    const range: DateRange = { from: dayAfterTomorrow, to: weekAfterTomorrow };
    const result = addToRange(today, range);
    expect(result?.from).toBe(today);
    expect(result?.to).toBe(weekAfterTomorrow);
  });

  it('expect when "from = today-2 & to = today+2" to return `from` the same and `to` as today', () => {
    const range: DateRange = {
      from: dayBeforeYesterday,
      to: weekAfterTomorrow
    };
    const result = addToRange(today, range);
    expect(result?.from).toBe(dayBeforeYesterday);
    expect(result?.to).toBe(today);
  });
});

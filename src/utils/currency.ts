const formatter = new Intl.NumberFormat('en-US', {
  style: 'currency',
  currency: 'USD',
  minimumFractionDigits: 2
});

export function format(val: number) {
  return formatter.format(val);
}

export function formatFromCents(val: number) {
  return formatter.format(val / 100);
}

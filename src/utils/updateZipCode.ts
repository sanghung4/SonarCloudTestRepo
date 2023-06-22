import { ChangeEvent } from 'react';

export default function updateZipcode(
  updateForm: (e: ChangeEvent<any>) => void
) {
  return (e: ChangeEvent<HTMLInputElement>) => {
    const { value } = e.target;
    e.target.value = value.replace(/\D/g, '').slice(0, 5);
    updateForm(e);
  };
}

import { success, SuccessType } from 'constants/success';
import { AlertPayload } from 'providers/Overlay/types';

export const getSuccess = (type: keyof typeof success): AlertPayload => {
  const payload = success[type] ? success[type] : success[SuccessType.GENERIC];
  return {
    svg: 'SuccessImage',
    options: { centered: true },
    ...payload,
  } as AlertPayload;
};

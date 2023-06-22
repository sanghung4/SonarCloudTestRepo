export const setItem = (key: string, value: string, userId?: string) => {
  localStorage.setItem(userId ? `${key}-${userId}` : key, value);
};

export const getItem = (key: string, userId?: string) => {
  return localStorage.getItem(userId ? `${key}-${userId}` : key);
};

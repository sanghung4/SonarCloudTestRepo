export function mockGoogle() {
  global.window.google = {
    maps: {
      // @ts-ignore
      places: {
        Autocomplete: jest.fn().mockImplementation(() => ({
          setFields: jest.fn(),
          setComponentRestrictions: jest.fn(),
          addListener: jest.fn()
        }))
      },
      Size: jest.fn(),
      Point: jest.fn()
    }
  };
}

export function mockGeolocation(lat: number, lng: number, err?: boolean) {
  // @ts-ignore
  global.navigator.geolocation = {
    getCurrentPosition: jest.fn().mockImplementation((onSuccess, onError) => {
      onSuccess({
        coords: {
          latitude: lat ?? 0,
          longitude: lng ?? 0
        }
      });
      if (err) {
        onError(new Error());
      }
    }),
    watchPosition: jest.fn().mockImplementation((onSuccess, onError) => {
      onSuccess({
        coords: {
          latitude: lat ?? 0,
          longitude: lng ?? 0
        }
      });
      if (err) {
        onError(new Error());
      }
    })
  };
}

export function mockLocalStorage() {
  // @ts-ignore
  global.window.localStorage = class mockLocalStorage {
    store: { [key: string]: string };
    constructor() {
      this.store = {};
    }
    clear() {
      this.store = {};
    }
    get length() {
      return Object.keys(this.store).length;
    }
    getItem(key: string) {
      return this.store[key] || null;
    }
    key(i: number) {
      return Object.keys(this.store)[i] || null;
    }
    removeItem(key: string) {
      delete this.store[key];
    }
    setItem(key: string, value: string) {
      this.store[key] = String(value);
    }
  };
}

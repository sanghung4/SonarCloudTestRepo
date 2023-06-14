import { BarCodeScanner, PermissionStatus } from 'expo-barcode-scanner';
import { useEffect, useState } from 'react';

export const usePermissions = (): {
  status: PermissionStatus;
  granted: boolean;
} => {
  const [permission, setPermission] = useState(PermissionStatus.UNDETERMINED);

  useEffect(() => {
    (async () => {
      const { status } = await BarCodeScanner.requestPermissionsAsync();
      setPermission(status);
    })();
  }, []);

  return {
    status: permission,
    granted: permission === PermissionStatus.GRANTED,
  };
};

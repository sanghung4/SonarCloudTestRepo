import { useEffect } from 'react';
import { loadLightningComponent } from './utils/util';
import { Box, useScreenSize } from '@dialexa/reece-component-library';

function ModifyAppointment() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();

  useEffect(() => {
    loadLightningComponent(
      'lexcontainercancel',
      'Inbound_Cancel_Appointment_custom'
    );
    loadLightningComponent(
      'lexcontainermodify',
      'Inbound_Modify_Appointment_Custom1'
    );
  }, []);

  return (
    <>
      <Box bgcolor="common.white">
        <Box py={2} mx={isSmallScreen ? 0 : 20}>
          <h2>Cancel Appointment</h2>
          <div id="lexcontainercancel"></div>
        </Box>
        <Box py={2} mx={isSmallScreen ? 0 : 20}>
          {/* Manage Appointments */}
          <h2>Modify Appointment</h2>
          <div id="lexcontainermodify"></div>
        </Box>
      </Box>
    </>
  );
}

export default ModifyAppointment;

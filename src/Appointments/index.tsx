import { useEffect } from 'react';
import { loadLightningComponent } from './utils/util';
import { Box, useScreenSize } from '@dialexa/reece-component-library';
import './appointment.css';

function Appointments() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();

  useEffect(() => {
    loadLightningComponent('lexcontainer', 'Inbound_Custom_Guest_Appointment');
  }, []);

  return (
    <>
      {/* New Appointments */}
      <Box bgcolor="common.white">
        <Box
          py={2}
          mx={isSmallScreen ? 0 : 20}
          minHeight={isSmallScreen ? 520 : 340}
        >
          <div id="lexcontainer"></div>
        </Box>
      </Box>
    </>
  );
}

export default Appointments;

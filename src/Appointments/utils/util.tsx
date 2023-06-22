declare global {
  interface Window {
    $Lightning: any;
  }
}

// CONSTANTS
export const LIGHTNING_SCRIPT_URL =
  'https://reeceusa.my.salesforce-sites.com/lightning/lightning.out.js';

// FUNCTIONS
export const loadLightningComponent = (idDOM: string, flowName: string) => {
  const script = document.createElement('script');
  script.src = LIGHTNING_SCRIPT_URL;
  script.async = true;

  script.onload = () => {
    window.$Lightning.use(
      'runtime_appointmentbooking:lightningOutGuest',
      function () {
        // Callback once framework and app load
        window.$Lightning.createComponent(
          'lightning:flow', // top-level component of your app
          {}, // attributes to set on the component when created
          idDOM, // the DOM location to insert the component
          function (component: any) {
            // API name of the Flow
            component.startFlow(flowName);
          }
        );
      },
      'https://reeceusa.my.salesforce-sites.com/' // Site endpoint
    );
  };
  document.body.appendChild(script);
};

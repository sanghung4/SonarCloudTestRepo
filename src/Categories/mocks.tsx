import { DocumentNode } from 'graphql';
import {
  ProductCategoriesDocument,
  ProductCategoriesQuery
} from 'generated/graphql';

export const success: MockCategory = {
  request: {
    query: ProductCategoriesDocument,
    variables: {
      engines: 'plumbing_hvac'
    }
  },
  result: {
    data: {
      productCategories: {
        categories: [
          {
            name: 'Faucets, Fixtures & Appliances',
            children: [
              {
                name: 'TBC',
                children: [
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Faucets, Showers & Bathroom Accessories',
                children: [
                  {
                    name: 'Showers',
                    __typename: 'Category'
                  },
                  {
                    name: 'Bathroom Accessories',
                    __typename: 'Category'
                  },
                  {
                    name: 'Tub & Shower',
                    __typename: 'Category'
                  },
                  {
                    name: 'Bathroom Faucets',
                    __typename: 'Category'
                  },
                  {
                    name: 'Kitchen Faucets',
                    __typename: 'Category'
                  },
                  {
                    name: 'Tub Filler',
                    __typename: 'Category'
                  },
                  {
                    name: 'Spare Parts',
                    __typename: 'Category'
                  },
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  },
                  {
                    name: 'Kitchen Accessories',
                    __typename: 'Category'
                  },
                  {
                    name: 'CATCLEAN',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Appliances',
                children: [
                  {
                    name: 'Refrigerators And Freezers',
                    __typename: 'Category'
                  },
                  {
                    name: 'Hoods and Vents',
                    __typename: 'Category'
                  },
                  {
                    name: 'Outdoor Grills and BBQ',
                    __typename: 'Category'
                  },
                  {
                    name: 'Ranges and Stoves',
                    __typename: 'Category'
                  },
                  {
                    name: 'Microwaves',
                    __typename: 'Category'
                  },
                  {
                    name: 'Garbage Disposals',
                    __typename: 'Category'
                  },
                  {
                    name: 'Appliance Accessories',
                    __typename: 'Category'
                  },
                  {
                    name: 'Dishwashers',
                    __typename: 'Category'
                  },
                  {
                    name: 'Dryers',
                    __typename: 'Category'
                  },
                  {
                    name: 'Ovens',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Toilets, Urinals, Sinks & Flush Valves',
                children: [
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  },
                  {
                    name: 'Toilet & Flush Valve Spare Parts',
                    __typename: 'Category'
                  },
                  {
                    name: 'CATCLEAN',
                    __typename: 'Category'
                  },
                  {
                    name: 'Manual Flush Valves',
                    __typename: 'Category'
                  },
                  {
                    name: 'Spare Parts',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Fans & Ventilation',
                children: [
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Tools',
                children: [],
                __typename: 'Category'
              }
            ],
            __typename: 'Category'
          },
          {
            name: 'Plumbing Installation, Tools, Hardware & Safety',
            children: [
              {
                name: 'Plumbing Installation',
                children: [
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  },
                  {
                    name: 'Pipe Clamps',
                    __typename: 'Category'
                  },
                  {
                    name: 'Pipe Insulation',
                    __typename: 'Category'
                  },
                  {
                    name: 'Faucet and Toilet Parts',
                    __typename: 'Category'
                  },
                  {
                    name: 'Gaskets',
                    __typename: 'Category'
                  },
                  {
                    name: 'Pipe Hangers',
                    __typename: 'Category'
                  },
                  {
                    name: 'Riser',
                    __typename: 'Category'
                  },
                  {
                    name: 'Insulation',
                    __typename: 'Category'
                  },
                  {
                    name: 'Anchors and Settings',
                    __typename: 'Category'
                  },
                  {
                    name: 'Compression Fittings',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Tools',
                children: [
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  },
                  {
                    name: 'Power Tool',
                    __typename: 'Category'
                  },
                  {
                    name: 'Brushes',
                    __typename: 'Category'
                  },
                  {
                    name: 'Tool',
                    __typename: 'Category'
                  },
                  {
                    name: 'Shovels',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'TBC',
                children: [
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Hardware',
                children: [
                  {
                    name: 'Screws',
                    __typename: 'Category'
                  },
                  {
                    name: 'Washers',
                    __typename: 'Category'
                  },
                  {
                    name: 'Nuts',
                    __typename: 'Category'
                  },
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  },
                  {
                    name: 'Bolts',
                    __typename: 'Category'
                  },
                  {
                    name: 'Pipe Hangers',
                    __typename: 'Category'
                  },
                  {
                    name: 'Nails',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Safety',
                children: [
                  {
                    name: 'Gloves',
                    __typename: 'Category'
                  },
                  {
                    name: 'Glasses',
                    __typename: 'Category'
                  },
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  },
                  {
                    name: 'Hard Hat',
                    __typename: 'Category'
                  },
                  {
                    name: 'Vest',
                    __typename: 'Category'
                  },
                  {
                    name: 'Shoe Covers',
                    __typename: 'Category'
                  },
                  {
                    name: 'Beanie',
                    __typename: 'Category'
                  },
                  {
                    name: 'Ear Plugs',
                    __typename: 'Category'
                  },
                  {
                    name: 'Face Shield',
                    __typename: 'Category'
                  },
                  {
                    name: 'Knee Pads',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Tools, Hardware & Safety',
                children: [
                  {
                    name: 'CATCLEAN',
                    __typename: 'Category'
                  },
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  },
                  {
                    name: 'Brushes',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Appliances',
                children: [
                  {
                    name: 'Garbage Disposals',
                    __typename: 'Category'
                  },
                  {
                    name: 'Dishwashers',
                    __typename: 'Category'
                  },
                  {
                    name: 'Washing Machines',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Pipe Accessories',
                children: [
                  {
                    name: 'Pipe Insulation',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'HVAC',
                children: [
                  {
                    name: 'CATCLEAN',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Discontinued',
                children: [],
                __typename: 'Category'
              }
            ],
            __typename: 'Category'
          },
          {
            name: 'Pipe & Fittings',
            children: [
              {
                name: 'Metal Pipe & Fittings',
                children: [
                  {
                    name: 'Copper Tube & Fittings',
                    __typename: 'Category'
                  },
                  {
                    name: 'Steel Pipe & Fittings',
                    __typename: 'Category'
                  },
                  {
                    name: 'Cast Iron Pipe & Fittings',
                    __typename: 'Category'
                  },
                  {
                    name: 'Brass Pipe & Fittings',
                    __typename: 'Category'
                  },
                  {
                    name: 'Ductile Iron Pipe & Fittings',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Plastic Pipe & Fittings - Supply',
                children: [
                  {
                    name: 'PEX Pipe & Fittings',
                    __typename: 'Category'
                  },
                  {
                    name: 'CPVC Pipe & Fittings',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Plastic Pipe & Fittings - Drainage',
                children: [
                  {
                    name: 'PVC Pipe & Fittings',
                    __typename: 'Category'
                  },
                  {
                    name: 'ABS Pipe & Fittings',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'TBC',
                children: [
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Plumbing Installation',
                children: [
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              }
            ],
            __typename: 'Category'
          },
          {
            name: 'Heating & Cooling',
            children: [
              {
                name: 'Air Distribution',
                children: [
                  {
                    name: 'Sheet Metal Pipe & Fittings',
                    __typename: 'Category'
                  },
                  {
                    name: 'Grills, Registers & Diffusers',
                    __typename: 'Category'
                  },
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  },
                  {
                    name: 'Flex Duct',
                    __typename: 'Category'
                  },
                  {
                    name: 'Sheet Metal Duct Pipe and Fittings',
                    __typename: 'Category'
                  },
                  {
                    name: 'Access & Inspection Panels',
                    __typename: 'Category'
                  },
                  {
                    name: 'CEILING RADIATION DAMPERS',
                    __typename: 'Category'
                  },
                  {
                    name: 'Grills, Registers, & Diffusers',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Repair & Replacement Parts',
                children: [
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  },
                  {
                    name: 'Capacitors',
                    __typename: 'Category'
                  },
                  {
                    name: 'OEM Parts',
                    __typename: 'Category'
                  },
                  {
                    name: 'Contactors, Relays, Transformers',
                    __typename: 'Category'
                  },
                  {
                    name: 'Motors',
                    __typename: 'Category'
                  },
                  {
                    name: 'Driers & Sight Glasses',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'TBC',
                children: [
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Residential Equipment',
                children: [
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  },
                  {
                    name: 'AC Condensers',
                    __typename: 'Category'
                  },
                  {
                    name: 'Residential Accessories',
                    __typename: 'Category'
                  },
                  {
                    name: 'Air Handlers',
                    __typename: 'Category'
                  },
                  {
                    name: 'HP Condensers',
                    __typename: 'Category'
                  },
                  {
                    name: 'Coils',
                    __typename: 'Category'
                  },
                  {
                    name: 'Furnaces',
                    __typename: 'Category'
                  },
                  {
                    name: 'Packaged Systems',
                    __typename: 'Category'
                  },
                  {
                    name: 'furnaces',
                    __typename: 'Category'
                  },
                  {
                    name: 'Condensers',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Commercial Equipment',
                children: [
                  {
                    name: 'Commercial Packaged Systems',
                    __typename: 'Category'
                  },
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  },
                  {
                    name: 'Commercial Air Handlers',
                    __typename: 'Category'
                  },
                  {
                    name: 'Geothermal',
                    __typename: 'Category'
                  },
                  {
                    name: 'Packaged Systems',
                    __typename: 'Category'
                  },
                  {
                    name: 'Residential Accessories',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Installation Supplies',
                children: [
                  {
                    name: 'Tapes, Silicon, & Adhesives',
                    __typename: 'Category'
                  },
                  {
                    name: 'Electrical',
                    __typename: 'Category'
                  },
                  {
                    name: 'Insulation',
                    __typename: 'Category'
                  },
                  {
                    name: 'CATCLEAN',
                    __typename: 'Category'
                  },
                  {
                    name: 'Condensate Management',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Fans & Ventilation',
                children: [
                  {
                    name: 'Gas Vent Pipe & Fittings',
                    __typename: 'Category'
                  },
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  },
                  {
                    name: 'Ventilation Fans',
                    __typename: 'Category'
                  },
                  {
                    name: 'CATCLEAN',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'IAQ',
                children: [
                  {
                    name: 'Air Filters',
                    __typename: 'Category'
                  },
                  {
                    name: 'Air Cleaners',
                    __typename: 'Category'
                  },
                  {
                    name: 'Dehumidifiers',
                    __typename: 'Category'
                  },
                  {
                    name: 'CATCLEAN',
                    __typename: 'Category'
                  },
                  {
                    name: 'Humidifiers',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Controls, Sensors, & Zoning',
                children: [
                  {
                    name: 'Thermostats',
                    __typename: 'Category'
                  },
                  {
                    name: 'Zoning',
                    __typename: 'Category'
                  },
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Ductless',
                children: [
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  },
                  {
                    name: 'Condensers',
                    __typename: 'Category'
                  },
                  {
                    name: 'Air Handlers',
                    __typename: 'Category'
                  },
                  {
                    name: 'Residential Accessories',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              }
            ],
            __typename: 'Category'
          },
          {
            name: 'Hot Water, Valves, Irrigation & Pumps',
            children: [
              {
                name: 'TBC',
                children: [
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Water Heater Parts & Accessories',
                children: [
                  {
                    name: 'Venting',
                    __typename: 'Category'
                  },
                  {
                    name: 'Water Heater Accessories',
                    __typename: 'Category'
                  },
                  {
                    name: 'Commercial Conversion Kits',
                    __typename: 'Category'
                  },
                  {
                    name: 'Burners',
                    __typename: 'Category'
                  },
                  {
                    name: 'Heating Element',
                    __typename: 'Category'
                  },
                  {
                    name: 'Gas Valves',
                    __typename: 'Category'
                  },
                  {
                    name: 'Switches',
                    __typename: 'Category'
                  },
                  {
                    name: 'Expansion Tanks',
                    __typename: 'Category'
                  },
                  {
                    name: 'Instantaneous',
                    __typename: 'Category'
                  },
                  {
                    name: 'Electronic Boards',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Specialty Plumbing Valves',
                children: [
                  {
                    name: 'Hydrants/Flow Control Valves',
                    __typename: 'Category'
                  },
                  {
                    name: 'Pressure Reducing Valves',
                    __typename: 'Category'
                  },
                  {
                    name: 'Wye Strainers',
                    __typename: 'Category'
                  },
                  {
                    name: 'Valve Kits',
                    __typename: 'Category'
                  },
                  {
                    name: 'Relief Valves',
                    __typename: 'Category'
                  },
                  {
                    name: 'Thermostatic Mixing Valves',
                    __typename: 'Category'
                  },
                  {
                    name: 'Hose Bibbs',
                    __typename: 'Category'
                  },
                  {
                    name: 'Dielectric Unions',
                    __typename: 'Category'
                  },
                  {
                    name: 'Sillcocks',
                    __typename: 'Category'
                  },
                  {
                    name: 'Automatic Control Valves',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Import Valves',
                children: [
                  {
                    name: 'Ball Valves',
                    __typename: 'Category'
                  },
                  {
                    name: 'Check Valves',
                    __typename: 'Category'
                  },
                  {
                    name: 'Gate Valves',
                    __typename: 'Category'
                  },
                  {
                    name: 'Balancing Valves',
                    __typename: 'Category'
                  },
                  {
                    name: 'Butterfly Valves',
                    __typename: 'Category'
                  },
                  {
                    name: 'Valve Accessories',
                    __typename: 'Category'
                  },
                  {
                    name: 'Globe Valves',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Domestic Valves',
                children: [
                  {
                    name: 'Ball Valves',
                    __typename: 'Category'
                  },
                  {
                    name: 'Check Valves',
                    __typename: 'Category'
                  },
                  {
                    name: 'Butterfly Valves',
                    __typename: 'Category'
                  },
                  {
                    name: 'Gate Valves',
                    __typename: 'Category'
                  },
                  {
                    name: 'Valve Accessories',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Pumps',
                children: [
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  },
                  {
                    name: 'Parts and Accessories',
                    __typename: 'Category'
                  },
                  {
                    name: 'CATCLEAN',
                    __typename: 'Category'
                  },
                  {
                    name: 'Recirculating Pumps',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Backflow Preventers & Accessories',
                children: [
                  {
                    name: 'Backflow Preventers',
                    __typename: 'Category'
                  },
                  {
                    name: 'Backflow Preventer Vacuum Breakers',
                    __typename: 'Category'
                  },
                  {
                    name: 'Backflow Preventer Kits',
                    __typename: 'Category'
                  },
                  {
                    name: 'Air Gaps',
                    __typename: 'Category'
                  },
                  {
                    name: 'Backflow Preventer Accessories',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Water Treatment',
                children: [
                  {
                    name: 'Water Filters',
                    __typename: 'Category'
                  },
                  {
                    name: 'Water Filter Parts & Accessories',
                    __typename: 'Category'
                  },
                  {
                    name: 'Water Softener Parts & Accessories',
                    __typename: 'Category'
                  },
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  },
                  {
                    name: 'Water Softeners',
                    __typename: 'Category'
                  },
                  {
                    name: 'CATCLEAN',
                    __typename: 'Category'
                  },
                  {
                    name: 'Water Conditioners',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Water Heaters - Residential',
                children: [
                  {
                    name: 'Gas',
                    __typename: 'Category'
                  },
                  {
                    name: 'Electric',
                    __typename: 'Category'
                  },
                  {
                    name: 'Heat Pump',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              },
              {
                name: 'Water Heaters - Commercial',
                children: [
                  {
                    name: 'Electric',
                    __typename: 'Category'
                  },
                  {
                    name: 'Gas',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              }
            ],
            __typename: 'Category'
          },
          {
            name: 'TBC',
            children: [
              {
                name: 'TBC',
                children: [
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  },
                  {
                    name: 'Sheet Metal Pipe & Fittings',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              }
            ],
            __typename: 'Category'
          },
          {
            name: 'Faucets, Fixtures Appliances',
            children: [
              {
                name: 'TBC',
                children: [
                  {
                    name: 'TBC',
                    __typename: 'Category'
                  }
                ],
                __typename: 'Category'
              }
            ],
            __typename: 'Category'
          },
          {
            name: 'Faucets, FiXtures & Appliances',
            children: [],
            __typename: 'Category'
          }
        ],
        __typename: 'ProductCategories'
      },
      __typename: 'Query'
    }
  }
};

type MockCategory = {
  request: {
    query: DocumentNode;
    variables: {
      engines?: string;
    };
  };
  result: {
    data: ProductCategoriesQuery | undefined;
  };
};

import { SearchProductDocument } from 'generated/graphql';

const mocks = [
  {
    request: {
      query: SearchProductDocument,
      variables: {
        productSearch: {
          searchTerm: 'copper',
          page: 1,
          size: 12,
          categoryLevel: 0,
          categories: [],
          filters: [],
          engine: 'plumbing_hvac'
        },
        shipToAccountId: 'shipto',
        userId: 'testuser'
      }
    },
    result: {
      data: {
        searchProduct: {
          pagination: {
            currentPage: 1,
            pageSize: 12,
            totalItemCount: 1
          },
          products: [
            {
              id: 'MSC-31747',
              partNumber: '31747',
              name: '3/4" Wrot Copper Reducing Coupling',
              manufacturerName: 'Generic',
              manufacturerNumber: 'CMAF',
              taxonomy: null,
              categories: [
                'Pipe & Fittings',
                'Metal Pipe & Fittings',
                'Copper Tube & Fittings'
              ],
              technicalDocuments: [
                {
                  name: 'MFR Catalog Page',
                  url: 'http://images.tradeservice.com/ATTACHMENTS/DIR100145/MUELERE00056_18_19.pdf'
                },
                {
                  name: 'Technical Specification',
                  url: 'http://images.tradeservice.com/ATTACHMENTS/DIR100240/MUELERE03072_1.pdf'
                },
                {
                  name: 'MFR Item Data',
                  url: 'http://images.tradeservice.com/ATTACHMENTS/DIR100145/MUELERE00577_1.pdf'
                }
              ],
              environmentalOptions: ['Low lead compliant'],
              upc: '68576823249',
              unspsc: '40183109',
              seriesModelFigureNumber: null,
              productOverview:
                'Adapter Fitting; Type Male, Straight; Size A 3/4"; Size B 3/4"; Thickness 0.033"; End Connection Female Soldered x Male Threaded; Material Copper; Material Specification ASTM B75; Process Wrot; Application Potable Water; Applicable Standard ASME B16.22/B1.20.1, NSF 61G; Packaging Quantity 25 per Inner Carton, 250 per Master Carton',
              featuresAndBenefits:
                'Lead Free; Copper Solder-Joint Fittings for Supply/Pressurized Systems have Been the Leading Brand of Copper Fittings for Over 80 Years; Available in Both Wrot Copper and Cast Bronze; The Acknowledged Experts at Engineering and Manufacturing Precision Solder-Joint Copper Fittings; Quality, Consistency and Reliability have Made the Streamline Brand Trusted and Specified All Around the World',
              techSpecifications: [
                {
                  name: 'material',
                  value: 'Copper'
                },
                {
                  name: 'Applicable Standard',
                  value: 'ASME B16.22/B1.20.1, NSF 61G'
                },
                {
                  name: 'Application',
                  value: 'Potable Water'
                },
                {
                  name: 'End Connection',
                  value: 'Female Soldered x Male Threaded'
                },
                {
                  name: 'Material',
                  value: 'Copper'
                },
                {
                  name: 'Material Specification',
                  value: 'ASTM B75'
                },
                {
                  name: 'Process',
                  value: 'Wrot'
                },
                {
                  name: 'Size A',
                  value: '3/4"'
                },
                {
                  name: 'Size B',
                  value: '3/4"'
                },
                {
                  name: 'Thickness',
                  value: '0.033"'
                },
                {
                  name: 'Type',
                  value: 'Male, Straight'
                }
              ],
              imageUrls: {
                thumb:
                  'http://images.tradeservice.com/ProductImages/DIR100103/MUELER_WB01130_SML.jpg',
                small:
                  'http://images.tradeservice.com/ProductImages/DIR100103/MUELER_WB01130_SML.jpg',
                medium:
                  'http://images.tradeservice.com/ProductImages/DIR100103/MUELER_WB01130_MED.jpg',
                large:
                  'http://images.tradeservice.com/ProductImages/DIR100103/MUELER_WB01130_LRG.jpg'
              },
              packageDimensions: null,
              minIncrementQty: 0,
              erp: 'ECLIPSE'
            }
          ],
          aggregates: {
            inStockLocation: [
              {
                value: 'in_stock_location',
                count: 595
              }
            ],
            category: [
              {
                value: 'Pipe & Fittings',
                count: 1595
              }
            ],
            brands: [
              {
                value: 'Generic',
                count: 953
              }
            ]
          },
          selectedAttributes: []
        }
      }
    }
  }
];

export const mockNoProducts = [
  {
    request: {
      query: SearchProductDocument,
      variables: {
        productSearch: {
          searchTerm: 'copper',
          page: 1,
          size: 12,
          categoryLevel: 0,
          categories: [],
          filters: [],
          engine: 'plumbing_hvac'
        },
        shipToAccountId: 'shipto',
        userId: 'testuser'
      }
    },
    result: {
      data: {
        searchProduct: null
      }
    }
  }
];

export default mocks;

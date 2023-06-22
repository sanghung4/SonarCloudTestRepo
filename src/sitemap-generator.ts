// istanbul ignore file
import axios from 'axios';
// import fetch from "node-fetch"
import https from 'https';
import router from './SitemapRoutes';
// require('dotenv').config();
require('dotenv').config({ path: '.env.development' });
const Sitemap = require('react-router-sitemap').default;

function generateSitemap(paramsConfig: Object) {
  const url = process.env.REACT_APP_URL || '';
  return new Sitemap(router)
    .applyParams(paramsConfig)
    .build(url, { limitCountPaths: 10000 })
    .save('./public/sitemap.xml');
}

async function getProducts() {
  try {
    const httpsAgent = new https.Agent({
      rejectUnauthorized: false
    });
    axios.defaults.httpsAgent = httpsAgent;
    const url = process.env.REACT_APP_API_URL || '';
    const apiSecret = process.env.REACT_APP_MAX_API_SECRET || '';
    const response = await axios.post(
      url,
      {
        query:
          'query AllProducts{\r\n  allProducts {\r\n    ...ProductResponse\r\n    __typename\r\n  }\r\n}\r\nfragment ProductResponse on ProductResponse {\r\n  id\r\n  name\r\n  __typename\r\n}'
      },
      {
        headers: {
          'x-max-api-secret': apiSecret
        }
      }
    );
    interface Product {
      id: string;
      name: string;
    }
    const allProducts = response.data.data.allProducts.map(
      (product: Product) => {
        product.name = product?.name?.replace(/(\/| - |\s|\.)/g, '-');
        product.name = product?.name?.replace(/[^a-zA-Z0-9- ]/g, '');
        product.name = product?.name?.toLowerCase();
        return product;
      }
    );
    const paramsConfig = {
      '/product/:name/:id': allProducts
    };

    generateSitemap(paramsConfig);
  } catch (error) {
    console.error('ERROR', error);
  }
}

getProducts();

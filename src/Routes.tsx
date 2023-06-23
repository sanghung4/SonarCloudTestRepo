import { LoginCallback } from '@okta/okta-react';
import { Routes as RoutesSwitch, Route } from 'react-router-dom';

import Catalog from 'pages/Catalog';
import Customer from 'pages/Customer';
import Home from 'pages/Home';
import Login from 'pages/Login';

function Routes() {
  return (
    <RoutesSwitch>
      <Route path="/" element={<Home />} />
      <Route path="/login" element={<Login />} />
      <Route path="/login/callback" element={<LoginCallback />} />
      <Route path="/customer/detail/:id" element={<Customer />} />
      <Route path="/catalog/detail/:id" element={<Catalog />} />
    </RoutesSwitch>
  );
}

export default Routes;

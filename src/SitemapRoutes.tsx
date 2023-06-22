import React from 'react';

import { Switch, Route } from 'react-router';

export default (
  <Switch>
    <Route path="/register" />
    <Route path="/forgot-password" />
    <Route path="/search" />
    <Route path="/privacy-policy" />
    <Route path="/terms-of-access" />
    <Route path="/terms-of-sale" />
    <Route path="/do-not-sell-my-info" />
    <Route path="/login" />
    <Route path="/logout" />
    <Route path="/location-search" />
    <Route path="/support" />
    <Route path="/max-welcome" />
    <Route path="/credit-forms" />
    <Route path="/company" />
    <Route path="/about" />
    <Route path="/news" />
    <Route path="/newsdetails/:id" />
    <Route path="/works-for-you" />
    <Route path="/brands/posh" />
    <Route path="/brands/bigdog" />
    <Route path="/brands" />
    <Route path="/jobform" />
    <Route path="/product/:name/:id" />
  </Switch>
);

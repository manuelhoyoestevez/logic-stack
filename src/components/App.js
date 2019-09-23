import React, { Component } from 'react';

import { Header } from './Header';
import { Navigation } from './Navigation';
import { Table } from './Table';

export class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      appTitle: 'awesome',
      navigation: [
        {
          label: 'Go to Google',
          href: 'http://www.google.es'
        },
        {
          label: 'Go to Marca',
          href: 'http://www.marca.com'
        }
      ]
    };
  }

  render() {
    const { appTitle, navigation } = this.state;
    const PRODUCTS = [
      {category: 'Sporting Goods', price: '$49.99', stocked: true, name: 'Football'},
      {category: 'Sporting Goods', price: '$9.99', stocked: true, name: 'Baseball'},
      {category: 'Sporting Goods', price: '$29.99', stocked: false, name: 'Basketball'},
      {category: 'Electronics', price: '$99.99', stocked: true, name: 'iPod Touch'},
      {category: 'Electronics', price: '$399.99', stocked: false, name: 'iPhone 5'},
      {category: 'Electronics', price: '$199.99', stocked: true, name: 'Nexus 7'}
    ];

    return (
      <div>
        <Header title={appTitle} />
        <Navigation items={navigation} />
        <Table table={[[1, 2], [3, 4]]} />
      </div>
    );
  }
}

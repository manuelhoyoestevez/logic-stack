import React from 'react';
import ReactDOM from 'react-dom';
import { graphviz } from 'd3-graphviz';
import { App } from './components/App';

const app = document.getElementById('app');

ReactDOM.render(<App />, app);

graphviz('#graph').renderDot('digraph { a -> b }');
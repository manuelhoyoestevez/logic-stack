import React from 'react';
import ReactDOM from 'react-dom';

import { sin, cuad } from './maths';

import App from './components/App';
import Grid from './grid';

const app = document.getElementById('app');

ReactDOM.render(<App />, app);
/*
const grid = new Grid(-5.2, 10.4, -5.2, 10.4, document.getElementById('canvas'));
grid.drawFunc(x => Math.sin(x), 'rgb(255,0,255)');
grid.drawFunc(x => Math.cos(x), 'rgb(255,0,0)');
grid.drawFunc(x => -Math.sin(x), 'rgb(255,0,255)');
grid.drawFunc(x => -Math.cos(x), 'rgb(255,0,0)');
grid.drawAxis('rgb(0,0,255)');
*/

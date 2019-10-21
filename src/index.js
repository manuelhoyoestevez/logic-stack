import React from 'react';
import ReactDOM from 'react-dom';

import { linearInterpolFunc } from './maths';

import App from './components/App';
import Grid from './grid';

const app = document.getElementById('app');

ReactDOM.render(<App />, app);
/*
// Función a analizar
const func = x => +Math.sin(x) + 0.5 * Math.cos(2 * x);

// Dibujar la función a analizar
const grid = new Grid(-5.2, 10.4, -2.2, 4.4, document.getElementById('canvas'));
grid.drawFunc(func, 'rgb(255,0,255)');
grid.drawAxis('rgb(0,0,255)');


const ts = [], xs = [];
for(let x = -5.2; x <= 30.0; x+= 1){
    ts.push(x);
    xs.push(func(x));
}

const interFunc = linearInterpolFunc(ts, xs);
grid.drawFunc(interFunc, 'rgb(0,255,0)');

var fft = require('fft-js').fft,
    fftUtil = require('fft-js').util;

var phasors= fft(xs);

console.log(xs);

//var frequencies = fftUtil.fftFreq(phasors, 8000), // Sample rate and coef is just used for length, and frequency step
//    magnitudes = fftUtil.fftMag(phasors);

*/

import React from 'react';
import ReactDOM from 'react-dom';

import { sin, cuad } from './maths';

import App from './components/App';
import Grid from './grid';

const app = document.getElementById('app');

//ReactDOM.render(<App />, app);
const canvas = document.getElementById("canvas");

const grid = new Grid(-5, 10, -2, 4, canvas);

grid.drawFunc(Math.sin, 'rgb(0,0,0)');

/*
function putPixel(ctx, x, y, r, g, b, a) {
    ctx.fillStyle = "rgba("+r+","+g+","+b+","+(a/255)+")";
    ctx.fillRect(x, y, 1, 1);
  }

  function draw2() {
    var canvas = document.getElementById("canvas");
    var ctx = canvas.getContext("2d");

    ctx.beginPath();
    ctx.moveTo(20, 20);
    ctx.strokeStyle = "rgb(0,0,255)";
    ctx.lineTo(20, 40);
    ctx.lineTo(40, 40);
    ctx.stroke();

    ctx.beginPath();
    ctx.moveTo(70, 70);
    ctx.strokeStyle = "rgb(255,0,0)";
    ctx.lineTo(80, 100);
    ctx.stroke();
    ctx.font = "20px Arial";
    ctx.fillText("Hello World", 10, 10);

    putPixel(ctx, 60, 60, 255, 0, 0, 255);
  }

      function fun1(x) { return Math.sin(x); }
      function fun2(x) { return sin(x); }
      function fun3(x) { return cuad(x); }

      function draw() {
        var canvas = document.getElementById("canvas");
        if (null==canvas || !canvas.getContext) return;
        var axes={}, ctx=canvas.getContext("2d");
        axes.x0 = .5 + .5*canvas.width;  // x0 pixels from left to x=0
        axes.y0 = .5 + .5*canvas.height; // y0 pixels from top to y=0
        axes.scale = 40;                 // 40 pixels from x=0 to x=1
        axes.doNegativeX = true;
        showAxes(ctx,axes);
        funGraph(ctx,axes,fun1,"rgb(11,153,11)",1);
        funGraph(ctx,axes,fun2,"rgb(66,44,255)",2);
        //funGraph(ctx,axes,fun3,"rgb(255,44,64)",3);
      }

      function funGraph (ctx,axes,func,color,thick) {
        var xx, yy, dx=4, x0=axes.x0, y0=axes.y0, scale=axes.scale;
        var iMax = Math.round((ctx.canvas.width-x0)/dx);
        var iMin = axes.doNegativeX ? Math.round(-x0/dx) : 0;
        ctx.beginPath();
        ctx.lineWidth = thick;
        ctx.strokeStyle = color;
        for (var i=iMin;i<=iMax;i++) {
          xx = dx*i; yy = scale*func(xx/scale);
          if (i==iMin)
            ctx.moveTo(x0+xx,y0-yy);
          else
            ctx.lineTo(x0+xx,y0-yy);
        }
        ctx.stroke();
      }

      function showAxes(ctx,axes) {
        var x0 = axes.x0, w = ctx.canvas.width;
        var y0 = axes.y0, h = ctx.canvas.height;
        var xmin = axes.doNegativeX ? 0 : x0;

        ctx.beginPath();
        ctx.strokeStyle = "rgb(128,128,128)";

        // X axis
        ctx.moveTo(xmin,y0);
        ctx.lineTo(w,y0);

        // Y axis
        ctx.moveTo(x0,0);
        ctx.lineTo(x0,h);

        ctx.stroke();
      }
*/
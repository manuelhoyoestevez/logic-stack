const K = (t, tIni, tLen, n) => Math.round((n - 1) * (t - tIni) / tLen);
const T = (k, tIni, tLen, n) => tIni + k * tLen / (n - 1);

export default class Grid {
  constructor(xMin, xLen, yMin, yLen, canvas) {
    this.xMin = xMin;
    this.xLen = xLen;
    this.yMin = yMin;
    this.yLen = yLen;
    this.canvas = canvas;
    this.width  = this.canvas.width;
    this.height = this.canvas.height;
    this.Ax = this.xLen / (this.width  - 1);
    this.Ay = this.yLen / (this.height - 1);
    this.ctx = this.canvas.getContext("2d");
/*
    console.log('this.xMin', this.xMin);
    console.log('this.xLen', this.xLen);
    console.log('this.yMin', this.yMin);
    console.log('this.yLen', this.yLen);
    console.log('this.width', this.width);
    console.log('this.height', this.height);
*/
  }

  calcX(j) {
    return T(j, this.xMin, this.xLen, this.width);
  }

  calcY(i) {
    return T(i, this.yMin, this.yLen, this.height);
  }

  calcJ(x) {
    return K(x, this.xMin, this.xLen, this.width);
  }

  calcI(y) {
    return K(y, this.yMin, this.yLen, this.height);
  }

  drawFunc(func, style) {
    this.ctx.beginPath();
    this.ctx.lineWidth = 2;
    this.ctx.strokeStyle = style;

    for (let j = 0; j < this.width; j++) {
      const x = this.calcX(j);
      const y = func(x);
      const i = this.calcI(y);

console.log('j', j, 'x', x, 'y', y, 'i', i, this.width, this.height);

      if (j == 0){
        this.ctx.moveTo(j, i);
      }
      else{
        this.ctx.lineTo(j, i);
      }
    }
    this.ctx.stroke();
  }
}
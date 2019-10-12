const K = (t, tIni, tLen, n) => Math.round((n - 1) * (t - tIni) / tLen);
const T = (k, tIni, tLen, n) => tIni + k * tLen / (n - 1);
const F = x => Math.round(x * 100.0) / 100.0;

export default class Grid {
  constructor(xMin, xLen, yMin, yLen, canvas) {
    this.canvas = canvas;
    this.width  = this.canvas.width;
    this.height = this.canvas.height;
    this.ctx    = this.canvas.getContext("2d");
    this.setDiemensions(xMin, xLen, yMin, yLen);
  }

  setDiemensions(xMin, xLen, yMin, yLen) {
    this.xMin = xMin;
    this.xLen = xLen;
    this.yMin = yMin;
    this.yLen = yLen;
    this.Ax = this.xLen / (this.width  - 1);
    this.Ay = this.yLen / (this.height - 1);
  }

  calcX(j) {
    return T(j, this.xMin, this.xLen, this.width);
  }

  calcY(i) {
    return T(this.height - i - 1, this.yMin, this.yLen, this.height);
  }

  calcJ(x) {
    return K(x, this.xMin, this.xLen, this.width);
  }

  calcI(y) {
    return this.height - K(y, this.yMin, this.yLen, this.height) - 1;
  }

  drawFunc(func, style) {
    this.ctx.beginPath();
    this.ctx.lineWidth = 2;
    this.ctx.strokeStyle = style;

    for (let j = 0; j < this.width; j++) {
      const x = this.calcX(j);
      const y = func(x);
      const i = this.calcI(y);

      if (j === 0) {
        this.ctx.moveTo(j, i);
      }
      else {
        this.ctx.lineTo(j, i);
      }
    }
    this.ctx.stroke();
  }

  drawAxis(style) {
    const { ctx, width, height, xMin, xLen, yMin, yLen } = this;
    const j0 = this.calcJ(0.0);
    const i0 = this.calcI(0.0);

    ctx.beginPath();
    ctx.lineWidth = 1;
    ctx.strokeStyle = style;

    ctx.moveTo(0, i0);
    ctx.lineTo(width, i0);

    ctx.moveTo(j0, 0);
    ctx.lineTo(j0, height);
    ctx.stroke();

    ctx.font = "10px Arial";

    const xSeg = 50 * xLen / width;
    const xExp = Math.floor(Math.log10(xSeg));
    const xInc = Math.round(xSeg * Math.pow(10, -xExp)) * Math.pow(10, +xExp);

    for(let x = xInc * Math.ceil(xMin / xInc); x <= xMin + xLen; x+= xInc){
      ctx.fillText(F(x), this.calcJ(x), i0);
    }

    const ySeg = 50 * yLen / height;
    const yExp = Math.floor(Math.log10(ySeg));
    const yInc = Math.round(ySeg * Math.pow(10, -yExp)) * Math.pow(10, +yExp);

    for(let y = yInc * Math.ceil(yMin / yInc); y <= yMin + yLen; y+= yInc){
      ctx.fillText(F(y), j0, this.calcI(y));
    }
  }
}
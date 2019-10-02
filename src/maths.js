export const abs = x => x < 0 ? -x : x;

export const isZero = x => x <= abs(0.000000001);

export const fact = (n) => {
	let r = 1;
	const parsedN = parseInt(abs(n)) || 0;
	for(let i = 2; i <= parsedN; i++) {
		r = r * i;
    }

	return r;
};

export const exp = (x, n) => {
	let r = 1;
	const parsedN = parseInt(abs(n)) || 0;
	for(let i = 0; i < parsedN; i++) {
		r = r * x;
	}

	return r;
};

export const sinPoly = [
	0, +1,
    0, -1/fact(3),
    0, +1/fact(5),
	0, -1/fact(7),
	0, +1/fact(9),
	0, -1/fact(11),
	0, +1/fact(13),
	0, -1/fact(15),
	0, +1/fact(17),
	0, -1/fact(19),
	0, +1/fact(21),
	0, -1/fact(23),
	0, +1/fact(25),
    0, -1/fact(27),
	0, +1/fact(29),
	0, -1/fact(31),
	0, +1/fact(33),
	0, -1/fact(35),
	0, +1/fact(37),
	0, -1/fact(39),
	0, +1/fact(41),
	0, -1/fact(43),
	0, +1/fact(45),
	0, -1/fact(47),
	0, +1/fact(49),
	0, -1/fact(51),
	0, +1/fact(53),
	0, -1/fact(55),
	0, +1/fact(57),
    0, -1/fact(59),
	0, +1/fact(61),
	0, -1/fact(63),
	0, +1/fact(65),
	0, -1/fact(67),
	0, +1/fact(69),
	0, -1/fact(71),
	0, +1/fact(73),
	0, -1/fact(75),
	0, +1/fact(77),
    0, -1/fact(79),
	0, +1/fact(81),
	0, -1/fact(83),
	0, +1/fact(85),
	0, -1/fact(87),
	0, +1/fact(89),
	0, -1/fact(91),
	0, +1/fact(93),
	0, -1/fact(95),
	0, +1/fact(97),
    0, -1/fact(99)
];


export const ruffini = (poly, x) => {
  if (!Array.isArray(poly)) {
    return null;
  }

  if (isNaN(x)) {
    return null;
  }

  const cleanPoly = Array.from(poly).reverse();

  while (cleanPoly.length > 0 && isZero(cleanPoly[0])) {
    cleanPoly.shift();
  }

  if (cleanPoly.length === 0) {
    return null;
  }

  const resPoly = [];

  let pivot = cleanPoly[0];

  for (let i = 1; i < cleanPoly.length; i++) {
    resPoly.push(pivot);
    pivot = pivot * x + cleanPoly[i];
  }

  return { rest: pivot, poly: resPoly.reverse() };
}

export const cuad = x => {
    let n = parseInt(x / (Math.PI / 2));
    console.log('x', x, 'n', n);
    return n % 4;
}

export const sin = x => {
    let r = 1.0;
    x = x - parseInt(x / (Math.PI/2)) * (Math.PI/2);

    let n = cuad(x)


    switch(n) {
        case 0: break;
        //case 1: x =  Math.PI - x; break;
        //case 2: r = -1.0; break;
        //case 3: x =  Math.PI - x; r = -1.0; break;
    }

    return r * ruffini(sinPoly, x).rest;
}
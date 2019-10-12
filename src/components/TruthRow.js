import React from 'react';

// props.literals: [a, b, c, d]
// props.literalsValue: 24

const intToBinary = (raw, literals) => {
  const row = {};
  let check = 1 << literals.length;

  literals.forEach(literal => {
    check = check >> 1;
    row[literal] = (raw & check) == check;
  });

  return row;
};

export default class TruthRow extends React.Component {
  constructor(props) {
    super(props);
//console.log('this.props', this.props);
    this.state = {
      value: this.props.value ? '1': '0'
    };
  }

  changeResult(event) {

  }

  render() {
    const entries = Object.entries(intToBinary(this.props.literalsValue, this.props.literals));
console.log('literalsValue', this.props.literalsValue, 'literals', this.props.literals, 'entries', entries);
    return (
      <tr>
        { entries.map(([literal, value]) => <td key={ literal }>{ value ? '1': '0' }</td>) }
        <td><input className="form-control" value={ this.state.value } onChange={ this.changeResult } style={{ width: '50px' }}></input></td>
      </tr>
    );
  }
}

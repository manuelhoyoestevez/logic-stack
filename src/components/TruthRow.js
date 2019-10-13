import React from 'react';

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
    this.onChangeValue = this.onChangeValue.bind(this);
  }

  onChangeValue(event) {
    event.preventDefault();

    switch(event.target.value.substr(event.target.value.length - 1)){
      case '0': this.props.changeValue(this.props.literalsValue, '0'); break;
      case '1': this.props.changeValue(this.props.literalsValue, '1'); break;
      default:  this.props.changeValue(this.props.literalsValue, 'X');
    }
  }

  render() {
    const entries = Object.entries(intToBinary(this.props.literalsValue, this.props.literals));
    return (
      <tr>
        { entries.map(([literal, value]) => <td key={ literal }>{ value ? '1': '0' }</td>) }
        <td><input className="form-control" value={ this.props.value } onChange={ this.onChangeValue } style={{ width: '50px' }}></input></td>
      </tr>
    );
  }
}

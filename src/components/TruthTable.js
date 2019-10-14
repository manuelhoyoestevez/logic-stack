import React from 'react';
import TruthRow from './TruthRow';

const intToMap = (raw, literals) => {
  const row = {};
  let check = 1 << literals.length;

  literals.forEach(literal => {
    check = check >> 1;
    row[literal] = (raw & check) == check;
  });

  return row;
};

export default class TruthTable extends React.Component {
  constructor(props) {
    super(props);
    this.onChangeValue = this.onChangeValue.bind(this);
  }

  onChangeValue(event) {
    event.preventDefault();
    switch(event.target.value.substr(event.target.value.length - 1)){
      case '0': this.props.onChangeTruthTableValue(event.target.getAttribute('i'), '0'); break;
      case '1': this.props.onChangeTruthTableValue(event.target.getAttribute('i'), '1'); break;
      default:  this.props.onChangeTruthTableValue(event.target.getAttribute('i'), 'X');
    }
  }

  render() {
    const { literals, values } = this.props;
    const r = Math.pow(2, literals.length);
    const headRow  = [<td key={ r }/>];
    const valueRow = [<td key={ r }/>];
    const literalsRows = {};
    const rows = [];

    for(const literal of literals) {
      literalsRows[literal] = [<th key={ r }>{ literal }</th>];
    }

    for(let i = 0; i < r; i++) {
      headRow.push(<th key={ i } style={{ textAlign: 'center' }}>{ i }</th>);
      valueRow.push(
        <th key={ i } style={{ textAlign: 'center' }}>
          <input i={ i } type="text" value={ values[i] } style={{ width: '15px' }} onChange={ this.onChangeValue }></input>
        </th>
      );
      const map = intToMap(i, literals);
      for(const literal of literals) {
        literalsRows[literal].push(<td key={ i } style={{ color: map[literal] ? 'blue' : 'red', textAlign: 'center' }}>{ map[literal] ? '1' : '0' }</td>);
      }
    }

    for(const literal of literals) {
      rows.push(<tr key={ literal }>{ literalsRows[literal] }</tr>);
    }

    return (
      <div className="table-responsive">
        <table className="table table-striped table-bordered table-hover">
          <thead><tr>{ headRow }</tr></thead>
          <tbody>{ rows }</tbody>
          <tfoot><tr>{ valueRow }</tr></tfoot>
        </table>
      </div>
    );
  }
}

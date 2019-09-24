import React from 'react';

class TruthRow extends React.Component {
  render() {
    return (
      <tr>{ this.props.row.map((value, key) => <td key={ key }>{ value }</td>) }</tr>
    );
  }
}

export default class TruthTable extends React.Component {
  render() {
    const rows = this.props.rows.map(
      (row, key) => (
        <TruthRow key={ key } row={ row } literals={ this.props.literals }></TruthRow>
      )
    );

    const header = this.props.literals.map(literal => <th key={ literal }>{ literal }</th>);
    header.push(<th key="_result_"></th>);

    return (
      <table border="2">
        <thead><tr>{ header }</tr></thead>
        <tbody>{ rows }</tbody>
      </table>
    );
  }
}

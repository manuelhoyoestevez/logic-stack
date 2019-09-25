import React from 'react';
import TruthRow from './TruthRow';

export default class TruthTable extends React.Component {
  constructor() {
    super();
    this.state = {
      output: []
    };
  }

  // proprs.literales: array de cadenas únicas y con un orden
  // 


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

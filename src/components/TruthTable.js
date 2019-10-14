import React from 'react';
import TruthRow from './TruthRow';

export default class TruthTable extends React.Component {
  render() {
    const entries = Object.entries(this.props.values);

    const rows = entries.map(
      ([literalValue, value]) => (
        <TruthRow
          key={ literalValue }
          literals={ this.props.literals }
          literalsValue={ literalValue }
          value={ value }
          onChangeTruthTableValue={ this.props.onChangeTruthTableValue }>
        </TruthRow>
      )
    );

    const header = this.props.literals.map(literal => <th key={ literal }>{ literal }</th>);
    header.push(<th key="_result_"></th>);

    return (
      <div className="table-responsive">
        <table className="table table-striped table-bordered table-hover">
          <thead><tr>{ header }</tr></thead>
          <tbody>{ rows }</tbody>
        </table>
      </div>
    );
  }
}

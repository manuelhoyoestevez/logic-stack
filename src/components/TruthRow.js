import React from 'react';

// props.literals: { a = 1, b = 0... }

export default class TruthRow extends React.Component {
  constructor() {
    super();
    this.state = {
      output: '' // '0', '1', o ''
    };
  }

  changeResult(event) {

  }

  render() {
    return (
      <tr>
        { Object.entries(this.props.literals).map(([literal, value]) => <td key={ literal }>{ value }</td>) }
        <td><input value={ this.state.result } onChange={ this.changeResult }></input></td>
      </tr>
    );
  }
}
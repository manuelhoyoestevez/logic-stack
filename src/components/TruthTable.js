import React from 'react';
import TruthRow from './TruthRow';

const bool2string = bool => {
  if(typeof bool === 'boolean'){
    return bool ? '1' : '0';
  }

  return bool;
}

const bools2strings = bools => {
  const ret = {};
  
  Object.keys(bools).forEach((key) => {
    ret[key] = bool2string(bools[key]);
  });

  return ret;
};

export default class TruthTable extends React.Component {
  static getDerivedStateFromProps(props, state) {
    if(state.change) {
      state.change = false;
      state.values =  bools2strings(state.values)
    }
    else {
      state.values = bools2strings(props.values);
    }

    return state;
  }

  get manolo () {
    return 'manolo';
  }

  constructor(props) {
    super(props);
    this.state = {
      values: bools2strings(this.props.values),
      change: false
    };

    this.changeValue = this.changeValue.bind(this);
    this.changeState = false;
  }

  changeValue(literalValue, value) {
    this.setState(state => {
      state.values[literalValue] = value;
      state.change = true;
      return state;
    });
  }

  render() {
    const entries = Object.entries(this.state.values);

    const rows = entries.map(
      ([literalValue, value]) => (
        <TruthRow
          key={ literalValue }
          literals={ this.props.literals } 
          literalsValue={ literalValue } 
          value={ value }
          changeValue={ this.changeValue }>
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

import React from 'react';
import Axios from 'axios';
import TruthTable from './TruthTable';
import DecissionTree from './DecisionTree';
import { graphviz } from 'd3-graphviz';

export default class App extends React.Component {

  constructor() {
    super();
    this.instance = Axios.create({
      baseURL: 'http://localhost:8081',
      headers: { 'Content-Type': 'application/json' },
    });

    this.state = {
      literals: [],
      rows: [],
      decision: 0,
      digraph: ''
    };
    this.onClickCalculate = this.onClickCalculate.bind(this);
  }

  onClickCalculate(event) {
    event.preventDefault();

    this.instance.post('/process-logic-expression', { expression: document.getElementById('expression').value })
      .then(({ data }) => {
        const { digraph, decision, truth: { literals, rows }} = data;
        this.setState({ digraph, decision, literals, rows });

        graphviz('#graph').renderDot(digraph);
      })
      .catch((error) => {
        console.error(error);
      });
  }

  render() {
    return (
      <div>
        <textarea id="expression"></textarea>
        <button onClick={ this.onClickCalculate }>Calculate!</button>
        <TruthTable literals={ this.state.literals } rows={ this.state.rows }/>
        <DecissionTree decision={ this.state.decision } digraph={ this.state.digraph }/>
      </div>
    );
  }
}

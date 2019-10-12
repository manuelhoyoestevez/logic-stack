import React from 'react';
import Axios from 'axios';
import TruthTable from './TruthTable';
import DecissionTree from './DecisionTree';
import { graphviz } from 'd3-graphviz';

export default class App extends React.Component {
  constructor(props) {
    super(props);
    this.instance = Axios.create({
      baseURL: 'http://localhost:8081',
      headers: { 'Content-Type': 'application/json' },
    });

    this.state = {
      logicExpression: 'return p -> q;',
      logicExpressionStatus: '',
      logicExpressionMessage: '',

      jsonExpressionTree: '',
      jsonExpressionStatus: '',
      jsonExpressionMessage: '',

      truthTable: { literals: [], values: {} },

      decision: 0,
      digraph: ''
    };

    this.onChangeLogicExpression = this.onChangeLogicExpression.bind(this);
    this.onClickLogicExpressionToExpressionTree = this.onClickLogicExpressionToExpressionTree.bind(this);
    this.onChangeJsonExpressionTree = this.onChangeJsonExpressionTree.bind(this);
    this.onClickExpressionTreeToTruthTable = this.onClickExpressionTreeToTruthTable.bind(this);
  }

  onChangeLogicExpression(event) {
    event.preventDefault();
    this.setState({
      logicExpression: event.target.value,
      logicExpressionStatus: '',
      logicExpressionMessage: ''
    });
  }

  onClickLogicExpressionToExpressionTree(event) {
    event.preventDefault();

    this.instance.post('/logic-expression-to-expression-tree', { expression: document.getElementById('logic-expression').value })
      .then(({ data }) => {
        this.setState({
          jsonExpressionTree: JSON.stringify(data.expressionTree, null, 2),
          logicExpressionStatus: 'has-success',
          logicExpressionMessage: ''
        });
        /*
        const { digraph, decision, truth: { literals, rows }} = data;
        this.setState({ digraph, decision, literals, rows });
        graphviz('#graph').renderDot(digraph);
        */
      })
      .catch((error) => {
        this.setState({
          jsonExpressionTree: '',
          logicExpressionStatus: 'has-error',
          logicExpressionMessage: error.response.data.error
        });
      });
  }

  onChangeJsonExpressionTree(event) {
    event.preventDefault();
    this.setState({
      jsonExpressionTree: event.target.value,
      jsonExpressionTreeStatus: '',
      jsonExpressionTreeMessage: ''
    });
  }

  onClickExpressionTreeToTruthTable(event) {
    event.preventDefault();

    this.instance.post('/expression-tree-to-truth-table', { expressionTree: JSON.parse(document.getElementById('json-expression-tree').value) })
      .then(({ data }) => {
        this.setState({
          truthTable: data.truthTable,
          jsonExpressionTreeStatus: 'has-success',
          jsonExpressionTreeMessage: ''
        });
      })
      .catch((error) => {
        this.setState({
          truthTable: null,
          jsonExpressionTreeMessage: 'has-error',
          jsonExpressionTreeMessage: error.response.data.error
        });
      });
  }

  table2expression(event) {
    event.preventDefault();

    this.instance.post('/process-thuth-table', { expression: document.getElementById('expression').value })
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
      <div className="row">
        <div className="col-lg-12">
          <div className="panel panel-default">
            <div className="panel-heading">Logic Stack</div>
            <div className="panel-body">
              <div className="row">
                <div className="col-md-10">
                  <div className={ `form-group ${ this.state.logicExpressionStatus }`}>
                    <label>Logic expression</label>
                    <textarea className="form-control" id="logic-expression" value={ this.state.logicExpression } rows="5" onChange={ this.onChangeLogicExpression }></textarea>
                    <p className="help-block">{ this.state.logicExpressionMessage }</p>
                  </div>
                </div>
                <div className="col-md-2">
                  <label>Calculate</label>
                  <button type="submit" className="btn btn-default" onClick={ this.onClickLogicExpressionToExpressionTree }>Calculate!</button>
                </div>
              </div>
              <div className="row">
                <div className="col-md-10">
                  <div className="form-group">
                    <label>Json tree expression</label>
                    <textarea className="form-control" id="json-expression-tree" value={ this.state.jsonExpressionTree } rows="5" onChange={ this.onChangeJsonExpressionTree }></textarea>
                    <p className="help-block">{ this.state.logicExpressionMessage }</p>
                  </div>
                </div>
                <div className="col-md-2">
                  <label>Calculate</label>
                  <button type="submit" className="btn btn-default" onClick={ this.onClickExpressionTreeToTruthTable }>Calculate!</button>
                </div>
              </div>
              <div className="row">
                <div className="col-md-6">
                  <TruthTable literals={ this.state.truthTable.literals } values={ this.state.truthTable.values } />
                </div>
                <div className="col-md-6">
                  <DecissionTree decision={ this.state.decision } digraph={ this.state.digraph }/>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

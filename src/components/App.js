import React from 'react';
import Axios from 'axios';
import TruthTable from './TruthTable';
import { graphviz } from 'd3-graphviz';

const bool2string = bool => {
  if(typeof bool === 'boolean'){
    return bool ? '1' : '0';
  }

  return bool;
}

const string2bool = string => {
  if(typeof string === 'string'){
    switch(string.toLowerCase().trim()){
      case '0':
      case 'false':
        return false;
      case '1':
      case 'true':
        return true;
    }
  }

  return null;
}

const stringTruthTable = ({literals, values}) => {
  const newTruthTable = {
    literals,
    values: {}
  };

  Object.keys(values).forEach((key) => {
    newTruthTable.values[key] = bool2string(values[key]);
  });

  return newTruthTable;
};

const boolTruthTable = ({literals, values}) => {
  const newTruthTable = {
    literals,
    values: {}
  };

  Object.keys(values).forEach((key) => {
    const val = string2bool(values[key]);
    if(val !== null) {
      newTruthTable.values[key] = val;
    }
  });

  return newTruthTable;
};

const getErrorMessage = (error) => {
  console.error('error', error);
  if (!error) {
    return 'Undefined error';
  }

  if (!error.response) {
    return error.toString();
  }

  if (typeof error.response !== 'object') {
    return "Undefined error format";
  }

  if (!error.response.data || typeof(error.response.data) !== 'object') {
    return "Undefined error format";
  }

  if (!error.response.data.error || typeof(error.response.data.error) !== 'string') {
    return "Undefined error format";
  }

  return error.response.data.error;
};

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
      truthTableStatus: '',
      truthTableMessage: '',

      jsonDecisionTree: '',
      jsonDecisionStatus: '',
      jsonDecisionMessage: '',
    };

    this.onChangeLogicExpression = this.onChangeLogicExpression.bind(this);
    this.onChangeJsonExpressionTree = this.onChangeJsonExpressionTree.bind(this);
    this.onChangeTruthTableValue = this.onChangeTruthTableValue.bind(this);
    this.onChangeJsonDecisionTree = this.onChangeJsonDecisionTree.bind(this);

    this.onClickLogicExpressionToExpressionTree = this.onClickLogicExpressionToExpressionTree.bind(this);
    this.onClickTruthTableToDecisionTree = this.onClickTruthTableToDecisionTree.bind(this);
  }

  onChangeTruthTableValue(literalValue, value) {
    this.setState(state => {
      state.truthTable.values[literalValue] = value;
      return state;
    });
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
        return this.instance.post('/expression-tree-to-truth-table', { expressionTree: data.expressionTree });
      })
      .then(({ data }) => {
        this.setState({
          truthTable: stringTruthTable(data.truthTable),
          jsonExpressionTreeStatus: 'has-success',
          jsonExpressionTreeMessage: ''
        });
        graphviz('#expression-tree-graph').renderDot(data.expressionTreeGraph);
      })
      .catch((error) => {
        this.setState({
          jsonExpressionTree: '',
          logicExpressionStatus: 'has-error',
          logicExpressionMessage: getErrorMessage(error)
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

  onClickTruthTableToDecisionTree(event) {
    event.preventDefault();

    this.instance.post('/truth-table-to-decision-tree', { truthTable: boolTruthTable(this.state.truthTable) })
      .then(({ data }) => {
        this.setState({
          jsonDecisionTree: JSON.stringify(data.decisionTree, null, 2),
          truthTableStatus: 'has-success',
          truthTableMessage: ''
        });

        graphviz('#decision-tree-graph').renderDot(data.decisionTreeGraph);
        return this.instance.post('/decision-tree-to-expression-tree', { decisionTree: data.decisionTree });
      })
      .then(({ data }) => {
        this.setState({
          jsonReducedExpressionTree: JSON.stringify(data.expressionTree, null, 2),
          jsonReducedLogicExpression: data.logicExpression,
          jsonDecisionTreeStatus: 'has-success',
          jsonDecisionTreeMessage: ''
        });

        graphviz('#reduced-expression-tree-graph').renderDot(data.expressionTreeGraph);
      })
      .catch((error) => {
        this.setState({
          truthTable: { literals: [], values: {} },
          truthTableStatus: 'has-error',
          truthTableMessage: getErrorMessage(error)
        });
      });
  }

  onChangeJsonDecisionTree(event) {
    event.preventDefault();
  }

  render() {
    return (
      <div className="row">
        <div className="col-lg-12">
          <div className="panel panel-default">
            <div className="panel-heading">Logic Stack</div>
            <div className="panel-body">
              <div className="row">
                <div className="col-md-6">
                  <div className={ `form-group ${ this.state.logicExpressionStatus }`}>
                    <label>Logic expression</label>
                    <textarea className="form-control" id="logic-expression" value={ this.state.logicExpression } rows="5" onChange={ this.onChangeLogicExpression }></textarea>
                    <p className="help-block">{ this.state.logicExpressionMessage }</p>
                    <button type="submit" className="btn btn-default" onClick={ this.onClickLogicExpressionToExpressionTree }>Calculate!</button>
                  </div>
                </div>
                <div className="col-md-6">
                  <div className={ `form-group ${ this.state.jsonExpressionTreeStatus }`}>
                    <label>Json expression tree</label>
                    <textarea className="form-control" id="json-expression-tree" value={ this.state.jsonExpressionTree } rows="5" onChange={ this.onChangeJsonExpressionTree }></textarea>
                    <p className="help-block">{ this.state.jsonExpressionTreeMessage }</p>
                  </div>
                </div>
              </div>
              <div className="row">
                <div className="col-md-12">
                  <div className={ `form-group ${ this.state.truthTableStatus }`}>
                    <label>Truth table</label>
                    <TruthTable
                      literals={ this.state.truthTable.literals }
                      values={ this.state.truthTable.values }
                      onChangeTruthTableValue={ this.onChangeTruthTableValue }/>
                    <p className="help-block">{ this.state.truthTableMessage }</p>
                    <button type="submit" className="btn btn-default" onClick={ this.onClickTruthTableToDecisionTree }>Calculate!</button>
                  </div>
                </div>
              </div>
              <div className="row">
              <div className="col-md-4">
                  <div className="form-group">
                    <label>Reduced logic expression</label>
                    <textarea className="form-control" id="reduced-logic-expression" value={ this.state.jsonReducedLogicExpression } rows="5" disabled></textarea>
                    <p className="help-block"></p>
                  </div>
                </div>
                <div className="col-md-4">
                  <div className={ `form-group ${ this.state.jsonDecisionTreeStatus }`}>
                    <label>Json decision tree</label>
                    <textarea className="form-control" id="json-decision-tree" value={ this.state.jsonDecisionTree } rows="5" onChange={ this.onChangeJsonDecisionTree }></textarea>
                    <p className="help-block">{ this.state.jsonDecisionTreeMessage }</p>
                  </div>
                </div>
                <div className="col-md-4">
                  <div className="form-group">
                    <label>Json reduced expression tree</label>
                    <textarea className="form-control" id="json-reduced-expression-tree" value={ this.state.jsonReducedExpressionTree } rows="5" disabled></textarea>
                    <p className="help-block"></p>
                  </div>
                </div>
              </div>
              <div className="row">
                <div className="col-md-12">
                  <div id="decision-tree-graph"></div>
                </div>
              </div>
              <div className="row">
                <div className="col-md-12">
                  <div id="expression-tree-graph"></div>
                </div>
              </div>
              <div className="row">
                <div className="col-md-12">
                  <div id="reduced-expression-tree-graph"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

import React from 'react';

class Row extends React.Component {
  constructor(props) {
    super(props);
    this.state = { row: props.row }; // Array simple
  }

  render() {
    let j = 1;
    const celds = [];

    this.state.row.forEach((celd) => {
        celds.push(
          <td key={j++}>{celd}</td>
        );
    });

    return (
      <tr>
        {celds}
      </tr>
    );
  }
}

export class Table extends React.Component {
  constructor(props) {
    super(props);
    this.state = { table: props.table }; // Array doble
  }

  componentDidMount() {
    this.timerID = setInterval(
      () => this.tick(),
      1000
    );
  }

  componentWillUnmount() {
    clearInterval(this.timerID);
  }

  tick() {
    this.setState((state, props) => {
      state.table.push(['x', 'y']);
      return { table: state.table };
    });
  }

  render() {
    let i = 1;
    const rows = [];

    this.state.table.forEach((row) => {
      rows.push(
        <Row key={i++} row={row}></Row>
      );
    });

    return (
      <table border="2">
        <thead></thead>
        <tbody>{rows}</tbody>
      </table>
    );
  }
}

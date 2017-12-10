import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import axios from 'axios';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import TextField from 'material-ui/TextField';
import {
  Table,
  TableBody,
  TableHeader,
  TableHeaderColumn,
  TableRow,
  TableRowColumn,
} from 'material-ui/Table';

class App extends Component {

  constructor(props) {
    super(props);
    this.state = {
      results: null
    }
  }


  handleSubmit = (e) => {
    e.preventDefault();
    let query = e.target.query.value;
    let formData = new FormData();
    formData.append('query', query);
    console.log(query);
    axios.post('http://localhost:8080/', formData)
      .then((data) => {
        console.log(data);
        this.setState({
          results: data.data
        });
      })
      .catch(err => {
        console.log(err);
      });
  }

  render() {
    let {results} = this.state;
    let searchResults = [];
    if (results) {
      results.forEach(result => {
        searchResults.push(
          <TableRow key={result.url}>
            <TableRowColumn><a href={result.fullUrl}>{result.url}</a></TableRowColumn>
            <TableRowColumn>{result.score}</TableRowColumn>
            <TableRowColumn>{result.pageRank}</TableRowColumn>
            <TableRowColumn>{result.frequency}</TableRowColumn>
            <TableRowColumn>{result.location}</TableRowColumn>
            <TableRowColumn>{result.distance}</TableRowColumn>
          </TableRow>
        );
      });
    }
    return (
      <MuiThemeProvider>
        <div className="App">
          <header className="App-header">
            <form onSubmit={this.handleSubmit}>
            <TextField
              name="query"
              hintText="Search"
              floatingLabelText="Search"
            />
              {/* <input name="query" type="text" placeholder="search" /> */}
            </form>
          </header>
          <Table className="search-results">
            <TableHeader>
              <TableRow>
                <TableHeaderColumn>URL</TableHeaderColumn>
                <TableHeaderColumn>Score</TableHeaderColumn>
                <TableHeaderColumn>Page Rank</TableHeaderColumn>
                <TableHeaderColumn>Frequency</TableHeaderColumn>
                <TableHeaderColumn>Location</TableHeaderColumn>
                <TableHeaderColumn>Distance</TableHeaderColumn>
              </TableRow>
            </TableHeader>
            <TableBody>
              {searchResults}
            </TableBody>
          </Table>
        </div>
      </MuiThemeProvider>
    );
  }
}

export default App;

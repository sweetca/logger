import React, { Component } from 'react';
import './App.css';
import Head from '../head/Head';
import Content from '../../containers/Content';

class App extends Component {
  render() {
    return (
      <div className="App">
          <Head />
          <Content />
          <footer></footer>
      </div>
    );
  }
}

export default App;

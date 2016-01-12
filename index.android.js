'use strict';

var React = require('react-native');
var {
  Image,
  ListView,
  Text,
  View
} = React;

var REQUEST_URL = 'http://dev-ltd-news.pantheon.io/?json=1';

var ReactPrototype = React.createClass({
  /**
   * Render the view.
   */
  render: function() {
    if (!this.state.loaded) {
      return this.renderLoadingView();
    }

    return (
      <ListView
        dataSource={this.state.dataSource}
        renderRow={this.renderPost}
        style={styles.listView}
        />
    );
  },

  renderLoadingView: function() {
    return (
      <Text>Loading...</Text>
    );
  },

  renderPost: function(post) {
    return(
      <View style={styles.postContainer}>
        <Text style={styles.h1}>Title</Text>
        <Text>{post.title}</Text>

        <Text style={styles.h1}>Subtitle</Text>
        <Text>{post.custom_fields.subtitle}</Text>

        <Text style={styles.h1}>Summary</Text>
        <Text>{post.custom_fields.summary_1}</Text>
        <Text>{post.custom_fields.summary_2}</Text>
        <Text>{post.custom_fields.summary_3}</Text>
      </View>
    );
  },

  /**
   * Set initial state of the component.
   */
  getInitialState: function() {
    return {
      dataSource: new ListView.DataSource({
        rowHasChanged: (row1, row2) => row1 !== row2,
      }),
      loaded: false,
    };
  },

  /**
   * Called just once after the component is loaded.
   */
  componentDidMount: function() {
    this.fetchData();
  },

  /**
   * Fetch data from the server.
   */
  fetchData: function() {
    fetch(REQUEST_URL)
      .then((response) => response.json())
      .then((responseData) => {
        this.setState({
          dataSource: this.state.dataSource.cloneWithRows(responseData.posts),
          loaded: true,
        });
      })
      .done();
  },
});

var styles = React.StyleSheet.create({
  postContainer: {
    borderColor: '#000000',
    borderWidth: 1,
    marginTop: 10,
    padding: 10,
  },
  h1: {
    fontSize: 20,
  },
  listView: {
    paddingLeft: 20,
    paddingRight: 20,
  },
});

React.AppRegistry.registerComponent('ReactPrototype', () => ReactPrototype);
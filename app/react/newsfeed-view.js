'use strict';

import React, {
  AppRegistry,
  Component,
  Image,
  ListView,
  ProgressBarAndroid,
  PullToRefreshViewAndroid,
  StyleSheet,
  Text,
  TouchableHighlight,
  View
} from 'react-native';

var Helpers = require('./newsfeed-helpers');
var NewsFeedPost = require('./newsfeed-post');

var TAKE_ACTION_TEXT = 'Take action';

var NewsFeedView = React.createClass({
  getInitialState: function() {
    return {
      dataSource: new ListView.DataSource({
        rowHasChanged: (row1, row2) => row1 !== row2,
      }),
      isRefreshing: false,
      loaded: false,
      requestUrl: undefined,
    };
  },
  componentDidMount: function() {
    // Only fetch data after we've received the URL to use from native
    var onNewsUrlReceived = function(url) {
      this.setState({requestUrl: url});
      this.fetchData();
    };

    React.NativeModules.ConfigModule.getNewsUrl(onNewsUrlReceived.bind(this));
  },
  fetchData: function() {
    fetch(this.state.requestUrl)
      .then((response) => response.json())
      .then((responseData) => {
        this.setState({
          dataSource: this.state.dataSource.cloneWithRows(responseData.posts),
          loaded: true,
        });
      })
      .done();
  },
  render: function() {
    if (!this.state.loaded) {
      return this.renderLoadingView();
    }

    return (
      <PullToRefreshViewAndroid
        style={{flex: 1}}
        refreshing={this.state.isRefreshing}
        onRefresh={this._onRefresh}
        colors={['#3932a9', '#00e4c8', '#ff0000']}
        progressBackgroundColor={'#ffffff'}
        >
        <ListView
          dataSource={this.state.dataSource}
          renderRow={this.renderRow}
          style={styles.listView} />
      </PullToRefreshViewAndroid>
    );
  },
  _onRefresh: function () {
    this.setState({isRefreshing: true});
    setTimeout(() => {
      this.fetchData();
      this.setState({
        isRefreshing: false,
      });
    }, 1000);
  },
  renderLoadingView: function() {
    return (
      <View style={styles.loadingContainer}>
        <ProgressBarAndroid animating={this.state.animating} style={styles.loadingIndicator} size="small" />
        <Text style={styles.subtitle}>
          Loading news...
        </Text>
      </View>
    );
  },
  renderRow: function(post: Object) {
    return (
      <NewsFeedPost
        key={post.id}
        post={post} />
    );
  },
});

var styles = React.StyleSheet.create({
  listView: {
    backgroundColor: '#eeeeee',
    paddingLeft: 10,
    paddingRight: 10,
    paddingBottom: 10,
  },
  loadingContainer: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#eeeeee',
  },
  loadingIndicator: {
    height: 40,
  },
  loadingText: {
    color: '#4a4a4a',
    fontFamily: 'Brandon Grotesque',
    fontSize: 15,
  },
});
/*

*/
module.exports = NewsFeedView;
/**
 * Main container view for the news feed.
 */

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
var NetworkErrorView = require('./network-error-view');
var NewsFeedPost = require('./newsfeed-post');
var Theme = require('./ldt-theme.js');

var TAKE_ACTION_TEXT = 'Take action';

var NewsFeedView = React.createClass({
  getInitialState: function() {
    return {
      dataSource: new ListView.DataSource({
        rowHasChanged: (row1, row2) => row1 !== row2,
      }),
      error: null,
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
    this.setState({
      loaded: false,
      error: null,
    });

    fetch(this.state.requestUrl)
      .then((response) => response.json())
      .catch((error) => this.catchError(error))
      .then((responseData) => {
        if (!responseData) {
          return;
        }

        this.setState({
          dataSource: this.state.dataSource.cloneWithRows(responseData.posts),
          loaded: true,
          error: null,
        });
      })
      .done();
  },
  catchError: function(error) {
    this.setState({
      loaded: true,
      error: error,
    });
  },
  render: function() {
    if (!this.state.loaded) {
      return this.renderLoadingView();
    }

    var bodyView;
    if (this.state.error) {
      bodyView = (
        <NetworkErrorView
          retryHandler={this.fetchData}
          errorMessage={this.state.error.message}
        />);
    }
    else {
      bodyView = (
        <ListView
          dataSource={this.state.dataSource}
          renderRow={this.renderRow}
          style={styles.listView}
        />);
    }

    return (
      <PullToRefreshViewAndroid
        style={{flex: 1}}
        refreshing={this.state.isRefreshing}
        onRefresh={this._onRefresh}
        colors={[Theme.colorCtaBlue, '#00e4c8', '#ff0000']}
        progressBackgroundColor={'#ffffff'}
        >
        {bodyView}
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
        <ProgressBarAndroid animating={this.state.animating} style={styles.loadingIndicator} styleAttr="Normal" />
        <Text style={Theme.styles.textBody}>
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
});

module.exports = NewsFeedView;
'use strict';
import React, {
  ProgressBarAndroid,
  AppRegistry,
  Component,
  Image,
  ListView,
  PullToRefreshViewAndroid,
  StyleSheet,
  Text,
  TouchableHighlight,
  View
} from 'react-native';

var Helpers = require('./newsfeed-helpers');

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
          renderRow={this.renderPost}
          style={styles.listView}
        />
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
  renderPost: function(post) {
    var causeStyle;
    var causeTitle;
    var formattedDate;
    var imgBackground;
    var imgOval;
    var linkToArticle;
    var viewCategory;

    if (typeof post !== 'undefined'
        && typeof post.attachments[0] !== 'undefined'
        && typeof post.attachments[0].images !== 'undefined'
        && typeof post.attachments[0].images.full !== 'undefined') {
        imgBackground = <Image
          style={{flex: 1, height: 128, alignItems: 'stretch'}}
          source={{uri: post.attachments[0].images.full.url}}>
            <View style={styles.titleContainer}>
              <Text style={styles.title}>{post.title.toUpperCase()}</Text>
            </View>
          </Image>;
    }
    else {
      imgBackground = <Text style={styles.title}>{post.title.toUpperCase()}</Text>;
    }

    imgOval = require('image!newsfeed_listitem_oval');

    if (typeof post.custom_fields.full_article_url !== 'undefined'
        && typeof post.custom_fields.full_article_url[0] !== 'undefined'
        && post.custom_fields.full_article_url[0]) {
        linkToArticle = <Text
            onPress={this.fullArticlePressed.bind(this, post.custom_fields.full_article_url[0])}
            style={styles.articleLink}>
            Read the full article
          </Text>;
    }
    else {
      linkToArticle = null;
    }

    formattedDate = Helpers.formatDate(post.date);
    viewCategory = null;
    if (post.categories.length > 0) {
      causeTitle = post.categories[0].title;
      viewCategory =
        <View style={styles.categoryContainer}>
          <Text style={styles.category}>{causeTitle}</Text>
        </View>;
    }

    causeStyle = {backgroundColor: '#FF0033'};
    causeStyle.backgroundColor = '#FF0033';
    switch (causeTitle) {
      case 'Animals':
        causeStyle.backgroundColor = '#1BC2DD';
        break;
      case 'Bullying':
        causeStyle.backgroundColor = '#E75526';
        break;
      case 'Disasters':
        causeStyle.backgroundColor = '#1D78FB';
        break;
      case 'Discrimination':
        causeStyle.backgroundColor = '#E1000D';
        break;
      case 'Education':
        causeStyle.backgroundColor = '#1AE3C6';
        break;
      case 'Environment':
        causeStyle.backgroundColor = '#12D168';
        break;
      case 'Homelessness':
        causeStyle.backgroundColor = '#FBB71D';
        break;
      case 'Mental Health':
        causeStyle.backgroundColor = '#BA2CC7';
        break;
      case 'Physical Health':
        causeStyle.backgroundColor = '#BA2CC7';
        break;
      case 'Relationships':
        causeStyle.backgroundColor = '#A01DFB';
        break;
      case 'Sex':
        causeStyle.backgroundColor = '#FB1DA9';
        break;
      case 'Violence':
        causeStyle.backgroundColor = '#F1921A';
        break;
    }

    return(
      <View style={styles.postContainer}>
        <View style={[styles.postHeader, causeStyle]}>
          <Text style={styles.date}>{formattedDate}</Text>
          {viewCategory}
        </View>
        {imgBackground}
        <View style={styles.postBody}>
          <Text style={styles.subtitle}>{post.custom_fields.subtitle}</Text>
          <View style={styles.summaryItem}>
            <Image style={styles.listItemOvalImage} source={imgOval} />
            <Text style={styles.summaryText}>{post.custom_fields.summary_1}</Text>
          </View>
          <View style={styles.summaryItem}>
            <Image style={styles.listItemOvalImage} source={imgOval} />
            <Text style={styles.summaryText}>{post.custom_fields.summary_2}</Text>
          </View>
          <View style={styles.summaryItem}>
            <Image style={styles.listItemOvalImage} source={imgOval} />
            <Text style={styles.summaryText}>{post.custom_fields.summary_3}</Text>
          </View>
          {linkToArticle}
        </View>
        <TouchableHighlight onPress={this.ctaButtonPressed.bind(this, post)} style={styles.btn}>
          <Text style={styles.btnText}>{TAKE_ACTION_TEXT.toUpperCase()}</Text>
        </TouchableHighlight>
      </View>
    );
  },
  ctaButtonPressed: function(post) {
    var campaignID = parseInt(post.custom_fields.campaign_id[0]);
    React.NativeModules.CampaignNavigationModule.presentCampaignWithCampaignID(campaignID);
  },
  fullArticlePressed: function(url) {
    React.NativeModules.WebViewModule.open(url);
  },
});

var styles = React.StyleSheet.create({
  postBody: {
    padding: 10,
  },
  postContainer: {
    backgroundColor: '#ffffff',
    marginTop: 10
  },
  loadingContainer: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#EEE',
  },
  postHeader: {
    flex: 1,
    flexDirection: 'row',
    backgroundColor: '#00e4c8',
    borderTopLeftRadius: 4,
    borderTopRightRadius: 4,
    padding: 4,
  },
  postHeaderText: {
    color: '#ffffff',
  },
  articleLink: {
    color: '#3932A9',
    fontFamily: 'brandon_bold',
  },
  btn: {
    backgroundColor: '#3932A9',
    borderBottomLeftRadius: 4,
    borderBottomRightRadius: 4,
    paddingBottom: 10,
    paddingTop: 10,
  },
  btnText: {
    color: '#ffffff',
    fontFamily: 'brandon_bold',
    fontSize: 16,
    textAlign: 'center',
  },
  category: {
    color: '#ffffff',
    fontFamily: 'Brandon Grotesque',
  },
  categoryContainer: {
    flex: 1,
    alignItems: 'flex-end',
  },
  date: {
    color: '#ffffff',
    fontFamily: 'brandon_reg',
  },
  listItemOvalImage: {
    // The height and width are based off the draw height of a single summaryText line
    width: 21.5,
    height: 21.5,
    resizeMode: 'contain',
  },
  listView: {
    backgroundColor: '#eeeeee',
    paddingLeft: 10,
    paddingRight: 10,
    paddingBottom: 10,
  },
  loadingIndicator: {
    height: 40,
  },
  subtitle: {
    color: '#4A4A4A',
    fontFamily: 'brandon_bold',
    fontSize: 18,
  },
  summaryItem: {
    flex: 1,
    flexDirection: 'row',
    marginBottom: 8,
    marginTop: 8,
  },
  summaryText: {
    color: '#4A4A4A',
    flex: 1,
    flexDirection: 'column',
    fontFamily: 'brandon_reg',
    fontSize: 15,
    marginLeft: 4,
  },
  title: {
    color: '#ffffff',
    flex: 1,
    flexDirection: 'column',
    fontFamily: 'brandon_bold',
    fontSize: 20,
    textAlign: 'center',
  },
  titleContainer: {
    backgroundColor: 'rgba(0,0,0,0.3)',
    alignItems: 'center',
    flex: 1,
    flexDirection: 'row',
    padding: 20,
  },
});

AppRegistry.registerComponent('NewsFeedView', () => NewsFeedView);
'use strict';

var React = require('react-native');
var {
  Image,
  IntentAndroid,
  ListView,
  NativeModules,
  Text,
  TouchableHighlight,
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
    var imgBackground;

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

    return(
      <View style={styles.postContainer}>
        <View style={styles.postHeader}>
          <Text style={styles.date}>{post.date}</Text>
        </View>

        {imgBackground}

        <View style={styles.postBody}>

          <Text style={styles.subtitle}>{post.custom_fields.subtitle}</Text>

          <View style={styles.summaryItem}>
            <Image source={require('./assets/oval.png')} />
            <Text style={styles.summaryText}>{post.custom_fields.summary_1}</Text>
          </View>
          <View style={styles.summaryItem}>
            <Image source={require('./assets/oval.png')} />
            <Text style={styles.summaryText}>{post.custom_fields.summary_2}</Text>
          </View>
          <View style={styles.summaryItem}>
            <Image source={require('./assets/oval.png')} />
            <Text style={styles.summaryText}>{post.custom_fields.summary_3}</Text>
          </View>

          <Text
            onPress={this.onPressLink.bind(this, post.custom_fields.full_article_url)}
            style={styles.articleLink}>
            Read the full article
          </Text>
        </View>

        <TouchableHighlight
          onPress={this.onPressAction.bind(this, post.custom_fields.campaign_id)}
          style={styles.btn}>

          <Text style={styles.btnText}>TAKE ACTION</Text>

        </TouchableHighlight>
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

  /**
   * Button event listener.
   */
  onPressAction: function(campaignIds) {
    console.log(campaignIds);
    if (campaignIds.length > 0) {
      NativeModules.ActivityNavigator.start(parseInt(campaignIds[0]));
    }
  },

  /**
   * Link event listener.
   */
  onPressLink: function(urls) {
    if (urls.length > 0) {
      IntentAndroid.openURL(urls[0]);
    }
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
  postHeader: {
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
    fontFamily: 'brandon_reg',
    fontSize: 16,
    textAlign: 'center',
  },
  date: {
    color: '#ffffff',
  },
  h1: {
    fontFamily: 'brandon_bold',
    fontSize: 20,
  },
  listView: {
    backgroundColor: '#eeeeee',
    paddingLeft: 10,
    paddingRight: 10,
    paddingBottom: 10,
  },
  subtitle: {
    color: '#454545',
    fontFamily: 'brandon_bold',
    fontSize: 16,
  },
  summaryItem: {
    flex: 1,
    flexDirection: 'row',
    marginBottom: 8,
    marginTop: 8,
  },
  summaryText: {
    flex: 1,
    flexDirection: 'column',
    fontFamily: 'brandon_reg',
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

React.AppRegistry.registerComponent('ReactPrototype', () => ReactPrototype);
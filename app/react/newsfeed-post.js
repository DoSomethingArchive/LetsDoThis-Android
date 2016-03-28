/**
 * This defines a single news feed posting that is displayed into the containing news feed view.
 */

'use strict';

import React, {
  StyleSheet,
  Text,
  Image,
  TouchableHighlight,
  View
} from 'react-native';

var Helpers = require('./newsfeed-helpers');
var Theme = require('./ldt-theme.js');

var TAKE_ACTION_TEXT = 'Take action';

var NewsFeedPost = React.createClass({
  getInitialState() {
    return {
      imageCreditHidden: true,
    };
  },

  /**
   * Action button onPress listener.
   */
  _onPressActionButton: function() {
    var campaignID = parseInt(this.props.post.campaign_id);
    var postID = parseInt(this.props.post.id);
    React.NativeModules.CampaignNavigationModule.presentCampaignWithCampaignID(campaignID, postID);
  },

  /**
   * Full article link onPress listener.
   */
  _onPressFullArticleButton: function() {
    var urlString = this.props.post.full_article_url;
    var postID = parseInt(this.props.post.id);
    React.NativeModules.WebViewModule.open(urlString, postID);
  },

  /**
   * Image info button onPress listener.
   */
  _onPressImageCreditButton: function() {
    this.setState({
      imageCreditHidden: !this.state.imageCreditHidden,
    });
  },

  /**
   * Share button onPress listener.
   */
  _onPressShareButton: function() {
    var post = this.props.post;
    var postTitle = Helpers.convertUnicode(post.title);
    var postID = parseInt(post.id);

    React.NativeModules.ShareIntentModule.share(postTitle, post.full_article_url, postID);
  },

  /**
   * Renders the button to link to the full article, if any.
   */
  renderFullArticleButton: function () {
    var post = this.props.post;
    if (post.full_article_url.length > 0) {
      return (
        <View style={styles.articleShareContainer}>
          <Text
            onPress={this._onPressFullArticleButton}
            style={[Theme.styles.textBodyBold, Theme.styles.textColorCtaBlue]}>
              View original story
          </Text>
          <TouchableHighlight
            activeOpacity={0.75}
            underlayColor={'#00000000'}
            style={styles.imageShareButton}
            onPress={this._onPressShareButton}>
            <Image style={styles.imageShareIcon} source={require('image!newsfeed_share_white')} />
          </TouchableHighlight>
        </View>
      );
    }
    return null;
  },

  /**
   * Renders the post's image.
   */
  renderImage: function() {
    var post = this.props.post;

    if (post.image_url.length > 0) {
      var viewImageCredit = null;
      if (post.photo_credit.length > 0) {
        var imageCreditOpacity = 1;
        if (this.state.imageCreditHidden) {
          imageCreditOpacity = 0;
        }
        viewImageCredit = (
          <View style={styles.imageCreditContainer}>
            <View style={[styles.imageCreditTextContainer, {opacity: imageCreditOpacity}]} >
              <Text style={[Theme.styles.textBody, {color: 'white'}]}>{post.photo_credit}</Text>
            </View>
            <TouchableHighlight
              activeOpacity={0.75}
              underlayColor={'#00000000'}
              style={styles.imageCreditButton}
              onPress={this._onPressImageCreditButton}>
              <Image
                style={styles.imageCreditIcon}
                source={require('image!newsfeed_info_icon')}
              />  
            </TouchableHighlight>
          </View>
        );
      }
      return (
        <Image
          style={styles.image}
          source={{uri: post.image_url}}>
          {viewImageCredit}
        </Image>
      );
    }
    return null;
  },

  /**
   * Renders a single summary item.
   */
  renderSummaryItem: function(summaryItemText) {
    if (summaryItemText.length > 0) {
      return (
        <View style={styles.summaryItem}>
        <Image 
          style={styles.listItemOvalImage}
          source={require('image!newsfeed_listitem_oval')} />
          <Text style={[Theme.styles.textBody, styles.summaryText]}>{summaryItemText}</Text>
        </View>
      );
    }
    return null;
  },

  /**
   * Primary render method that renders an entire post.
   */
  render: function() {
    var post = this.props.post;
    var postTitle = Helpers.convertUnicode(post.title);
    var causeTitle, causeStyle = null;
    if (post.categories.length > 0) {
      causeTitle = post.categories[0].title;
      causeStyle = {backgroundColor: '#' + post.categories[0].hex};
    }

    return(
      <View style={[styles.wrapper]}>
        <View style={[styles.header, causeStyle]}>
          <Text style={[Theme.styles.textCaptionBold, {color: 'white'}]}>{Helpers.formatDate(post.date)}</Text>
          <View style={styles.causeContainer}>
            <Text style={[Theme.styles.textCaptionBold, {color: 'white'}]}>{causeTitle}</Text>
          </View>
        </View>
        {this.renderImage()}
        <View style={styles.content}>
          <Text style={Theme.styles.textHeading}>{postTitle.toUpperCase()}</Text>
          {this.renderSummaryItem(post.summary_1)}
          {this.renderSummaryItem(post.summary_2)}
          {this.renderSummaryItem(post.summary_3)}
          {this.renderFullArticleButton()}
        </View>
        <TouchableHighlight onPress={this._onPressActionButton} style={styles.actionButton}>
          <Text style={[Theme.styles.textBodyBold, styles.actionButtonText]}>
            {'Take action'.toUpperCase()}
          </Text>
        </TouchableHighlight>
      </View>
    );
  }
});

var styles = React.StyleSheet.create({
  wrapper: {
    backgroundColor: '#FFFFFF',
    marginTop: 14,
    marginLeft: 7,
    marginRight: 7,
  },
  header: {
    flex: 1,
    flexDirection: 'row',
    backgroundColor: '#00e4c8',
    borderTopLeftRadius: 6,
    borderTopRightRadius: 6,
    padding: 4,
  },
  causeContainer: {
    flex: 1,
    alignItems: 'flex-end',
  },
  content: {
    padding: 20,
  },
  actionButton: {
    backgroundColor: Theme.colorCtaBlue,
    borderBottomLeftRadius: 6,
    borderBottomRightRadius: 6,
    paddingBottom: 10,
    paddingTop: 10,
  },
  actionButtonText: {
    color: '#ffffff',
    textAlign: 'center',
  },
  articleShareContainer: {
    flex: 1,
    flexDirection: 'row',
    marginTop: 14,
  },
  image: {
    flex: 1,
    height: 180,
    justifyContent: 'flex-end',
  },
  imageCreditContainer: {
    backgroundColor: 'transparent',
    margin: 8,
  },
  imageCreditIcon: {
    tintColor: '#ffffff',
    width: 20,
    height: 20,
  },
  imageCreditButton: {
    flex: 1,
    alignItems: 'flex-end',
  },
  imageCreditTextContainer: {
    backgroundColor: 'rgba(0,0,0,0.66)',
    paddingTop: 6,
    paddingBottom: 6,
    paddingLeft: 15,
    paddingRight: 15,
    marginRight: 37,
    flex: 1,
    borderTopLeftRadius: 6,
    borderTopRightRadius: 6,
    borderBottomLeftRadius: 6,
    borderBottomRightRadius: 6,
  },
  imageShareButton: {
    flex: 1,
    alignItems: 'flex-end',
  },
  imageShareIcon: {
    tintColor: Theme.colorCtaBlue,
    width: 22,
    height: 22,
    resizeMode: 'contain',
  },
  listItemOvalImage: {
    // The height and width are based off the draw height of a single summaryText line
    width: 21.5,
    height: 21.5,
    resizeMode: 'contain',
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
    marginLeft: 4,
  },
});

module.exports = NewsFeedPost;
'use strict';

import React, {
  StyleSheet,
  Text,
  Image,
  TouchableHighlight,
  View
} from 'react-native';

var Helpers = require('./newsfeed-helpers');
var styles = require('./styles.js');

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
    React.NativeModules.CampaignNavigationModule.presentCampaignWithCampaignID(campaignID);
  },

  /**
   * Full article link onPress listener.
   */
  _onPressFullArticleButton: function() {
    var urlString = this.props.post.full_article_url;
    React.NativeModules.WebViewModule.open(urlString);
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

    React.NativeModules.ShareIntentModule.share(postTitle, post.full_article_url);
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
            style={styles.fullArticleButton}>
              Read the full article
          </Text>
          <TouchableHighlight
            activeOpacity={0.75}
            underlayColor={'#00000000'}
            style={styles.imageShareButton}
            onPress={this._onPressShareButton}>
            <Image style={styles.imageShareIcon} source={require('image!ic_share_white')} />
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
    
    if (typeof post.attachments[0] !== 'undefined'
        && typeof post.attachments[0].images !== 'undefined'
        && typeof post.attachments[0].images.full !== 'undefined') {

      var viewImageCredit = null;
      var imageCreditText = post.photo_credit;
      if (imageCreditText.length > 0) {
        var imageCreditOpacity = 1;
        if (this.state.imageCreditHidden) {
          imageCreditOpacity = 0;
        }
        viewImageCredit = (
          <View style={styles.imageCreditContainer}>
            <View style={[styles.imageCreditTextContainer, {opacity: imageCreditOpacity}]} >
              <Text style={styles.imageCreditText}>{imageCreditText}</Text>
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
          source={{uri: post.attachments[0].images.full.url}}>
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
          <Text style={styles.summaryText}>{summaryItemText}</Text>
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
      causeStyle = {backgroundColor: Helpers.causeBackgroundColor(causeTitle)};
    }

    return(
      <View style={[styles.wrapper]}>
        <View style={[styles.header, causeStyle]}>
          <Text style={styles.headerText}>{Helpers.formatDate(post.date)}</Text>
          <View style={styles.causeContainer}>
            <Text style={styles.headerText}>{causeTitle}</Text>
          </View>
        </View>
        {this.renderImage()}
        <View style={styles.content}>
          <Text style={styles.title}>{postTitle.toUpperCase()}</Text>
          {this.renderSummaryItem(post.summary_1)}
          {this.renderSummaryItem(post.summary_2)}
          {this.renderSummaryItem(post.summary_3)}
          {this.renderFullArticleButton()}
        </View>
        <TouchableHighlight onPress={this._onPressActionButton} style={styles.actionButton}>
          <Text style={styles.actionButtonText}>{'Take action'.toUpperCase()}</Text>
        </TouchableHighlight>
      </View>
    );
  }
});

module.exports = NewsFeedPost;
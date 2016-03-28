/**
 * This is the view to display when there is a network error detected while loading the news feed.
 */

'use strict';

import React, {
  StyleSheet,
  Text,
  Image,
  TouchableHighlight,
  View
} from 'react-native';

var Theme = require('./ldt-theme.js');

var NetworkErrorView = React.createClass({
  render: function() {
    var retryHandler = null;
    if (this.props.retryHandler) {
      retryHandler = this.props.retryHandler;
    } 
    return (
      <View style={styles.container}>
        <TouchableHighlight onPress={retryHandler}>
          <View style={styles.button}>
            <Image
              style={styles.image}
              source={require('image!newsfeed_network_error')}
            />  
            <Text style={[Theme.styles.textHeading, styles.text]}>
              Unable to load the news
            </Text>
            <Text style={[Theme.styles.textBody, styles.text, {marginTop: 22}]}>
              Please check your network connection, then tap or swipe down to refresh and try again.
            </Text>
          </View>
        </TouchableHighlight>   
      </View>
    );
  },
});

var styles = React.StyleSheet.create({
  container: {
    flex: 1,  
    justifyContent: 'center',
    backgroundColor: '#FFF',
  },
  button: {
    backgroundColor: '#FFF',
    padding: 16,
    alignItems: 'center',
  },
  image: {
    flex: 1,
    height: 100,
    width: 100,
    resizeMode: 'contain',
    alignItems: 'center',
    margin: 16,
  },
  text: {
    textAlign: 'center',
  }
});

module.exports = NetworkErrorView;
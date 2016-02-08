'use strict';

var React = require('react-native');

var {
  StyleSheet,
} = React;

var colorCtaBlue = '#3634AD';
var colorTextDefault = '#4A4A4A';
var fontFamilyName = 'brandon_reg';
var fontFamilyBoldName = 'brandon_bold';
var fontSizeCaption = 13;
var fontSizeBody = 16;
var fontSizeHeading = 18;
var fontSizeTitle = 24;

module.exports = {
  colorCtaBlue: colorCtaBlue,
  styles: StyleSheet.create({
    backgroundColorCtaBlue: {
      backgroundColor: colorCtaBlue,
    },
    textColorCtaBlue: {
      color: colorCtaBlue,
    },
    textCaption: {
      color: colorTextDefault,
      fontFamily: fontFamilyName,
      fontSize: fontSizeCaption,
    },
    textCaptionBold: {
      color: colorTextDefault,
      fontFamily: fontFamilyBoldName,
      fontSize: fontSizeCaption,
    },
    textBody: {
      color: colorTextDefault,
      fontFamily: fontFamilyName,
      fontSize: fontSizeBody,
    },
    textBodyBold: {
      color: colorTextDefault,
      fontFamily: fontFamilyBoldName,
      fontSize: fontSizeBody,
    },
    textSubheading: {
      color: colorTextDefault,
      fontFamily: fontFamilyName,
      fontSize: fontSizeHeading,
    },
    textHeading: {
      color: colorTextDefault,
      fontFamily: fontFamilyBoldName,
      fontSize: fontSizeHeading,
    },
    textTitle: {
      color: colorTextDefault,
      fontFamily: fontFamilyBoldName,
      fontSize: fontSizeTitle,
    },
  }),
};
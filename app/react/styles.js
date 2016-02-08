'use strict';

var React = require('react-native');

var {
  StyleSheet,
} = React;

module.exports = StyleSheet.create({
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
  headerText: {
    color: '#ffffff',
    fontFamily: 'brandon_reg',
  },
  causeContainer: {
    flex: 1,
    alignItems: 'flex-end',
  },
  content: {
    padding: 20,
  },
  fullArticleButton: {
    color: '#3634AD',
    fontFamily: 'brandon_bold',
  },
  actionButton: {
    backgroundColor: '#3634AD',
    borderBottomLeftRadius: 6,
    borderBottomRightRadius: 6,
    paddingBottom: 10,
    paddingTop: 10,
  },
  actionButtonText: {
    color: '#ffffff',
    fontFamily: 'brandon_bold',
    fontSize: 16,
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
  imageCreditText: {
    color: '#FFFFFF',
    fontFamily: 'brandon_reg',
    fontSize: 15,
  },
  imageShareButton: {
    flex: 1,
    alignItems: 'flex-end',
  },
  imageShareIcon: {
    tintColor: '#3634AD',
    width: 22,
    height: 22,
  },
  listItemOvalImage: {
    // The height and width are based off the draw height of a single summaryText line
    width: 21.5,
    height: 21.5,
    resizeMode: 'contain',
  },
  title: {
    color: '#4A4A4A',
    fontFamily: 'brandon_bold',
    fontSize: 20,
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
});

// var Theme = require('react-native').NativeModules.LDTTheme;
// var colorCtaBlue = Theme.colorCtaBlue;
// var colorTextDefault = '#4A4A4A';
// var fontFamilyName = Theme.fontName;
// var fontFamilyBoldName = Theme.fontNameBold;
// var fontSizeCaption = Theme.fontSizeCaption;
// var fontSizeBody = Theme.fontSizeBody;
// var fontSizeHeading = Theme.fontSizeHeading;
// var fontSizeTitle = Theme.fontSizeTitle;

// module.exports = StyleSheet.create({
//   backgroundColorCtaBlue: {
//     backgroundColor: colorCtaBlue,
//   },
//   textColorCtaBlue: {
//     color: colorCtaBlue,
//   },
//   textCaption: {
//     color: colorTextDefault,
//     fontFamily: fontFamilyName,
//     fontSize: fontSizeCaption,
//   },
//   textCaptionBold: {
//     color: colorTextDefault,
//     fontFamily: fontFamilyBoldName,
//     fontSize: fontSizeCaption,
//   },
//   textBody: {
//     color: colorTextDefault,
//     fontFamily: fontFamilyName,
//     fontSize: fontSizeBody,
//   },
//   textBodyBold: {
//     color: colorTextDefault,
//     fontFamily: fontFamilyBoldName,
//     fontSize: fontSizeBody,
//   },
//   textSubheading: {
//     color: colorTextDefault,
//     fontFamily: fontFamilyName,
//     fontSize: fontSizeHeading,
//   },
//   textHeading: {
//     color: colorTextDefault,
//     fontFamily: fontFamilyBoldName,
//     fontSize: fontSizeHeading,
//   },
//   textTitle: {
//     color: colorTextDefault,
//     fontFamily: fontFamilyBoldName,
//     fontSize: fontSizeTitle,
//   },
// });
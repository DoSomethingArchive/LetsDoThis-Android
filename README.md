# Let's Do This

## Development

#### secrets.xml

Make sure to also add a __secrets.xml__ file to the __app/src/main/res/values__ folder for your local development and build boxes to set secret values needed by the app.

```
<resources>
 <string name="facebook_app_id">FACEBOOK APP ID HERE</string>
 <string name="parse_app_id">PARSE APP ID HERE</string>
 <string name="parse_client_key">PARSE CLIENT KEY HERE</string>
 <string name="api_key">NORTHSTAR API KEY HERE</string>
</resources>
```

#### fabric.properties

With the addition of Fabric, you'll also likely need to add a __fabric.properties__ file to the __app/__ folder in order to successfully build.

```
apiSecret=FABRIC SECRET HERE
apiKey=FABRIC KEY HERE
```

#### Fonts

Font files need to be included in the __app/src/main/assets/fonts__ folder. See the [README](https://github.com/DoSomething/LetsDoThis-Android/blob/master/app/src/main/assets/fonts/README.md) for more info.

# Let's Do This

## Development

Make sure to also add a __secrets.xml__ file here for your local development and build boxes to set
secret values needed by the app.

#### secrets.xml
```
<resources>
 <string name="facebook_app_id">FACEBOOK APP ID HERE</string>
 <string name="parse_app_id">PARSE APP ID HERE</string>
 <string name="parse_client_key">PARSE CLIENT KEY HERE</string>
 <string name="api_key">NORTHSTAR API KEY HERE</string>
</resources>
```

With the addition of Fabric, you'll also likely need to add a __fabric.properties__ file in the __app/__ folder in order to successfully build.

#### fabric.properties
```
apiSecret=FABRIC SECRET HERE
apiKey=FABRIC KEY HERE
```

# Let's Do This

## Development

#### secrets.properties

You will need to add a __secrets.properties__ file to the __app/__ folder. This file will include
the keys required to access our Northstar API and 3rd party services.

Your file should look similar to the following code block. Consult the
[documentation](https://github.com/DoSomething/ServerConfig/wiki/3.0-Mobile-app:-Let's-Do-This)
what values should go in here.

```
# Secret ids and keys

# Fabric - build distribution and Crashlytics
FabricApiKey=FABRIC API KEY HERE

# Facebook App Credentials
FacebookAppIdProduction=FACEBOOK PRODUCTION APP ID HERE
FacebookAppIdDebug=FACEBOOK TEST APP ID HERE

# Parse - push notifications
ParseAppId=PARSE APP ID HERE
ParseClientKey=PARSE CLIENT KEY HERE

# Northstar - user API
NorthstarAppIdDebug=NORTHSTAR STAGING APP ID HERE
NorthstarApiKeyDebug=NORTHSTAR STAGING API KEY HERE
NorthstarAppIdThor=NORTHSTAR THOR APP ID HERE
NorthstarApiKeyThor=NORTHSTAR THOR API KEY HERE
NorthstarAppIdProduction=NORTHSTAR PRODUCTION APP ID HERE
NorthstarApiKeyProduction=NORTHSTAR PRODUCTION API KEY HERE
```

#### fabric.properties

With the addition of Fabric, you'll also likely need to add a __fabric.properties__ file to the __app/__ folder in order to successfully build.

```
apiSecret=FABRIC SECRET HERE
apiKey=FABRIC KEY HERE
```

#### Fonts

Font files need to be included in the __app/src/main/assets/fonts__ folder. See the [README](https://github.com/DoSomething/LetsDoThis-Android/blob/master/app/src/main/assets/fonts/README.md) for more info.

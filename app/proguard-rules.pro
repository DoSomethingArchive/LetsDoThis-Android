# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/izzyoji/android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#
# TODO: `minifyEnabled true` was disabled in the build.gradle. Things were getting removed that
#   probably needed to stick around. Pretty sure the below ends up keeping what's needed, but would
#   need a considerable amount of more testing before I'm comfortable letting it go out into the
#   world. I also feel like there's GOT to be a better way. Anyway, just leaving it here until we've
#   got time to revisit it.
#

-dontwarn com.google.android.gms.**
-dontwarn okio.**
-dontwarn retrofit.**

# Retrofit
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*

# Crashlytics
-keepattributes SourceFile,LineNumberTable

# More Retrofit
-keep class com.google.gson.** { *; }
-keep class com.google.inject.** { *; }
-keep class org.apache.http.** { *; }
-keep class org.apache.james.mime4j.** { *; }
-keep class javax.inject.** { *; }
-keep class retrofit.** { *; }
-keep class com.google.android.gms.** { *; }

# okhttp
-keep class com.squareup.** { *; }

# New Relic
-keep class com.newrelic.** { *; }
-dontwarn com.newrelic.**
-keepattributes Exceptions, Signature, InnerClasses, LineNumberTable

# Parse
-keep class com.parse.** { *; }

# Touch lab
-keep class co.touchlab.android.** { *; }

# All the letsdothis stuff
-keep class org.dosomething.letsdothis.** { *; }

# And a few more things
-keep class com.j256.ormlite.** { *; }
-keep class com.crashlytics.** { *; }
-keep class com.viewpagerindicator.** { *; }
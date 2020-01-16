# android-sdk-sample-java
Example application which implementes Chartboost SDK to show basic use cases. It will guide user through initialisation of the SDK, caching and showing 3 types of different ads. 

## Chartboost SDK

Sign up for an account at [https://www.chartboost.com/](https://www.chartboost.com/) to receive your app id. 
For more information go to [https://answers.chartboost.com/en-us/child_article/android](https://answers.chartboost.com/en-us/child_article/android) 

## Download 

Chartboost SDK is available as an AAR via JCenter; to use it, add the following to your `build.gradle`.

```
repositories {
    maven { url  "https://chartboostmobile.bintray.com/Chartboost" }
}

dependencies {
    implementation 'com.chartboost:chartboost-sdk:8.0.1'
}
```

## General information 

To display basic ads with Chartboost SDK, user needs to initialise the SDK before trying to launch the ad.

SDK provides API calls where some are required and others could be optional. 

```
Chartboost.setPIDataUseConsent(this, Chartboost.CBPIDataUseConsent.YES_BEHAVIORAL);
```
*OPTIONAL: GDPR Compliance method with which you can limit our SDK's data collection. Call this method *before* calling Chartboost.startWithAppId() to ensure that our SDK, as well as dependencies such as Moat, will be restricted. Default is Chartboost.CBPIDataUseConsent.UNKNOWN
 
```
Chartboost.startWithAppId(getApplicationContext(), DEFAULT_ID, DEFAULT_SIGNATURE);
```
*REQUIRED: Start Chartboost with required appId, appSignature and delegate.This method must be executed before any other Chartboost SDK methods can be used. Once executed this call will also control session tracking and background tasks used by Chartboost.

```
Chartboost.setShouldPrefetchVideoContent(true);
```
*OPTIONAL: Decide if Chartboost SDK will attempt to prefetch videos from the Chartboost API servers. Default is true.

 ```
Chartboost.setShouldRequestInterstitialsInFirstSession(true);
```
*OPTIONAL: Decide if Chartboost SDK should show interstitials in the first session. Default is true. Set to control if Chartboost SDK can show interstitials in the first session. The session count is controlled via the startWithAppId:appSignature:delegate: method in the Chartboost class.

 ```
Chartboost.setAutoCacheAds(true);
```
*OPTIONAL: Sets whether or not a new impression will automatically be cached. Default is true. 
This method has no effect if called before startWithAppId has been called. 

 ```
Chartboost.setDelegate(delegate);
```
*OPTIONAL: This directly sets the delegate object used by Chartboost. Without delegate user won’t receive any feedback from the SDK about the ads state.

```
Chartboost.setLoggingLevel(CBLogging.Level.ALL);
```
*OPTIONAL: Set the Chartboost SDK logging level. Default is Level.INTEGRATION

## Cache Interstitial
```
public static void cacheInterstitial(final String location)
```
Cache an interstitial with a location argument which is location or in other words name for the impression stored internally in the SDK.


## Show Interstitial
```
public static void showInterstitial(final String location)
```
Load an interstitial with a location argument. Even if the interstitial is already cached, it is shown asynchronously.

## Cache Rewarded video
```
public static void cacheRewardedVideo(final String location)
```
Cache rewarded interstitial with a default location argument or in other words name for the impression stored internally in the SDK.


## Show Rewarded video 
```
public static void showRewardedVideo(final String location)
```
Load rewarded interstitial with a default location argument. Even if the rewarded interstitial is already cached, it is shown asynchronously.

## Banners
To create new banner it is possible to do it in the xml layout file.
```
<com.chartboost.sdk.ChartboostBanner
            android:id="@+id/example_banner"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            chartboost:location="start"
            chartboost:size="STANDARD" />
```


to cache the ad
```
chartboostBanner?.cache()
```

and show to make the ad visible
```
chartboostBanner?.show()
```

and at the end of the activity lifecycle developer should detach Banner from the view and destroy the object. In order to do so, use:
```
chartboostBanner?.detachBanner()
```

# android-sdk-sample-java
Example application which implementes Chartboost SDK to show basic use cases. It will guide user through initialisation of the SDK, caching and showing 3 types of different ads.

## Chartboost SDK

Sign up for an account at [https://www.chartboost.com/](https://www.chartboost.com/) to receive your app id.
For more information go to [https://answers.chartboost.com/en-us/child_article/android](https://answers.chartboost.com/en-us/child_article/android)

## Download

Chartboost SDK is available as an AAR via Maven Central; to use it, add the following to your `build.gradle`.

```
repositories {
    mavenCentral()
    maven {
        url 'https://cboost.jfrog.io/artifactory/chartboost-ads/'
    }
}

dependencies {
    implementation 'com.chartboost:chartboost-sdk:9.6.0'
}
```

## General information

To display basic ads with Chartboost SDK, user needs to initialise the SDK before trying to launch the ad.

SDK provides API calls where some are required and others could be optional.

```
Chartboost.addDataUseConsent(this, new GDPR(GDPR.GDPR_CONSENT.BEHAVIORAL));
Chartboost.addDataUseConsent(this, new CCPA(CCPA.CCPA_CONSENT.OPT_IN_SALE));
Chartboost.addDataUseConsent(this, new COPPA(true));
```
*OPTIONAL: GDPR Compliance method with which you can limit our SDK's data collection. Call this method *before* calling Chartboost.startWithAppId() to ensure that our SDK, as well as dependencies such as Moat, will be restricted.

```
Chartboost.startWithAppId(getApplicationContext(), DEFAULT_ID, DEFAULT_SIGNATURE, startError -> {
            if (startError == null) {
              // handle success
            } else {
              // handle failure
            }
        });
```
*REQUIRED: Start Chartboost with required appId, appSignature and delegate.This method must be executed before any other Chartboost SDK methods can be used. Once executed this call will also control session tracking and background tasks used by Chartboost.


```
Chartboost.setLoggingLevel(LoggingLevel.ALL);
```
*OPTIONAL: Set the Chartboost SDK logging level. Default is Level.INTEGRATION

## Cache Interstitial
```
Interstitial chartboostInterstitial = new Interstitial("location", new InterstitialCallback() {
    @Override
    public void onAdDismiss(@NonNull DismissEvent dismissEvent) {

    }

    @Override
    public void onAdLoaded(@NonNull CacheEvent cacheEvent, @Nullable CacheError cacheError) {

    }

    @Override
    public void onAdRequestedToShow(@NonNull ShowEvent showEvent) {

    }

    @Override
    public void onAdShown(@NonNull ShowEvent showEvent, @Nullable ShowError showError) {

    }

    @Override
    public void onAdClicked(@NonNull ClickEvent clickEvent, @Nullable ClickError clickError) {

    }

    @Override
    public void onImpressionRecorded(@NonNull ImpressionEvent impressionEvent) {

    }
}, mediation);
chartboostInterstitial.cache();

```
Create and cache an interstitial ad object.


## Show Interstitial
```
chartboostInterstitial.show();

```
Show an interstitial ad object.

## Cache Rewarded video
```
Rewarded chartboostRewarded = new Rewarded("start", new RewardedCallback() {
    @Override
    public void onRewardEarned(@NonNull RewardEvent rewardEvent) {

    }

    @Override
    public void onAdDismiss(@NonNull DismissEvent dismissEvent) {

    }

    @Override
    public void onAdLoaded(@NonNull CacheEvent cacheEvent, @Nullable CacheError cacheError) {

    }

    @Override
    public void onAdRequestedToShow(@NonNull ShowEvent showEvent) {

    }

    @Override
    public void onAdShown(@NonNull ShowEvent showEvent, @Nullable ShowError showError) {

    }

    @Override
    public void onAdClicked(@NonNull ClickEvent clickEvent, @Nullable ClickError clickError) {

    }

    @Override
    public void onImpressionRecorded(@NonNull ImpressionEvent impressionEvent) {

    }
}, mediation);
chartboostRewarded.cache();

```
Create and cache rewarded ad object.


## Show Rewarded video
```
chartboostRewarded.show();

```
Show a rewarded ad object.

## Banners
To create new banner it is possible to do it in the xml layout file.
```
Banner chartboostBanner = new Banner(this, "start", Banner.BannerSize.STANDARD, new BannerCallback() {
    @Override
    public void onAdLoaded(@NonNull CacheEvent cacheEvent, @Nullable CacheError cacheError) {

    }

    @Override
    public void onAdRequestedToShow(@NonNull ShowEvent showEvent) {

    }

    @Override
    public void onAdShown(@NonNull ShowEvent showEvent, @Nullable ShowError showError) {

    }

    @Override
    public void onAdClicked(@NonNull ClickEvent clickEvent, @Nullable ClickError clickError) {

    }

    @Override
    public void onImpressionRecorded(@NonNull ImpressionEvent impressionEvent) {

    }
}, mediation);

```


to cache the ad
```
chartboostBanner.cache();

```

and show to make the ad visible
```
chartboostBanner.show();

```

and at the end of the activity lifecycle developer should detach Banner from the view and destroy the object. In order to do so, use:
```
chartboostBanner.detachBanner();

```

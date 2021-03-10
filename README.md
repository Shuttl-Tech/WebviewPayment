# [Android] Payments WebView

[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-orange.svg)](http://makeapullrequest.com)

![Alt Text](https://media.giphy.com/media/sj7ik2M7Dl35YKNReZ/giphy.gif)

A module that will open webview payments screen and close whenever a redirect url is hit

* [Getting Started](#getting-started-)
* [Features](#features-)
* [FAQ](#faq-)
* [Contributing](#contributing-)

Payments WebView simplifies payments webview dialog handling

## Getting Started 👣

Payments WebView is distributed through `to be added`. To use it you need to add the following **Gradle dependency** to your `build.gradle` file of your android app module (NOT the root file).

Following need to be added in the app's `AndroidManifest` root

```xml
<uses-permission android:name="android.permission.INTERNET" />

```

Initiate like this

```kotlin
            WebPaymentsHelper.initiatePayment(
                supportFragmentManager,
                InitiatePayment(payment_url, result_url_1),
                this
            )
```

Parameters needed

```kotlin
@Keep
@Parcelize
data class InitiatePayment(val url: String? = "", val redirectUrl: String? = "") : Parcelable
```

Implement this interface for all the callbacks

```kotlin
interface PaymentStatusInterface {

    fun onUrlChange(url: String? = "")

    fun onPaymentCompleted(url: String? = "")

    fun onClosed(manual: Boolean)

    fun onError(request: WebResourceRequest?, error: WebResourceError?)

}
```

## Features 🧰

Don't forget to check the `changelog to be added` to have a look at all the changes in the latest version

* **API >= 21** compatible
* Easy to integrate
* Implement `PaymentStatusInterface` in your Activity
* Handle Callbacks based on requirements
* Pass in `InitiatePayment` object with required parameter

## FAQ ❓

* **Why is it not working for me?** - dependency issue maybe, create an issue if it doesn't work

## Contributing 🤝

**We're looking for contributors! Don't be shy.** 😁 Feel free to open issues/pull requests to help me improve this project.

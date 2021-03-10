package com.shuttl.payment.webpayments.helpers

import android.webkit.WebResourceError
import android.webkit.WebResourceRequest

interface PaymentStatusInterface {

    fun onUrlChange(url: String? = "")

    fun onPaymentCompleted(url: String? = "")

    fun onClosed(manual: Boolean)

    fun onError(request: WebResourceRequest?, error: WebResourceError?)

}
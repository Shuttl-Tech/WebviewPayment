package com.shuttl.payment.webpayments.helpers

interface PaymentStatusInterface {

    fun onUrlChange(url: String? = "")

    fun onPaymentCompleted(url: String? = "")

    fun onClosed()

}
package com.shuttl.payment.webpayments.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class PaymentState(val orderId: String? = ""): Parcelable
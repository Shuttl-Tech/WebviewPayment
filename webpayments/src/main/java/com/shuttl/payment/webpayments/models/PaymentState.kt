package com.shuttl.payment.webpayments.models

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep

@Keep
data class PaymentState(val orderId: String? = ""): Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(orderId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PaymentState> {
        override fun createFromParcel(parcel: Parcel): PaymentState {
            return PaymentState(parcel)
        }

        override fun newArray(size: Int): Array<PaymentState?> {
            return arrayOfNulls(size)
        }
    }

}
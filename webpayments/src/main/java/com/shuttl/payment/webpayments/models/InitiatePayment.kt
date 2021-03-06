package com.shuttl.payment.webpayments.models

import android.os.Parcel
import android.os.Parcelable
import java.net.URLEncoder

data class InitiatePayment(
    val url: String? = "https://demo2.2c2p.com/2C2PFrontEnd/RedirectV3/payment",
    val redirectUrl: String? = "https://localhost/",
    val formData: InitiatePaymentFormData? = InitiatePaymentFormData()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(InitiatePaymentFormData::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeString(redirectUrl)
        parcel.writeParcelable(formData, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InitiatePayment> {
        override fun createFromParcel(parcel: Parcel): InitiatePayment {
            return InitiatePayment(parcel)
        }

        override fun newArray(size: Int): Array<InitiatePayment?> {
            return arrayOfNulls(size)
        }
    }

}

data class InitiatePaymentFormData(
    val version: String? = "8.5",
    val merchant_id: String? = "014010000000002",
    val currency: String? = "764",
    val result_url_1: String? = "http://localhost",
    val hash_value: String? = "CDB71A2CF97B0430AF555921AB8DAE7B11A4010404F1E221DAB9F882E15BF183",
    val payment_description: String? = "",
    val order_id: String? = "a811f641-c5bb-4d9c-a1e0-1485cb18fb40",
    val amount: String? = "000000001000"
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    fun toValuesString(): String {
        return "amount=${URLEncoder.encode(amount.toString(), "UTF-8")}&" +
                "currency=${URLEncoder.encode(currency.toString(), "UTF-8")}&" +
                "hash_value=${URLEncoder.encode(hash_value.toString(), "UTF-8")}&" +
                "merchant_id=${URLEncoder.encode(merchant_id.toString(), "UTF-8")}&" +
                "order_id=${URLEncoder.encode(order_id.toString(), "UTF-8")}&" +
                "payment_description=${URLEncoder.encode(payment_description.toString(), "UTF-8")}&" +
                "result_url_1=${URLEncoder.encode(result_url_1.toString(), "UTF-8")}&" +
                "version=${URLEncoder.encode(version.toString(), "UTF-8")}"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(version)
        parcel.writeString(merchant_id)
        parcel.writeString(currency)
        parcel.writeString(result_url_1)
        parcel.writeString(hash_value)
        parcel.writeString(payment_description)
        parcel.writeString(order_id)
        parcel.writeString(amount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InitiatePaymentFormData> {
        override fun createFromParcel(parcel: Parcel): InitiatePaymentFormData {
            return InitiatePaymentFormData(parcel)
        }

        override fun newArray(size: Int): Array<InitiatePaymentFormData?> {
            return arrayOfNulls(size)
        }
    }

}

/*

{
  "cached": false,
  "data": {
    "form_data": {
      "amount": "000000001000",
      "currency": "764",
      "hash_value": "CDB71A2CF97B0430AF555921AB8DAE7B11A4010404F1E221DAB9F882E15BF183",
      "merchant_id": "014010000000002",
      "order_id": "a811f641-c5bb-4d9c-a1e0-1485cb18fb40",
      "payment_description": "",
      "result_url_1": "http://localhost",
      "version": "8.5"
    },
    "merchant_id": "014010000000002",
    "order_id": "a811f641-c5bb-4d9c-a1e0-1485cb18fb40",
    "payment_url": "https://demo2.2c2p.com/2C2PFrontEnd/RedirectV3/payment"
  },
  "success": true,
  "title": "This is default title"
}

 */
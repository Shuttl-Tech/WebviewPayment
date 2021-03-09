package com.shuttl.payment.webpayments.helpers

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.shuttl.payment.webpayments.models.InitiatePayment
import com.shuttl.payment.webpayments.ui.WebPaymentsDialog

object WebPaymentsHelper {

    fun initiatePayment(fragmentManager: FragmentManager, initiatePayment: InitiatePayment? = null, paymentStatus: PaymentStatusInterface) {
        initiatePayment?.let {
            WebPaymentsDialog.newInstance(fragmentManager, initiatePayment, paymentStatus)
        }
    }

}
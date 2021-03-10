package com.shuttl.payment.webpayments.helpers

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.shuttl.payment.webpayments.models.InitiatePayment
import com.shuttl.payment.webpayments.ui.WebPaymentsDialog

object WebPaymentsHelper {

    fun initiatePayment(fragmentManager: FragmentManager, initiatePayment: InitiatePayment? = null, paymentStatus: PaymentStatusInterface) {
        initiatePayment?.let {
            WebPaymentsDialog.newInstance(fragmentManager, initiatePayment, paymentStatus)
        }
    }

    fun forceClosePaymentDialog(fragmentManager: FragmentManager) {
        val frag = fragmentManager.findFragmentByTag(WebPaymentsDialog.javaClass.simpleName) as? DialogFragment
        if (frag?.dialog?.isShowing == true) {
            frag.dismiss()
        }
    }

    fun isPaymentScreenOpen(fragmentManager: FragmentManager): Boolean {
        val frag = fragmentManager.findFragmentByTag(WebPaymentsDialog.javaClass.simpleName) as? DialogFragment
        return frag?.dialog?.isShowing == true
    }


}
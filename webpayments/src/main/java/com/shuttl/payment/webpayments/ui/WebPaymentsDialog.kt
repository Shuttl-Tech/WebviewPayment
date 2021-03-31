package com.shuttl.payment.webpayments.ui

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.PEEK_HEIGHT_AUTO
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shuttl.payment.webpayments.R
import com.shuttl.payment.webpayments.helpers.PaymentStatusInterface
import com.shuttl.payment.webpayments.helpers.dpToPx
import com.shuttl.payment.webpayments.models.InitiatePayment
import com.shuttl.payment.webpayments.ui.custom.ObservableWebView

class WebPaymentsDialog : DialogFragment() {

    companion object {
        fun newInstance(fragmentManager: FragmentManager, payment: InitiatePayment, paymentStatus: PaymentStatusInterface) {
            val frag = WebPaymentsDialog()
            val args = Bundle()
            args.putParcelable("data", payment)
            frag.arguments = args
            frag.setInterface(paymentStatus)
            frag.show(fragmentManager, WebPaymentsDialog.javaClass.simpleName)
        }
    }

    private val webViewClient by lazy {
        object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                if ((url ?: "") == getInitiatePayment()?.redirectUrl) {
                    paymentStatus?.onPaymentCompleted(url ?: "")
                    this@WebPaymentsDialog.dismiss()
                } else
                    paymentStatus?.onUrlChange(url ?: "")
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                paymentStatus?.onError(request, error)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }
    }

    // Variables
    private var paymentStatus: PaymentStatusInterface? = null

    // UI
    private var manualExit: Boolean = false
    private var webView: ObservableWebView? = null
    private var done: AppCompatTextView? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = Dialog(this.requireContext(), R.style.CustomBottomSheetDialogTheme)
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_webpayments, null)
        setUpUI(view)
        bottomSheetDialog.setContentView(view)
        return bottomSheetDialog
    }


    private fun setUpUI(view: View) {
        webView = view.findViewById(R.id.webview)
        done = view.findViewById(R.id.done)
        done?.setOnClickListener {
            manualExit = true
            dismiss()
        }
        webView?.webViewClient = webViewClient
        webView?.settings?.setLoadWithOverviewMode(true);
        webView?.settings?.userAgentString = "Android"
        webView?.settings?.useWideViewPort = true
        webView?.settings?.javaScriptEnabled = true
        val payment = getInitiatePayment()
        webView?.postUrl(payment?.url ?: "", (payment?.formData?.toValuesString() ?: "").encodeToByteArray())
    }

    private fun getInitiatePayment(): InitiatePayment? = arguments?.getParcelable("data")

    fun setInterface(paymentStatus: PaymentStatusInterface) {
        this.paymentStatus = paymentStatus
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }


    override fun onDismiss(dialog: DialogInterface) {
        paymentStatus?.onClosed(manualExit)
        super.onDismiss(dialog)
    }

}
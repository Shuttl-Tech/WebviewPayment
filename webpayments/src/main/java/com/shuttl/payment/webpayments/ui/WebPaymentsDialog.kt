package com.shuttl.payment.webpayments.ui

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.shuttl.payment.webpayments.R
import com.shuttl.payment.webpayments.helpers.PaymentStatusInterface
import com.shuttl.payment.webpayments.models.InitiatePayment


class WebPaymentsDialog : DialogFragment() {

    companion object {
        fun newInstance(
            fragmentManager: FragmentManager,
            payment: InitiatePayment,
            paymentStatus: PaymentStatusInterface
        ) {
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
                progress?.visibility = View.VISIBLE
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
                progress?.visibility = View.INVISIBLE
            }
        }
    }

    // Variables
    private var paymentStatus: PaymentStatusInterface? = null

    // UI
    private var manualExit: Boolean = false
    private var webView: WebView? = null
    private var progress: ProgressBar? = null
    private var done: AppCompatTextView? = null

    @ExperimentalStdlibApi
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = Dialog(
            this.requireContext())
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_webpayments, null)
        setUpUI(view)
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bottomSheetDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return bottomSheetDialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    @ExperimentalStdlibApi
    private fun setUpUI(view: View) {
        webView = view.findViewById(R.id.webview)
        done = view.findViewById(R.id.done)
        progress = view.findViewById(R.id.progress)
        done?.setOnClickListener {
            manualExit = true
            dismiss()
        }
        webView?.webViewClient = webViewClient
        webView?.settings?.loadWithOverviewMode = true;
        webView?.settings?.userAgentString = "Android"
        webView?.settings?.useWideViewPort = true
        webView?.settings?.javaScriptEnabled = true
        val payment = getInitiatePayment()
        webView?.postUrl(
            payment?.url ?: "",
            (payment?.formData?.toValuesString() ?: "").encodeToByteArray()
        )
    }

    private fun getInitiatePayment(): InitiatePayment? = arguments?.getParcelable("data")

    fun setInterface(paymentStatus: PaymentStatusInterface) {
        this.paymentStatus = paymentStatus
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as AppCompatActivity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }


    override fun onDismiss(dialog: DialogInterface) {
        paymentStatus?.onClosed(manualExit)
        super.onDismiss(dialog)
    }

}
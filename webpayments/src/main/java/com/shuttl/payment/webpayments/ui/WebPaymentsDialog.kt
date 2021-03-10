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
                Log.d("Url Started", url ?: "")
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
    private var mCurrentWebViewScrollY = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(this.requireContext(), R.style.CustomBottomSheetDialogTheme)
        setUpBottomSheet(bottomSheetDialog)
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_webpayments, null)
        setUpUI(view)
        bottomSheetDialog.setContentView(view)
        return bottomSheetDialog
    }

    private fun setUpBottomSheet(bottomSheetDialog: BottomSheetDialog) {
        bottomSheetDialog.setOnShowListener { dialog ->
            val dialog1 = dialog as BottomSheetDialog
            val bottomSheet: View = dialog1.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) as View
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            BottomSheetBehavior.from(bottomSheet).skipCollapsed = true
            BottomSheetBehavior.from(bottomSheet).isHideable = true
            val windowHeight: Int = getWindowHeight()
            val layoutParams = bottomSheet.layoutParams
            if (layoutParams != null) {
                layoutParams.height = windowHeight - dpToPx(56)
            }
            bottomSheet.layoutParams = layoutParams
            BottomSheetBehavior.from(bottomSheet).peekHeight = layoutParams.height
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED)
            BottomSheetBehavior.from(bottomSheet).let {
                it.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_DRAGGING && mCurrentWebViewScrollY > 0) {
                            it.setState(BottomSheetBehavior.STATE_EXPANDED);
                        } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                            manualExit = true
                            dismiss()
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    }
                })
            }
        }
    }

    private fun setUpUI(view: View) {
        webView = view.findViewById(R.id.webview)
        webView?.onScrollChangedCallback = object : ObservableWebView.OnScrollChangeListener {
            override fun onScrollChanged(currentHorizontalScroll: Int, currentVerticalScroll: Int,
                                         oldHorizontalScroll: Int, oldcurrentVerticalScroll: Int) {
                mCurrentWebViewScrollY = currentVerticalScroll
            }
        }
        webView?.webViewClient = webViewClient
        webView?.settings?.javaScriptEnabled = true
        val payment = getInitiatePayment()
        webView?.loadUrl(payment?.url ?: "")
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
        super.onDismiss(dialog)
        paymentStatus?.onClosed(manualExit)
    }

}
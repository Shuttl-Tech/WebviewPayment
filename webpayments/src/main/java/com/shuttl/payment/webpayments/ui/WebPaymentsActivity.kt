package com.shuttl.payment.webpayments.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.shuttl.payment.webpayments.R
import com.shuttl.payment.webpayments.models.InitiatePayment

class WebPaymentsActivity : AppCompatActivity() {

    private var manualExit: Boolean = false
    private var webView: WebView? = null
    private var done: AppCompatTextView? = null

    companion object {
        fun start(context: Context, payment: InitiatePayment?) {
            val intent = Intent(context, WebPaymentsActivity::class.java)
            intent.putExtra("data", payment)
            context.startActivity(intent)
        }
    }

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_webpayments)
        setUpUI()
    }

    private fun getInitiatePayment(): InitiatePayment =
        intent?.getParcelableExtra("data") ?: InitiatePayment()

    private val webViewClient by lazy {
        object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.d("Url Started", url ?: "")
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }
    }

    @ExperimentalStdlibApi
    private fun setUpUI() {
        webView = findViewById(R.id.webview)
        done = findViewById(R.id.done)
        done?.setOnClickListener {
            manualExit = true
        }
        webView?.webViewClient = webViewClient
        webView?.settings?.loadWithOverviewMode = true
        webView?.settings?.domStorageEnabled = true
        webView?.settings?.userAgentString = "Android"
        webView?.settings?.useWideViewPort = true
        webView?.settings?.javaScriptEnabled = true
        val payment = getInitiatePayment()
        webView?.postUrl(
            payment?.url ?: "",
            (payment?.formData?.toValuesString() ?: "").encodeToByteArray()
        )
    }


}
package com.shuttl.payment.twoctwop

import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.shuttl.payment.webpayments.helpers.PaymentStatusInterface
import com.shuttl.payment.webpayments.helpers.WebPaymentsHelper
import com.shuttl.payment.webpayments.models.InitiatePayment

class MainActivity : AppCompatActivity(), PaymentStatusInterface {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<AppCompatButton>(R.id.start_webview).setOnClickListener {
            WebPaymentsHelper.initiatePayment(
                supportFragmentManager,
                InitiatePayment(),
                this
            )
        }
    }

    override fun onUrlChange(url: String?) {
    }

    override fun onPaymentCompleted(url: String?) {
        Toast.makeText(
            this,
            "Payment was completed, Make a call to backend for status",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onClosed(manual: Boolean) {
        Toast.makeText(
            this,
            if (manual) "Closed Payment Screen Manually" else "Closed Payment Screen Automatically",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onError(request: WebResourceRequest?, error: WebResourceError?) {
    }

}
package com.shuttl.payment.twoctwop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.shuttl.payment.webpayments.helpers.PaymentStatusInterface
import com.shuttl.payment.webpayments.helpers.WebPaymentsHelper
import com.shuttl.payment.webpayments.models.InitiatePayment

class MainActivity : AppCompatActivity(), PaymentStatusInterface {

    val payment_url = "https://sandbox-pgw-ui.2c2p.com/payment/4.1/#/token/kSAops9Zwhos8hSTSeLTUV6oNgaNh3rI9kXUSOfK4XT1Fj78uKABaZO%2boUKrG%2bjQNpPKEvl1CMHXkNR13MAeDGSMhNNq0DyXDzHUzLP63z8%3d"
    val result_url_1 = "http://localhost/devPortal/V3_UI_PHP_JT01_devPortal/result.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<AppCompatButton>(R.id.start_webview).setOnClickListener {
            WebPaymentsHelper.initiatePayment(
                supportFragmentManager,
                InitiatePayment(payment_url, result_url_1),
                this
            )
        }
    }

    override fun onUrlChange(url: String?) {
    }

    override fun onPaymentCompleted(url: String?) {
    }

    override fun onClosed() {
    }
}
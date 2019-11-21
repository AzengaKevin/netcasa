package com.shadow.netcasa

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar.progress = 0

        display.loadUrl("https://cart.netcasa.co.ke")
        display.settings.javaScriptEnabled = true
        display.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
                swipeToRefreshLayout.isRefreshing = false
            }
        }

        display.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar.progress = newProgress
            }

        }

        swipeToRefreshLayout.setOnRefreshListener {
            display.reload()
        }

    }

    override fun onStart() {
        super.onStart()

        display.viewTreeObserver.addOnScrollChangedListener {
            swipeToRefreshLayout.isEnabled = display.scrollY == 0
        }
    }

    override fun onStop() {
        super.onStop()
        display.viewTreeObserver.removeOnScrollChangedListener {
            //Do nothing
        }
    }

    override fun onBackPressed() {
        if (display.canGoBack()) {
            display.goBack()
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Do you want to exit the app?")
                setPositiveButton("Yes") { _, _ ->
                    finish()
                }

                setNegativeButton("Cancel") { _, _ ->

                }
            }.create().show()
        }
    }
}

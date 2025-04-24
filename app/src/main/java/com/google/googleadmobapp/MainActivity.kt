package com.google.googleadmobapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.googleadmobapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(context) {
                val adView = AdView(context)
                adView.adUnitId = "ca-app-pub-3940256099942544/9214589741"
// Request an anchored adaptive banner with a width of 360.
                adView.setAdSize(
                    AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                        context,
                        360
                    )
                )
                context.adView = adView

                binding.adViewContainer.removeAllViews()
                binding.adViewContainer.addView(adView)

                val adRequest = AdRequest.Builder().build()
                adView.loadAd(adRequest)

                adView.adListener = object : AdListener()
                {
                    override fun onAdLoaded()
                    {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }

        binding.progressBar.visibility = View.VISIBLE

        binding.textView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse("https://www.google.com/"))
            startActivity(intent)
        }
    }

    private lateinit var binding: ActivityMainBinding
    private var adView: AdView? = null
    private val context = this
}
package com.shatsy.admobflutter

import android.content.Context
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.MobileAds
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import android.os.Bundle



fun createAdListener(channel: MethodChannel) : AdListener {
  return object: AdListener() {
    override fun onAdLoaded() = channel.invokeMethod("loaded", null)
    override fun onAdFailedToLoad(errorCode: Int) = channel.invokeMethod("failedToLoad", hashMapOf("errorCode" to errorCode))
    override fun onAdClicked() = channel.invokeMethod("clicked", null)
    override fun onAdImpression() = channel.invokeMethod("impression", null)
    override fun onAdOpened() = channel.invokeMethod("opened", null)
    override fun onAdLeftApplication() = channel.invokeMethod("leftApplication", null)
    override fun onAdClosed() = channel.invokeMethod("closed", null)
  }
}

class AdmobFlutterPlugin(private val context: Context): MethodCallHandler {
  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val defaultChannel = MethodChannel(registrar.messenger(), "admob_flutter")
      defaultChannel.setMethodCallHandler(AdmobFlutterPlugin(registrar.context()))

      val interstitialChannel = MethodChannel(registrar.messenger(), "admob_flutter/interstitial")
      interstitialChannel.setMethodCallHandler(AdmobInterstitial(registrar))

      val rewardChannel = MethodChannel(registrar.messenger(), "admob_flutter/reward")
      rewardChannel.setMethodCallHandler(AdmobReward(registrar))

      registrar
        .platformViewRegistry()
        .registerViewFactory("admob_flutter/banner", AdmobBannerFactory(registrar.messenger()))
    }


    fun getExtrasBundle(personalizedAds: Boolean): Bundle {
      val npa: String = if (personalizedAds) "0" else "1"
      val extras = Bundle()
      extras.putString("npa", npa)
      return extras
    }
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    when(call.method) {
      "getPlatformVersion" -> result.success("Android ${android.os.Build.VERSION.RELEASE}")
      "initialize" -> {
        MobileAds.initialize(context)
        result.success(null)
      }
      "banner_size" -> {
        val args = call.arguments as HashMap<*, *>
        val name = args["name"] as String
        val width = args["width"] as Int
        when(name) {
          "SMART_BANNER" -> {
            val metrics = context.resources.displayMetrics
            result.success(hashMapOf(
                    "width" to AdSize.SMART_BANNER.getWidthInPixels(context) / metrics.density,
                    "height" to AdSize.SMART_BANNER.getHeightInPixels(context) / metrics.density
            ))
          }
          "ADAPTIVE_BANNER" -> {
            val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, width)
            result.success(hashMapOf(
              "width" to adSize.width,
              "height" to adSize.height
            ))
          }
          else -> result.error("banner_size",  "not implemented name", name)
        }
      }
      else -> result.notImplemented()
    }
  }
}

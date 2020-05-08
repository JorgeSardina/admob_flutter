import 'dart:ui';
import 'package:flutter/services.dart';
import 'package:admob_flutter/admob_flutter.dart';

class Admob {
  static const MethodChannel _channel =  MethodChannel('admob_flutter');

  static Future<void> initialize(String appId) async {
    await _channel.invokeMethod('initialize', appId);
  }

  static Future<Size> bannerSize(AdmobBannerSize admobBannerSize) async {
    final rawResult =
        await _channel.invokeMethod('banner_size', admobBannerSize.toMap);
    final resultMap = Map<String, num>.from(rawResult);
    return Size(
        resultMap["width"].ceilToDouble(), resultMap["height"].ceilToDouble());
  }
}

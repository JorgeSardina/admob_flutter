import 'package:flutter/services.dart';

class Admob {
  static Future<void> initialize(String appId) async {
    MethodChannel _channel = const MethodChannel('admob_flutter');
    await _channel.invokeMethod('initialize', appId);
  }
}

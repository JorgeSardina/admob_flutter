#import "AdmobFlutterPlugin.h"

@implementation AdmobFlutterPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"admob_flutter"
            binaryMessenger:[registrar messenger]];
  AdmobFlutterPlugin* instance = [[AdmobFlutterPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    result(FlutterMethodNotImplemented);

}

@end
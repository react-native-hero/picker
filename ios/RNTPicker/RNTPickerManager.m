
#import "RNTPickerManager.h"
#import "HeroPicker-Swift.h"
#import <React/RCTFont.h>

@implementation RNTPickerManager

RCT_EXPORT_MODULE(RNTPicker)

- (UIView *)view {
    return [PickerView new];
}

RCT_EXPORT_VIEW_PROPERTY(items, NSArray<NSDictionary *>)
RCT_EXPORT_VIEW_PROPERTY(selectedIndex, NSInteger)
RCT_EXPORT_VIEW_PROPERTY(color, UIColor)
RCT_EXPORT_VIEW_PROPERTY(textAlign, NSTextAlignment)
RCT_EXPORT_VIEW_PROPERTY(rowHeight, NSInteger)
RCT_EXPORT_VIEW_PROPERTY(onChange, RCTBubblingEventBlock)

RCT_CUSTOM_VIEW_PROPERTY(fontSize, NSNumber, PickerView) {
  view.font = [RCTFont updateFont:view.font withSize:json ?: @(defaultView.font.pointSize)];
}
RCT_CUSTOM_VIEW_PROPERTY(fontWeight, NSString, PickerView) {
  view.font = [RCTFont updateFont:view.font withWeight:json];
}
RCT_CUSTOM_VIEW_PROPERTY(fontStyle, NSString, PickerView) {
  view.font = [RCTFont updateFont:view.font withStyle:json];
}
RCT_CUSTOM_VIEW_PROPERTY(fontFamily, NSString, PickerView) {
  view.font = [RCTFont updateFont:view.font withFamily:json ?: defaultView.font.familyName];
}

@end

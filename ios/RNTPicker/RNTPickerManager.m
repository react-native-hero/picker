
#import "RNTPickerManager.h"
#import "HeroPicker-Swift.h"

@implementation RNTPickerManager

RCT_EXPORT_MODULE(RNTPicker)

- (UIView *)view {
    return [PickerView new];
}

RCT_EXPORT_VIEW_PROPERTY(options, NSArray<NSDictionary *>)
RCT_EXPORT_VIEW_PROPERTY(selectedIndex, NSInteger)
RCT_EXPORT_VIEW_PROPERTY(color, UIColor)
RCT_EXPORT_VIEW_PROPERTY(fontSize, NSInteger)
RCT_EXPORT_VIEW_PROPERTY(rowHeight, NSInteger)
RCT_EXPORT_VIEW_PROPERTY(onChange, RCTBubblingEventBlock)

@end

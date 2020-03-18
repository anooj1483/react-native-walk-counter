
#import "RNWalkCounter.h"


#import <CoreMotion/CoreMotion.h>
#import "RCTBridge.h"
#import "RCTEventDispatcher.h"

#define NullErr [NSNull null]

@interface RNWalkCounter ()
@property (nonatomic, readonly) CMWalkCounter *walkCounter;
@end


@implementation RNWalkCounter

@synthesize bridge = _bridge;

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(isStepCountingAvailable:(RCTResponseSenderBlock) callback) {
  callback(@[NullErr, @([CMWalkCounter isStepCountingAvailable])]);
}

RCT_EXPORT_METHOD(isFloorCountingAvailable:(RCTResponseSenderBlock) callback) {
  callback(@[NullErr, @([CMWalkCounter isFloorCountingAvailable])]);
}

RCT_EXPORT_METHOD(isDistanceAvailable:(RCTResponseSenderBlock) callback) {
  callback(@[NullErr, @([CMWalkCounter isDistanceAvailable])]);
}

RCT_EXPORT_METHOD(isCadenceAvailable:(RCTResponseSenderBlock) callback) {
  callback(@[NullErr, @([CMWalkCounter isCadenceAvailable])]);
}

RCT_EXPORT_METHOD(isPaceAvailable:(RCTResponseSenderBlock) callback) {
  callback(@[NullErr, @([CMWalkCounter isPaceAvailable])]);
}

RCT_EXPORT_METHOD(queryWalkCounterDataBetweenDates:(NSDate *)startDate endDate:(NSDate *)endDate handler:(RCTResponseSenderBlock)handler) {
  [self.pedometer queryWalkCounterDataFromDate:startDate
                                      toDate:endDate
                                 withHandler:^(CMWalkCounterData *WalkCounterData, NSError *error) {
                                   handler(@[error.description?:NullErr, [self dictionaryFromWalkCounterData:WalkCounterData]]);
                                 }];
}

RCT_EXPORT_METHOD(startWalkCounterUpdatesFromDate:(NSDate *)date) {
  [self.pedometer startWalkCounterUpdatesFromDate:date?:[NSDate date]
                                    withHandler:^(CMWalkCounterData *WalkCounterData, NSError *error) {
                                      if (WalkCounterData) {
                                        [[self.bridge eventDispatcher] sendDeviceEventWithName:@"WalkCounterDataDidUpdate" body:[self dictionaryFromWalkCounterData:WalkCounterData]];
                                      }
                                    }];
}

- (NSDictionary *)dictionaryFromWalkCounterData:(CMWalkCounterData *)data {

  static NSDateFormatter *formatter;
  static dispatch_once_t onceToken;
  dispatch_once(&onceToken, ^{
    formatter = [[NSDateFormatter alloc] init];
    formatter.dateFormat = @"yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ";
    formatter.locale = [NSLocale localeWithLocaleIdentifier:@"en_US_POSIX"];
    formatter.timeZone = [NSTimeZone timeZoneWithName:@"UTC"];
  });
  return @{

           @"startDate": [formatter stringFromDate:data.startDate]?:NullErr,
           @"endDate": [formatter stringFromDate:data.endDate]?:NullErr,
           @"numberOfSteps": data.numberOfSteps?:NullErr,
           @"distance": data.distance?:NullErr,
           @"floorsAscended": data.floorsAscended?:NullErr,
           @"floorsDescended": data.floorsDescended?:NullErr,
           @"currentPace": data.currentPace?:NullErr,
           @"currentCadence": data.currentCadence?:NullErr,
           };
}

RCT_EXPORT_METHOD(stopWalkCounterUpdates) {
  [self.walkCounter stopWalkCounterUpdates];
}

#pragma mark - Private

- (instancetype)init
{
  self = [super init];
  if (self == nil) {
    return nil;
  }

  _walkCounter = [CMWalkCounter new];

  return self;
}

@end
  

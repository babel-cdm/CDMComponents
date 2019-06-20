#import <Foundation/Foundation.h>

@class MainCDMComponents, MainSecurityUtils, MainCommonLoggerUtils, MainSecurityError, MainCommonCDMComponentsError, MainSecurityErrorCommonCode, MainKotlinEnum, MainCommonEither, MainIOSCode, MainKeychainDataSourceImp, MainCommonLoggerUtilsLogLevel, MainCommonEitherLeft, MainCommonEitherRight, MainKotlinNothing;

@protocol MainKotlinComparable, MainKeychainDataSource, MainCommonLogger;

NS_ASSUME_NONNULL_BEGIN

@interface KotlinBase : NSObject
- (instancetype)init __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (void)initialize __attribute__((objc_requires_super));
@end;

@interface KotlinBase (KotlinBaseCopying) <NSCopying>
@end;

__attribute__((objc_runtime_name("KotlinMutableSet")))
__attribute__((swift_name("KotlinMutableSet")))
@interface MainMutableSet<ObjectType> : NSMutableSet<ObjectType>
@end;

__attribute__((objc_runtime_name("KotlinMutableDictionary")))
__attribute__((swift_name("KotlinMutableDictionary")))
@interface MainMutableDictionary<KeyType, ObjectType> : NSMutableDictionary<KeyType, ObjectType>
@end;

@interface NSError (NSErrorKotlinException)
@property (readonly) id _Nullable kotlinException;
@end;

__attribute__((objc_runtime_name("KotlinNumber")))
__attribute__((swift_name("KotlinNumber")))
@interface MainNumber : NSNumber
- (instancetype)initWithChar:(char)value __attribute__((unavailable));
- (instancetype)initWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
- (instancetype)initWithShort:(short)value __attribute__((unavailable));
- (instancetype)initWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
- (instancetype)initWithInt:(int)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
- (instancetype)initWithLong:(long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
- (instancetype)initWithLongLong:(long long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
- (instancetype)initWithFloat:(float)value __attribute__((unavailable));
- (instancetype)initWithDouble:(double)value __attribute__((unavailable));
- (instancetype)initWithBool:(BOOL)value __attribute__((unavailable));
- (instancetype)initWithInteger:(NSInteger)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
+ (instancetype)numberWithChar:(char)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
+ (instancetype)numberWithShort:(short)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
+ (instancetype)numberWithInt:(int)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
+ (instancetype)numberWithLong:(long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
+ (instancetype)numberWithLongLong:(long long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
+ (instancetype)numberWithFloat:(float)value __attribute__((unavailable));
+ (instancetype)numberWithDouble:(double)value __attribute__((unavailable));
+ (instancetype)numberWithBool:(BOOL)value __attribute__((unavailable));
+ (instancetype)numberWithInteger:(NSInteger)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
@end;

__attribute__((objc_runtime_name("KotlinByte")))
__attribute__((swift_name("KotlinByte")))
@interface MainByte : MainNumber
- (instancetype)initWithChar:(char)value;
+ (instancetype)numberWithChar:(char)value;
@end;

__attribute__((objc_runtime_name("KotlinUByte")))
__attribute__((swift_name("KotlinUByte")))
@interface MainUByte : MainNumber
- (instancetype)initWithUnsignedChar:(unsigned char)value;
+ (instancetype)numberWithUnsignedChar:(unsigned char)value;
@end;

__attribute__((objc_runtime_name("KotlinShort")))
__attribute__((swift_name("KotlinShort")))
@interface MainShort : MainNumber
- (instancetype)initWithShort:(short)value;
+ (instancetype)numberWithShort:(short)value;
@end;

__attribute__((objc_runtime_name("KotlinUShort")))
__attribute__((swift_name("KotlinUShort")))
@interface MainUShort : MainNumber
- (instancetype)initWithUnsignedShort:(unsigned short)value;
+ (instancetype)numberWithUnsignedShort:(unsigned short)value;
@end;

__attribute__((objc_runtime_name("KotlinInt")))
__attribute__((swift_name("KotlinInt")))
@interface MainInt : MainNumber
- (instancetype)initWithInt:(int)value;
+ (instancetype)numberWithInt:(int)value;
@end;

__attribute__((objc_runtime_name("KotlinUInt")))
__attribute__((swift_name("KotlinUInt")))
@interface MainUInt : MainNumber
- (instancetype)initWithUnsignedInt:(unsigned int)value;
+ (instancetype)numberWithUnsignedInt:(unsigned int)value;
@end;

__attribute__((objc_runtime_name("KotlinLong")))
__attribute__((swift_name("KotlinLong")))
@interface MainLong : MainNumber
- (instancetype)initWithLongLong:(long long)value;
+ (instancetype)numberWithLongLong:(long long)value;
@end;

__attribute__((objc_runtime_name("KotlinULong")))
__attribute__((swift_name("KotlinULong")))
@interface MainULong : MainNumber
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value;
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value;
@end;

__attribute__((objc_runtime_name("KotlinFloat")))
__attribute__((swift_name("KotlinFloat")))
@interface MainFloat : MainNumber
- (instancetype)initWithFloat:(float)value;
+ (instancetype)numberWithFloat:(float)value;
@end;

__attribute__((objc_runtime_name("KotlinDouble")))
__attribute__((swift_name("KotlinDouble")))
@interface MainDouble : MainNumber
- (instancetype)initWithDouble:(double)value;
+ (instancetype)numberWithDouble:(double)value;
@end;

__attribute__((objc_runtime_name("KotlinBoolean")))
__attribute__((swift_name("KotlinBoolean")))
@interface MainBoolean : MainNumber
- (instancetype)initWithBool:(BOOL)value;
+ (instancetype)numberWithBool:(BOOL)value;
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CDMComponents")))
@interface MainCDMComponents : KotlinBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)cDMComponents __attribute__((swift_name("init()")));
@property (readonly) MainSecurityUtils *securityUtils;
@property (readonly) MainCommonLoggerUtils *loggerUtils;
@end;

__attribute__((swift_name("CommonCDMComponentsError")))
@interface MainCommonCDMComponentsError : KotlinBase
- (instancetype)initWithId:(int32_t)id description:(NSString *)description __attribute__((swift_name("init(id:description:)"))) __attribute__((objc_designated_initializer));
@property (readonly, getter=description_) NSString *description;
@property (readonly) int32_t id;
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SecurityError")))
@interface MainSecurityError : MainCommonCDMComponentsError
- (instancetype)initWithId:(int32_t)id description:(NSString *)description __attribute__((swift_name("init(id:description:)"))) __attribute__((objc_designated_initializer));
@end;

__attribute__((swift_name("KotlinComparable")))
@protocol MainKotlinComparable
@required
- (int32_t)compareToOther:(id _Nullable)other __attribute__((swift_name("compareTo(other:)")));
@end;

__attribute__((swift_name("KotlinEnum")))
@interface MainKotlinEnum : KotlinBase <MainKotlinComparable>
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer));
- (int32_t)compareToOther:(MainKotlinEnum *)other __attribute__((swift_name("compareTo(other:)")));
@property (readonly) NSString *name;
@property (readonly) int32_t ordinal;
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SecurityError.CommonCode")))
@interface MainSecurityErrorCommonCode : MainKotlinEnum
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
@property (class, readonly) MainSecurityErrorCommonCode *securityDefaultAndroidError;
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (int32_t)compareToOther:(MainSecurityErrorCommonCode *)other __attribute__((swift_name("compareTo(other:)")));
@property (readonly) int32_t code;
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SecurityUtils")))
@interface MainSecurityUtils : KotlinBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (MainCommonEither *)storeSecureKey:(NSString *)key value:(NSString *)value __attribute__((swift_name("storeSecure(key:value:)")));
- (MainCommonEither *)retrieveFromSecureStorageKey:(NSString *)key __attribute__((swift_name("retrieveFromSecureStorage(key:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IOSCode")))
@interface MainIOSCode : MainKotlinEnum
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
@property (class, readonly) MainIOSCode *securityDefaultIosError;
@property (class, readonly) MainIOSCode *keyNotFound;
@property (class, readonly) MainIOSCode *unableToEncrypt;
@property (class, readonly) MainIOSCode *wrongValueParam;
@property (class, readonly) MainIOSCode *saveKeyError;
@property (class, readonly) MainIOSCode *recoverError;
@property (class, readonly) MainIOSCode *unableToDecrypt;
@property (class, readonly) MainIOSCode *encodingError;
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (int32_t)compareToOther:(MainIOSCode *)other __attribute__((swift_name("compareTo(other:)")));
@property (readonly) int32_t code;
@end;

__attribute__((swift_name("KeychainDataSource")))
@protocol MainKeychainDataSource
@required
- (MainCommonEither *)generateAppKey __attribute__((swift_name("generateAppKey()")));
- (MainCommonEither *)getAppKeyPrivate:(BOOL)private_ __attribute__((swift_name("getAppKey(private:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KeychainDataSourceImp")))
@interface MainKeychainDataSourceImp : KotlinBase <MainKeychainDataSource>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IOSSecurityConstantsKt")))
@interface MainIOSSecurityConstantsKt : KotlinBase
@property (class, readonly) NSString *PUBLIC_KEY_ALIAS;
@property (class, readonly) NSString *PRIVATE_KEY_ALIAS;
@property (class, readonly) NSString *APP_ALIAS;
@property (class, readonly) int32_t KEY_SIZE;
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KeychainDataSourceImpKt")))
@interface MainKeychainDataSourceImpKt : KotlinBase
@property (class, readonly) NSString *NOT_FOUND;
@property (class, readonly) NSString *WRONG_VALUE;
@property (class, readonly) NSString *SAVE_KEY;
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SecurityUtilsKt")))
@interface MainSecurityUtilsKt : KotlinBase
@property (class, readonly) NSString *ENCRYPT_ERROR;
@property (class, readonly) NSString *DECRYPT_ERROR;
@property (class, readonly) NSString *EMPTY_RECOVER;
@property (class, readonly) NSString *ENCODING;
@end;

__attribute__((swift_name("CommonLogger")))
@protocol MainCommonLogger
@required
- (void)logDTag:(NSString *)tag message:(NSString *)message __attribute__((swift_name("logD(tag:message:)")));
- (void)logETag:(NSString *)tag message:(NSString *)message __attribute__((swift_name("logE(tag:message:)")));
- (void)logITag:(NSString *)tag message:(NSString *)message __attribute__((swift_name("logI(tag:message:)")));
- (void)logWTag:(NSString *)tag message:(NSString *)message __attribute__((swift_name("logW(tag:message:)")));
- (void)setLevelLevel:(MainCommonLoggerUtilsLogLevel *)level __attribute__((swift_name("setLevel(level:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CommonLoggerUtils")))
@interface MainCommonLoggerUtils : KotlinBase <MainCommonLogger>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)loggerUtils __attribute__((swift_name("init()")));
@end;

__attribute__((swift_name("CommonEither")))
@interface MainCommonEither : KotlinBase
- (id _Nullable)foldLeft:(id _Nullable (^)(id _Nullable))left right:(id _Nullable (^)(id _Nullable))right __attribute__((swift_name("fold(left:right:)")));
- (MainCommonEitherLeft *)leftA:(id _Nullable)a __attribute__((swift_name("left(a:)")));
- (MainCommonEither *)mapF:(id _Nullable (^)(id _Nullable))f __attribute__((swift_name("map(f:)")));
- (MainCommonEitherRight *)rightB:(id _Nullable)b __attribute__((swift_name("right(b:)")));
@property (readonly) BOOL isLeft;
@property (readonly) BOOL isRight;
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CommonLoggerUtils.LogLevel")))
@interface MainCommonLoggerUtilsLogLevel : MainKotlinEnum
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
@property (class, readonly) MainCommonLoggerUtilsLogLevel *debug;
@property (class, readonly) MainCommonLoggerUtilsLogLevel *info;
@property (class, readonly) MainCommonLoggerUtilsLogLevel *warn;
@property (class, readonly) MainCommonLoggerUtilsLogLevel *error;
@property (class, readonly) MainCommonLoggerUtilsLogLevel *none;
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (int32_t)compareToOther:(MainCommonLoggerUtilsLogLevel *)other __attribute__((swift_name("compareTo(other:)")));
@property (readonly) int32_t value;
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CommonEither.Left")))
@interface MainCommonEitherLeft : MainCommonEither
- (instancetype)initWithValue:(id _Nullable)value __attribute__((swift_name("init(value:)"))) __attribute__((objc_designated_initializer));
- (id _Nullable)component1 __attribute__((swift_name("component1()")));
- (MainCommonEitherLeft *)doCopyValue:(id _Nullable)value __attribute__((swift_name("doCopy(value:)")));
- (id _Nullable)foldLeft:(id _Nullable (^)(id _Nullable))left right:(id _Nullable (^)(MainKotlinNothing *))right __attribute__((swift_name("fold(left:right:)")));
- (MainCommonEither *)mapF:(id _Nullable (^)(MainKotlinNothing *))f __attribute__((swift_name("map(f:)")));
@property (readonly) id _Nullable value;
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CommonEither.Right")))
@interface MainCommonEitherRight : MainCommonEither
- (instancetype)initWithValue:(id _Nullable)value __attribute__((swift_name("init(value:)"))) __attribute__((objc_designated_initializer));
- (id _Nullable)component1 __attribute__((swift_name("component1()")));
- (MainCommonEitherRight *)doCopyValue:(id _Nullable)value __attribute__((swift_name("doCopy(value:)")));
- (id _Nullable)foldLeft:(id _Nullable (^)(MainKotlinNothing *))left right:(id _Nullable (^)(id _Nullable))right __attribute__((swift_name("fold(left:right:)")));
@property (readonly) id _Nullable value;
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinNothing")))
@interface MainKotlinNothing : KotlinBase
@end;

NS_ASSUME_NONNULL_END

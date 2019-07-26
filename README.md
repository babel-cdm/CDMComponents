# CDMComponents
--
## Installation
### iOS
Import framework generated at:

`/security/build/xcode-frameworks`

### Android
[![](https://jitpack.io/v/babel-cdm/CDMComponents.svg)](https://jitpack.io/#babel-cdm/CDMComponents)

root build.gradle

	allprojects {
			repositories {
				...
				maven { url 'https://jitpack.io' }
			}
		}
		

dependency

	dependencies {
	        implementation 'com.github.babel-cdm.CDMComponents:common:1.0.1@aar'
	        implementation 'com.github.babel-cdm.CDMComponents:security:1.0.1@aar'
	}
manifest

	<provider
                tools:replace="android:authorities"
                android:name="com.babel.cdm.components.security.SecurityUtilsProvider"
                android:authorities="${applicationId}.components.security.SecurityUtilsProvider"
                android:exported="false" />

## LoggerUtils
Component to use custom logger.
### LogLevels
- DEBUG(40) -> `logD`
- INFO(30) -> `logI`
- WARN(20) -> `logW`
- ERROR(10) -> `logE`
- NONE(0)

### Set log level
#### iOS

`CDMComponents().loggerUtils.setLevel(level: .debug)`
#### Android

`CDMComponents.loggerUtils.setLevel(DEBUG)`

### Log X
#### iOS

`CDMComponents().loggerUtils.logD(tag: "TAG", message: "MESSAGE")`
##### Android

`CDMComponents.loggerUtils.logD("TAG","MESSAGE")`

## SecurityUtils 
Component to store info securely.
### StoreSecure -> `Either<CDMComponentsError,String>`
#### iOS

`CDMComponents().securityUtils.storeSecure(key:"KEY",value:"VALUE")`
#### Android

`CDMComponents.securityUtils.storeSecure("KEY","VALUE")`

### Retrieve from secure storage -> `Either<CDMComponentsError,String>`
#### iOS

`CDMComponents().securityUtils.retrieveFromSecureStorage(key: "KEY")`
#### Android

`CDMComponents.securityUtils.retrieveFromSecureStorage("KEY")`

## Common
### Either
#### iOS - fold

	either.fold(left: { error in
            let e = error as! CommonCDMComponentsError
            print(e.id)
            return nil
        }, right: { ok in
            let o = ok as! String
            print(o)
            return nil
        })
#### Android - fold

	either.fold({
            Log.d("TAG","Error: $it")
        },{
            Log.d("TAG",it)
        })
#### iOS - map

	either.map(f: {result in
            let o = result as! String
            print(o)
            return nil
        })
#### Android - map

	either.map { 
            Log.d("TAG",it)
        }

## 🛑 Error codes
### Android
| Code                              |Title         |Description             |
| ----------------------------------|--------------|------------------------|
| <span style="color:red">**`2101`**|`SECURITY_DEFAULT_ANDROID_ERROR`|Security component default error.|
| <span style="color:red">**`2102`**|`KEYSTORE_EXCEPTION`|Keystore exception error.|
| <span style="color:red">**`2103`**|`KEY_EXCEPTION`|Key exception error.|
| <span style="color:red">**`2104`**|`NULL_POINTER`|Security component null pointer error.|
| <span style="color:red">**`2105`**|`APP_KEY_DOES_NOT_EXIST`|App key not exists error.|	
### iOS
| Code                              |Title         |Description             |
| ----------------------------------|--------------|------------------------|
| <span style="color:red">**`2201`**|`SECURITY_DEFAULT_IOS_ERROR`|Security component default error.|
| <span style="color:red">**`2202`**|`KEY_NOT_FOUND`|App key not fould error.|
| <span style="color:red">**`2203`**|`UNABLE_TO_ENCRYPT`|Encrypt error.|
| <span style="color:red">**`2204`**|`WRONG_VALUE_PARAM`|Wrong value param error.|
| <span style="color:red">**`2205`**|`SAVE_KEY_ERROR`|Error saving key.|
| <span style="color:red">**`2206`**|`RECOVER_ERROR`|Error recovering.|
| <span style="color:red">**`2207`**|`UNABLE_TO_DECRYPT`|Dencrypt error.|
| <span style="color:red">**`2208`**|`ENCODING_ERROR`|Encoding error.|
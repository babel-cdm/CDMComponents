package com.babel.cdm.components.security

import com.babel.cdm.components.common.CDMComponentsError
import com.babel.cdm.components.common.Either

actual class SecurityUtils actual constructor() {

    actual fun storeSecure(key: String, value: String): Either<CDMComponentsError, String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual fun retrieveFromSecureStorage(key: String): Either<CDMComponentsError, String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
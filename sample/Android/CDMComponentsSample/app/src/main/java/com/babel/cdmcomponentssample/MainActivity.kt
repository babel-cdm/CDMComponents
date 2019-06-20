package com.babel.cdmcomponentssample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.babel.cdm.components.security.CDMComponents
import com.babel.cdm.components.security.SecurityUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CDMComponents.securityUtils.storeSecure("A","Hello world")
        CDMComponents.securityUtils.retrieveFromSecureStorage("A").fold({
            Log.d("MainActivity","Error: $it")
        },{
            Log.d("MainActivity",it)
        })
    }
}

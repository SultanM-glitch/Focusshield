package com.shield.focusblocker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class BlockActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Prevent closing block screen via back button
    }
}

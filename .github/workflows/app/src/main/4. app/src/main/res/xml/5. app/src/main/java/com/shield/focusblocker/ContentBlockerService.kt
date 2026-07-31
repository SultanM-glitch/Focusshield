package com.shield.focusblocker

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class ContentBlockerService : AccessibilityService() {

    private val blockedKeywords = listOf(
        "porn", "xxx", "adult", "xvideos", "pornhub", "xnxx",
        "erotic", "nsfw", "sex", "hentai", "strip"
    )

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val rootNode = rootInActiveWindow ?: return
        val packageName = event?.packageName?.toString() ?: ""

        // Anti-Uninstall Protection: Protect Settings from being accessed
        if (packageName == "com.android.settings") {
            if (scanTextForKeywords(rootNode, listOf("FocusShield", "Uninstall", "Device admin"))) {
                performGlobalAction(GLOBAL_ACTION_HOME)
                return
            }
        }

        // Real-Time Screen & URL Scanner
        if (scanTextForKeywords(rootNode, blockedKeywords)) {
            performGlobalAction(GLOBAL_ACTION_HOME)
            triggerBlockOverlay()
        }
    }

    private fun scanTextForKeywords(node: AccessibilityNodeInfo?, keywords: List<String>): Boolean {
        if (node == null) return false

        val text = node.text?.toString()?.lowercase() ?: ""
        val contentDesc = node.contentDescription?.toString()?.lowercase() ?: ""

        for (keyword in keywords) {
            if (text.contains(keyword) || contentDesc.contains(keyword)) {
                return true
            }
        }

        for (i in 0 until node.childCount) {
            if (scanTextForKeywords(node.getChild(i), keywords)) {
                return true
            }
        }
        return false
    }

    private fun triggerBlockOverlay() {
        val intent = Intent(this, BlockActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(intent)
    }

    override fun onInterrupt() {}
}

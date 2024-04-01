package az.gurfdev.todoapp.tools

import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

class InsetsWithKeyboardCallback(window: Window) : androidx.core.view.OnApplyWindowInsetsListener {

    init {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // For better support for devices API 29 and lower
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }
    }

    override fun onApplyWindowInsets(
        v: View,
        windowInsets: WindowInsetsCompat
    ): WindowInsetsCompat {
        val systemBarsInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        val systemBarsImeInsets =
            windowInsets.getInsets(WindowInsetsCompat.Type.systemBars() + WindowInsetsCompat.Type.ime())

        v.setPadding(
            systemBarsInsets.left,
            systemBarsInsets.top,
            systemBarsInsets.right,
            systemBarsImeInsets.bottom
        )

        return WindowInsetsCompat.CONSUMED
    }
}
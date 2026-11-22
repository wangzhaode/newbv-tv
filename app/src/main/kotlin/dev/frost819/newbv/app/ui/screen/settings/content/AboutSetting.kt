package dev.frost819.newbv.app.ui.screen.settings.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import dev.frost819.newbv.BuildConfig
import dev.frost819.newbv.app.ui.screen.settings.SettingsMenuNavItem

/**
 * 关于页。
 *
 * 显示本地构建版本。
 */
@Composable
fun AboutSetting(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = SettingsMenuNavItem.About.displayName,
                style = MaterialTheme.typography.displaySmall,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "当前版本：${BuildConfig.VERSION_NAME}")
        }
        Text(
            modifier = Modifier.align(Alignment.BottomCenter),
            text = "https://github.com/Frost819/newBV",
        )
    }
}

package dev.frost819.newbv.app.ui.screen.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.ListItem
import androidx.tv.material3.Text
import dev.frost819.newbv.app.viewmodel.settings.LogViewerViewModel
import dev.frost819.newbv.core.focus.touchClickable

/**
 * 日志查看页。
 *
 * 在电视上列出已保存的日志文件。
 *
 * @param onBack 返回上一页。
 * @param viewModel 日志查看 ViewModel。
 */
@Composable
fun LogViewerScreen(
    onBack: () -> Unit,
    viewModel: LogViewerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val focusRequester = remember { FocusRequester() }

    BackHandler(onBack = onBack)
    LaunchedEffect(Unit) {
        runCatching { focusRequester.requestFocus() }
    }

    Scaffold(
        topBar = {
            Text(text = "日志管理", modifier = Modifier.padding(horizontal = 48.dp, vertical = 24.dp), fontSize = 24.sp)
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 48.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            item {
                ListItem(
                    modifier =
                        Modifier
                            .focusRequester(focusRequester)
                            .touchClickable(onClick = viewModel::createManualLog),
                    selected = false,
                    onClick = viewModel::createManualLog,
                    headlineContent = { Text(text = "手动保存日志") },
                )
            }
            items(items = uiState.logFiles, key = { it.name }) { file ->
                ListItem(
                    selected = false,
                    onClick = {},
                    headlineContent = { Text(text = file.name) },
                    supportingContent = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(text = viewModel.getLogTypeDisplayName(file))
                            Text(text = "${file.length() / 1024} KB")
                        }
                    },
                )
            }
            if (uiState.logFiles.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "无日志")
                    }
                }
            }
        }
    }
}

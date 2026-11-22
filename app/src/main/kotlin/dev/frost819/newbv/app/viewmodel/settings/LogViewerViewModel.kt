package dev.frost819.newbv.app.viewmodel.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.frost819.newbv.core.log.CrashHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * 日志查看页 ViewModel。
 *
 * 管理电视上的日志文件。
 *
 * @param application 用于获取 filesDir 和 assets。
 * @param crashHandler 崩溃处理器（提供崩溃/手动日志列表）。
 */
@HiltViewModel
class LogViewerViewModel
    @Inject
    constructor(
        application: Application,
        private val crashHandler: CrashHandler,
    ) : AndroidViewModel(application) {
        private val _uiState = MutableStateFlow(LogViewerUiState())
        val uiState: StateFlow<LogViewerUiState> = _uiState.asStateFlow()

        init {
            refreshLogs()
        }

        /**
         * 刷新日志文件列表。
         *
         * 合并崩溃日志和手动日志，按最后修改时间降序排列。
         */
        fun refreshLogs() {
            viewModelScope.launch(Dispatchers.IO) {
                val logs =
                    (
                        crashHandler.listManualLogs() +
                            crashHandler.listCrashLogs()
                    ).sortedByDescending { it.lastModified() }
                _uiState.value = _uiState.value.copy(logFiles = logs)
            }
        }

        /**
         * 创建手动日志（含 logcat 输出）。
         *
         * 创建后刷新列表。
         */
        fun createManualLog() {
            viewModelScope.launch(Dispatchers.IO) {
                crashHandler.createManualLog()
                refreshLogs()
            }
        }

        /**
         * 获取日志文件的类型显示名。
         */
        fun getLogTypeDisplayName(file: File): String =
            when {
                file.name.startsWith(CrashHandler.MANUAL_LOG_PREFIX) -> "手动日志"
                file.name.startsWith(CrashHandler.CRASH_LOG_PREFIX) -> "崩溃日志"
                else -> "未知"
            }
    }

/**
 * 日志查看页 UI 状态。
 *
 * @property logFiles 日志文件列表（按时间降序）。
 */
data class LogViewerUiState(
    val logFiles: List<File> = emptyList(),
)

package dev.frost819.newbv.app.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import dev.frost819.newbv.biliapi.entity.ugc.UgcItem
import dev.frost819.newbv.app.ui.component.FocusSaver
import dev.frost819.newbv.app.ui.component.ListFooterTip
import dev.frost819.newbv.app.ui.component.TvLazyVerticalGrid
import dev.frost819.newbv.app.ui.component.focusSaverItem
import dev.frost819.newbv.app.ui.component.videocard.SmallVideoCard
import dev.frost819.newbv.app.ui.component.videocard.VideoCardData
import dev.frost819.newbv.app.ui.navigation.UserSpaceRoute
import dev.frost819.newbv.app.ui.navigation.navigateFromVideoCard
import dev.frost819.newbv.app.util.formatHourMinSec
import dev.frost819.newbv.app.util.toWanString
import dev.frost819.newbv.app.viewmodel.common.CollectWatchLaterEffects
import dev.frost819.newbv.app.viewmodel.common.WatchLaterViewModel
import dev.frost819.newbv.app.viewmodel.home.HomeViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

/**
 * 推荐视频列表页。
 *
 * 首条内容以精选封面显示，其余内容按窗口宽度排列。
 * 距离底部约 20 条时加载下一页。
 * 支持从详情页返回后恢复焦点到之前点击的卡片。
 */
@Composable
fun RecommendScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    navController: NavController,
    focusSaver: FocusSaver,
) {
    val state by viewModel.uiState.collectAsState()
    val gridState = rememberLazyGridState()
    val watchLaterViewModel: WatchLaterViewModel = hiltViewModel()
    val featured = state.recommendItems.firstOrNull()
    val remainingVideos = remember(state.recommendItems) { state.recommendItems.drop(1) }

    CollectWatchLaterEffects(watchLaterViewModel)

    LaunchedEffect(gridState) {
        snapshotFlow {
            gridState.layoutInfo.visibleItemsInfo
                .lastOrNull()
                ?.index
        }.distinctUntilChanged()
            .filter { index ->
                index != null && index >= state.recommendItems.size - 19
            }.collect {
                viewModel.loadRecommend()
            }
    }

    TvLazyVerticalGrid(
        modifier = modifier,
        state = gridState,
        columns = GridCells.Adaptive(minSize = 180.dp),
        contentPadding = PaddingValues(start = 24.dp, top = 9.dp, end = 24.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        if (featured != null) {
            item(key = "recommend-feature", span = { GridItemSpan(maxLineSpan) }) {
                val video = remember(featured) { featured.toVideoCard() }
                FeaturedVideo(
                    video = video,
                    onClick = { navController.navigateFromVideoCard(video, forceDetail = true) },
                    modifier = Modifier.focusSaverItem(focusSaver, "recommend_feature"),
                )
            }
        }
        if (remainingVideos.isNotEmpty()) {
            item(key = "recommend-grid-title", span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "推荐视频",
                    modifier = Modifier.padding(top = 2.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
        itemsIndexed(
            items = remainingVideos,
            key = { index, _ -> index + 1 },
        ) { index, item ->
            val cardData = remember(item) { item.toVideoCard() }
            SmallVideoCard(
                modifier = Modifier.focusSaverItem(focusSaver, "rcmd_${index + 1}"),
                data = cardData,
                onClick = {
                    navController.navigateFromVideoCard(cardData)
                },
                onGoToDetailPage = {
                    navController.navigateFromVideoCard(cardData, forceDetail = true)
                },
                onGoToUpPage =
                    item.authorMid?.let { mid ->
                        { navController.navigate(UserSpaceRoute(mid = mid, name = item.author)) }
                    },
                onAddWatchLater = { watchLaterViewModel.addToView(aid = item.aid) },
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            ListFooterTip(
                isLoading = state.recommendLoading,
                isError = state.recommendError,
                hasMore = state.recommendHasMore,
                itemsIsEmpty = state.recommendItems.isEmpty(),
            )
        }
    }
}

private fun UgcItem.toVideoCard(): VideoCardData =
    VideoCardData(
        avid = aid,
        bvid = bvid,
        title = title,
        cover = cover,
        playString = play.takeIf { it != -1 }.toWanString(),
        danmakuString = danmaku.takeIf { it != -1 }.toWanString(),
        timeString = (duration * 1000L).formatHourMinSec(),
        upName = author,
        upMid = authorMid,
        pubTime = pubTime,
    )

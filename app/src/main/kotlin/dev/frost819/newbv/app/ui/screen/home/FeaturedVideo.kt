package dev.frost819.newbv.app.ui.screen.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil3.compose.AsyncImage
import dev.frost819.newbv.app.ui.component.videocard.VideoCardData
import dev.frost819.newbv.core.focus.touchClickable

/** 首页推荐中的首个视频。点击整张封面进入详情，遥控器焦点由调用方保存。 */
@Composable
internal fun FeaturedVideo(
    video: VideoCardData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(18.dp)
    val backdrop = Color(0xFF251525)
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(194.dp).touchClickable(onClick = onClick),
        shape = CardDefaults.shape(shape),
        scale = CardDefaults.scale(focusedScale = 1.012f),
        border = CardDefaults.border(
            focusedBorder = Border(BorderStroke(2.dp, MaterialTheme.colorScheme.border), shape = shape),
        ),
    ) {
        Box(Modifier.fillMaxSize().background(backdrop)) {
            AsyncImage(
                model = video.cover,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxWidth(0.68f).fillMaxHeight(),
            )
            Box(
                Modifier.fillMaxSize().background(
                    Brush.horizontalGradient(
                        0f to backdrop,
                        0.32f to backdrop,
                        0.55f to backdrop.copy(alpha = 0.83f),
                        0.83f to backdrop.copy(alpha = 0.12f),
                        1f to backdrop.copy(alpha = 0.16f),
                    ),
                ),
            )
            Column(
                modifier = Modifier.align(Alignment.CenterStart).fillMaxWidth(0.62f).padding(horizontal = 26.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp),
            ) {
                Text("精选推荐", color = Color(0xFFFFB9D4), style = MaterialTheme.typography.labelLarge)
                Text(
                    text = video.title,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = listOf(video.upName, video.timeString).filter(String::isNotBlank).joinToString("  ·  "),
                    color = Color(0xFFEAD9E6),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(
                    modifier = Modifier.background(Color(0xFFFFB9D4), RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color(0xFF42172E),
                        modifier = Modifier.size(16.dp),
                    )
                    Text("查看视频", color = Color(0xFF42172E), style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

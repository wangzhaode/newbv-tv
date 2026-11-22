package dev.frost819.newbv.core.theme

import androidx.compose.ui.graphics.Color

/**
 * new BV 品牌色板。
 *
 * 暖粉色用于主要交互，青绿色保留给辅助状态。
 * 所有颜色以 `Color` 表示，供 [BVTheme] 构建 colorScheme。
 */
object BVColors {
    /** 深色主题中的品牌色，兼顾白色按钮文字的可读性。 */
    val Primary = Color(0xFFAB2E62)

    /** 深色背景下使用的主色变体。 */
    val PrimaryStrong = Color(0xFF982451)

    /** 浅色背景下使用的主色变体。 */
    val PrimaryLight = Color(0xFFFFB9D4)

    /** 低饱和青绿，用于辅助操作和信息状态。 */
    val Secondary = Color(0xFF5B9B94)
    val SecondaryLight = Color(0xFF79B8AE)

    /** 浅色背景下使用的中性绿色变体，作为强调色文字/图标时保证对比度。 */
    val SecondaryStrong = Color(0xFF3F7A73)

    /** 黄色，用于投币/警告。 */
    val Yellow = Color(0xFFC49B5C)

    /** 绿色，用于成功状态。 */
    val Green = Color(0xFF70A684)

    /** 红色，用于错误/直播。 */
    val Red = Color(0xFFC87878)

    /** 深色主题中性色。 */
    val DarkBackground = Color(0xFF141118)
    val DarkSurface = Color(0xFF1D1722)
    val DarkSurfaceVariant = Color(0xFF332638)
    val DarkOnBackground = Color(0xFFF2F2F2)
    val DarkOnSurface = Color(0xFFF2F2F2)
    val DarkOnSurfaceVariant = Color(0xFFB8B8B8)
    val DarkBorder = Color(0xFFFF91BD)

    /** 浅色主题中性色。 */
    val LightBackground = Color(0xFFFFF6FA)
    val LightSurface = Color(0xFFFFFFFF)
    val LightSurfaceVariant = Color(0xFFF4E4EC)
    val LightOnBackground = Color(0xFF172033)
    val LightOnSurface = Color(0xFF172033)
    val LightOnSurfaceVariant = Color(0xFF59657C)
    val LightBorder = PrimaryStrong
}

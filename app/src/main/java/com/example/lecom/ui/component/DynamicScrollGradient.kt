package com.example.lecom.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController

// Data classes for configuration
data class GradientConfig(
    val startGradient: Brush,
    val endGradient: Brush,
    val startStatusBarColor: Color,
    val endStatusBarColor: Color
)

enum class StackPosition {
    GRADIENT_CONTEXT,
    SHORT_INFO,
    CHILD
}

enum class VisibleComponent {
    GRADIENT_CONTEXT,
    SHORT_INFO,
    CHILD
}

/**
 * Main DynamicScrollGradient Composable
 *
 * Usage:
 * DynamicScrollGradient(
 *     gradientContextChild = { Text("Header") },
 *     startGradient = Brush.linearGradient(...),
 *     endGradient = Brush.linearGradient(...),
 *     startStatusBarColor = Color.Blue,
 *     endStatusBarColor = Color.DarkBlue
 * ) {
 *     // Main content
 *     Text("Scrollable content")
 * }
 */
@Composable
fun DynamicScrollGradient(
    gradientContextChild: @Composable () -> Unit,
    startGradientColors: List<Color>,
    endGradientColors: List<Color>,
    startStatusBarColor: Color,
    endStatusBarColor: Color,
    modifier: Modifier = Modifier,
    shortInfoChild: (@Composable () -> Unit)? = null,
    shortInfoStatusBarColor: Color? = null,
    childStatusBarColor: Color? = null,
    scrollThreshold: Float = 150f,
    borderRadius: Dp = 32.dp,
    padding: PaddingValues = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
    decorationImageUrl: String? = null,
    shortInfoBgColor: Color? = null,
    shortInfoBorderColor: Color? = null,
    shortInfoShadowColor: Color? = Color.Transparent,
    gradientContextShadowColor: Color? = Color.Transparent,
    shortInfoBgGradient: Brush? = null,
    enableScrollGradient: Boolean = true,
    updateGradientContextStatusBarColor: Boolean = true,
    statusBarIconsLight: Boolean = true,
    navigationBarColor: Color = Color.White,
    statusBarHeight: Dp = 0.dp,
    canUpdateSystemUI: Boolean = true,
    shortInfoStackChild: (@Composable () -> Unit)? = null,
    shortInfoStackBgChild: (@Composable () -> Unit)? = null,
    stackPosition: StackPosition = StackPosition.GRADIENT_CONTEXT,
    stackChildInitialPosition: Dp = 64.dp,
    enableScrollGradientAfterChild: Boolean = false,
    content: @Composable () -> Unit
) {
    val scrollState = rememberScrollState()
    val systemUiController = rememberSystemUiController()

    // Calculate scroll factor
    val scrollFactor by remember {
        derivedStateOf {
            (scrollState.value / scrollThreshold).coerceIn(0f, 1f)
        }
    }

    // Interpolate gradient colors
    val currentGradientColors by remember {
        derivedStateOf {
            interpolateLinearGradient(
                startGradientColors,
                endGradientColors,
                scrollFactor
            )
        }
    }

    val currentGradient = Brush.linearGradient(
        colors = currentGradientColors,
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )

    // Track component positions
    var gradientContextHeight by remember { mutableStateOf(0) }
    var shortInfoHeight by remember { mutableStateOf(0) }
    var currentTopComponent by remember { mutableStateOf(VisibleComponent.GRADIENT_CONTEXT) }

    // Detect which component is at top
    LaunchedEffect(scrollState.value, gradientContextHeight, shortInfoHeight) {
        currentTopComponent = when {
            scrollState.value < gradientContextHeight -> VisibleComponent.GRADIENT_CONTEXT
            shortInfoChild != null && scrollState.value < (gradientContextHeight + shortInfoHeight) ->
                VisibleComponent.SHORT_INFO
            else -> VisibleComponent.CHILD
        }
    }

    // Update status bar color based on visible component
    val statusBarColor = when (currentTopComponent) {
        VisibleComponent.GRADIENT_CONTEXT -> {
            if (updateGradientContextStatusBarColor) {
                androidx.compose.ui.graphics.lerp(startStatusBarColor, endStatusBarColor, scrollFactor)
            } else {
                Color.Transparent
            }
        }
        VisibleComponent.SHORT_INFO -> shortInfoStatusBarColor ?: startStatusBarColor
        VisibleComponent.CHILD -> childStatusBarColor ?: startStatusBarColor
    }

    // Update system UI
    LaunchedEffect(statusBarColor, canUpdateSystemUI) {
        if (canUpdateSystemUI) {
            systemUiController.setStatusBarColor(
                color = statusBarColor,
                darkIcons = !statusBarIconsLight
            )
            systemUiController.setNavigationBarColor(
                color = navigationBarColor,
                darkIcons = true
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Gradient Context Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    gradientContextHeight = coordinates.size.height
                }
        ) {
            when (stackPosition) {
                StackPosition.GRADIENT_CONTEXT -> {
                    BuildStackedWidget(
                        baseContent = {
                            GradientContextContainer(
                                gradient = currentGradient,
                                borderRadius = borderRadius,
                                padding = padding,
                                decorationImageUrl = decorationImageUrl,
                                shadowColor = gradientContextShadowColor,
                                content = gradientContextChild
                            )
                        },
                        stackChild = shortInfoStackChild,
                        stackBgChild = shortInfoStackBgChild,
                        stackChildInitialPosition = stackChildInitialPosition
                    )
                }
                else -> {
                    GradientContextContainer(
                        gradient = currentGradient,
                        borderRadius = borderRadius,
                        padding = padding,
                        decorationImageUrl = decorationImageUrl,
                        shadowColor = gradientContextShadowColor,
                        content = gradientContextChild
                    )
                }
            }
        }

        // Short Info Section
        if (shortInfoChild != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        shortInfoHeight = coordinates.size.height
                    }
            ) {
                when (stackPosition) {
                    StackPosition.SHORT_INFO -> {
                        BuildStackedWidget(
                            baseContent = {
                                ShortInfoContainer(
                                    bgColor = shortInfoBgColor,
                                    bgGradient = shortInfoBgGradient,
                                    borderColor = shortInfoBorderColor,
                                    shadowColor = shortInfoShadowColor,
                                    borderRadius = borderRadius,
                                    content = shortInfoChild
                                )
                            },
                            stackChild = shortInfoStackChild,
                            stackBgChild = shortInfoStackBgChild,
                            stackChildInitialPosition = stackChildInitialPosition
                        )
                    }
                    else -> {
                        ShortInfoContainer(
                            bgColor = shortInfoBgColor,
                            bgGradient = shortInfoBgGradient,
                            borderColor = shortInfoBorderColor,
                            shadowColor = shortInfoShadowColor,
                            borderRadius = borderRadius,
                            content = shortInfoChild
                        )
                    }
                }
            }
        }

        // Main Content Section
        Box(modifier = Modifier.fillMaxWidth()) {
            when (stackPosition) {
                StackPosition.CHILD -> {
                    BuildStackedWidget(
                        baseContent = content,
                        stackChild = shortInfoStackChild,
                        stackBgChild = shortInfoStackBgChild,
                        stackChildInitialPosition = stackChildInitialPosition
                    )
                }
                else -> {
                    Column {
                        if (shortInfoStackChild != null || shortInfoStackBgChild != null) {
                            Spacer(modifier = Modifier.height(stackChildInitialPosition))
                        }
                        content()
                    }
                }
            }
        }
    }
}

@Composable
private fun GradientContextContainer(
    gradient: Brush,
    borderRadius: Dp,
    padding: PaddingValues,
    decorationImageUrl: String?,
    shadowColor: Color?,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (shadowColor != Color.Transparent) 8.dp else 0.dp,
                shape = RoundedCornerShape(
                    bottomStart = borderRadius,
                    bottomEnd = borderRadius
                )
            )
            .clip(
                RoundedCornerShape(
                    bottomStart = borderRadius,
                    bottomEnd = borderRadius
                )
            )
            .background(gradient)
            .padding(padding)
    ) {
        // Background image if provided
        if (!decorationImageUrl.isNullOrEmpty()) {
            AsyncImage(
                model = decorationImageUrl,
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Content
        content()
    }
}

@Composable
private fun ShortInfoContainer(
    bgColor: Color?,
    bgGradient: Brush?,
    borderColor: Color?,
    shadowColor: Color?,
    borderRadius: Dp,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (shadowColor != Color.Transparent) 8.dp else 0.dp,
                shape = RoundedCornerShape(
                    bottomStart = borderRadius,
                    bottomEnd = borderRadius
                )
            ),
        shape = RoundedCornerShape(
            bottomStart = borderRadius,
            bottomEnd = borderRadius
        ),
        color = bgColor ?: Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (bgGradient != null) {
                        Modifier.background(bgGradient)
                    } else {
                        Modifier
                    }
                )
        ) {
            content()
        }
    }
}

@Composable
private fun BuildStackedWidget(
    baseContent: @Composable () -> Unit,
    stackChild: (@Composable () -> Unit)?,
    stackBgChild: (@Composable () -> Unit)?,
    stackChildInitialPosition: Dp
) {
    var stackChildSize by remember { mutableStateOf(IntSize.Zero) }
    var stackBgChildSize by remember { mutableStateOf(IntSize.Zero) }

    val density = LocalDensity.current
    val stackChildHalfHeight = with(density) {
        if (stackChildSize.height > 0) {
            (stackChildSize.height / 2).toDp()
        } else {
            stackChildInitialPosition
        }
    }

    val stackBgChildHeight = with(density) {
        if (stackBgChildSize.height > 0) {
            stackBgChildSize.height.toDp()
        } else {
            stackChildInitialPosition * 2
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        // Base content
        baseContent()

        // Background stack child
        if (stackBgChild != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .offset(y = stackBgChildHeight)
                    .onGloballyPositioned { coordinates ->
                        stackBgChildSize = coordinates.size
                    }
            ) {
                stackBgChild()
            }
        }

        // Foreground stack child
        if (stackChild != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .offset(y = stackChildHalfHeight)
                    .onGloballyPositioned { coordinates ->
                        stackChildSize = coordinates.size
                    }
            ) {
                stackChild()
            }
        }
    }
}

// Helper function to interpolate between two gradients
private fun interpolateLinearGradient(
    startColors: List<Color>,
    endColors: List<Color>,
    fraction: Float
): List<Color> {
    val size = maxOf(startColors.size, endColors.size)
    return List(size) { index ->
        val startColor = startColors.getOrElse(index) { startColors.last() }
        val endColor = endColors.getOrElse(index) { endColors.last() }
        androidx.compose.ui.graphics.lerp(startColor, endColor, fraction)
    }
}

// Builder for common gradient styles
object DynamicGradientBuilder {
    @Composable
    fun BuildOneCStyle(
        gradientContextChild: @Composable () -> Unit,
        modifier: Modifier = Modifier,
        shortInfoChild: (@Composable () -> Unit)? = null,
        decorationImageUrl: String? = null,
        shortInfoStatusBarColor: Color? = null,
        childStatusBarColor: Color? = null,
        updateGradientContextStatusBarColor: Boolean = true,
        statusBarIconsLight: Boolean = true,
        navigationBarColor: Color = Color.White,
        shortInfoStackChild: (@Composable () -> Unit)? = null,
        content: @Composable () -> Unit
    ) {
        DynamicScrollGradient(
            modifier = modifier,
            gradientContextChild = gradientContextChild,
            startGradientColors = listOf(Color(0xFF6366F1), Color(0xFF4F46E5)),
            endGradientColors = listOf(Color(0xFF3730A3), Color(0xFF1E1B4B)),
            startStatusBarColor = Color(0xFF6366F1),
            endStatusBarColor = Color(0xFF1E1B4B),
            shortInfoChild = shortInfoChild,
            shortInfoStatusBarColor = shortInfoStatusBarColor,
            childStatusBarColor = childStatusBarColor,
            updateGradientContextStatusBarColor = updateGradientContextStatusBarColor,
            statusBarIconsLight = statusBarIconsLight,
            navigationBarColor = navigationBarColor,
            decorationImageUrl = decorationImageUrl,
            shortInfoStackChild = shortInfoStackChild,
            borderRadius = 32.dp,
            content = content
        )
    }
}

// Usage Example
@Composable
fun ExampleUsage() {
    DynamicGradientBuilder.BuildOneCStyle(
        gradientContextChild = {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Dynamic Gradient Header",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
                Text(
                    text = "Scroll to see the gradient change",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        },
        shortInfoChild = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Short Info Section")
            }
        }
    ) {
        // Main scrollable content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            repeat(20) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Item $index",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
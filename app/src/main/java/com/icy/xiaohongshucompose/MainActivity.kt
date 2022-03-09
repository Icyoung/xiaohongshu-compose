package com.icy.xiaohongshucompose

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.Indicator
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.sharp.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.icy.xiaohongshucompose.ui.theme.XiaohongshuComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XiaohongshuComposeTheme {
                var selectedIndex: Int by remember { mutableStateOf(0) }
                Scaffold(bottomBar = {
                    BottomBar(selectedIndex) {
                        selectedIndex = it
                    }
                }) {

                }
            }
        }
    }
}

@Composable
fun BottomBar(selectedIndex: Int, onSelectedChanged: (Int) -> Unit) {
    BottomAppBar(
        modifier = Modifier.height(44.dp),
        backgroundColor = MaterialTheme.colors.background
    ) {
        BottomBarItem("首页", selectedIndex == 0, 0, Modifier.weight(1f).clickable {
            onSelectedChanged(0)
        })
        BottomBarItem("购物", selectedIndex == 1, 0, Modifier.weight(1f).clickable {
            onSelectedChanged(1)
        })
        Box(modifier = Modifier.padding(horizontal = 30.dp)) {
            IconButton(
                onClick = {},
                modifier = Modifier.background(
                    MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(4.dp)
                ).size(40.dp, 25.dp),
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Publish your content",
                    tint = MaterialTheme.colors.background
                )

            }
        }
        BottomBarItem("消息", selectedIndex == 2, 99, Modifier.weight(1f).clickable {
            onSelectedChanged(2)
        })
        BottomBarItem("我", selectedIndex == 3, 0, Modifier.weight(1f).clickable {
            onSelectedChanged(3)
        })
    }
}

@Composable
fun BottomBarItem(title: String, isSelected: Boolean, unRead: Int, modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            title,
            modifier = Modifier.padding(4.dp).addBadge(unRead.toString(), unRead != 0),
            style = selectTextStyle(isSelected)
        )
    }
}

@Composable
fun selectTextStyle(isSelected: Boolean): TextStyle = if (isSelected) {
    MaterialTheme.typography.body1.copy(
        color = MaterialTheme.typography.button.color.copy(alpha = 0.8f),
        fontWeight = FontWeight.Bold
    )
} else {
    MaterialTheme.typography.body2.copy(
        color = MaterialTheme.typography.caption.color,
        fontWeight = FontWeight.SemiBold
    )
}

fun Modifier.addBadge(data: String, show: Boolean = true): Modifier =
    this.drawWithContent {
        drawContent()
        if (!show) {
            return@drawWithContent
        }
        val badgeOffset = Offset(x = size.width - 5.dp.toPx(), y = -6.dp.toPx());
        val badgeSize = Size(height = 12.dp.toPx(), width = 20.dp.toPx());
        drawRoundRect(
            color = Color.Red,
            topLeft = badgeOffset,
            size = badgeSize,
            cornerRadius = CornerRadius(x = 6.dp.toPx())
        )

        val paint = Paint().apply {
            textAlign = Paint.Align.CENTER
            textSize = 10.sp.toPx()
            color = 0xFFFFFFFF.toInt()
        }

        drawContext.canvas.nativeCanvas.drawText(
            data,
            badgeSize.center.x + badgeOffset.x, badgeSize.height * 3 / 4 + badgeOffset.y, paint
        )
    }

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    XiaohongshuComposeTheme {
        var selectedIndex: Int by remember { mutableStateOf(0) }
        Scaffold(
            topBar = {
                TopAppBar(backgroundColor = MaterialTheme.colors.background) {
                    Image(
                        painterResource(R.mipmap.ic_main_lead),
                        "Mood",
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)
                    )
                    Box(
                        modifier = Modifier.weight(1f).padding(horizontal = 50.dp, vertical = 10.dp)
                            .align(alignment = Alignment.Bottom)
                    ) {
                        var tabIndex by remember { mutableStateOf(0) }
                        TabRow(selectedTabIndex = tabIndex,
                            modifier = Modifier.align(alignment = Alignment.Center),
                            backgroundColor = MaterialTheme.colors.background,
                            divider = {
                                Divider(color = Color.Transparent)
                            },
                            indicator = { tab ->
                                Indicator(
                                    Modifier.tabIndicatorOffset(tab[tabIndex])
                                        .padding(horizontal = 10.dp)
                                        .clip(
                                            RoundedCornerShape(1.dp)
                                        ),
                                    color = MaterialTheme.colors.primary
                                )
                            }
                        ) {
                            listOf("关注", "发现", "深圳").forEachIndexed { index, s ->
                                Tab(selected = tabIndex == index, onClick = {
                                    tabIndex = index;
                                }) {
                                    Text(
                                        s,
                                        style = selectTextStyle(tabIndex == index).copy(fontSize = 16.sp),
                                        modifier = Modifier.padding(bottom = 5.dp)
                                    )
                                }
                            }
                        }
                    }
                    Image(
                        painterResource(R.mipmap.ic_search),
                        "Search",
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 15.dp)
                    )
                }
            },
            bottomBar = {
                BottomBar(selectedIndex) {
                    selectedIndex = it
                }
            }) {

        }
    }
}
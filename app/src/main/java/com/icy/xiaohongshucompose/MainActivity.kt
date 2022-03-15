package com.icy.xiaohongshucompose

import android.graphics.Paint
import android.os.Bundle
import android.util.Log.d
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.Indicator
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.icy.xiaohongshucompose.ui.theme.XiaohongshuComposeTheme
import com.icy.xiaohongshucompose.ui.widget.StaggeredVerticalGrid
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XiaohongshuComposeTheme {
                var topTabIndex by remember { mutableStateOf(0) }
                var toolTabIndex by remember { mutableStateOf(0) }
                var bottomTabIndex: Int by remember { mutableStateOf(0) }
                Scaffold(
                    topBar = {
                        TopAppBar(topTabIndex) {
                            topTabIndex = it
                        }
                    },
                    bottomBar = {
                        BottomBar(bottomTabIndex) {
                            bottomTabIndex = it
                        }
                    }) {

                    Content(toolTabIndex) {
                        toolTabIndex = it
                    }
                }
            }
        }
    }
}

@Composable
fun TopAppBar(selectedIndex: Int, onSelectedChanged: (index: Int) -> Unit) {
    Column {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.background,
            elevation = 0.dp
        ) {
            Image(
                painterResource(R.mipmap.ic_main_lead),
                "Mood",
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 17.dp)
            )
            Box(
                modifier = Modifier.weight(1f).padding(horizontal = 50.dp, vertical = 10.dp)
                    .align(alignment = Alignment.Bottom)
            ) {
                TabRow(selectedTabIndex = selectedIndex,
                    modifier = Modifier.align(alignment = Alignment.Center),
                    backgroundColor = MaterialTheme.colors.background,
                    divider = {
                        Divider(color = Color.Transparent)
                    },
                    indicator = { tab ->
                        Indicator(
                            Modifier.tabIndicatorOffset(tab[selectedIndex])
                                .padding(horizontal = 10.dp)
                                .clip(
                                    RoundedCornerShape(1.dp)
                                ),
                            color = MaterialTheme.colors.primary
                        )
                    }
                ) {
                    listOf("关注", "发现", "深圳").forEachIndexed { index, s ->
                        Tab(selected = selectedIndex == index, onClick = {
                            onSelectedChanged(index)
                        }) {
                            Text(
                                s,
                                style = selectTextStyle(selectedIndex == index).copy(fontSize = 16.sp),
                                modifier = Modifier.padding(bottom = 5.dp)
                            )
                        }
                    }
                }
            }
            Image(
                painterResource(R.mipmap.ic_search),
                "Search",
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp)
            )
        }
        Divider(thickness = 0.5.dp)
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
        color = MaterialTheme.typography.overline.color,
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun discoverContent() {
    val pics = listOf(
        "https://ci.xiaohongshu.com/bw1/350af518-85c1-42f6-b3a1-04213f311896?imageMogr2/format/jpg/quality/92/auto-orient/strip/crop/450x600/gravity/center",
        "https://ci.xiaohongshu.com/bw1/2d68329c-4c8a-4112-8a7d-9038af7598d5?imageMogr2/format/jpg/quality/92/auto-orient/strip/crop/450x600/gravity/center",
        "https://ci.xiaohongshu.com/bw1/350af518-85c1-42f6-b3a1-04213f311896?imageMogr2/format/jpg/quality/92/auto-orient/strip/crop/450x600/gravity/center",
        "https://ci.xiaohongshu.com/bw1/2d68329c-4c8a-4112-8a7d-9038af7598d5?imageMogr2/format/jpg/quality/92/auto-orient/strip/crop/450x600/gravity/center",
        "https://ci.xiaohongshu.com/bw1/350af518-85c1-42f6-b3a1-04213f311896?imageMogr2/format/jpg/quality/92/auto-orient/strip/crop/450x600/gravity/center",
        "https://ci.xiaohongshu.com/bw1/2d68329c-4c8a-4112-8a7d-9038af7598d5?imageMogr2/format/jpg/quality/92/auto-orient/strip/crop/450x600/gravity/center",
        "https://ci.xiaohongshu.com/bw1/350af518-85c1-42f6-b3a1-04213f311896?imageMogr2/format/jpg/quality/92/auto-orient/strip/crop/450x600/gravity/center",
        "https://ci.xiaohongshu.com/bw1/2d68329c-4c8a-4112-8a7d-9038af7598d5?imageMogr2/format/jpg/quality/92/auto-orient/strip/crop/450x600/gravity/center"
    )
    val descs = listOf(
        "气泡酒，还是草本茶， Whatever 好喝好喝🍺",
        "减脂期也能吃的甜甜圈🍵",
        "气泡酒，还是草本茶， Whatever 好喝好喝🍺",
        "减脂期也能吃的甜甜圈🍵",
        "气泡酒，还是草本茶， Whatever 好喝好喝🍺",
        "减脂期也能吃的甜甜圈🍵",
        "气泡酒，还是草本茶， Whatever 好喝好喝🍺",
        "减脂期也能吃的甜甜圈🍵",
    )
    val avatars = listOf(
        "https://sns-avatar-qc.xhscdn.com/avatar/620376f3fc01b139c3bd0d0c.jpg?imageView2/1/w/540/format/jpg",
        "https://sns-avatar-qc.xhscdn.com/avatar/620376f3fc01b139c3bd0d0c.jpg?imageView2/1/w/540/format/jpg",
        "https://sns-avatar-qc.xhscdn.com/avatar/620376f3fc01b139c3bd0d0c.jpg?imageView2/1/w/540/format/jpg",
        "https://sns-avatar-qc.xhscdn.com/avatar/620376f3fc01b139c3bd0d0c.jpg?imageView2/1/w/540/format/jpg",
        "https://sns-avatar-qc.xhscdn.com/avatar/620376f3fc01b139c3bd0d0c.jpg?imageView2/1/w/540/format/jpg",
        "https://sns-avatar-qc.xhscdn.com/avatar/620376f3fc01b139c3bd0d0c.jpg?imageView2/1/w/540/format/jpg",
        "https://sns-avatar-qc.xhscdn.com/avatar/620376f3fc01b139c3bd0d0c.jpg?imageView2/1/w/540/format/jpg",
        "https://sns-avatar-qc.xhscdn.com/avatar/620376f3fc01b139c3bd0d0c.jpg?imageView2/1/w/540/format/jpg",
    )
    val names = listOf("好喝鬼", "好吃鬼", "好喝鬼", "好吃鬼", "好喝鬼", "好吃鬼", "好喝鬼", "好吃鬼")
    pics.mapIndexed { i, _ -> discoverContentItem(pics[i], descs[i], avatars[i], names[i], 99) }
}

@Composable
fun discoverContentItem(pic: String, desc: String, avatar: String, name: String, like: Int) {
    Column(modifier = Modifier.padding(2.dp, 5.dp)) {
        AsyncImage(
            model = pic,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.clip(RoundedCornerShape(5.dp)).fillMaxWidth()
        )
        Text(text = desc)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                AsyncImage(
                    model = avatar, contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clip(CircleShape).size(30.dp)
                )
                Text(name)
            }
            Icon(
                Icons.Default.Star, "like",
                modifier = Modifier.background(Color.White).width(20.dp),
                tint = Color(0x85000000)
            )
            Text("$like")

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Content(selectedIndex: Int, onSelectedChanged: (index: Int) -> Unit) {
    val toolbarHeight = 48.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                toolbarOffsetHeightPx.value =
                    (toolbarOffsetHeightPx.value + available.y).coerceIn(-toolbarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            StaggeredVerticalGrid(
                modifier = Modifier.padding(top = toolbarHeight, start = 2.dp, end = 2.dp),
                maxColumnWidth = 300.dp
            ) {
                discoverContent()
            }
        }
        Box(
            modifier = Modifier
                .height(toolbarHeight)
                .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
        ) {
            CustomScrollableTabRow(
                selectedTabIndex = selectedIndex,
                edgePadding = 10.dp,
                backgroundColor = MaterialTheme.colors.background,
                indicator = {},
                divider = {
                    Divider(color = Color.Transparent)
                },
            ) {
                listOf(
                    "推荐",
                    "视频",
                    "直播",
                    "摄影",
                    "旅行",
                    "彩妆",
                    "情感",
                    "旅行",
                    "科技",
                    "校园",
                    "心理",
                    "潮鞋"
                ).forEachIndexed { index, s ->
                    Tab(
                        selected = selectedIndex == index, onClick = {
                            onSelectedChanged(index)
                        },
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text(
                            s,
                            style = selectTextStyle(selectedIndex == index).copy(fontSize = 15.sp),
                        )
                    }
                }
            }
            Row(modifier = Modifier.align(Alignment.TopEnd).padding(vertical = 10.dp)) {
                Box(
                    modifier = Modifier.width(30.dp).fillMaxHeight().background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.White.copy(alpha = 0f),
                                Color.White
                            )
                        )
                    )
                )
                Icon(
                    Icons.Default.KeyboardArrowDown, "More type for content",
                    modifier = Modifier.background(Color.White).width(55.dp),
                    tint = Color(0x85000000)
                )
            }
        }


    }
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
        var topTabIndex by remember { mutableStateOf(0) }
        var toolTabIndex by remember { mutableStateOf(0) }
        var bottomTabIndex: Int by remember { mutableStateOf(0) }
        Scaffold(
            topBar = {
                TopAppBar(topTabIndex) {
                    topTabIndex = it
                }
            },
            bottomBar = {
                BottomBar(bottomTabIndex) {
                    bottomTabIndex = it
                }
            }) {
            Content(toolTabIndex) {
                toolTabIndex = it
            }
        }
    }
}
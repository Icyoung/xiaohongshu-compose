package com.icy.xiaohongshucompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        BottomBarItem("首页", selectedIndex == 0, Modifier.weight(1f).clickable {
            onSelectedChanged(0)
        })
        BottomBarItem("购物", selectedIndex == 1, Modifier.weight(1f).clickable {
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
        BottomBarItem("消息", selectedIndex == 2, Modifier.weight(1f).clickable {
            onSelectedChanged(2)
        })
        BottomBarItem("我", selectedIndex == 3, Modifier.weight(1f).clickable {
            onSelectedChanged(3)
        })
    }
}

@Composable
fun BottomBarItem(title: String, isSelected: Boolean, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            title,
            style = if (isSelected) {
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
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
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
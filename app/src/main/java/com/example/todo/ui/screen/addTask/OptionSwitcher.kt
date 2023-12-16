package com.example.todo.ui.screen.addTask

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OptionSwitcher(
    modifier: Modifier = Modifier,
    options: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit
) {
    var tabWidth by remember { mutableStateOf(0.dp) }
    val cornerRadius = 20.dp
    val density = LocalDensity.current
    val indicatorOffset by animateDpAsState(
        targetValue = tabWidth * selectedIndex,
        label = "Switcher tab animation"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(cornerRadius))
            .onGloballyPositioned {
                val width = it.size.width / options.size
                tabWidth = with(density) { width.toDp() }
            }
            .height(IntrinsicSize.Min)
    ) {

        Box(
            modifier = Modifier
                .offset(x = indicatorOffset)
                .clip(RoundedCornerShape(cornerRadius))
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxHeight()
                .width(tabWidth)
        )

        Row {
            options.forEachIndexed { index, option ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(cornerRadius))
                        .clickable { onSelected(index) }
                        .weight(1F)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (index == selectedIndex) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun OptionSwitcherPreview1() {
    OptionSwitcher(
        modifier = Modifier.background(Color.White).padding(8.dp),
        options = listOf("Option 1", "Option 2", "Option3"),
        selectedIndex = 0, onSelected = {}
    )
}
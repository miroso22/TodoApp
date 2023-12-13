package com.example.todo.widget.popup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo.ui.screen.main.MainViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun WidgetPopup(viewModel: MainViewModel = koinViewModel()) {
    WidgetPopupContent(onCreateTask = viewModel::addTask)
}

@Composable
private fun WidgetPopupContent(onCreateTask: (String) -> Unit) = Row(
    modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .padding(8.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    var description by remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier.weight(1F),
        value = description,
        onValueChange = { description = it }
    )
    IconButton(onClick = { onCreateTask(description); description = "" }) {
        Image(
            painter = painterResource(id = android.R.drawable.ic_media_play),
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun WidgetPopupContentPreview() {
    WidgetPopupContent(onCreateTask = {})
}
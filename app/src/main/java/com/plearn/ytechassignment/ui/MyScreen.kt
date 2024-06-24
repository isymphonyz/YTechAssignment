package com.plearn.ytechassignment.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plearn.ytechassignment.viewnodel.GridViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppScreen(gridViewModel: GridViewModel) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scrollStateVertical = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    var isCreateGridButtonEnable by remember { mutableStateOf(false) }
    var rows by remember { mutableStateOf("") }
    var columns by remember { mutableStateOf("") }

    isCreateGridButtonEnable = rows.isNotEmpty() && columns.isNotEmpty()

    Scaffold(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)
            .imePadding(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollStateVertical),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                fontSize = 22.sp,
                text = "Simple Grid"
            )
            Text(
                fontSize = 12.sp,
                text = "Size of grid depends on the performance of the device."
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                TextField(
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f),
                    value = rows,
                    onValueChange = { rows = it },
                    label = { Text("Rows") },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword
                    ),
                    visualTransformation = VisualTransformation.None
                )
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f),
                    value = columns,
                    onValueChange = { columns = it },
                    label = { Text("Columns") },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword
                    ),
                    visualTransformation = VisualTransformation.None
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier
                    .alpha( if (isCreateGridButtonEnable) 0.5f else 1.0f),
                enabled = isCreateGridButtonEnable,
                onClick = {
                    keyboardController?.hide()
                    gridViewModel.initializeGrid(rows.toInt(), columns.toInt())
                }
            ) {
                Text("Create Grid")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Grid(gridViewModel, snackBarHostState)

            Spacer(modifier = Modifier.height(16.dp))

            val shortestPathResult by gridViewModel.shortestPathResult.collectAsState()
            Text(text = shortestPathResult)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier
                    .alpha( if (isCreateGridButtonEnable) 0.5f else 1.0f),
                enabled = isCreateGridButtonEnable,
                onClick = {
                    keyboardController?.hide()
                    gridViewModel.findShortestPath()
                }
            ) {
                Text("Find The Shortest Path")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }


}

@Composable
fun Grid(
    viewModel: GridViewModel,
    snackBarHostState: SnackbarHostState
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollStateHorizontal = rememberScrollState()

    val grid by viewModel.grid.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .horizontalScroll(scrollStateHorizontal),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        grid.forEachIndexed { rowIndex, row ->
            Row {
                row.forEachIndexed { colIndex, cell ->
                    TextField(
                        value = cell.toString(),
                        onValueChange = {
                            val newValue = it.replace(cell.toString(), "")
                            when {
                                newValue.length == 1 && (newValue == "0" || newValue == "1") -> {
                                    viewModel.updateCell(rowIndex, colIndex, newValue.toInt())
                                }
                                newValue.isNotEmpty() -> {
                                    coroutineScope.launch {
                                        snackBarHostState.showSnackbar("Please enter a value of 0 or 1")
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(4.dp)
                            .width(40.dp),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.NumberPassword
                        ),
                        visualTransformation = VisualTransformation.None
                    )
                }
            }
        }
    }
}
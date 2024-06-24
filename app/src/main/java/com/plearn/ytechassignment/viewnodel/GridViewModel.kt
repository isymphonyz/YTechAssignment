package com.plearn.ytechassignment.viewnodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GridViewModel : ViewModel() {

    private val _grid = MutableStateFlow<List<List<Int>>>(emptyList())
    val grid: StateFlow<List<List<Int>>> get() = _grid

    private val _shortestPathResult = MutableStateFlow("")
    val shortestPathResult: StateFlow<String> get() = _shortestPathResult

    fun initializeGrid(rows: Int, columns: Int) {
        _grid.value = List(rows) { List(columns) { 0 } }
    }

    fun updateCell(row: Int, col: Int, value: Int) {
        _grid.value = _grid.value.toMutableList().apply {
            this[row] = this[row].toMutableList().apply {
                this[col] = value
            }
        }
    }

    fun findShortestPath() {
        val grid = _grid.value
        val rows = grid.size
        val cols = if (rows > 0) grid[0].size else 0

        if (rows == 0 || cols == 0 || grid[0][0] == 0 || grid[rows - 1][cols - 1] == 0) {
            _shortestPathResult.value = "No path found"
            return
        }

        val directions = listOf(
            Point(0, 1),  // right
            Point(0, -1), // left
            Point(1, 0),  // down
            Point(-1, 0)  // up
        )

        val queue: MutableList<Pair<Point, Int>> = mutableListOf(Pair(Point(0, 0), 0))
        val visited = Array(rows) { BooleanArray(cols) }
        visited[0][0] = true

        while (queue.isNotEmpty()) {
            val (current, steps) = queue.removeAt(0)

            if (current.x == rows - 1 && current.y == cols - 1) {
                _shortestPathResult.value = "The shortest path is $steps steps"
                return
            }

            for (direction in directions) {
                val newX = current.x + direction.x
                val newY = current.y + direction.y

                if (newX in 0 until rows && newY in 0 until cols && grid[newX][newY] == 1 && !visited[newX][newY]) {
                    queue.add(Pair(Point(newX, newY), steps + 1))
                    visited[newX][newY] = true
                }
            }
        }

        _shortestPathResult.value = "No path found"
    }
}

data class Point(val x: Int, val y: Int)
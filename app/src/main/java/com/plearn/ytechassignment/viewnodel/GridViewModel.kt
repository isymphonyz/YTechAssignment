package com.plearn.ytechassignment.viewnodel

import androidx.lifecycle.ViewModel
import com.plearn.ytechassignment.model.Point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GridViewModel : ViewModel() {

    private val _grid = MutableStateFlow<List<List<Int>>>(emptyList())
    val grid: StateFlow<List<List<Int>>> get() = _grid

    private val _shortestPathResult = MutableStateFlow("")
    val shortestPathResult: StateFlow<String> get() = _shortestPathResult

    /**
     * This function initializes the grid with the specified number of rows and columns.
     * Each cell in the grid is set to 0 by default
     * @param rows a number of rows in grid.
     * @param columns a number of columns in grid.
     */
    fun initializeGrid(rows: Int, columns: Int) {
        _grid.value = List(rows) { List(columns) { 0 } }
    }

    /**
     * This function updates the value of a specific cell in the grid.
     * @param row a specific row.
     * @param col a specific column.
     * @param value a new value of specific cell.
     */
    fun updateCell(row: Int, col: Int, value: Int) {
        _grid.value = _grid.value.toMutableList().apply {
            this[row] = this[row].toMutableList().apply {
                this[col] = value
            }
        }
    }

    /**
     * This function calculates the shortest path from the top-left corner
     * to the bottom-right corner of the grid.
     */
    fun findShortestPath() {
        val grid = _grid.value
        val rows = grid.size
        val cols = if (rows > 0) grid[0].size else 0

        // If any of these conditions are true, it sets the shortest path result to "No path found"
        if (rows == 0 || cols == 0 || grid[0][0] == 0 || grid[rows - 1][cols - 1] == 0) {
            _shortestPathResult.value = "No path found"
            return
        }

        // Movement Directions
        val directions = listOf(
            Point(0, 1),  // right
            Point(0, -1), // left
            Point(1, 0),  // down
            Point(-1, 0)  // up
        )

        // BFS. It starts with the top-left corner (0, 0) with a step count of 0.
        val queue: MutableList<Pair<Point, Int>> = mutableListOf(Pair(Point(0, 0), 0))
        val visited = Array(rows) { BooleanArray(cols) }
        visited[0][0] = true

        while (queue.isNotEmpty()) {
            val (current, steps) = queue.removeAt(0)

            // If the current cell is the bottom-right corner,
            // sets the shortest path result to the number of steps taken.
            if (current.x == rows - 1 && current.y == cols - 1) {
                _shortestPathResult.value = "The shortest path is $steps steps"
                return
            }

            for (direction in directions) {
                val newX = current.x + direction.x
                val newY = current.y + direction.y

                // Checks if the new position is within bounds, is not an obstacle and has not been visited.
                // If valid, adds the new position to the queue with an incremented step count.
                if (
                    newX in 0 until rows &&
                    newY in 0 until cols &&
                    grid[newX][newY] == 1 &&
                    !visited[newX][newY]
                ) {
                    queue.add(Pair(Point(newX, newY), steps + 1))
                }
            }
        }

        _shortestPathResult.value = "No path found"
    }
}
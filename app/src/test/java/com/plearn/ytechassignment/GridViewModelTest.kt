package com.plearn.ytechassignment

import com.plearn.ytechassignment.viewnodel.GridViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class GridViewModelTest {

    private lateinit var viewModel: GridViewModel

    @Before
    fun setup() {
        viewModel = GridViewModel()
    }

    @Test
    fun testInitializeGrid() = runTest {
        viewModel.initializeGrid(3, 3)
        val grid = viewModel.grid.first()

        assertEquals(3, grid.size)
        assertEquals(3, grid[0].size)
        assertEquals(0, grid[0][0])
        assertEquals(0, grid[1][1])
        assertEquals(0, grid[2][2])
    }

    @Test
    fun testUpdateCell() = runTest {
        viewModel.initializeGrid(3, 3)
        viewModel.updateCell(1, 1, 1)
        val grid = viewModel.grid.first()

        assertEquals(1, grid[1][1])
    }

    @Test
    fun testFindShortestPath_PathExists() = runTest {
        viewModel.initializeGrid(3, 3)
        viewModel.updateCell(0, 0, 1)
        viewModel.updateCell(0, 1, 1)
        viewModel.updateCell(0, 2, 1)
        viewModel.updateCell(1, 2, 1)
        viewModel.updateCell(2, 2, 1)

        viewModel.findShortestPath()
        val result = viewModel.shortestPathResult.first()

        assertEquals("The shortest path is 4 steps", result)
    }

    @Test
    fun testFindShortestPath_NoPath() = runTest {
        viewModel.initializeGrid(3, 3)
        viewModel.updateCell(0, 0, 1)
        viewModel.updateCell(0, 1, 0)
        viewModel.updateCell(0, 2, 1)
        viewModel.updateCell(1, 2, 0)
        viewModel.updateCell(2, 2, 1)

        viewModel.findShortestPath()
        val result = viewModel.shortestPathResult.first()

        assertEquals("No path found", result)
    }

    @Test
    fun testFindShortestPath_WithMultiPath() = runTest {
        viewModel.initializeGrid(7, 5)
        viewModel.updateCell(0, 0, 1)
        viewModel.updateCell(0, 1, 1)
        viewModel.updateCell(0, 2, 1)
        viewModel.updateCell(0, 3, 1)
        viewModel.updateCell(0, 4, 1)
        viewModel.updateCell(1, 1, 1)
        viewModel.updateCell(1, 2, 1)
        viewModel.updateCell(1, 3, 1)
        viewModel.updateCell(1, 4, 1)
        viewModel.updateCell(2, 3, 1)
        viewModel.updateCell(2, 4, 1)
        viewModel.updateCell(3, 1, 1)
        viewModel.updateCell(3, 2, 1)
        viewModel.updateCell(3, 3, 1)
        viewModel.updateCell(3, 4, 1)
        viewModel.updateCell(4, 1, 1)
        viewModel.updateCell(4, 4, 1)
        viewModel.updateCell(5, 1, 1)
        viewModel.updateCell(5, 4, 1)
        viewModel.updateCell(6, 1, 1)
        viewModel.updateCell(6, 2, 1)
        viewModel.updateCell(6, 3, 1)
        viewModel.updateCell(6, 4, 1)

        viewModel.findShortestPath()
        val result = viewModel.shortestPathResult.first()

        assertEquals("The shortest path is 10 steps", result)
    }
}
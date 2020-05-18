/*
 * MIT License
 *
 * Copyright (c) 2020 Andrew Krepps
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.askrepps.nonogram

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import org.junit.Test

/**
 * Tests for [PuzzleState] and [MutablePuzzleState].
 */
class PuzzleStateTest {
    @Test
    fun testStateDimensions() {
        val state = PuzzleState(3, 4)
        assertThat(state.rows, equalTo(3))
        assertThat(state.columns, equalTo(4))
    }

    @Test
    fun testInvalidDimensionsThrowsException() {
        assertThat({ PuzzleState(0, 1) }, throws<IllegalArgumentException>())
        assertThat({ PuzzleState(1, 0) }, throws<IllegalArgumentException>())
        assertThat({ PuzzleState(0, 0) }, throws<IllegalArgumentException>())
        assertThat({ PuzzleState(-1, 1) }, throws<IllegalArgumentException>())
        assertThat({ PuzzleState(1, -1) }, throws<IllegalArgumentException>())
        assertThat({ PuzzleState(-1, -1) }, throws<IllegalArgumentException>())
    }

    @Test
    fun testStateNumberOfCells() {
        testCellGridDimensions(1, 1)
        testCellGridDimensions(1, 3)
        testCellGridDimensions(3, 1)
        testCellGridDimensions(2, 3)
        testCellGridDimensions(3, 2)
        testCellGridDimensions(3, 3)
    }

    private fun testCellGridDimensions(rows: Int, columns: Int) {
        val state = PuzzleState(rows, columns)
        val grid = state.cellGrid
        assertThat(grid.size, equalTo(rows))
        for (row in grid) {
            assertThat(row.size, equalTo(columns))
        }
    }

    @Test
    fun testCellsInitializedToOpen() {
        val state = PuzzleState(10, 15)
        val grid = state.cellGrid
        for (row in 0 until 10) {
            for (col in 0 until 15) {
                assertThat(grid[row][col], equalTo(CellContents.OPEN))
                assertThat(state.getCell(row, col), equalTo(CellContents.OPEN))
            }
        }
    }

    @Test
    fun testInvalidGetCellCoordinatesThrowsException() {
        val state = PuzzleState(10, 15)
        assertThat({ state.getCell(-1, 0) }, throws<IllegalArgumentException>())
        assertThat({ state.getCell(0, -1) }, throws<IllegalArgumentException>())
        assertThat({ state.getCell(-1, -1) }, throws<IllegalArgumentException>())
        assertThat({ state.getCell(10, 0) }, throws<IllegalArgumentException>())
        assertThat({ state.getCell(0, 15) }, throws<IllegalArgumentException>())
        assertThat({ state.getCell(10, 15) }, throws<IllegalArgumentException>())
    }

    @Test
    fun testMarkCell() {
        val state = MutablePuzzleState(10, 15)
        state.markCell(1, 2, CellContents.FILLED)
        state.markCell(3, 4, CellContents.X)

        val grid = state.cellGrid
        for (row in 0 until 10) {
            for (col in 0 until 15) {
                val expectedContents = when {
                    row == 1 && col == 2 -> CellContents.FILLED
                    row == 3 && col == 4 -> CellContents.X
                    else -> CellContents.OPEN
                }
                assertThat(grid[row][col], equalTo(expectedContents))
                assertThat(state.getCell(row, col), equalTo(expectedContents))
            }
        }
    }

    @Test
    fun testInvalidMarkCellCoordinatesThrowsException() {
        val state = MutablePuzzleState(10, 15)
        assertThat({ state.markCell(-1, 0, CellContents.OPEN) }, throws<IllegalArgumentException>())
        assertThat({ state.markCell(0, -1, CellContents.OPEN) }, throws<IllegalArgumentException>())
        assertThat({ state.markCell(-1, -1, CellContents.OPEN) }, throws<IllegalArgumentException>())
        assertThat({ state.markCell(10, 0, CellContents.OPEN) }, throws<IllegalArgumentException>())
        assertThat({ state.markCell(0, 15, CellContents.OPEN) }, throws<IllegalArgumentException>())
        assertThat({ state.markCell(10, 15, CellContents.OPEN) }, throws<IllegalArgumentException>())
    }

    @Test
    fun testSingleRowDimension() {
        val state = PuzzleState(10, 15)
        assertThat(state.getRow(1).size, equalTo(15))
        assertThat(state.getRow(7).size, equalTo(15))
    }

    @Test
    fun testGetRow() {
        val state = MutablePuzzleState(2, 3)
        state.markCell(0, 0, CellContents.FILLED)
        state.markCell(0, 1, CellContents.X)
        state.markCell(1, 1, CellContents.X)
        state.markCell(1, 2, CellContents.FILLED)

        assertThat(state.getRow(0), equalTo(listOf(CellContents.FILLED, CellContents.X, CellContents.OPEN)))
        assertThat(state.getRow(1), equalTo(listOf(CellContents.OPEN, CellContents.X, CellContents.FILLED)))
    }

    @Test
    fun testInvalidRowIndexThrowsException() {
        val state = PuzzleState(3, 4)
        assertThat({ state.getRow(-1) }, throws<IllegalArgumentException>())
        assertThat({ state.getRow(-42) }, throws<IllegalArgumentException>())
        assertThat({ state.getRow(3) }, throws<IllegalArgumentException>())
        assertThat({ state.getRow(42) }, throws<IllegalArgumentException>())
    }

    @Test
    fun testSingleColumnDimensions() {
        val state = PuzzleState(10, 15)
        assertThat(state.getColumn(1).size, equalTo(10))
        assertThat(state.getColumn(12).size, equalTo(10))
    }

    @Test
    fun testGetColumn() {
        val state = MutablePuzzleState(2, 3)
        state.markCell(0, 0, CellContents.FILLED)
        state.markCell(0, 1, CellContents.X)
        state.markCell(1, 1, CellContents.X)
        state.markCell(1, 2, CellContents.FILLED)

        assertThat(state.getColumn(0), equalTo(listOf(CellContents.FILLED, CellContents.OPEN)))
        assertThat(state.getColumn(1), equalTo(listOf(CellContents.X, CellContents.X)))
        assertThat(state.getColumn(2), equalTo(listOf(CellContents.OPEN, CellContents.FILLED)))
    }

    @Test
    fun testInvalidColumnIndexThrowsException() {
        val state = PuzzleState(4, 3)
        assertThat({ state.getColumn(-1) }, throws<IllegalArgumentException>())
        assertThat({ state.getColumn(-42) }, throws<IllegalArgumentException>())
        assertThat({ state.getColumn(3) }, throws<IllegalArgumentException>())
        assertThat({ state.getColumn(42) }, throws<IllegalArgumentException>())
    }
}

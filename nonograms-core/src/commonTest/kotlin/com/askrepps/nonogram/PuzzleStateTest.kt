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

import com.askrepps.nonogram.internal.FILLED
import com.askrepps.nonogram.internal.OPEN
import com.askrepps.nonogram.internal.X
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Tests for [PuzzleState] and [MutablePuzzleState].
 */
class PuzzleStateTest {
    @Test
    fun testStateDimensions() {
        val state = PuzzleState(3, 4)
        assertEquals(3, state.rows)
        assertEquals(4, state.columns)
        assertEquals(0 until 3, state.rowIndices)
        assertEquals(0 until 4, state.columnIndices)
    }

    @Test
    fun testInvalidDimensionsThrowsException() {
        assertFailsWith<IllegalArgumentException> { PuzzleState(0, 1) }
        assertFailsWith<IllegalArgumentException> { PuzzleState(1, 0) }
        assertFailsWith<IllegalArgumentException> { PuzzleState(0, 0) }
        assertFailsWith<IllegalArgumentException> { PuzzleState(-1, 1) }
        assertFailsWith<IllegalArgumentException> { PuzzleState(1, -1) }
        assertFailsWith<IllegalArgumentException> { PuzzleState(-1, -1) }
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
        val grid = state.getCellGrid()
        assertEquals(rows, grid.size)
        for (row in grid) {
            assertEquals(columns, row.size)
        }
    }

    @Test
    fun testCellsInitializedToOpen() {
        val state = PuzzleState(10, 15)
        val grid = state.getCellGrid()
        for (row in state.rowIndices) {
            for (col in state.columnIndices) {
                assertEquals(OPEN, grid[row][col])
                assertEquals(OPEN, state.getCell(row, col))
            }
        }
    }

    @Test
    fun testInvalidGetCellCoordinatesThrowsException() {
        val state = PuzzleState(10, 15)
        assertFailsWith<IllegalArgumentException> { state.getCell(-1, 0) }
        assertFailsWith<IllegalArgumentException> { state.getCell(0, -1) }
        assertFailsWith<IllegalArgumentException> { state.getCell(-1, -1) }
        assertFailsWith<IllegalArgumentException> { state.getCell(10, 0) }
        assertFailsWith<IllegalArgumentException> { state.getCell(0, 15) }
        assertFailsWith<IllegalArgumentException> { state.getCell(10, 15) }
    }

    @Test
    fun testMarkCell() {
        val state = MutablePuzzleState(10, 15)
        state.markCell(1, 2, FILLED)
        state.markCell(3, 4, X)

        val grid = state.getCellGrid()
        for (row in state.rowIndices) {
            for (col in state.columnIndices) {
                val expectedContents = when {
                    row == 1 && col == 2 -> FILLED
                    row == 3 && col == 4 -> X
                    else -> OPEN
                }
                assertEquals(expectedContents, grid[row][col])
                assertEquals(expectedContents, state.getCell(row, col))
            }
        }
    }

    @Test
    fun testInvalidMarkCellCoordinatesThrowsException() {
        val state = MutablePuzzleState(10, 15)
        assertFailsWith<IllegalArgumentException> { state.markCell(-1, 0, OPEN) }
        assertFailsWith<IllegalArgumentException> { state.markCell(0, -1, OPEN) }
        assertFailsWith<IllegalArgumentException> { state.markCell(-1, -1, OPEN) }
        assertFailsWith<IllegalArgumentException> { state.markCell(10, 0, OPEN) }
        assertFailsWith<IllegalArgumentException> { state.markCell(0, 15, OPEN) }
        assertFailsWith<IllegalArgumentException> { state.markCell(10, 15, OPEN) }
    }

    @Test
    fun testSingleRowDimension() {
        val state = PuzzleState(10, 15)
        assertEquals(15, state.getRow(1).size)
        assertEquals(15, state.getRow(7).size)
    }

    @Test
    fun testGetRow() {
        val state = MutablePuzzleState(2, 3)
        state.markCell(0, 0, FILLED)
        state.markCell(0, 1, X)
        state.markCell(1, 1, X)
        state.markCell(1, 2, FILLED)

        assertEquals(listOf(FILLED, X, OPEN), state.getRow(0))
        assertEquals(listOf(OPEN, X, FILLED), state.getRow(1))
    }

    @Test
    fun testInvalidRowIndexThrowsException() {
        val state = PuzzleState(3, 4)
        assertFailsWith<IllegalArgumentException> { state.getRow(-1) }
        assertFailsWith<IllegalArgumentException> { state.getRow(-42) }
        assertFailsWith<IllegalArgumentException> { state.getRow(3) }
        assertFailsWith<IllegalArgumentException> { state.getRow(42) }
    }

    @Test
    fun testSingleColumnDimensions() {
        val state = PuzzleState(10, 15)
        assertEquals(10, state.getColumn(1).size)
        assertEquals(10, state.getColumn(12).size)
    }

    @Test
    fun testGetColumn() {
        val state = MutablePuzzleState(2, 3)
        state.markCell(0, 0, FILLED)
        state.markCell(0, 1, X)
        state.markCell(1, 1, X)
        state.markCell(1, 2, FILLED)

        assertEquals(listOf(FILLED, OPEN), state.getColumn(0))
        assertEquals(listOf(X, X), state.getColumn(1))
        assertEquals(listOf(OPEN, FILLED), state.getColumn(2))
    }

    @Test
    fun testInvalidColumnIndexThrowsException() {
        val state = PuzzleState(4, 3)
        assertFailsWith<IllegalArgumentException> { state.getColumn(-1) }
        assertFailsWith<IllegalArgumentException> { state.getColumn(-42) }
        assertFailsWith<IllegalArgumentException> { state.getColumn(3) }
        assertFailsWith<IllegalArgumentException> { state.getColumn(42) }
    }
}

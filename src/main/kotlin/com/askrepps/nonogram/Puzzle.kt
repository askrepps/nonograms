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

import com.askrepps.nonogram.internal.allNonnegative
import com.askrepps.nonogram.internal.anyZeroStandsAlone
import com.askrepps.nonogram.internal.getCellIndex
import com.askrepps.nonogram.internal.hintsFitWithin

/**
 * A nonogram puzzle definition (not including cell contents).
 *
 * @throws IllegalArgumentException if the puzzle definition is invalid.
 */
data class PuzzleDefinition(
    val rows: Int,
    val columns: Int,
    val rowHints: List<List<Int>>,
    val columnHints: List<List<Int>>
) {
    init {
        // validate puzzle definition
        require(rows > 0 && columns > 0) {
            "Puzzle must have positive dimensions"
        }
        require(rowHints.size == rows && columnHints.size == columns) {
            "Puzzle must have hints for every row and column"
        }
        require(rowHints.all { it.isNotEmpty() } && columnHints.all { it.isNotEmpty() }) {
            "Puzzle must have hint values for every row and column"
        }
        require(rowHints.all { it.allNonnegative } && columnHints.all { it.allNonnegative }) {
            "Puzzle must have non-negative hint values"
        }
        require(rowHints.all { it.anyZeroStandsAlone } && columnHints.all { it.anyZeroStandsAlone }) {
            "Hint values of zero must be the only value in that row or column"
        }
        require(rowHints.sumBy { it.sum() } == columnHints.sumBy { it.sum() }) {
            "Puzzle must have the same total squares in row and column hints"
        }
        require(rowHints.all { it.hintsFitWithin(columns) } && columnHints.all { it.hintsFitWithin(rows) }) {
            "Puzzle must have all hints fit in their corresponding row or column"
        }
    }
}

/**
 * Possible contents of a puzzle grid cell.
 */
enum class CellContents { OPEN, FILLED, X }

/**
 * The state of a nonogram puzzle grid.
 *
 * @throws IllegalArgumentException if the puzzle dimensions are invalid.
 */
open class PuzzleState(val rows: Int, val columns: Int) {
    internal val cells = MutableList(rows * columns) { CellContents.OPEN }

    init {
        require(rows > 0 && columns > 0) {
            "Puzzle must have positive dimensions"
        }
    }

    /**
     * The current contents of all puzzle cells as a 2-D grid stored in row-major order.
     */
    val cellGrid: List<List<CellContents>>
        get() = List(rows) { row -> List(columns) { col -> getCell(row, col) } }

    /**
     * Get the contents of a cell located at ([row], [col]).
     *
     * @throws IllegalArgumentException if [row] or [col] are invalid.
     */
    fun getCell(row: Int, col: Int): CellContents {
        validateGridCoordinates(row, col)
        return cells[getCellIndex(row, col, columns)]
    }

    /**
     * Get the contents of the entire row of the puzzle with index [row].
     *
     * @throws IllegalArgumentException if [row] is invalid.
     */
    fun getRow(row: Int): List<CellContents> {
        validateRow(row)
        return List(columns) { col -> getCell(row, col) }
    }

    /**
     * Get the contents of the entire column of the puzzle with index [col].
     *
     * @throws IllegalArgumentException if [col] is invalid.
     */
    fun getColumn(col: Int): List<CellContents> {
        validateColumn(col)
        return List(rows) { row -> getCell(row, col) }
    }

    protected fun validateGridCoordinates(row: Int, col: Int) {
        validateRow(row)
        validateColumn(col)
    }

    private fun validateRow(row: Int) {
        require(row in 0 until rows) { "Invalid row index ($row), must be from 0 - ${rows - 1}" }
    }

    private fun validateColumn(col: Int) {
        require(col in 0 until columns) { "Invalid column index ($col), must be from 0 - ${columns - 1}" }
    }

    protected fun getCellIndex(row: Int, col: Int) = getCellIndex(row, col, columns)
}

/**
 * The state of a nonogram puzzle grid that can be edited (i.e., by a solver algorithm).
 */
class MutablePuzzleState(rows: Int, columns: Int) : PuzzleState(rows, columns) {
    /**
     * Mark the cell located at ([row], [col]) with new [contents].
     *
     * @throws IllegalArgumentException if the grid coordinates are invalid.
     */
    fun markCell(row: Int, col: Int, contents: CellContents) {
        validateGridCoordinates(row, col)
        cells[getCellIndex(row, col)] = contents
    }
}

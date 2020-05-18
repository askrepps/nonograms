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

import java.lang.RuntimeException

/**
 * Solve a nonogram puzzle.
 *
 * @throws SolverException if the puzzle could not be solved.
 */
fun PuzzleDefinition.solve(): PuzzleState {
    val state = MutablePuzzleState(rows, columns)
    do {
        var changesMade = false
        for (row in rowIndices) {
            changesMade = state.applyHints(this, row, RowOrColumn.ROW) || changesMade
        }
        for (col in columnIndices) {
            changesMade = state.applyHints(this, col, RowOrColumn.COLUMN) || changesMade
        }
    } while (changesMade)
    return state
}

/**
 * Indicates whether a line is a row or column of the puzzle.
 */
internal enum class RowOrColumn { ROW, COLUMN }

/**
 * Apply hints to a row or column of the puzzle.
 *
 * @return true if changes were made to the puzzle state, false otherwise.
 */
internal fun MutablePuzzleState.applyHints(
    puzzle: PuzzleDefinition,
    lineIndex: Int,
    rowOrColumn: RowOrColumn
): Boolean {
    val lineData: List<CellContents>
    val hints: List<Int>
    when (rowOrColumn) {
        RowOrColumn.ROW -> {
            require(lineIndex in puzzle.rowIndices) {
                "Invalid row index ($lineIndex), must be from ${puzzle.rowIndices}"
            }
            lineData = getRow(lineIndex)
            hints = puzzle.rowHints[lineIndex]
        }
        RowOrColumn.COLUMN -> {
            require(lineIndex in puzzle.columnIndices) {
                "Invalid column index ($lineIndex), must be from ${puzzle.columnIndices}"
            }
            lineData = getColumn(lineIndex)
            hints = puzzle.columnHints[lineIndex]
        }
    }

    // check if there is anything to do
    if (lineData.all { it != CellContents.OPEN }) {
        return false
    }

    // lines with 0 are completely marked with Xs
    if (hints.first() == 0) {
        markLine(lineIndex, lineData.size, rowOrColumn, CellContents.X)
        return true
    }

    // lines where the hint is equal to the size are completely filled in
    if (hints.first() == lineData.size) {
        markLine(lineIndex, lineData.size, rowOrColumn, CellContents.FILLED)
    }

    return false
}

private fun MutablePuzzleState.markLine(
    lineIndex: Int,
    lineSize: Int,
    rowOrColumn: RowOrColumn,
    contents: CellContents
) {
    when (rowOrColumn) {
        RowOrColumn.ROW -> {
            for (col in 0 until lineSize) {
                markCell(lineIndex, col, contents)
            }
        }
        RowOrColumn.COLUMN -> {
            for (row in 0 until lineSize) {
                markCell(row, lineIndex, contents)
            }
        }
    }
}

/**
 * Exception indicating the puzzle could not be solved.
 */
class SolverException(message: String) : RuntimeException(message)

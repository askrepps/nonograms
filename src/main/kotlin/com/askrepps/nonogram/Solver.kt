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
    return when (rowOrColumn) {
        RowOrColumn.ROW -> {
            applyHintsToLine(getRow(lineIndex), puzzle.rowHints[lineIndex]) { col, contents ->
                markCell(lineIndex, col, contents)
            }
        }
        RowOrColumn.COLUMN -> {
            applyHintsToLine(getColumn(lineIndex), puzzle.columnHints[lineIndex]) { row, contents ->
                markCell(row, lineIndex, contents)
            }
        }
    }
}

internal fun applyHintsToLine(
    lineData: List<CellContents>,
    hints: List<Int>,
    markCell: (Int, CellContents) -> Unit
): Boolean {
    // check if there is anything to do
    if (lineData.all { it != CellContents.OPEN }) {
        return false
    }

    // lines with 0 are completely marked with Xs
    if (hints.first() == 0) {
        markLine(lineData.size, CellContents.X, markCell)
        return true
    }

    // lines where the hint is equal to the size are completely filled in
    if (hints.first() == lineData.size) {
        markLine(lineData.size, CellContents.FILLED, markCell)
        return true
    }

    return false
}

private fun markLine(
    lineSize: Int,
    contents: CellContents,
    markCell: (Int, CellContents) -> Unit
) {
    for (i in 0 until lineSize) {
        markCell(i, contents)
    }
}

/**
 * Exception indicating the puzzle could not be solved.
 */
class SolverException(message: String) : RuntimeException(message)

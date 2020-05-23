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

import com.askrepps.nonogram.internal.totalHintLength
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

    // check for lines where there is a guaranteed overlap
    val slack = lineData.size - hints.totalHintLength
    val maxHint = hints.max() ?: -1
    if (maxHint > slack && fillOverlappingCells(lineData, hints, slack, markCell)) {
        return true
    }

    // check special rules for single-hint case
    if (hints.size == 1 && maxHint > 0) {
        val onlyHint = hints.first()
        // check for border cells that can be ruled out due to distance from filled in cells
        if (fillSingleHintShrinkingBorderCells(lineData, onlyHint, markCell)) {
            return true
        }
    }

    // check for completed lines
    if (lineData.count { it == CellContents.FILLED } == hints.sum()) {
        ruleOutOpenCellsInLine(lineData, markCell)
        return true
    }

    return false
}

private fun fillOverlappingCells(
    lineData: List<CellContents>,
    hints: List<Int>,
    slack: Int,
    markCell: (Int, CellContents) -> Unit
): Boolean {
    var index = 0
    var changes = false
    for (hint in hints) {
        for (j in 0 until hint) {
            if (j >= slack) {
                if (lineData[index] != CellContents.FILLED) {
                    markCell(index, CellContents.FILLED)
                    changes = true
                }
            }
            index++
        }
        if (slack == 0 && index < lineData.size && lineData[index] != CellContents.X) {
            markCell(index, CellContents.X)
            changes = true
        }
        index++
    }
    return changes
}

private fun fillSingleHintShrinkingBorderCells(
    lineData: List<CellContents>,
    hint: Int,
    markCell: (Int, CellContents) -> Unit
): Boolean {
    var changes = false
    val firstFilledIndex = lineData.indexOfFirst { it == CellContents.FILLED }
    val lastFilledIndex = lineData.indexOfLast { it == CellContents.FILLED }
    val slack = hint - (lastFilledIndex - firstFilledIndex + 1)
    if (slack > 0 && firstFilledIndex in lineData.indices && lastFilledIndex in lineData.indices) {
        val slackSpace = (firstFilledIndex - slack)..(lastFilledIndex + slack)
        for (index in lineData.indices) {
            if (index !in slackSpace && lineData[index] != CellContents.X) {
                markCell(index, CellContents.X)
                changes = true
            }
        }
    }
    return changes
}

private fun ruleOutOpenCellsInLine(
    lineData: List<CellContents>,
    markCell: (Int, CellContents) -> Unit
) {
    for (i in lineData.indices) {
        if (lineData[i] == CellContents.OPEN) {
            markCell(i, CellContents.X)
        }
    }
}

/**
 * Exception indicating the puzzle could not be solved.
 */
class SolverException(message: String) : RuntimeException(message)

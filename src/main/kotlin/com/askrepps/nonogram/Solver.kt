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

import com.askrepps.nonogram.internal.HintSpacingGenerator
import com.askrepps.nonogram.internal.addTo

/**
 * Solve a nonogram puzzle.
 *
 * @throws SolverException if the puzzle could not be solved (i.e. no solution or no unique solution).
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

    // ensure all cells have been marked
    if (!state.cellGrid.all { row -> row.all { cell -> cell != CellContents.OPEN } }) {
        throw SolverException(SolverFailureReason.NO_UNQIUE, "Puzzle does not have a unique solution")
    }

    return state
}

/**
 * The reasons a puzzle cannot be solved.
 */
enum class SolverFailureReason { NO_SOLUTION, NO_UNQIUE }

/**
 * Exception indicating the puzzle could not be solved.
 */
class SolverException(val reason: SolverFailureReason, message: String) : RuntimeException(message)

/**
 * Indicates whether a line is a row or column of the puzzle.
 */
internal enum class RowOrColumn { ROW, COLUMN }

/**
 * Apply hints to a row or column of the puzzle.
 *
 * @return true if changes were made to the puzzle state, false otherwise.
 *
 * @throws SolverException if the puzzle has no solution.
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

/**
 * Apply hints to a line of data.
 *
 * @return true if changes were made to the puzzle state, false otherwise.
 *
 * @throws SolverException if the puzzle has no solution.
 */
internal fun applyHintsToLine(
    lineData: List<CellContents>,
    hints: List<Int>,
    markCell: (Int, CellContents) -> Unit
): Boolean {
    // try all valid combinations of spacing between hint values
    val counts = IntArray(lineData.size) { 0 }
    val tempCounts = IntArray(lineData.size) { 0 }
    var validCount = 0
    for (spaces in HintSpacingGenerator(hints, lineData.size)) {
        var isValid = true
        var cellIndex = 0
        var hintIndex = 0

        // validate each hint group can be satisfied with the current spacing
        tempCounts.fill(0)
        while (isValid && hintIndex < hints.size) {
            // validate cells before hint group can be X
            val xLength = spaces[hintIndex]
            isValid = isValid && validateSection(lineData, CellContents.X, cellIndex, xLength)
            cellIndex += xLength

            // validate hint group cells can be filled
            val fillLength = hints[hintIndex]
            isValid = isValid && validateSection(lineData, CellContents.FILLED, cellIndex, fillLength) { filledIndex ->
                // track which cells are successfully filled
                tempCounts[filledIndex]++
            }
            cellIndex += fillLength

            hintIndex++
        }
        // validate remainder of cells can be X
        isValid = isValid && validateSection(lineData, CellContents.X, cellIndex)

        if (isValid) {
            // add valid filled cells to the total counts to track overlap between spacing combinations
            tempCounts.addTo(counts)
            validCount++
        }
    }

    // check for contradictions
    if (validCount == 0) {
        throw SolverException(SolverFailureReason.NO_SOLUTION, "Puzzle does not have a solution")
    }

    // apply results
    var changes = false
    for (index in lineData.indices) {
        // fill cells that appear in every valid combination
        if (counts[index] == validCount && lineData[index] != CellContents.FILLED) {
            markCell(index, CellContents.FILLED)
            changes = true
        }
        // put x in cells that appear in no valid combinations
        if (counts[index] == 0 && lineData[index] != CellContents.X) {
            markCell(index, CellContents.X)
            changes = true
        }
    }
    return changes
}

private fun validateSection(
    lineData: List<CellContents>,
    contents: CellContents,
    startIndex: Int,
    length: Int = -1,
    onCellValid: (Int) -> Unit = {}
): Boolean {
    val endIndex =
        if (length < 0) {
            lineData.size
        } else {
            startIndex + length
        }
    val contradiction = when (contents) {
        CellContents.FILLED -> CellContents.X
        CellContents.X -> CellContents.FILLED
        CellContents.OPEN -> throw IllegalArgumentException("Open cell cannot be contradicted")
    }

    var isValid = true
    var index = startIndex
    while (isValid && index < endIndex) {
        if (lineData[index] == contradiction) {
            isValid = false
        } else {
            onCellValid(index)
        }
        index++
    }
    return isValid
}

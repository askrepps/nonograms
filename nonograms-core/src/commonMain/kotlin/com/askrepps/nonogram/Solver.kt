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
import com.askrepps.nonogram.internal.IndexSelectionGenerator
import com.askrepps.nonogram.internal.addTo

/**
 * Solve a nonogram puzzle.
 *
 * @throws SolverException if the puzzle could not be solved (i.e., no solution or no unique solution).
 */
fun PuzzleDefinition.solve(): PuzzleSolution {
    val state = MutablePuzzleState(rows, columns)
    var requiredMultiLineReasoning = false
    do {
        var changesMade = state.applySingleLineHints(this)
        if (!state.isFullyMarked()) {
            if (state.applyMultiLineHints(this)) {
                requiredMultiLineReasoning = true
                changesMade = true
            }
        }
    } while (changesMade)

    if (!state.isFullyMarked()) {
        throw SolverNoUniqueSolutionException("Puzzle does not have a unique solution", state)
    }

    return PuzzleSolution(state, requiredMultiLineReasoning)
}

/**
 * The complete or partial puzzle solution produced by the solver algorithm.
 */
data class PuzzleSolution(val state: PuzzleState, val requiredMultiLineReasoning: Boolean)

/**
 * Exception indicating the puzzle could not be solved.
 */
sealed class SolverException(
    message: String,
    val state: PuzzleState?,
    override val cause: Throwable? = null
) : RuntimeException(message)

/**
 * Exception indicating the puzzle has no solution.
 */
class SolverNoSolutionException(
    message: String,
    state: PuzzleState?,
    override val cause: Throwable? = null
) : SolverException(message, state)

/**
 * Exception indicating the puzzle has no unique solution.
 */
class SolverNoUniqueSolutionException(
    message: String,
    state: PuzzleState?,
    override val cause: Throwable? = null
) : SolverException(message, state)

/**
 * Indicates whether a line is a row or column of the puzzle.
 */
internal enum class RowOrColumn { ROW, COLUMN }

/**
 * Run solver algorithm that considers each [puzzle] row and column in isolation.
 *
 * @return true if changes were made to the puzzle state, false otherwise.
 *
 * @throws SolverNoSolutionException if the puzzle has no solution.
 */
internal fun MutablePuzzleState.applySingleLineHints(puzzle: PuzzleDefinition): Boolean {
    var anyChangesMade = false
    do {
        var changesMadeThisPass = false
        for (row in rowIndices) {
            if (applyHints(puzzle, row, RowOrColumn.ROW)) {
                changesMadeThisPass = true
            }
        }
        for (col in columnIndices) {
            if (applyHints(puzzle, col, RowOrColumn.COLUMN)) {
                changesMadeThisPass = true
            }
        }
        if (changesMadeThisPass) {
            anyChangesMade = true
        }
    } while (changesMadeThisPass)
    return anyChangesMade
}

/**
 * Apply hints to a row or column of the puzzle.
 *
 * @return true if changes were made to the puzzle state, false otherwise.
 *
 * @throws SolverNoSolutionException if the puzzle has no solution.
 */
internal fun MutablePuzzleState.applyHints(
    puzzle: PuzzleDefinition,
    lineIndex: Int,
    rowOrColumn: RowOrColumn
): Boolean {
    try {
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
    } catch (e: SolverNoSolutionException) {
        // attach state to exception and re-throw
        throw SolverNoSolutionException(message = checkNotNull(e.message), state = this, cause = e)
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
        tempCounts.fill(0)
        val isValid = validateSpacing(lineData, hints, spaces) { contents, index ->
            // track which cells are successfully filled
            if (contents == CellContents.FILLED) {
                tempCounts[index]++
            }
        }

        if (isValid) {
            // add valid filled cells to the total counts to track overlap between spacing combinations
            tempCounts.addTo(counts)
            validCount++
        }
    }

    // apply results and detect changes
    return applyCountResults(lineData, counts, validCount, markCell)
}

private fun validateSpacing(
    lineData: List<CellContents>,
    hints: List<Int>,
    spaces: List<Int>,
    onCellValid: (CellContents, Int) -> Unit = { _, _ -> }
): Boolean {
    var isValid = true
    var cellIndex = 0
    var hintIndex = 0

    // validate each hint group can be satisfied with the current spacing
    while (isValid && hintIndex < hints.size) {
        // validate cells before hint group can be X
        val xLength = spaces[hintIndex]
        isValid = isValid && validateSection(lineData, CellContents.X, cellIndex, xLength, onCellValid)
        cellIndex += xLength

        // validate hint group cells can be filled
        val fillLength = hints[hintIndex]
        isValid = isValid && validateSection(lineData, CellContents.FILLED, cellIndex, fillLength, onCellValid)
        cellIndex += fillLength

        hintIndex++
    }
    // validate remainder of cells can be X
    return isValid && validateSection(lineData, CellContents.X, cellIndex, onCellValid = onCellValid)
}

private fun validateSection(
    lineData: List<CellContents>,
    contents: CellContents,
    startIndex: Int,
    length: Int = -1,
    onCellValid: (CellContents, Int) -> Unit
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
            onCellValid(contents, index)
        }
        index++
    }
    return isValid
}

/**
 * Run solver algorithm that considers multiple [puzzle] rows and columns in combination.
 *
 * @return true if changes were made to the puzzle state, false otherwise.
 */
internal fun MutablePuzzleState.applyMultiLineHints(puzzle: PuzzleDefinition): Boolean {
    val allValidRowSpacings = puzzle.rowHints.mapIndexed { row, hints ->
        HintSpacingGenerator(hints, columns)
            .filter { spaces -> validateSpacing(getRow(row), hints, spaces) }
            .toList()
    }

    // try all valid combinations of spacing between hint values for all rows
    val counts = IntArray(cells.size) { 0 }
    var validCount = 0
    val openLine = List(columns) { CellContents.OPEN }
    for (selectedSpaces in IndexSelectionGenerator(allValidRowSpacings.map { it.size })) {
        // generate puzzle state using selected spacings
        val tempState = MutablePuzzleState(rows, columns)
        for (row in rowIndices) {
            val hints = puzzle.rowHints[row]
            val spaces = allValidRowSpacings[row][selectedSpaces[row]]
            validateSpacing(openLine, hints, spaces) { contents, column ->
                tempState.markCell(row, column, contents)
            }
        }

        // validate generated state is consistent with column hints
        if (columnIndices.all { validateLineData(tempState.getColumn(it), puzzle.columnHints[it]) }) {
            tempState.cells.forEachIndexed { index, contents ->
                if (contents == CellContents.FILLED) {
                    counts[index]++
                }
            }
            validCount++
        }
    }

    // apply results and detect changes
    return applyCountResults(cells, counts, validCount) { index, contents ->
        cells[index] = contents
    }
}

private fun validateLineData(lineData: List<CellContents>, hints: List<Int>): Boolean {
    // must have at least one hint
    if (hints.isEmpty()) {
        return false
    }

    // validate empty hint
    if (hints[0] == 0 && lineData.all { it != CellContents.FILLED }) {
        return true
    }

    var index = 0
    for (hint in hints) {
        if (index < lineData.size) {
            // find next group of filled cells (if not found, hint is not satisfied)
            val nextFilled = lineData.subList(index, lineData.size).indexOfFirst { it == CellContents.FILLED }
            if (nextFilled < 0) {
                return false
            }
            index += nextFilled
        }

        // validate group of filled cells is exactly the size specified by the hint
        if (index + hint > lineData.size || lineData.subList(index, index + hint).any { it == CellContents.X }) {
            return false
        }
        if (index + hint < lineData.size && lineData[index + hint] == CellContents.FILLED) {
            return false
        }

        index += hint
    }
    return true
}

private fun applyCountResults(
    cells: List<CellContents>,
    counts: IntArray,
    validCount: Int,
    markCell: (Int, CellContents) -> Unit
): Boolean {
    // check for contradictions
    if (validCount == 0) {
        throw SolverNoSolutionException("Puzzle does not have a solution", null)
    }

    var changes = false
    for (index in cells.indices) {
        // fill cells that appear in every valid combination
        if (counts[index] == validCount && cells[index] != CellContents.FILLED) {
            markCell(index, CellContents.FILLED)
            changes = true
        }
        // put x in cells that appear in no valid combinations
        if (counts[index] == 0 && cells[index] != CellContents.X) {
            markCell(index, CellContents.X)
            changes = true
        }
    }
    return changes
}

private fun PuzzleState.isFullyMarked(): Boolean = cellGrid.all { row -> row.all { it != CellContents.OPEN } }

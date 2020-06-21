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

import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import org.w3c.dom.asList
import kotlin.browser.document

private fun CellContents.printedSymbol() =
    when (this) {
        CellContents.OPEN -> " "
        CellContents.FILLED -> "■"
        CellContents.X -> "x"
    }

private fun clearPage() {
    document.body?.let { body ->
        for (child in body.children.asList()) {
            child.remove()
        }
    }
}

private fun renderPage(pageCreator: DIV.() -> Unit) {
    clearPage()
    document.body!!.append.div {
        pageCreator()
    }
}

private fun renderResultPage(
    puzzleDefinition: PuzzleDefinition,
    state: PuzzleState?,
    e: SolverException? = null
) = renderPage {
    addTitle()
    addButtons()
    br
    br
    addResultsTable(puzzleDefinition, state)
    if (e != null) {
        p {
            style = "color: red;"
            +(e.message ?: "Unknown error occurred")
        }
    }
    br
    br
    addFooter()
}

private fun DIV.addTitle() {
    h1 {
        +"Nonogram Solver"
    }
}

private fun DIV.addResultsTable(puzzleDefinition: PuzzleDefinition, results: PuzzleState?) {
    val state = results ?: PuzzleState(puzzleDefinition.rows, puzzleDefinition.columns)
    val hintTableRows = puzzleDefinition.columnHints.maxBy { it.size }?.size ?: 0
    val hintTableColumns = puzzleDefinition.rowHints.maxBy { it.size }?.size ?: 0
    val totalTableColumns = state.columns + hintTableColumns

    table {
        style = "border-collapse: collapse;"
        for (hintRow in 0 until hintTableRows) {
            tr {
                for (column in 0 until totalTableColumns) {
                    td {
                        val hintColumnIndex = column - hintTableColumns
                        if (hintColumnIndex in puzzleDefinition.columnHints.indices) {
                            val hints = puzzleDefinition.columnHints[hintColumnIndex]
                            val hintIndex = hints.size - hintTableRows + hintRow
                            if (hintIndex in hints.indices) {
                                +hints[hintIndex].toString()
                            } else {
                                +""
                            }
                        } else {
                            +""
                        }
                    }
                }
            }
        }
        for (puzzleRow in state.rowIndices) {
            tr {
                for (column in 0 until totalTableColumns) {
                    td {
                        if (column < hintTableColumns) {
                            val hints = puzzleDefinition.rowHints[puzzleRow]
                            val hintIndex = hints.size - hintTableColumns + column
                            if (hintIndex in hints.indices) {
                                +hints[hintIndex].toString()
                            } else {
                                +""
                            }
                        } else {
                            style = "border: 1px solid black;"
                            val puzzleColumn = column - hintTableColumns
                            +state.getCell(puzzleRow, puzzleColumn).printedSymbol()
                        }
                    }
                }
            }
        }
    }
}

private fun DIV.addButtons() {
    input {
        type = InputType.button
        value = "Puzzle 0"
        style = "margin-right: 10px;"
        onClickFunction = {
            solvePuzzle(0)
        }
    }
    input {
        type = InputType.button
        value = "Puzzle 1"
        style = "margin-right: 10px;"
        onClickFunction = {
            solvePuzzle(1)
        }
    }
    input {
        type = InputType.button
        value = "Puzzle 2 (requires multi-line reasoning)"
        style = "margin-right: 10px;"
        onClickFunction = {
            solvePuzzle(2)
        }
    }
    input {
        type = InputType.button
        value = "Puzzle 3 (no unique solution)"
        onClickFunction = {
            solvePuzzle(3)
        }
    }
}

private fun DIV.addFooter() {
    a {
        href = "3RD-PARTY-LICENSES.txt"
        +"Third-Party License Notice"
    }
}

fun solvePuzzle(puzzleId: Int) {
    val puzzleDefinition =
        when (puzzleId) {
            0 -> PuzzleDefinition(
                rows = 8,
                columns = 8,
                rowHints = listOf(
                    listOf(0),
                    listOf(1, 1),
                    listOf(1, 1),
                    listOf(1, 1),
                    listOf(0),
                    listOf(0),
                    listOf(6),
                    listOf(0)
                ),
                columnHints = listOf(
                    listOf(0),
                    listOf(1),
                    listOf(3, 1),
                    listOf(1),
                    listOf(1),
                    listOf(3, 1),
                    listOf(1),
                    listOf(0)
                )
            )
            1 -> PuzzleDefinition(
                rows = 5,
                columns = 5,
                rowHints = listOf(
                    listOf(5),
                    listOf(3),
                    listOf(3, 1),
                    listOf(4),
                    listOf(0)
                ),
                columnHints = listOf(
                    listOf(1, 1),
                    listOf(4),
                    listOf(4),
                    listOf(2, 1),
                    listOf(1, 2)
                )
            )
            2 -> PuzzleDefinition(
                rows = 8,
                columns = 8,
                rowHints = listOf(
                    listOf(0),
                    listOf(1, 1),
                    listOf(1, 1),
                    listOf(1, 1),
                    listOf(0),
                    listOf(1, 1),
                    listOf(4),
                    listOf(0)
                ),
                columnHints = listOf(
                    listOf(0),
                    listOf(1),
                    listOf(3, 1),
                    listOf(1),
                    listOf(1),
                    listOf(3, 1),
                    listOf(1),
                    listOf(0)
                )
            )
            3 -> PuzzleDefinition(
                rows = 4,
                columns = 4,
                rowHints = listOf(
                    listOf(1, 1),
                    listOf(1, 1),
                    listOf(1, 1),
                    listOf(1, 1)
                ),
                columnHints = listOf(
                    listOf(1, 1),
                    listOf(1, 1),
                    listOf(1, 1),
                    listOf(1, 1)
                )
            )
            else -> throw IllegalArgumentException("Unknown puzzle ID $puzzleId")
        }

    try {
        val solution = puzzleDefinition.solve()
        renderResultPage(puzzleDefinition, solution)
    } catch (e: SolverException) {
        renderResultPage(puzzleDefinition, e.state, e)
    }
}

fun main() {
    solvePuzzle(0)
}

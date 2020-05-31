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
import kotlin.browser.document

private fun CellContents.printedSymbol() =
    when (this) {
        CellContents.OPEN -> " "
        CellContents.FILLED -> "■"
        CellContents.X -> "x"
    }

fun main() {
    val puzzleDefinition = PuzzleDefinition(
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

    val solution = puzzleDefinition.solve()
    val hintTableRows = puzzleDefinition.columnHints.maxBy { it.size }?.size ?: 0
    val hintTableColumns = puzzleDefinition.rowHints.maxBy { it.size }?.size ?: 0
    val totalTableColumns = solution.columns + hintTableColumns

    document.body!!.append.div {
        h1 {
            +"Nonogram Solver"
        }

        p {
            +"Solution to placeholder hard-coded puzzle:"
        }

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
            for (puzzleRow in solution.rowIndices) {
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
                                +solution.getCell(puzzleRow, puzzleColumn).printedSymbol()
                            }
                        }
                    }
                }
            }
        }

        br
        br
        a {
            href = "3RD-PARTY-LICENSES.txt"
            +"Third-Party License Notice"
        }
    }
}

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
import kotlinx.html.js.onInputFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.asList
import org.w3c.dom.events.Event
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

var rowsInput = ""
var columnsInput = ""
var rowHintsInput = ""
var columnHintsInput = ""

private val Event.inputValue
    get() = when (val t = target) {
        is HTMLInputElement -> t.value
        is HTMLTextAreaElement -> t.value
        else -> throw IllegalArgumentException("Unhandled target tag type: ${target?.let { it::class.simpleName} }")
    }

private fun renderSolverPage(
    puzzleDefinition: PuzzleDefinition? = null,
    state: PuzzleState? = null,
    error: Exception? = null
): Unit = renderPage {
    addTitle()
    br
    br
    p {
        +"Number of rows:"
    }
    input {
        type = InputType.text
        value = rowsInput
        onInputFunction = {
            rowsInput = it.inputValue
        }
    }
    br
    p {
        +"Number of columns:"
    }
    input {
        type = InputType.text
        value = columnsInput
        onInputFunction = {
            columnsInput = it.inputValue
        }
    }
    br
    p {
        +"Row hints (1 row of space-separated hints per line)"
    }
    textArea(rows = "10", cols = "10") {
        +rowHintsInput
        onInputFunction = {
            rowHintsInput = it.inputValue
        }
    }
    br
    p {
        +"Column hints (1 column of space-separated hints per line)"
    }
    textArea(rows = "10", cols = "10") {
        +columnHintsInput
        onInputFunction = {
            columnHintsInput = it.inputValue
        }
    }
    br
    br
    input {
        type = InputType.button
        value = "Solve"
        onClickFunction = {
            solveEnteredPuzzle()
        }
    }

    if (puzzleDefinition != null) {
        br
        br
        addResultsTable(puzzleDefinition, state)
    }
    if (error != null) {
        br
        br
        p {
            style = "color: red;"
            +(error.message ?: "Unknown error occurred")
        }
    }
    br
    br
    addFooter()
}

private fun parseHintInput(input: String, label: String) = input.split('\n')
    .filter { !it.isBlank() }
    .map { line ->
        line.split("\\s+".toRegex()).map {
            it.toIntOrNull()
                ?: throw Exception("$label do not contain lines of valid space-separated numbers")
        }
    }.ifEmpty {
        throw Exception("$label do not contain lines of valid space-separated numbers")
    }

private fun solveEnteredPuzzle() {
    var puzzle: PuzzleDefinition? = null
    var state: PuzzleState? = null
    try {
        val numRows = rowsInput.toIntOrNull() ?: throw Exception("Number of rows is not a valid number")
        val numCols = columnsInput.toIntOrNull() ?: throw Exception("Number of columns is not a valid number")
        val rowHints = parseHintInput(rowHintsInput, "Row hints")
        val columnHints = parseHintInput(columnHintsInput, "Column hints")

        puzzle = PuzzleDefinition(numRows, numCols, rowHints, columnHints)
        state = puzzle.solve()
        renderSolverPage(puzzle, state)
    } catch (e: Exception) {
        renderSolverPage(puzzle, state, e)
    }
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

private fun DIV.addFooter() {
    a {
        href = "3RD-PARTY-LICENSES.txt"
        +"Third-Party License Notice"
    }
}

fun main() {
    renderSolverPage()
}

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

import kotlinx.browser.document
import kotlinx.html.DIV
import kotlinx.html.InputType
import kotlinx.html.a
import kotlinx.html.br
import kotlinx.html.dom.append
import kotlinx.html.h1
import kotlinx.html.img
import kotlinx.html.input
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onInputFunction
import kotlinx.html.p
import kotlinx.html.style
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.textArea
import kotlinx.html.tr
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.asList
import org.w3c.dom.events.Event
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

private val CellContents.symbolImage
    get() = when (this) {
        CellContents.OPEN -> "open.png"
        CellContents.FILLED -> "fill.png"
        CellContents.X -> "x.png"
    }

private fun clearPage() {
    document.body?.let { body ->
        for (child in body.children.asList()) {
            child.remove()
        }
    }
}

@OptIn(ExperimentalTime::class)
private fun renderPage(pageCreator: DIV.() -> Unit) {
    val renderTime = measureTime {
        clearPage()
        document.body!!.append.div {
            pageCreator()
        }
    }
    println("Render time: ${renderTime.toDouble(DurationUnit.SECONDS)} seconds")
}

const val EXAMPLE_ROWS_INPUT = "8"
const val EXAMPLE_COLUMNS_INPUT = "8"
val EXAMPLE_ROW_HINTS_INPUT = """
    |0
    |1 1
    |1 1
    |1 1
    |0
    |1 1
    |4
    |0
""".trimMargin()
val EXAMPLE_COLUMN_HINTS_INPUT = """
    |0
    |1
    |3 1
    |1
    |1
    |3 1
    |1
    |0
""".trimMargin()

private var rowsInput = EXAMPLE_ROWS_INPUT
private var columnsInput = EXAMPLE_COLUMNS_INPUT
private var rowHintsInput = EXAMPLE_ROW_HINTS_INPUT
private var columnHintsInput = EXAMPLE_COLUMN_HINTS_INPUT

private val Event.inputValue
    get() = when (val t = target) {
        is HTMLInputElement -> t.value
        is HTMLTextAreaElement -> t.value
        else -> throw IllegalArgumentException("Unhandled target tag type: ${target?.let { it::class.simpleName }}")
    }

private fun renderSolverPage(
    puzzleDefinition: PuzzleDefinition? = null,
    solution: PuzzleSolution? = null,
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
        style = "margin-right: 10px"
        onClickFunction = {
            solveEnteredPuzzle()
        }
    }
    input {
        type = InputType.button
        value = "Clear"
        style = "margin-right: 10px"
        onClickFunction = {
            rowsInput = ""
            columnsInput = ""
            rowHintsInput = ""
            columnHintsInput = ""
            renderSolverPage()
        }
    }
    input {
        type = InputType.button
        value = "Example"
        style = "margin-right: 10px"
        onClickFunction = {
            rowsInput = EXAMPLE_ROWS_INPUT
            columnsInput = EXAMPLE_COLUMNS_INPUT
            rowHintsInput = EXAMPLE_ROW_HINTS_INPUT
            columnHintsInput = EXAMPLE_COLUMN_HINTS_INPUT
            renderSolverPage()
        }
    }
    if (puzzleDefinition != null) {
        br
        br
        addResultsTable(puzzleDefinition, solution)
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

private fun parseHintInput(input: String, label: String): List<List<Int>> {
    val fail: () -> Nothing = { throw Exception("$label do not contain lines of valid space-separated numbers") }
    return input.split('\n')
        .filter { it.isNotBlank() }
        .map { line ->
            line.split("\\s+".toRegex())
                .filter { it.isNotBlank() }
                .map {
                    it.trim().toIntOrNull() ?: fail()
                }
        }.ifEmpty { fail() }
}

@OptIn(ExperimentalTime::class)
private fun solveEnteredPuzzle() {
    var puzzle: PuzzleDefinition? = null
    var solution: PuzzleSolution? = null
    try {
        val elapsedTime = measureTime {
            val numRows = rowsInput.trim().toIntOrNull() ?: throw Exception("Number of rows is not a valid number")
            val numCols = columnsInput.trim().toIntOrNull() ?: throw Exception("Number of columns is not a valid number")
            val rowHints = parseHintInput(rowHintsInput, "Row hints")
            val columnHints = parseHintInput(columnHintsInput, "Column hints")

            puzzle = PuzzleDefinition(numRows, numCols, rowHints, columnHints)
            solution = puzzle?.solve()
        }
        println("Solve time: ${elapsedTime.toDouble(DurationUnit.SECONDS)} seconds")
        renderSolverPage(puzzle, solution)
    } catch (e: SolverException) {
        solution = e.state?.let { PuzzleSolution(it, requiredMultiLineReasoning = false) }
        renderSolverPage(puzzle, solution, e)
    } catch (e: Exception) {
        renderSolverPage(puzzle, solution, e)
    }
}

private fun DIV.addTitle() {
    h1 {
        +"Nonogram Solver"
    }
}

private const val HINT_CELL_STYLE = "text-align: center; vertical-align: middle; min-width: 21px; min-height: 21px;"

private fun DIV.addResultsTable(puzzleDefinition: PuzzleDefinition, solution: PuzzleSolution?) {
    val state = solution?.state ?: PuzzleState(puzzleDefinition.rows, puzzleDefinition.columns)
    val hintTableRows = puzzleDefinition.columnHints.maxByOrNull { it.size }?.size ?: 0
    val hintTableColumns = puzzleDefinition.rowHints.maxByOrNull { it.size }?.size ?: 0
    val totalTableColumns = state.columns + hintTableColumns

    table {
        style = "border-collapse: collapse;"
        for (hintRow in 0 until hintTableRows) {
            tr {
                for (column in 0 until totalTableColumns) {
                    td {
                        style = HINT_CELL_STYLE
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
                        style = HINT_CELL_STYLE
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
                            img {
                                style = "vertical-align: middle;"
                                src = state.getCell(puzzleRow, puzzleColumn).symbolImage
                            }
                        }
                    }
                }
            }
        }
    }

    if (solution?.requiredMultiLineReasoning == true) {
        p {
            +"Note: The puzzle required multi-line reasoning to solve"
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

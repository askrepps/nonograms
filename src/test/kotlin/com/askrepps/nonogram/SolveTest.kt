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

import com.askrepps.nonogram.internal.FILLED
import com.askrepps.nonogram.internal.X
import com.askrepps.nonogram.internal.createEmptyPuzzleDefinitionWithDimensions
import com.askrepps.nonogram.internal.createFullPuzzleDefinitionWithDimensions
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.has
import com.natpryce.hamkrest.throws
import org.junit.Test

/**
 * Tests for [solve].
 */
class SolveTest {
    @Test
    fun testSolvePuzzleDimensions() {
        val solution11 = createEmptyPuzzleDefinitionWithDimensions(1, 1).solve()
        assertThat(solution11.rows, equalTo(1))
        assertThat(solution11.columns, equalTo(1))

        val solution24 = createEmptyPuzzleDefinitionWithDimensions(2, 4).solve()
        assertThat(solution24.rows, equalTo(2))
        assertThat(solution24.columns, equalTo(4))
    }

    @Test
    fun testSolveEmptyPuzzle() {
        val solution = createEmptyPuzzleDefinitionWithDimensions(2, 3).solve()
        for (row in solution.rowIndices) {
            for (col in solution.columnIndices) {
                assertThat(solution.getCell(row, col), equalTo(X))
            }
        }
    }

    @Test
    fun testSolveFullPuzzle() {
        val solution = createFullPuzzleDefinitionWithDimensions(3, 2).solve()
        for (row in solution.rowIndices) {
            for (col in solution.columnIndices) {
                assertThat(solution.getCell(row, col), equalTo(FILLED))
            }
        }
    }

    @Test
    fun testSolveOddCheckerboardPuzzle() {
        val puzzle = PuzzleDefinition(
            rows = 3,
            columns = 3,
            rowHints = listOf(listOf(1, 1), listOf(1), listOf(1, 1)),
            columnHints = listOf(listOf(1, 1), listOf(1), listOf(1, 1))
        )
        val solution = puzzle.solve()
        for (row in solution.rowIndices) {
            for (col in solution.columnIndices) {
                val expectedValue =
                    if ((row + col) % 2 == 0) {
                        FILLED
                    } else {
                        X
                    }
                assertThat(solution.getCell(row, col), equalTo(expectedValue))
            }
        }
    }

    @Test
    fun testEvenCheckerboardPuzzleHasNoUniqueSolution() {
        val puzzle = PuzzleDefinition(
            rows = 2,
            columns = 2,
            rowHints = listOf(listOf(1), listOf(1)),
            columnHints = listOf(listOf(1), listOf(1))
        )
        assertThat({ puzzle.solve() }, throws(has(SolverException::reason, equalTo(SolverFailureReason.NO_UNQIUE))))
    }

    @Test
    fun testImpossiblePuzzleHasNoSolution() {
        val puzzle = PuzzleDefinition(
            rows = 2,
            columns = 2,
            rowHints = listOf(listOf(0), listOf(2)),
            columnHints = listOf(listOf(0), listOf(2))
        )
        assertThat({ puzzle.solve() }, throws(has(SolverException::reason, equalTo(SolverFailureReason.NO_SOLUTION))))
    }

    @Test
    fun testSolve5x5Puzzle() {
        val puzzle = PuzzleDefinition(
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

        val expectedSolution = listOf(
            listOf(FILLED, FILLED, FILLED, FILLED, FILLED),
            listOf(     X, FILLED, FILLED, FILLED,      X),
            listOf(FILLED, FILLED, FILLED,      X, FILLED),
            listOf(     X, FILLED, FILLED, FILLED, FILLED),
            listOf(     X,      X,      X,      X,      X)
        )

        assertThat(puzzle.solve().cellGrid, equalTo(expectedSolution))
    }
}

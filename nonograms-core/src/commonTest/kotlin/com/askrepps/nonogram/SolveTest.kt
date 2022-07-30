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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Tests for [solve].
 */
class SolveTest {
    @Test
    fun testSolvePuzzleDimensions() {
        val solution11 = createEmptyPuzzleDefinitionWithDimensions(1, 1).solve()
        val state11 = solution11.state
        assertEquals(1, state11.rows)
        assertEquals(1, state11.columns)

        val solution24 = createEmptyPuzzleDefinitionWithDimensions(2, 4).solve()
        val state24 = solution24.state
        assertEquals(2, state24.rows)
        assertEquals(4, state24.columns)
    }

    @Test
    fun testSolveEmptyPuzzle() {
        val solution = createEmptyPuzzleDefinitionWithDimensions(2, 3).solve()
        val state = solution.state
        for (row in state.rowIndices) {
            for (col in state.columnIndices) {
                assertEquals(X, state.getCell(row, col))
            }
        }
        assertFalse(solution.requiredMultiLineReasoning)
    }

    @Test
    fun testSolveFullPuzzle() {
        val solution = createFullPuzzleDefinitionWithDimensions(3, 2).solve()
        val state = solution.state
        for (row in state.rowIndices) {
            for (col in state.columnIndices) {
                assertEquals(FILLED, state.getCell(row, col))
            }
        }
        assertFalse(solution.requiredMultiLineReasoning)
    }

    @Test
    fun testSolveOddCheckerboardPuzzle() {
        val puzzle = PuzzleDefinition(
            rowHints = listOf(listOf(1, 1), listOf(1), listOf(1, 1)),
            columnHints = listOf(listOf(1, 1), listOf(1), listOf(1, 1))
        )
        val solution = puzzle.solve()
        val state = solution.state
        for (row in state.rowIndices) {
            for (col in state.columnIndices) {
                val expectedValue =
                    if ((row + col) % 2 == 0) {
                        FILLED
                    } else {
                        X
                    }
                assertEquals(expectedValue, state.getCell(row, col))
            }
        }
        assertFalse(solution.requiredMultiLineReasoning)
    }

    @Test
    fun testEvenCheckerboardPuzzleHasNoUniqueSolution() {
        val puzzle = PuzzleDefinition(
            rowHints = listOf(listOf(1), listOf(1)),
            columnHints = listOf(listOf(1), listOf(1))
        )
        assertFailsWith<SolverNoUniqueSolutionException> { puzzle.solve() }
    }

    @Test
    fun testImpossiblePuzzleHasNoSolution() {
        val puzzle = PuzzleDefinition(
            rowHints = listOf(listOf(0), listOf(2)),
            columnHints = listOf(listOf(0), listOf(2))
        )
        assertFailsWith<SolverNoSolutionException> { puzzle.solve() }
    }

    @Test
    fun testSolve5x5Puzzle() {
        val puzzle = PuzzleDefinition(
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

        val solution = puzzle.solve()
        assertEquals(expectedSolution, solution.state.getCellGrid())
        assertFalse(solution.requiredMultiLineReasoning)
    }

    @Test
    fun testSolveSmilePuzzle() {
        val puzzle = PuzzleDefinition(
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

        // puzzle has a unique solution, but it requires considering multiple lines at once
        val expectedSolution = listOf(
            listOf(X,      X,      X,      X,      X,      X,      X, X),
            listOf(X,      X, FILLED,      X,      X, FILLED,      X, X),
            listOf(X,      X, FILLED,      X,      X, FILLED,      X, X),
            listOf(X,      X, FILLED,      X,      X, FILLED,      X, X),
            listOf(X,      X,      X,      X,      X,      X,      X, X),
            listOf(X, FILLED,      X,      X,      X,      X, FILLED, X),
            listOf(X,      X, FILLED, FILLED, FILLED, FILLED,      X, X),
            listOf(X,      X,      X,      X,      X,      X,      X, X)
        )

        val solution = puzzle.solve()
        assertEquals(expectedSolution, solution.state.getCellGrid())
        assertTrue(solution.requiredMultiLineReasoning)
    }
}

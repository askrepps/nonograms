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
import com.askrepps.nonogram.internal.OPEN
import com.askrepps.nonogram.internal.X
import com.askrepps.nonogram.internal.markCell
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Tests for [applyHints] and supporting functions.
 *
 * Note: The code under test assumes that hint values are valid w.r.t. the
 *       validation performed during [PuzzleDefinition] initialization, so
 *       only valid hints will be tested.
 */
class ApplyHintsTest {
    @Test
    fun testApplyEmptyHint() {
        val line = mutableListOf(OPEN, OPEN, OPEN)
        val hints = listOf(0)
        val expectedLine = listOf(X, X, X)
        runApplyHintTest(line, hints, expectedLine)
        runNoFurtherChangesTest(line, hints)

        line[1] = OPEN
        runApplyHintTest(line, hints, expectedLine)
        runNoFurtherChangesTest(line, hints)
    }

    @Test
    fun testApplyFillHint() {
        val line = mutableListOf(OPEN, OPEN, OPEN)
        val hints = listOf(3)
        val expectedLine = listOf(FILLED, FILLED, FILLED)
        runApplyHintTest(line, hints, expectedLine)
        runNoFurtherChangesTest(line, hints)

        line[1] = OPEN
        runApplyHintTest(line, hints, expectedLine)
        runNoFurtherChangesTest(line, hints)
    }

    @Test
    fun testApplyCompletedSingleHint() {
        val line = mutableListOf(OPEN, FILLED, FILLED)
        val hints = listOf(2)
        val expectedLine = listOf(X, FILLED, FILLED)
        runApplyHintTest(line, hints, expectedLine)
        runNoFurtherChangesTest(line, hints)
    }

    @Test
    fun testApplyCompletedMultiHint() {
        val line = mutableListOf(FILLED, OPEN, FILLED)
        val hints = listOf(1, 1)
        val expectedLine = listOf(FILLED, X, FILLED)
        runApplyHintTest(line, hints, expectedLine)
        runNoFurtherChangesTest(line, hints)
    }

    @Test
    fun testApplyExactFitMultiHint() {
        val line1 = mutableListOf(OPEN, OPEN, OPEN)
        val hints1 = listOf(1, 1)
        val expectedLine1 = listOf(FILLED, X, FILLED)
        runApplyHintTest(line1, hints1, expectedLine1)
        runNoFurtherChangesTest(line1, hints1)

        val line2 = mutableListOf(OPEN, OPEN, OPEN, OPEN)
        val hints2 = listOf(2, 1)
        val expectedLine2 = listOf(FILLED, FILLED, X, FILLED)
        runApplyHintTest(line2, hints2, expectedLine2)
        runNoFurtherChangesTest(line2, hints2)
    }

    @Test
    fun testApplyOverlapSingleHint() {
        val line = mutableListOf(OPEN, OPEN, OPEN, OPEN)
        val hints = listOf(3)
        val expectedLine = listOf(OPEN, FILLED, FILLED, OPEN)
        runApplyHintTest(line, hints, expectedLine)
        runNoFurtherChangesTest(line, hints)

        line[1] = OPEN
        runApplyHintTest(line, hints, expectedLine)
        runNoFurtherChangesTest(line, hints)
    }

    @Test
    fun testApplyOverlapMultiHint() {
        val line = MutableList(5) { OPEN }
        val hints = listOf(1, 2)
        val expectedLine = listOf(OPEN, OPEN, OPEN, FILLED, OPEN)
        runApplyHintTest(line, hints, expectedLine)
        runNoFurtherChangesTest(line, hints)
    }

    @Test
    fun testApplyShrinkingBorderSingleHintToEmptyStart() {
        val line = mutableListOf(OPEN, OPEN, FILLED, OPEN)
        val hints = listOf(2)
        val expectedLine = listOf(X, OPEN, FILLED, OPEN)
        runApplyHintTest(line, hints, expectedLine)
        runNoFurtherChangesTest(line, hints)
    }

    @Test
    fun testApplyShrinkingBorderSingleHintAfterStartingX() {
        val line = mutableListOf(X, OPEN, OPEN, FILLED, OPEN)
        val hints = listOf(2)
        val expectedLine = listOf(X, X, OPEN, FILLED, OPEN)
        runApplyHintTest(line, hints, expectedLine)
        runNoFurtherChangesTest(line, hints)
    }

    @Test
    fun testApplyShrinkingBorderSingleHintToEmptyEnd() {
        val line = mutableListOf(OPEN, FILLED, OPEN, OPEN)
        val hints = listOf(2)
        val expectedLine = listOf(OPEN, FILLED, OPEN, X)
        runApplyHintTest(line, hints, expectedLine)
        runNoFurtherChangesTest(line, hints)
    }

    @Test
    fun testApplyShrinkingBorderSingleHintBeforeEndingX() {
        val line = mutableListOf(OPEN, FILLED, OPEN, OPEN, X)
        val hints = listOf(2)
        val expectedLine = listOf(OPEN, FILLED, OPEN, X, X)
        runApplyHintTest(line, hints, expectedLine)
        runNoFurtherChangesTest(line, hints)
    }

    @Test
    fun testApplyShrinkingBorderSingleHintLargerContext() {
        val line = mutableListOf(OPEN, OPEN, FILLED, FILLED, OPEN, OPEN, OPEN, OPEN)
        val hints = listOf(3)
        val expectedLine = listOf(X, OPEN, FILLED, FILLED, OPEN, X, X, X)
        runApplyHintTest(line, hints, expectedLine)
        runNoFurtherChangesTest(line, hints)
    }

    @Test
    fun testApplyShrinkingBorderMultiHintEmptyStart() {
        val line = mutableListOf(OPEN, OPEN, FILLED, FILLED, OPEN, OPEN, OPEN, OPEN)
        val hints = listOf(3, 1)
        val expectedLine = listOf(X, OPEN, FILLED, FILLED, OPEN, OPEN, OPEN, OPEN)
        runApplyHintTest(line, hints, expectedLine)
        runNoFurtherChangesTest(line, hints)
    }

    @Test
    fun testApplyShrinkingBorderMultiHintAfterStartingX() {
        val line = mutableListOf(X, OPEN, OPEN, FILLED, FILLED, OPEN, OPEN, OPEN)
        val hints = listOf(3, 1)
        val expectedLine = listOf(X, X, OPEN, FILLED, FILLED, OPEN, OPEN, OPEN)
        runApplyHintTest(line, hints, expectedLine)
        runNoFurtherChangesTest(line, hints)
    }

    @Test
    fun testApplyShrinkingBorderMultiHintToEmptyEnd() {
        val line = mutableListOf(OPEN, OPEN, OPEN, OPEN, FILLED, FILLED, OPEN, OPEN)
        val hints = listOf(1, 3)
        val expectedLine = listOf(OPEN, OPEN, OPEN, OPEN, FILLED, FILLED, OPEN, X)
        runApplyHintTest(line, hints, expectedLine)
        runNoFurtherChangesTest(line, hints)
    }

    @Test
    fun testApplyShrinkingBorderMultiHintBeforeEndingX() {
        val line = mutableListOf(OPEN, OPEN, OPEN, FILLED, FILLED, OPEN, OPEN, X)
        val hints = listOf(1, 3)
        val expectedLine = listOf(OPEN, OPEN, OPEN, FILLED, FILLED, OPEN, X, X)
        runApplyHintTest(line, hints, expectedLine)
        runNoFurtherChangesTest(line, hints)
    }

    @Test
    fun testApplySpacedOutOverlap() {
        // 2 has to be in middle open section
        val line = mutableListOf(OPEN, OPEN, OPEN, X, OPEN, OPEN, OPEN, X, OPEN, OPEN, OPEN)
        val hints = listOf(1, 2, 1)
        val expectedLine = listOf(OPEN, OPEN, OPEN, X, OPEN, FILLED, OPEN, X, OPEN, OPEN, OPEN)
        runApplyHintTest(line, hints, expectedLine)
        runNoFurtherChangesTest(line, hints)
    }

    @Test
    fun testApplySpacedOutNoOverlapStart() {
        // 2 could be in first or second open section
        val line = mutableListOf(OPEN, OPEN, OPEN, X, OPEN, OPEN, OPEN, X, OPEN, OPEN, OPEN)
        val hints = listOf(2, 1, 1)
        runNoFurtherChangesTest(line, hints)
    }

    @Test
    fun testApplySpacedOutNoOverlapEnd() {
        // 2 could be in second or third open section
        val line = mutableListOf(OPEN, OPEN, OPEN, X, OPEN, OPEN, OPEN, X, OPEN, OPEN, OPEN)
        val hints = listOf(1, 1, 2)
        runNoFurtherChangesTest(line, hints)
    }

    @Test
    fun testApplySplitHintGroups() {
        // group of 3 is too large for any hint, so the open cell between the fills must be X
        val line = mutableListOf(OPEN, FILLED, OPEN, FILLED, OPEN, OPEN, OPEN, OPEN)
        val hints = listOf(1, 2, 1)
        val expectedLine = listOf(X, FILLED, X, FILLED, FILLED, X, OPEN, OPEN)
        runApplyHintTest(line, hints, expectedLine)
        runNoFurtherChangesTest(line, hints)
    }

    @Test
    fun testApplyThatHintGroupHasToBeAOne() {
        // filled cell must be the first or second 1 (but nothing else can be deduced)
        val line = mutableListOf(OPEN, OPEN, OPEN, OPEN, OPEN, FILLED, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN)
        val hints = listOf(2, 1, 1, 2)
        val expectedLine = listOf(OPEN, OPEN, OPEN, OPEN, X, FILLED, X, OPEN, OPEN, OPEN, OPEN ,OPEN)
        runApplyHintTest(line, hints, expectedLine)
        runNoFurtherChangesTest(line, hints)
    }

    // Note: There is an IntelliJ bug causing an erroneous error to appear when accessing internal members declared
    //       in a main source set from the corresponding test source set even though the gradle build and tests work
    //       (see https://youtrack.jetbrains.com/issue/KT-38842)

    private fun runApplyHintTest(line: MutableList<CellContents>, hints: List<Int>, expectedLine: List<CellContents>) {
        @Suppress("INVISIBLE_MEMBER")
        assertTrue(applyHintsToLine(line, hints, line.markCell))
        assertEquals(expectedLine, line)
    }

    private fun runNoFurtherChangesTest(line: MutableList<CellContents>, hints: List<Int>) {
        @Suppress("INVISIBLE_MEMBER")
        assertFalse(applyHintsToLine(line, hints, line.markCell))
    }
}

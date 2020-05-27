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

package com.askrepps.nonogram.internal

import kotlin.test.*

/**
 * Tests for [totalHintLength] and [hintsFitWithin].
 *
 * Note: The code under test assumes that hint values are valid w.r.t. all other
 *       validation performed during [PuzzleDefinition] initialization, so only
 *       valid hint values will be tested.
 */
class HintLengthTest {
    @Test
    fun testSingleHintLength() {
        assertEquals(0, performGetTotalHintLength(listOf(0)))
        assertEquals(1, performGetTotalHintLength(listOf(1)))
    }

    @Test
    fun testMultipleHintsLength() {
        assertEquals(3, performGetTotalHintLength(listOf(1, 1)))
        assertEquals(4, performGetTotalHintLength(listOf(2, 1)))
        assertEquals(5, performGetTotalHintLength(listOf(1, 3)))
        assertEquals(5, performGetTotalHintLength(listOf(1, 1, 1)))
        assertEquals(15, performGetTotalHintLength(listOf(1, 2, 5, 2, 1)))
    }

    @Test
    fun testSingleHintExactFit() {
        assertTrue(performGetHintsFitWithin(listOf(0), 0))
        assertTrue(performGetHintsFitWithin(listOf(1), 1))
        assertTrue(performGetHintsFitWithin(listOf(5), 5))
    }

    @Test
    fun testSingleHintSubsetFit() {
        assertTrue(performGetHintsFitWithin(listOf(0), 1))
        assertTrue(performGetHintsFitWithin(listOf(1), 3))
        assertTrue(performGetHintsFitWithin(listOf(5), 8))
    }

    @Test
    fun testSingleHintDoesNotFit() {
        assertFalse(performGetHintsFitWithin(listOf(1), 0))
        assertFalse(performGetHintsFitWithin(listOf(5), 4))
        assertFalse(performGetHintsFitWithin(listOf(5), 1))
    }

    @Test
    fun testMultipleHintsExactFit() {
        assertTrue(performGetHintsFitWithin(listOf(1, 1), 3))
        assertTrue(performGetHintsFitWithin(listOf(1, 2), 4))
        assertTrue(performGetHintsFitWithin(listOf(3, 1), 5))
        assertTrue(performGetHintsFitWithin(listOf(1, 1, 1), 5))
        assertTrue(performGetHintsFitWithin(listOf(1, 2, 5, 2, 1), 15))
    }

    @Test
    fun testMultipleHintsSubsetFit() {
        assertTrue(performGetHintsFitWithin(listOf(1, 1), 5))
        assertTrue(performGetHintsFitWithin(listOf(1, 2), 8))
        assertTrue(performGetHintsFitWithin(listOf(3, 1), 10))
        assertTrue(performGetHintsFitWithin(listOf(1, 1, 1), 10))
        assertTrue(performGetHintsFitWithin(listOf(1, 2, 5, 2, 1), 20))
    }

    @Test
    fun testMultipleHintsDoNotFit() {
        assertFalse(performGetHintsFitWithin(listOf(1, 1), 2))
        assertFalse(performGetHintsFitWithin(listOf(1, 2), 2))
        assertFalse(performGetHintsFitWithin(listOf(3, 1), 4))
        assertFalse(performGetHintsFitWithin(listOf(1, 1, 1), 3))
        assertFalse(performGetHintsFitWithin(listOf(1, 2, 5, 2, 1), 10))
    }

    @Test
    fun testNegativeLengthThrowsException() {
        assertFailsWith<IllegalArgumentException> { performGetHintsFitWithin(listOf(0), -1) }
        assertFailsWith<IllegalArgumentException> { performGetHintsFitWithin(listOf(1, 2), -42) }
    }

    // Note: There is an IntelliJ bug causing an erroneous error to appear when accessing internal members declared
    //       in a main source set from the corresponding test source set even though the gradle build and tests work
    //       (see https://youtrack.jetbrains.com/issue/KT-38842)

    private fun performGetTotalHintLength(hints: List<Int>) =
        @Suppress("INVISIBLE_MEMBER")
        hints.totalHintLength

    private fun performGetHintsFitWithin(hints: List<Int>, length: Int) =
        @Suppress("INVISIBLE_MEMBER")
        hints.hintsFitWithin(length)
}

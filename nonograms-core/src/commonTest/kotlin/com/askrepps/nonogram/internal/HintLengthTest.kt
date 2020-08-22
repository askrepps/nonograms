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
        assertEquals(0, listOf(0).totalHintLength)
        assertEquals(1, listOf(1).totalHintLength)
    }

    @Test
    fun testMultipleHintsLength() {
        assertEquals(3, listOf(1, 1).totalHintLength)
        assertEquals(4, listOf(2, 1).totalHintLength)
        assertEquals(5, listOf(1, 3).totalHintLength)
        assertEquals(5, listOf(1, 1, 1).totalHintLength)
        assertEquals(15, listOf(1, 2, 5, 2, 1).totalHintLength)
    }

    @Test
    fun testSingleHintExactFit() {
        assertTrue(listOf(0).hintsFitWithin(0))
        assertTrue(listOf(1).hintsFitWithin(1))
        assertTrue(listOf(5).hintsFitWithin(5))
    }

    @Test
    fun testSingleHintSubsetFit() {
        assertTrue(listOf(0).hintsFitWithin(1))
        assertTrue(listOf(1).hintsFitWithin(3))
        assertTrue(listOf(5).hintsFitWithin(8))
    }

    @Test
    fun testSingleHintDoesNotFit() {
        assertFalse(listOf(1).hintsFitWithin(0))
        assertFalse(listOf(5).hintsFitWithin(4))
        assertFalse(listOf(5).hintsFitWithin(1))
    }

    @Test
    fun testMultipleHintsExactFit() {
        assertTrue(listOf(1, 1).hintsFitWithin(3))
        assertTrue(listOf(1, 2).hintsFitWithin(4))
        assertTrue(listOf(3, 1).hintsFitWithin(5))
        assertTrue(listOf(1, 1, 1).hintsFitWithin(5))
        assertTrue(listOf(1, 2, 5, 2, 1).hintsFitWithin(15))
    }

    @Test
    fun testMultipleHintsSubsetFit() {
        assertTrue(listOf(1, 1).hintsFitWithin(5))
        assertTrue(listOf(1, 2).hintsFitWithin(8))
        assertTrue(listOf(3, 1).hintsFitWithin(10))
        assertTrue(listOf(1, 1, 1).hintsFitWithin(10))
        assertTrue(listOf(1, 2, 5, 2, 1).hintsFitWithin(20))
    }

    @Test
    fun testMultipleHintsDoNotFit() {
        assertFalse(listOf(1, 1).hintsFitWithin(2))
        assertFalse(listOf(1, 2).hintsFitWithin(2))
        assertFalse(listOf(3, 1).hintsFitWithin(4))
        assertFalse(listOf(1, 1, 1).hintsFitWithin(3))
        assertFalse(listOf(1, 2, 5, 2, 1).hintsFitWithin(10))
    }

    @Test
    fun testNegativeLengthThrowsException() {
        assertFailsWith<IllegalArgumentException> { listOf(0).hintsFitWithin(-1) }
        assertFailsWith<IllegalArgumentException> { listOf(1, 2).hintsFitWithin(-42) }
    }
}

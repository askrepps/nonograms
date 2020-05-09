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

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import org.junit.Test

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
        assertThat(listOf(0).totalHintLength, equalTo(0))
        assertThat(listOf(1).totalHintLength, equalTo(1))
    }

    @Test
    fun testMultipleHintsLength() {
        assertThat(listOf(1, 1).totalHintLength, equalTo(3))
        assertThat(listOf(2, 1).totalHintLength, equalTo(4))
        assertThat(listOf(1, 3).totalHintLength, equalTo(5))
        assertThat(listOf(1, 1, 1).totalHintLength, equalTo(5))
        assertThat(listOf(1, 2, 5, 2, 1).totalHintLength, equalTo(15))
    }

    @Test
    fun testSingleHintExactFit() {
        assertThat(listOf(0).hintsFitWithin(0), equalTo(true))
        assertThat(listOf(1).hintsFitWithin(1), equalTo(true))
        assertThat(listOf(5).hintsFitWithin(5), equalTo(true))
    }

    @Test
    fun testSingleHintSubsetFit() {
        assertThat(listOf(0).hintsFitWithin(1), equalTo(true))
        assertThat(listOf(1).hintsFitWithin(3), equalTo(true))
        assertThat(listOf(5).hintsFitWithin(8), equalTo(true))
    }

    @Test
    fun testSingleHintDoesNotFit() {
        assertThat(listOf(1).hintsFitWithin(0), equalTo(false))
        assertThat(listOf(5).hintsFitWithin(4), equalTo(false))
        assertThat(listOf(5).hintsFitWithin(1), equalTo(false))
    }

    @Test
    fun testMultipleHintsExactFit() {
        assertThat(listOf(1, 1).hintsFitWithin(3), equalTo(true))
        assertThat(listOf(1, 2).hintsFitWithin(4), equalTo(true))
        assertThat(listOf(3, 1).hintsFitWithin(5), equalTo(true))
        assertThat(listOf(1, 1, 1).hintsFitWithin(5), equalTo(true))
        assertThat(listOf(1, 2, 5, 2, 1).hintsFitWithin(15), equalTo(true))
    }

    @Test
    fun testMultipleHintsSubsetFit() {
        assertThat(listOf(1, 1).hintsFitWithin(5), equalTo(true))
        assertThat(listOf(1, 2).hintsFitWithin(8), equalTo(true))
        assertThat(listOf(3, 1).hintsFitWithin(10), equalTo(true))
        assertThat(listOf(1, 1, 1).hintsFitWithin(10), equalTo(true))
        assertThat(listOf(1, 2, 5, 2, 1).hintsFitWithin(20), equalTo(true))
    }

    @Test
    fun testMultipleHintsDoNotFit() {
        assertThat(listOf(1, 1).hintsFitWithin(2), equalTo(false))
        assertThat(listOf(1, 2).hintsFitWithin(2), equalTo(false))
        assertThat(listOf(3, 1).hintsFitWithin(4), equalTo(false))
        assertThat(listOf(1, 1, 1).hintsFitWithin(3), equalTo(false))
        assertThat(listOf(1, 2, 5, 2, 1).hintsFitWithin(10), equalTo(false))
    }

    @Test
    fun testNegativeLengthThrowsException() {
        assertThat({ listOf(0).hintsFitWithin(-1) }, throws<IllegalArgumentException>())
        assertThat({ listOf(1, 2).hintsFitWithin(-42) }, throws<IllegalArgumentException>())
    }
}

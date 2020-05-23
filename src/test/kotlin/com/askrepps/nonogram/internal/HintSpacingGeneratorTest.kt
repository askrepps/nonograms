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
import org.junit.Test

/**
 * Tests for [HintSpacingIterator].
 *
 * Note: The code under test assumes that hint values are valid w.r.t. the
 *       validation performed during [PuzzleDefinition] initialization, so
 *       only valid hints will be tested.
 */
class HintSpacingGeneratorTest {
    @Test
    fun testGenerateNoSpaceSequenceEmptyHint() {
        val lineSize = 5
        val hints = listOf(0)
        val expectedSequence = listOf(
            listOf(0)
        )
        runSequenceTest(hints, lineSize, expectedSequence)
    }

    @Test
    fun testGenerateNoSpaceSequenceFullHint() {
        val lineSize = 5
        val hints = listOf(5)
        val expectedSequence = listOf(
            listOf(0)
        )
        runSequenceTest(hints, lineSize, expectedSequence)
    }

    @Test
    fun testGenerateSingleSpaceSequenceSingleHint() {
        val lineSize = 10
        val hints = listOf(9)
        val expectedSequence = listOf(
            listOf(1),
            listOf(0)
        )
        runSequenceTest(hints, lineSize, expectedSequence)
    }

    @Test
    fun testGenerateSpaceSequenceSingleHint() {
        val lineSize = 10
        val hints = listOf(6)
        val expectedSequence = listOf(
            listOf(4),
            listOf(3),
            listOf(2),
            listOf(1),
            listOf(0)
        )
        runSequenceTest(hints, lineSize, expectedSequence)
    }

    @Test
    fun testGenerateSpaceSequenceExactTwoHint() {
        val lineSize = 10
        val hints = listOf(3, 6)
        val expectedSequence = listOf(
            listOf(0, 1)
        )
        runSequenceTest(hints, lineSize, expectedSequence)
    }

    @Test
    fun testGenerateSpaceSequenceExactFourHint() {
        val lineSize = 10
        val hints = listOf(1, 2, 2, 2)
        val expectedSequence = listOf(
            listOf(0, 1, 1, 1)
        )
        runSequenceTest(hints, lineSize, expectedSequence)
    }

    @Test
    fun testGenerateSpaceSequenceMultiOneHint() {
        val lineSize = 10
        val hints = listOf(1, 1, 1)
        val expectedSequence = listOf(
            listOf(5, 1, 1),
            listOf(4, 2, 1),
            listOf(3, 3, 1),
            listOf(2, 4, 1),
            listOf(1, 5, 1),
            listOf(0, 6, 1),
            listOf(0, 5, 2),
            listOf(0, 4, 3),
            listOf(0, 3, 4),
            listOf(0, 2, 5),
            listOf(0, 1, 6),
            listOf(0, 1, 5),
            listOf(0, 1, 4),
            listOf(0, 1, 3),
            listOf(0, 1, 2),
            listOf(0, 1, 1)
        )
        runSequenceTest(hints, lineSize, expectedSequence)
    }

    @Test
    fun testGenerateSpaceSequenceMultiSmallerHint() {
        val lineSize = 5
        val hints = listOf(1, 2)
        val expectedSequence = listOf(
            listOf(1, 1),
            listOf(0, 2),
            listOf(0, 1)
        )
        runSequenceTest(hints, lineSize, expectedSequence)
    }

    @Test
    fun testGenerateSpaceSequenceMultiLargerHint() {
        val lineSize = 15
        val hints = listOf(3, 1, 6)
        val expectedSequence = listOf(
            listOf(3, 1, 1),
            listOf(2, 2, 1),
            listOf(1, 3, 1),
            listOf(0, 4, 1),
            listOf(0, 3, 2),
            listOf(0, 2, 3),
            listOf(0, 1, 4),
            listOf(0, 1, 3),
            listOf(0, 1, 2),
            listOf(0, 1, 1)

        )
        runSequenceTest(hints, lineSize, expectedSequence)
    }

    private fun generateFullSequence(hints: List<Int>, lineSize: Int) =
        HintSpacingGenerator(hints, lineSize).toList()

    private fun runSequenceTest(hints: List<Int>, lineSize: Int, expectedSequence: List<List<Int>>) =
        assertThat(generateFullSequence(hints, lineSize), equalTo(expectedSequence))
}

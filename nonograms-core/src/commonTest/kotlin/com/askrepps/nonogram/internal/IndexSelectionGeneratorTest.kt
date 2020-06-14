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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Tests for [IndexSelectionGenerator].
 */
class IndexSelectionGeneratorTest {
    @Test
    fun testGenerateSelectionsOneSingleElementList() {
        val lengths = listOf(1)
        val expectedSequence = listOf(
            listOf(0)
        )
        runSequenceTest(lengths, expectedSequence)
    }

    @Test
    fun testGenerateSelectionsOneMultiElementList() {
        val lengths = listOf(3)
        val expectedSequence = listOf(
            listOf(0),
            listOf(1),
            listOf(2)
        )
        runSequenceTest(lengths, expectedSequence)
    }

    @Test
    fun testGenerateSelectionsMultipleSingleElementLists() {
        val lengths = listOf(1, 1, 1)
        val expectedSequence = listOf(
            listOf(0, 0, 0)
        )
        runSequenceTest(lengths, expectedSequence)
    }

    @Test
    fun testGenerateSelectionsMultipleMultiElementLists() {
        val lengths = listOf(1, 2, 3)
        val expectedSequence = listOf(
            listOf(0, 0, 0),
            listOf(0, 1, 0),
            listOf(0, 0, 1),
            listOf(0, 1, 1),
            listOf(0, 0, 2),
            listOf(0, 1, 2)
        )
        runSequenceTest(lengths, expectedSequence)
    }

    @Test
    fun testInvalidLengthsThrowsException() {
        assertFailsWith<IllegalArgumentException> { generateFullSequence(emptyList()) }
        assertFailsWith<IllegalArgumentException> { generateFullSequence(listOf(0)) }
        assertFailsWith<IllegalArgumentException> { generateFullSequence(listOf(-1)) }
        assertFailsWith<IllegalArgumentException> { generateFullSequence(listOf(1, 0)) }
        assertFailsWith<IllegalArgumentException> { generateFullSequence(listOf(0, 1)) }
        assertFailsWith<IllegalArgumentException> { generateFullSequence(listOf(-1, 0)) }
        assertFailsWith<IllegalArgumentException> { generateFullSequence(listOf(0, -1)) }
    }

    // Note: There is an IntelliJ bug causing an erroneous error to appear when accessing internal members declared
    //       in a main source set from the corresponding test source set even though the gradle build and tests work
    //       (see https://youtrack.jetbrains.com/issue/KT-38842)

    private fun generateFullSequence(lengths: List<Int>) =
        @Suppress("INVISIBLE_MEMBER")
        IndexSelectionGenerator(lengths).toList()

    private fun runSequenceTest(lengths: List<Int>, expectedSequence: List<List<Int>>) {
        assertEquals(expectedSequence, generateFullSequence(lengths))
    }
}

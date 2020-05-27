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

/**
 * Tests for [allNonnegative].
 */
class AllNonnegativeTest {
    @Test
    fun testEmptyListAllNonnegative() {
        runAllNonnegativeTest(emptyList(), true)
    }

    @Test
    fun testSingleZeroValueAllNonnegative() {
        runAllNonnegativeTest(listOf(0), true)
    }

    @Test
    fun testSinglePositiveValueAllNonnegative() {
        runAllNonnegativeTest(listOf(1), true)
        runAllNonnegativeTest(listOf(42), true)
    }

    @Test
    fun testMultipleNonnegativeValuesAllNonnegative() {
        runAllNonnegativeTest(listOf(1, 0, 7, 42), true)
    }

    @Test
    fun testSingleNegativeValueNotAllNonnegative() {
        runAllNonnegativeTest(listOf(-1), false)
        runAllNonnegativeTest(listOf(-7), false)
    }

    @Test
    fun testOneNegativeValueNotAllNonnegative() {
        runAllNonnegativeTest(listOf(-1, 0, 7, 42), false)
        runAllNonnegativeTest(listOf(1, 0, -7, 42), false)
        runAllNonnegativeTest(listOf(1, 0, 7, -42), false)
    }

    @Test
    fun testAllNegativeValuesNotAllNonnegative() {
        runAllNonnegativeTest(listOf(-1, -7, -42), false)
    }

    @Test
    fun testOtherIterableTypesAllNonnegative() {
        runAllNonnegativeTest(emptySet(), true)
        runAllNonnegativeTest(setOf(0), true)
        runAllNonnegativeTest(setOf(1, 0, 7, 42), true)

        runAllNonnegativeTest((0 until 0), true)
        runAllNonnegativeTest((0..0), true)
        runAllNonnegativeTest((0..10), true)
    }

    @Test
    fun testOtherIterableTypesNotAllNonnegative() {
        runAllNonnegativeTest(setOf(-1), false)
        runAllNonnegativeTest(setOf(1, 0, -7, 42), false)
        runAllNonnegativeTest(setOf(-1, -7, -42), false)

        runAllNonnegativeTest((-1..-1), false)
        runAllNonnegativeTest((-1..10), false)
        runAllNonnegativeTest((-10..-1), false)
    }

    // Note: There is an IntelliJ bug causing an erroneous error to appear when accessing internal members declared
    //       in a main source set from the corresponding test source set even though the gradle build and tests work
    //       (see https://youtrack.jetbrains.com/issue/KT-38842)

    @Suppress("INVISIBLE_MEMBER")
    private fun runAllNonnegativeTest(iterable: Iterable<Int>, expectedResult: Boolean) {
        assertEquals(expectedResult, iterable.allNonnegative)
    }
}

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
 * Tests for [anyIndexed].
 */
class AnyIndexedTest {
    @Test
    fun testEmptyListIsFalse() {
        val list = emptyList<Int>()
        val predicate = { _: Int, _: Int -> true }
        val expectedResult = false
        runAnyIndexedTest(list, predicate, expectedResult)
    }

    @Test
    fun testAllTrueIsTrue() {
        val list = listOf(1, 2, 3)
        val predicate = { _: Int, _: Int -> true }
        val expectedResult = true
        runAnyIndexedTest(list, predicate, expectedResult)
    }

    @Test
    fun testAllFalseIsFalse() {
        val list = listOf(1, 2, 3)
        val predicate = { _: Int, _: Int -> false }
        val expectedResult = false
        runAnyIndexedTest(list, predicate, expectedResult)
    }

    @Test
    fun testOneTrueIsTrue() {
        val list = listOf(1, 2, 3)
        val expectedResult = true
        for (e in list) {
            val predicate = { _: Int, x: Int -> x == e }
            runAnyIndexedTest(list, predicate, expectedResult)
        }
    }

    @Test
    fun testIndexMatters() {
        val list1 = listOf(1, 2, 3)
        val list2 = listOf(3, 2, 1)
        val predicate: (Int, Int) -> Boolean = { index, x -> x < index }
        runAnyIndexedTest(list1, predicate, expectedResult = false)
        runAnyIndexedTest(list2, predicate, expectedResult = true)
    }

    // Note: There is an IntelliJ bug causing an erroneous error to appear when accessing internal members declared
    //       in a main source set from the corresponding test source set even though the gradle build and tests work
    //       (see https://youtrack.jetbrains.com/issue/KT-38842)

    private fun <T> runAnyIndexedTest(
        collection: Collection<T>,
        predicate: (Int, T) -> Boolean,
        expectedResult: Boolean
    ) {
        @Suppress("INVISIBLE_MEMBER")
        assertEquals(expectedResult, collection.anyIndexed(predicate))
    }
}

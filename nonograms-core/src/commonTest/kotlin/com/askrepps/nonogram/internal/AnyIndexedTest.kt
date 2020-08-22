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
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Tests for [anyIndexed].
 */
class AnyIndexedTest {
    @Test
    fun testEmptyListIsFalse() {
        assertFalse(emptyList<Int>().anyIndexed { _: Int, _: Int -> true })
    }

    @Test
    fun testAllTrueIsTrue() {
        assertTrue(listOf(1, 2, 3).anyIndexed { _: Int, _: Int -> true })
    }

    @Test
    fun testAllFalseIsFalse() {
        assertFalse(listOf(1, 2, 3).anyIndexed { _: Int, _: Int -> false })
    }

    @Test
    fun testOneTrueIsTrue() {
        val list = listOf(1, 2, 3)
        for (e in list) {
            assertTrue(list.anyIndexed { _: Int, x: Int -> x == e })
        }
    }

    @Test
    fun testIndexMatters() {
        val list1 = listOf(1, 2, 3)
        val list2 = listOf(3, 2, 1)
        val predicate: (Int, Int) -> Boolean = { index, x -> x < index }
        assertFalse(list1.anyIndexed(predicate))
        assertTrue(list2.anyIndexed(predicate))
    }
}

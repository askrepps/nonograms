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
 * Tests for [allNonnegative].
 */
class AllNonnegativeTest {
    @Test
    fun testEmptyListAllNonnegative() {
        assertTrue(emptyList<Int>().allNonnegative)
    }

    @Test
    fun testSingleZeroValueAllNonnegative() {
        assertTrue(listOf(0).allNonnegative)
    }

    @Test
    fun testSinglePositiveValueAllNonnegative() {
        assertTrue(listOf(1).allNonnegative)
        assertTrue(listOf(42).allNonnegative)
    }

    @Test
    fun testMultipleNonnegativeValuesAllNonnegative() {
        assertTrue(listOf(1, 0, 7, 42).allNonnegative)
    }

    @Test
    fun testSingleNegativeValueNotAllNonnegative() {
        assertFalse(listOf(-1).allNonnegative)
        assertFalse(listOf(-7).allNonnegative)
    }

    @Test
    fun testOneNegativeValueNotAllNonnegative() {
        assertFalse(listOf(-1, 0, 7, 42).allNonnegative)
        assertFalse(listOf(1, 0, -7, 42).allNonnegative)
        assertFalse(listOf(1, 0, 7, -42).allNonnegative)
    }

    @Test
    fun testAllNegativeValuesNotAllNonnegative() {
        assertFalse(listOf(-1, -7, -42).allNonnegative)
    }

    @Test
    fun testOtherIterableTypesAllNonnegative() {
        assertTrue(emptySet<Int>().allNonnegative)
        assertTrue(setOf(0).allNonnegative)
        assertTrue(setOf(1, 0, 7, 42).allNonnegative)

        assertTrue((0 until 0).allNonnegative)
        assertTrue((0..0).allNonnegative)
        assertTrue((0..10).allNonnegative)
    }

    @Test
    fun testOtherIterableTypesNotAllNonnegative() {
        assertFalse(setOf(-1).allNonnegative)
        assertFalse(setOf(1, 0, -7, 42).allNonnegative)
        assertFalse(setOf(-1, -7, -42).allNonnegative)

        assertFalse((-1..-1).allNonnegative)
        assertFalse((-1..10).allNonnegative)
        assertFalse((-10..-1).allNonnegative)
    }
}

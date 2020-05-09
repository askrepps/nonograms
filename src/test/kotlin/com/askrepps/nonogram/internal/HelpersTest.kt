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

class HelpersTest {
    @Test
    fun testEmptyListAllNonnegative() {
        assertThat(emptyList<Int>().allNonnegative(), equalTo(true))
    }

    @Test
    fun testSingleZeroValueAllNonnegative() {
        assertThat(listOf(0).allNonnegative(), equalTo(true))
    }

    @Test
    fun testSinglePositiveValueAllNonnegative() {
        assertThat(listOf(1).allNonnegative(), equalTo(true))
        assertThat(listOf(42).allNonnegative(), equalTo(true))
    }

    @Test
    fun testMultipleNonnegativeValuesAllNonnegative() {
        assertThat(listOf(1, 0, 7, 42).allNonnegative(), equalTo(true))
    }

    @Test
    fun testSingleNegativeValueNotAllNonnegative() {
        assertThat(listOf(-1).allNonnegative(), equalTo(false))
        assertThat(listOf(-7).allNonnegative(), equalTo(false))
    }

    @Test
    fun testOneNegativeValueNotAllNonnegative() {
        assertThat(listOf(-1, 0, 7, 42).allNonnegative(), equalTo(false))
        assertThat(listOf(1, 0, -7, 42).allNonnegative(), equalTo(false))
        assertThat(listOf(1, 0, 7, -42).allNonnegative(), equalTo(false))
    }

    @Test
    fun testAllNegativeValuesNotAllNonnegative() {
        assertThat(listOf(-1, -7, -42).allNonnegative(), equalTo(false))
    }

    @Test
    fun testEmptyNestedListAllNonnegative() {
        assertThat(emptyList<List<Int>>().allNonnegative(), equalTo(true))
    }

    @Test
    fun testMultipleNonnegativeListsAllNonnegative() {
        assertThat(listOf(listOf(0), listOf(1, 2, 3)).allNonnegative(), equalTo(true))
    }

    @Test
    fun testOneNestedNegativeValueNotAllNonnegative() {
        assertThat(listOf(listOf(1, 2), listOf(3, -4)).allNonnegative(), equalTo(false))
        assertThat(listOf(listOf(-1, 2), listOf(3, 4)).allNonnegative(), equalTo(false))
    }

    @Test
    fun testAllNestedNegativeValuesNotAllNonnegative() {
        assertThat(listOf(listOf(-1, -2), listOf(-3, -4)).allNonnegative(), equalTo(false))
    }

    @Test
    fun testOtherIterableTypesAllNonnegative() {
        assertThat(emptySet<Int>().allNonnegative(), equalTo(true))
        assertThat(setOf(0).allNonnegative(), equalTo(true))
        assertThat(setOf(1, 0, 7, 42).allNonnegative(), equalTo(true))

        assertThat((0 until 0).allNonnegative(), equalTo(true))
        assertThat((0..0).allNonnegative(), equalTo(true))
        assertThat((0..10).allNonnegative(), equalTo(true))
    }

    @Test
    fun testOtherIterableTypesNotAllNonnegative() {
        assertThat(setOf(-1).allNonnegative(), equalTo(false))
        assertThat(setOf(1, 0, -7, 42).allNonnegative(), equalTo(false))
        assertThat(setOf(-1, -7, -42).allNonnegative(), equalTo(false))

        assertThat((-1..-1).allNonnegative(), equalTo(false))
        assertThat((-1..10).allNonnegative(), equalTo(false))
        assertThat((-10..-1).allNonnegative(), equalTo(false))
    }

    @Test
    fun testOtherNestedIterableTypesAllNonnegative() {
        assertThat(setOf(setOf(0), setOf(1, 2, 3)).allNonnegative(), equalTo(true))
        assertThat(listOf(setOf(1, 2, 3), listOf(4, 5, 6)).allNonnegative(), equalTo(true))
    }

    @Test
    fun testOtherNestedIterableTypesNotAllNonnegative() {
        assertThat(listOf(setOf(1, 2), listOf(3, -4)).allNonnegative(), equalTo(false))
        assertThat(setOf(listOf(-1, 2), setOf(3, 4)).allNonnegative(), equalTo(false))
        assertThat(listOf(setOf(-1, -2), setOf(-3, -4)).allNonnegative(), equalTo(false))
    }
}

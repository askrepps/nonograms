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

class ListIsSortedTest {
    @Test
    fun testEmptyListIsSorted() {
        assertThat(emptyList<Int>().isSortedAscending(), equalTo(true))
        assertThat(emptyList<Int>().isSortedDescending(), equalTo(true))
    }

    @Test
    fun testSingleElementListIsSorted() {
        assertThat(listOf(1).isSortedAscending(), equalTo(true))
        assertThat(listOf(-42).isSortedDescending(), equalTo(true))
    }

    @Test
    fun testDuplicatedElementListIsSorted() {
        assertThat(listOf(-42, -42).isSortedAscending(), equalTo(true))
        assertThat(listOf(1, 1).isSortedDescending(), equalTo(true))
    }

    @Test
    fun testAscendingListIsSortedAscending() {
        assertThat(listOf(1, 2, 3).isSortedAscending(), equalTo(true))
        assertThat(listOf(7, 13, 18, 89, 183).isSortedAscending(), equalTo(true))
        assertThat(listOf(1, 2, 2, 3, 3).isSortedAscending(), equalTo(true))
    }

    @Test
    fun testDescendingListIsSortedDescending() {
        assertThat(listOf(3, 2, 1).isSortedDescending(), equalTo(true))
        assertThat(listOf(183, 89, 18, 13, 7).isSortedDescending(), equalTo(true))
        assertThat(listOf(3, 3, 2, 2, 1).isSortedDescending(), equalTo(true))
    }

    @Test
    fun testDescendingListNotSortedAscending() {
        assertThat(listOf(3, 2, 1).isSortedAscending(), equalTo(false))
        assertThat(listOf(183, 89, 18, 13, 7).isSortedAscending(), equalTo(false))
        assertThat(listOf(3, 3, 2, 2, 1).isSortedAscending(), equalTo(false))
    }

    @Test
    fun testAscendingListNotSortedDescending() {
        assertThat(listOf(1, 2, 3).isSortedAscending(), equalTo(true))
        assertThat(listOf(7, 13, 18, 89, 183).isSortedAscending(), equalTo(true))
        assertThat(listOf(1, 2, 2, 3, 3).isSortedAscending(), equalTo(true))
    }
}

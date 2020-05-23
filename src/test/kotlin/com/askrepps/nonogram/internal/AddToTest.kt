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
 * Tests for [addTo].
 */
class AddToTest {
    @Test
    fun testAddEmptyArrays() {
        val a1 = intArrayOf()
        val a2 = intArrayOf()
        a1.addTo(a2)
        assertThat(a1.toList(), equalTo(emptyList()))
        assertThat(a2.toList(), equalTo(emptyList()))
    }

    @Test
    fun testAddArrays() {
        val a1 = intArrayOf(1, 7, -4, 5, 8)
        val a2 = intArrayOf(0, 6, 2, -42, 999)
        a1.addTo(a2)
        assertThat(a1.toList(), equalTo(listOf(1, 7, -4, 5, 8)))
        assertThat(a2.toList(), equalTo(listOf(1, 13, -2, -37, 1007)))
    }

    @Test
    fun testMismatchedArraySizesThrowsException() {
        val a1 = intArrayOf(1, 7, -4, 5, 8)
        val a2 = intArrayOf(0, 6, 2, -42)
        assertThat({ a1.addTo(a2) }, throws<IllegalArgumentException>())
    }
}

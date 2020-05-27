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
 * Tests for [addTo].
 */
class AddToTest {
    @Test
    fun testAddEmptyArrays() {
        val a1 = intArrayOf()
        val a2 = intArrayOf()
        performAddTo(a1, a2)
        assertEquals(emptyList(), a1.toList())
        assertEquals(emptyList(), a2.toList())
    }

    @Test
    fun testAddArrays() {
        val a1 = intArrayOf(1, 7, -4, 5, 8)
        val a2 = intArrayOf(0, 6, 2, -42, 999)
        performAddTo(a1, a2)
        assertEquals(listOf(1, 7, -4, 5, 8), a1.toList())
        assertEquals(listOf(1, 13, -2, -37, 1007), a2.toList())
    }

    @Test
    fun testMismatchedArraySizesThrowsException() {
        val a1 = intArrayOf(1, 7, -4, 5, 8)
        val a2 = intArrayOf(0, 6, 2, -42)
        assertFailsWith<IllegalArgumentException> { performAddTo(a1, a2) }
    }

    // Note: There is an IntelliJ bug causing an erroneous error to appear when accessing internal members declared
    //       in a main source set from the corresponding test source set even though the gradle build and tests work
    //       (see https://youtrack.jetbrains.com/issue/KT-38842)

    private fun performAddTo(a1: IntArray, a2: IntArray) {
        @Suppress("INVISIBLE_MEMBER")
        a1.addTo(a2)
    }
}

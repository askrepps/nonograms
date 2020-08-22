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
 * Tests for [anyZeroStandsAlone].
 */
class AnyZeroStandsAloneTest {
    @Test
    fun testZeroStandsAloneIfNoZero() {
        assertTrue(emptyList<Int>().anyZeroStandsAlone)
        assertTrue(listOf(1).anyZeroStandsAlone)
        assertTrue(listOf(1, 2, 3).anyZeroStandsAlone)
    }

    @Test
    fun testZeroStandsAloneIfOneZero() {
        assertTrue(listOf(0).anyZeroStandsAlone)
    }

    @Test
    fun testZeroDoesNotStandAloneIfMultipleZeros() {
        assertFalse(listOf(0, 0).anyZeroStandsAlone)
        assertFalse(listOf(0, 0, 0).anyZeroStandsAlone)
    }

    @Test
    fun testZeroDoesNotStandAloneIfZeroAndOtherElements() {
        assertFalse(listOf(0, 1).anyZeroStandsAlone)
        assertFalse(listOf(1, 0).anyZeroStandsAlone)
        assertFalse(listOf(-1, 0).anyZeroStandsAlone)
        assertFalse(listOf(0, -1).anyZeroStandsAlone)
        assertFalse(listOf(0, -1, 2).anyZeroStandsAlone)
        assertFalse(listOf(-1, 0, 2).anyZeroStandsAlone)
        assertFalse(listOf(-1, 2, 0).anyZeroStandsAlone)
    }

    @Test
    fun testZeroStandsAloneInOtherCollectionTypes() {
        assertTrue(emptySet<Int>().anyZeroStandsAlone)
        assertTrue(setOf(1).anyZeroStandsAlone)
        assertTrue(setOf(1, 2, 3).anyZeroStandsAlone)
        assertTrue(setOf(0).anyZeroStandsAlone)
        assertTrue(setOf(0, 0).anyZeroStandsAlone)
        assertTrue(setOf(0, 0, 0).anyZeroStandsAlone)
    }

    @Test
    fun testZeroDoesNotStandAloneInOtherCollectionTypes() {
        assertFalse(setOf(0, 1).anyZeroStandsAlone)
        assertFalse(setOf(1, 0).anyZeroStandsAlone)
        assertFalse(setOf(-1, 0).anyZeroStandsAlone)
        assertFalse(setOf(0, -1).anyZeroStandsAlone)
        assertFalse(setOf(0, -1, 2).anyZeroStandsAlone)
        assertFalse(setOf(-1, 0, 2).anyZeroStandsAlone)
        assertFalse(setOf(-1, 2, 0).anyZeroStandsAlone)
    }
}

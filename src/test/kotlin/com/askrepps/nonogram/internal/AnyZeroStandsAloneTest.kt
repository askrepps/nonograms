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

/**
 * Tests for [anyZeroStandsAlone].
 */
class AnyZeroStandsAloneTest {
    @Test
    fun testZeroStandsAloneIfNoZero() {
        assertThat(emptyList<Int>().anyZeroStandsAlone, equalTo(true))
        assertThat(listOf(1).anyZeroStandsAlone, equalTo(true))
        assertThat(listOf(1, 2, 3).anyZeroStandsAlone, equalTo(true))
    }

    @Test
    fun testZeroStandsAloneIfOneZero() {
        assertThat(listOf(0).anyZeroStandsAlone, equalTo(true))
    }

    @Test
    fun testZeroDoesNotStandAloneIfMultipleZeros() {
        assertThat(listOf(0, 0).anyZeroStandsAlone, equalTo(false))
        assertThat(listOf(0, 0, 0).anyZeroStandsAlone, equalTo(false))
    }

    @Test
    fun testZeroDoesNotStandAloneIfZeroAndOtherElements() {
        assertThat(listOf(0, 1).anyZeroStandsAlone, equalTo(false))
        assertThat(listOf(1, 0).anyZeroStandsAlone, equalTo(false))
        assertThat(listOf(-1, 0).anyZeroStandsAlone, equalTo(false))
        assertThat(listOf(0, -1).anyZeroStandsAlone, equalTo(false))
        assertThat(listOf(0, -1, 2).anyZeroStandsAlone, equalTo(false))
        assertThat(listOf(-1, 0, 2).anyZeroStandsAlone, equalTo(false))
        assertThat(listOf(-1, 2, 0).anyZeroStandsAlone, equalTo(false))
    }

    @Test
    fun testZeroStandsAloneInOtherCollectionTypes() {
        assertThat(emptySet<Int>().anyZeroStandsAlone, equalTo(true))
        assertThat(setOf(1).anyZeroStandsAlone, equalTo(true))
        assertThat(setOf(1, 2, 3).anyZeroStandsAlone, equalTo(true))
        assertThat(setOf(0).anyZeroStandsAlone, equalTo(true))
        assertThat(setOf(0, 0).anyZeroStandsAlone, equalTo(true))
        assertThat(setOf(0, 0, 0).anyZeroStandsAlone, equalTo(true))
    }

    @Test
    fun testZeroDoesNotStandAloneInOtherCollectionTypes() {
        assertThat(setOf(0, 1).anyZeroStandsAlone, equalTo(false))
        assertThat(setOf(1, 0).anyZeroStandsAlone, equalTo(false))
        assertThat(setOf(-1, 0).anyZeroStandsAlone, equalTo(false))
        assertThat(setOf(0, -1).anyZeroStandsAlone, equalTo(false))
        assertThat(setOf(0, -1, 2).anyZeroStandsAlone, equalTo(false))
        assertThat(setOf(-1, 0, 2).anyZeroStandsAlone, equalTo(false))
        assertThat(setOf(-1, 2, 0).anyZeroStandsAlone, equalTo(false))
    }
}

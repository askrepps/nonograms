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
 * Tests for [anyZeroStandsAlone].
 */
class AnyZeroStandsAloneTest {
    @Test
    fun testZeroStandsAloneIfNoZero() {
        runAnyZeroStandsAloneTest(emptyList(), true)
        runAnyZeroStandsAloneTest(listOf(1), true)
        runAnyZeroStandsAloneTest(listOf(1, 2, 3), true)
    }

    @Test
    fun testZeroStandsAloneIfOneZero() {
        runAnyZeroStandsAloneTest(listOf(0), true)
    }

    @Test
    fun testZeroDoesNotStandAloneIfMultipleZeros() {
        runAnyZeroStandsAloneTest(listOf(0, 0), false)
        runAnyZeroStandsAloneTest(listOf(0, 0, 0), false)
    }

    @Test
    fun testZeroDoesNotStandAloneIfZeroAndOtherElements() {
        runAnyZeroStandsAloneTest(listOf(0, 1), false)
        runAnyZeroStandsAloneTest(listOf(1, 0), false)
        runAnyZeroStandsAloneTest(listOf(-1, 0), false)
        runAnyZeroStandsAloneTest(listOf(0, -1), false)
        runAnyZeroStandsAloneTest(listOf(0, -1, 2), false)
        runAnyZeroStandsAloneTest(listOf(-1, 0, 2), false)
        runAnyZeroStandsAloneTest(listOf(-1, 2, 0), false)
    }

    @Test
    fun testZeroStandsAloneInOtherCollectionTypes() {
        runAnyZeroStandsAloneTest(emptySet(), true)
        runAnyZeroStandsAloneTest(setOf(1), true)
        runAnyZeroStandsAloneTest(setOf(1, 2, 3), true)
        runAnyZeroStandsAloneTest(setOf(0), true)
        runAnyZeroStandsAloneTest(setOf(0, 0), true)
        runAnyZeroStandsAloneTest(setOf(0, 0, 0), true)
    }

    @Test
    fun testZeroDoesNotStandAloneInOtherCollectionTypes() {
        runAnyZeroStandsAloneTest(setOf(0, 1), false)
        runAnyZeroStandsAloneTest(setOf(1, 0), false)
        runAnyZeroStandsAloneTest(setOf(-1, 0), false)
        runAnyZeroStandsAloneTest(setOf(0, -1), false)
        runAnyZeroStandsAloneTest(setOf(0, -1, 2), false)
        runAnyZeroStandsAloneTest(setOf(-1, 0, 2), false)
        runAnyZeroStandsAloneTest(setOf(-1, 2, 0), false)
    }

    // Note: There is an IntelliJ bug causing an erroneous error to appear when accessing internal members declared
    //       in a main source set from the corresponding test source set even though the gradle build and tests work
    //       (see https://youtrack.jetbrains.com/issue/KT-38842)

    private fun runAnyZeroStandsAloneTest(collection: Collection<Int>, expectedResult: Boolean) {
        @Suppress("INVISIBLE_MEMBER")
        assertEquals(expectedResult, collection.anyZeroStandsAlone)
    }
}

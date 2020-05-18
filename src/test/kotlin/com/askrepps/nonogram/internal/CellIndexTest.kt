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
 * Tests for [getCellIndex].
 *
 * Note: The code under test assumes that row and column values are valid
 *       w.r.t. valid puzzle dimensions and consistent with the row size.
 */
class CellIndexTest {
    @Test
    fun testCellIndex() {
        assertThat(getCellIndex(0, 0, 1), equalTo(0))
        assertThat(getCellIndex(0, 0, 4), equalTo(0))
        assertThat(getCellIndex(0, 1, 4), equalTo(1))
        assertThat(getCellIndex(1, 0, 4), equalTo(4))
        assertThat(getCellIndex(0, 2, 4), equalTo(2))
        assertThat(getCellIndex(2, 0, 4), equalTo(8))
        assertThat(getCellIndex(2, 1, 4), equalTo(9))
        assertThat(getCellIndex(2, 3, 5), equalTo(13))
    }
}

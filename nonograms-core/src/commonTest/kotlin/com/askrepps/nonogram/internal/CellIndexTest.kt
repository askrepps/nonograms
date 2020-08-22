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
 * Tests for [getCellIndex].
 *
 * Note: The code under test assumes that row and column values are valid
 *       w.r.t. valid puzzle dimensions and consistent with the row size.
 */
class CellIndexTest {
    @Test
    fun testCellIndex() {
        assertEquals(0, getCellIndex(0, 0, 1))
        assertEquals(0, getCellIndex(0, 0, 4))
        assertEquals(1, getCellIndex(0, 1, 4))
        assertEquals(4, getCellIndex(1, 0, 4))
        assertEquals(2, getCellIndex(0, 2, 4))
        assertEquals(8, getCellIndex(2, 0, 4))
        assertEquals(9, getCellIndex(2, 1, 4))
        assertEquals(13, getCellIndex(2, 3, 5))
    }
}

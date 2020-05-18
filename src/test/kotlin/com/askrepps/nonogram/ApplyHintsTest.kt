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

package com.askrepps.nonogram

import com.askrepps.nonogram.internal.markCell
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

/**
 * Tests for [applyHints] and supporting functions.
 */
class ApplyHintsTest {
    @Test
    fun testApplyEmptyHint() {
        val line = mutableListOf(CellContents.OPEN, CellContents.OPEN, CellContents.OPEN)
        val hints = listOf(0)
        assertThat(applyHintsToLine(line, hints, line.markCell), equalTo(true))
        assertThat(line, equalTo(listOf(CellContents.X, CellContents.X, CellContents.X)))
        assertThat(applyHintsToLine(line, hints, line.markCell), equalTo(false))

        line[1] = CellContents.OPEN
        assertThat(applyHintsToLine(line, hints, line.markCell), equalTo(true))
        assertThat(line, equalTo(listOf(CellContents.X, CellContents.X, CellContents.X)))
    }

    @Test
    fun testApplyFillHint() {
        val line = mutableListOf(CellContents.OPEN, CellContents.OPEN, CellContents.OPEN)
        val hints = listOf(3)
        assertThat(applyHintsToLine(line, hints, line.markCell), equalTo(true))
        assertThat(line, equalTo(listOf(CellContents.FILLED, CellContents.FILLED, CellContents.FILLED)))
        assertThat(applyHintsToLine(line, hints, line.markCell), equalTo(false))

        line[1] = CellContents.OPEN
        assertThat(applyHintsToLine(line, hints, line.markCell), equalTo(true))
        assertThat(line, equalTo(listOf(CellContents.FILLED, CellContents.FILLED, CellContents.FILLED)))
    }
}

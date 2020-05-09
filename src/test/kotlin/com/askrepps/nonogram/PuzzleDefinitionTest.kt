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

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.throws
import org.junit.Test

class PuzzleDefinitionTest {
    @Test
    fun testValidDimensionsDoNotThrowExceptions() {
        PuzzleDefinition(1, 1)
        PuzzleDefinition(1, 5)
        PuzzleDefinition(5, 1)
        PuzzleDefinition(5, 5)
        PuzzleDefinition(10, 10)
        PuzzleDefinition(15, 15)
    }

    @Test
    fun testInvalidDimensionsThrowExceptions() {
        assertThat({ PuzzleDefinition(0, 1) }, throws<IllegalArgumentException>())
        assertThat({ PuzzleDefinition(1, 0) }, throws<IllegalArgumentException>())
        assertThat({ PuzzleDefinition(0, 0) }, throws<IllegalArgumentException>())
        assertThat({ PuzzleDefinition(1, -1) }, throws<IllegalArgumentException>())
        assertThat({ PuzzleDefinition(-1, 1) }, throws<IllegalArgumentException>())
        assertThat({ PuzzleDefinition(-1, -1) }, throws<IllegalArgumentException>())
        assertThat({ PuzzleDefinition(-5, -5) }, throws<IllegalArgumentException>())
        assertThat({ PuzzleDefinition(-10, -10) }, throws<IllegalArgumentException>())
        assertThat({ PuzzleDefinition(-15, -15) }, throws<IllegalArgumentException>())
    }
}

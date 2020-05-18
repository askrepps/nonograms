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

import com.askrepps.nonogram.internal.createEmptyPuzzleDefinitionWithDimensions
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import org.junit.Test

/**
 * Tests for [PuzzleDefinition].
 */
class PuzzleDefinitionTest {
    @Test
    fun testPuzzleDimensions() {
        val puzzle = createEmptyPuzzleDefinitionWithDimensions(3, 4)
        assertThat(puzzle.rows, equalTo(3))
        assertThat(puzzle.columns, equalTo(4))
        assertThat(puzzle.rowIndices, equalTo(0 until 3))
        assertThat(puzzle.columnIndices, equalTo(0 until 4))
    }

    @Test
    fun testValidDimensionsDoNotThrowExceptions() {
        createEmptyPuzzleDefinitionWithDimensions(1, 1)
        createEmptyPuzzleDefinitionWithDimensions(1, 5)
        createEmptyPuzzleDefinitionWithDimensions(5, 1)
        createEmptyPuzzleDefinitionWithDimensions(5, 5)
        createEmptyPuzzleDefinitionWithDimensions(10, 10)
        createEmptyPuzzleDefinitionWithDimensions(15, 15)
    }

    @Test
    fun testInvalidDimensionsThrowExceptions() {
        assertThat({ createEmptyPuzzleDefinitionWithDimensions(0, 1) }, throws<IllegalArgumentException>())
        assertThat({ createEmptyPuzzleDefinitionWithDimensions(1, 0) }, throws<IllegalArgumentException>())
        assertThat({ createEmptyPuzzleDefinitionWithDimensions(0, 0) }, throws<IllegalArgumentException>())
        assertThat({ createEmptyPuzzleDefinitionWithDimensions(1, -1) }, throws<IllegalArgumentException>())
        assertThat({ createEmptyPuzzleDefinitionWithDimensions(-1, 1) }, throws<IllegalArgumentException>())
        assertThat({ createEmptyPuzzleDefinitionWithDimensions(-1, -1) }, throws<IllegalArgumentException>())
        assertThat({ createEmptyPuzzleDefinitionWithDimensions(-5, -5) }, throws<IllegalArgumentException>())
        assertThat({ createEmptyPuzzleDefinitionWithDimensions(-10, -10) }, throws<IllegalArgumentException>())
        assertThat({ createEmptyPuzzleDefinitionWithDimensions(-15, -15) }, throws<IllegalArgumentException>())
    }

    @Test
    fun testValidHintsDoNotThrowExceptions() {
        PuzzleDefinition(1, 1, listOf(listOf(1)), listOf(listOf(1)))
        PuzzleDefinition(2, 2, listOf(listOf(2), listOf(1)), listOf(listOf(1), listOf(2)))
        PuzzleDefinition(
            5,
            5,
            listOf(
                listOf(5),
                listOf(3),
                listOf(3, 1),
                listOf(4),
                listOf(0)
            ),
            listOf(
                listOf(1, 1),
                listOf(4),
                listOf(4),
                listOf(2, 1),
                listOf(1, 2)
            )
        )
    }

    @Test
    fun testInvalidHintDimensionsThrowsException() {
        assertThat(
            { PuzzleDefinition(1, 1, emptyList(), emptyList()) },
            throws<IllegalArgumentException>()
        )
        assertThat(
            { PuzzleDefinition(1, 1, listOf(listOf(1)), emptyList()) },
            throws<IllegalArgumentException>()
        )
        assertThat(
            { PuzzleDefinition(1, 1, listOf(emptyList()), listOf(listOf(1))) },
            throws<IllegalArgumentException>()
        )
        assertThat(
            { PuzzleDefinition(1, 1, listOf(listOf(1)), listOf(emptyList())) },
            throws<IllegalArgumentException>()
        )
        assertThat(
            { PuzzleDefinition(1, 1, listOf(emptyList()), listOf(emptyList())) },
            throws<IllegalArgumentException>()
        )
        assertThat(
            { PuzzleDefinition(1, 1, listOf(listOf(1), listOf(1)), listOf(listOf(1))) },
            throws<IllegalArgumentException>()
        )
        assertThat(
            { PuzzleDefinition(1, 1, listOf(listOf(1)), listOf(listOf(1), listOf(1))) },
            throws<IllegalArgumentException>()
        )
        assertThat(
            { PuzzleDefinition(1, 1, listOf(listOf(1), listOf(1)), listOf(listOf(1), listOf(1))) },
            throws<IllegalArgumentException>()
        )
    }

    @Test
    fun testNegativeHintValuesThrowsException() {
        assertThat(
            { PuzzleDefinition(2, 2, listOf(listOf(2), listOf(-1)), listOf(listOf(0), listOf(1))) },
            throws<IllegalArgumentException>()
        )
        assertThat(
            { PuzzleDefinition(2, 2, listOf(listOf(0), listOf(1)), listOf(listOf(2), listOf(-1))) },
            throws<IllegalArgumentException>()
        )
        assertThat(
            { PuzzleDefinition(2, 2, listOf(listOf(2), listOf(-1, 1, -1)), listOf(listOf(0), listOf(1))) },
            throws<IllegalArgumentException>()
        )
        assertThat(
            { PuzzleDefinition(2, 2, listOf(listOf(0), listOf(1)), listOf(listOf(2), listOf(-1, 1, -1))) },
            throws<IllegalArgumentException>()
        )
    }

    @Test
    fun testHintValueZeroBesideOtherValueThrowsException() {
        assertThat(
            { PuzzleDefinition(2, 2, listOf(listOf(1, 0), listOf(0)), listOf(listOf(1), listOf(0))) },
            throws<IllegalArgumentException>()
        )
        assertThat(
            { PuzzleDefinition(2, 2, listOf(listOf(1), listOf(0)), listOf(listOf(0, 1), listOf(0))) },
            throws<IllegalArgumentException>()
        )
    }

    @Test
    fun testInconsistentHintsThrowsException() {
        assertThat(
            { PuzzleDefinition(2, 2, listOf(listOf(1), listOf(1)), listOf(listOf(2), listOf(2))) },
            throws<IllegalArgumentException>()
        )
        assertThat(
            { PuzzleDefinition(2, 2, listOf(listOf(2), listOf(2)), listOf(listOf(1), listOf(1))) },
            throws<IllegalArgumentException>()
        )
    }

    @Test
    fun testIllFittingHintsThrowsException() {
        assertThat(
            { PuzzleDefinition(2, 2, listOf(listOf(3), listOf(0)), listOf(listOf(2), listOf(1))) },
            throws<IllegalArgumentException>()
        )
        assertThat(
            { PuzzleDefinition(2, 2, listOf(listOf(2), listOf(0)), listOf(listOf(1, 1), listOf(0))) },
            throws<IllegalArgumentException>()
        )
        assertThat(
            { PuzzleDefinition(3, 2, listOf(listOf(3), listOf(0), listOf(0)), listOf(listOf(2), listOf(1))) },
            throws<IllegalArgumentException>()
        )
        assertThat(
            { PuzzleDefinition(2, 3, listOf(listOf(2), listOf(1)), listOf(listOf(3), listOf(0), listOf(0))) },
            throws<IllegalArgumentException>()
        )
    }
}

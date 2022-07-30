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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Tests for [PuzzleDefinition].
 */
class PuzzleDefinitionTest {
    @Test
    fun testPuzzleDimensions() {
        val puzzle = createEmptyPuzzleDefinitionWithDimensions(3, 4)
        assertEquals(3, puzzle.rows)
        assertEquals(4, puzzle.columns)
        assertEquals(0 until 3, puzzle.rowIndices)
        assertEquals(0 until 4, puzzle.columnIndices)
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
        assertFailsWith<IllegalArgumentException> { createEmptyPuzzleDefinitionWithDimensions(0, 1) }
        assertFailsWith<IllegalArgumentException> { createEmptyPuzzleDefinitionWithDimensions(1, 0) }
        assertFailsWith<IllegalArgumentException> { createEmptyPuzzleDefinitionWithDimensions(0, 0) }
        assertFailsWith<IllegalArgumentException> { createEmptyPuzzleDefinitionWithDimensions(1, -1) }
        assertFailsWith<IllegalArgumentException> { createEmptyPuzzleDefinitionWithDimensions(-1, 1) }
        assertFailsWith<IllegalArgumentException> { createEmptyPuzzleDefinitionWithDimensions(-1, -1) }
        assertFailsWith<IllegalArgumentException> { createEmptyPuzzleDefinitionWithDimensions(-5, -5) }
        assertFailsWith<IllegalArgumentException> { createEmptyPuzzleDefinitionWithDimensions(-10, -10) }
        assertFailsWith<IllegalArgumentException> { createEmptyPuzzleDefinitionWithDimensions(-15, -15) }
    }

    @Test
    fun testValidHintsDoNotThrowExceptions() {
        PuzzleDefinition(listOf(listOf(1)), listOf(listOf(1)))
        PuzzleDefinition(listOf(listOf(2), listOf(1)), listOf(listOf(1), listOf(2)))
        PuzzleDefinition(
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
        assertFailsWith<IllegalArgumentException> {
            PuzzleDefinition(1, 1, emptyList(), emptyList())
        }
        assertFailsWith<IllegalArgumentException> {
            PuzzleDefinition(1, 1, listOf(listOf(1)), emptyList())
        }
        assertFailsWith<IllegalArgumentException> {
            PuzzleDefinition(1, 1, listOf(emptyList()), listOf(listOf(1)))
        }
        assertFailsWith<IllegalArgumentException> {
            PuzzleDefinition(1, 1, listOf(listOf(1)), listOf(emptyList()))
        }
        assertFailsWith<IllegalArgumentException> {
            PuzzleDefinition(1, 1, listOf(emptyList()), listOf(emptyList()))
        }
        assertFailsWith<IllegalArgumentException> {
            PuzzleDefinition(1, 1, listOf(listOf(1), listOf(1)), listOf(listOf(1)))
        }
        assertFailsWith<IllegalArgumentException> {
            PuzzleDefinition(1, 1, listOf(listOf(1)), listOf(listOf(1), listOf(1)))
        }
        assertFailsWith<IllegalArgumentException> {
            PuzzleDefinition(1, 1, listOf(listOf(1), listOf(1)), listOf(listOf(1), listOf(1)))
        }
    }

    @Test
    fun testNegativeHintValuesThrowsException() {
        assertFailsWith<IllegalArgumentException> {
            PuzzleDefinition(listOf(listOf(2), listOf(-1)), listOf(listOf(0), listOf(1)))
        }
        assertFailsWith<IllegalArgumentException> {
            PuzzleDefinition(listOf(listOf(0), listOf(1)), listOf(listOf(2), listOf(-1)))
        }
        assertFailsWith<IllegalArgumentException> {
            PuzzleDefinition(listOf(listOf(2), listOf(-1, 1, -1)), listOf(listOf(0), listOf(1)))
        }
        assertFailsWith<IllegalArgumentException> {
            PuzzleDefinition(listOf(listOf(0), listOf(1)), listOf(listOf(2), listOf(-1, 1, -1)))
        }
    }

    @Test
    fun testHintValueZeroBesideOtherValueThrowsException() {
        assertFailsWith<IllegalArgumentException> {
            PuzzleDefinition(listOf(listOf(1, 0), listOf(0)), listOf(listOf(1), listOf(0)))
        }
        assertFailsWith<IllegalArgumentException> {
            PuzzleDefinition(listOf(listOf(1), listOf(0)), listOf(listOf(0, 1), listOf(0)))
        }
    }

    @Test
    fun testInconsistentHintsThrowsException() {
        assertFailsWith<IllegalArgumentException> {
            PuzzleDefinition(listOf(listOf(1), listOf(1)), listOf(listOf(2), listOf(2)))
        }
        assertFailsWith<IllegalArgumentException>{
            PuzzleDefinition(listOf(listOf(2), listOf(2)), listOf(listOf(1), listOf(1)))
        }
    }

    @Test
    fun testIllFittingHintsThrowsException() {
        assertFailsWith<IllegalArgumentException> {
            PuzzleDefinition(listOf(listOf(3), listOf(0)), listOf(listOf(2), listOf(1)))
        }
        assertFailsWith<IllegalArgumentException>{
            PuzzleDefinition(listOf(listOf(2), listOf(0)), listOf(listOf(1, 1), listOf(0)))
        }
        assertFailsWith<IllegalArgumentException>{
            PuzzleDefinition(listOf(listOf(3), listOf(0), listOf(0)), listOf(listOf(2), listOf(1)))
        }
        assertFailsWith<IllegalArgumentException>{
            PuzzleDefinition(listOf(listOf(2), listOf(1)), listOf(listOf(3), listOf(0), listOf(0)))
        }
    }
}

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

import com.askrepps.nonogram.CellContents
import com.askrepps.nonogram.PuzzleDefinition
import kotlin.math.max

internal fun createEmptyPuzzleDefinitionWithDimensions(rows: Int, columns: Int) =
    PuzzleDefinition(
        rows,
        columns,
        List(max(rows, 0)) { listOf(0) },
        List(max(columns, 0)) { listOf(0) }
    )

internal fun createFullPuzzleDefinitionWithDimensions(rows: Int, columns: Int) =
    PuzzleDefinition(
        rows,
        columns,
        List(max(rows, 0)) { listOf(columns) },
        List(max(columns, 0)) { listOf(rows) }
    )

internal val MutableList<CellContents>.markCell: (Int, CellContents) -> Unit
    get() = { index, contents ->
        set(index, contents)
    }

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

/**
 * A nonogram puzzle definition (not including cell contents).
 *
 * @throws IllegalArgumentException if the puzzle definition is invalid.
 */
data class PuzzleDefinition(
    val rows: Int,
    val columns: Int,
    val rowHints: List<List<Int>>,
    val columnHints: List<List<Int>>
) {
    init {
        // validate puzzle definition
        require(rows > 0 && columns > 0) {
            "Puzzle must have positive dimensions"
        }
        require(rowHints.size == rows && columnHints.size == columns) {
            "Puzzle must have hints for every row and column"
        }
        require(rowHints.all { it.isNotEmpty() } && columnHints.all { it.isNotEmpty() }) {
            "Puzzle must have hint values for every row and column"
        }
        require(rowHints.sumBy { it.sum() } == columnHints.sumBy { it.sum() }) {
            "Puzzle must have the same total squares in row and column hints"
        }
        require(rowHints.all { it.sum() <= columns - it.size + 1 } &&
                columnHints.all { it.sum() <= rows - it.size + 1 }
        ) {
            "Puzzle must have all hints fit in their corresponding row or column"
        }
    }
}

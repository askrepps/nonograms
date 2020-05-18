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

/**
 * Check that all [Int] elements in an [Iterable] are non-negative.
 */
internal val Iterable<Int>.allNonnegative get() = all { it >= 0 }

/**
 * Check that if a [Collection] contains zero it is the only element.
 */
internal val Collection<Int>.anyZeroStandsAlone get() = size == 1 || !contains(0)

/**
 * Get the total length required to fit a [List] of nonogram hints in a row or column (including spaces between hints).
 *
 * This property assumes that hint values are valid (all non-negative, zero stands alone, etc.).
 */
internal val List<Int>.totalHintLength get() = sum() + size - 1

/**
 * Check that the [List] of nonogram hints will fit in a row of column with the given [length].
 *
 * This function assumes that hint values are valid (all non-negative, zero stands alone, etc.).
 *
 * @throws IllegalArgumentException if [length] is negative
 */
internal fun List<Int>.hintsFitWithin(length: Int): Boolean {
    require(length >= 0) { "length cannot be negative" }
    return totalHintLength <= length
}

/**
 * Get the index of the cell located at ([row], [col]) in a row-major grid with rows of size [rowSize].
 *
 * This function assumes that [row], [col], and [rowSize] are valid and self-consistent.
 */
internal fun getCellIndex(row: Int, col: Int, rowSize: Int) = row * rowSize + col

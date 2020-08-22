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
 * A [Sequence] of [List]s of spaces to insert before the cells corresponding to each hint value that
 * represents all possible combinations of spacing that can be applied to the [hints] in the given [lineSize].
 */
internal class HintSpacingGenerator(private val hints: List<Int>, private val lineSize: Int) : Sequence<List<Int>> {
    override fun iterator() = HintSpacingIterator(hints, lineSize)
}

/**
 * An [Iterator] that generates the next set of spacings to use.
 *
 * This code assumes the hints are valid and consistent w.r.t. the rules checked during puzzle initialization.
 */
internal class HintSpacingIterator(private val hints: List<Int>, private val lineSize: Int) : Iterator<List<Int>> {
    private val currentSpacing = mutableListOf<Int>()
    private val slack: Int

    init {
        val maxHint = hints.maxOrNull() ?: 0
        slack =
            if (maxHint > 0) {
                lineSize - hints.totalHintLength
            } else {
                0
            }
    }

    // done when all cells are shifted to the right as far as possible
    override fun hasNext() = currentSpacing.isEmpty() || currentSpacing.first() < slack

    override fun next(): List<Int> {
        if (currentSpacing.isEmpty()) {
            initializeSpaces()
        } else {
            advanceSpaces()
        }
        return currentSpacing.toList()
    }

    private fun initializeSpaces() {
        // start with cells shifted as far to the left as possible
        currentSpacing.add(0)
        for (i in 1 until hints.size) {
            currentSpacing.add(1)
        }
    }

    private fun advanceSpaces() {
        do {
            for (index in hints.indices.reversed()) {
                val (minSpacing, maxSpacing) =
                    if (index == 0) {
                        // the first hint group can be flush with the left edge of the grid
                        Pair(0, slack)
                    } else {
                        // other hints groups always have an extra space
                        //   (included in hints.totalHintLength, not in slack)
                        Pair(1, slack + 1)
                    }
                if (currentSpacing[index] < maxSpacing) {
                    currentSpacing[index]++
                    break
                } else {
                    currentSpacing[index] = minSpacing
                }
            }
        } while (currentSpacing.sum() + hints.sum() > lineSize)
        // skip over space combinations that will not fit within the line
    }
}

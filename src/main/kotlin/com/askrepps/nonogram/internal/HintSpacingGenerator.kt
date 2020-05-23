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
internal class HintSpacingIterator(private val hints: List<Int>, lineSize: Int) : Iterator<List<Int>> {
    private val maxHint = hints.max() ?: 0
    private val slack =  if (maxHint > 0) lineSize - hints.totalHintLength else 0
    private var currentSpacing = mutableListOf<Int>()

    override fun hasNext() =
        when (currentSpacing.size) {
            0 -> true
            1 -> currentSpacing.first() > 0
            else -> currentSpacing.last() < slack
        }

    override fun next(): List<Int> {
        if (currentSpacing.isEmpty()) {
            initializeSpaces()
        } else {
            advanceSpaces()
        }
        return currentSpacing.toList()
    }

    private fun initializeSpaces() {
        currentSpacing.add(slack)
        for (i in 1 until hints.size) {
            currentSpacing.add(1)
        }
    }

    private fun advanceSpaces() {
        if (currentSpacing.size == 1) {
            currentSpacing[0] -= 1
        } else {
            var advancing = true
            var index = 0
            while (index < currentSpacing.size - 1 && advancing) {
                if (currentSpacing[index] == 1) {
                    index++
                } else {
                    currentSpacing[index] -= 1
                    currentSpacing[index + 1] += 1
                    advancing = false
                }
            }
        }
    }
}

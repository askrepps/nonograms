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
 * A [Sequence] of [List]s containing indices that selects all combinations
 * of elements in the [Collection]s described by [collectionLengths].
 *
 * @throws IllegalArgumentException if [collectionLengths] is empty or the lengths are not all positive.
 */
internal class IndexSelectionGenerator(private val collectionLengths: List<Int>) : Sequence<List<Int>> {
    init {
        require(collectionLengths.isNotEmpty()) {
            "Must have at least one collection"
        }
        require(collectionLengths.all { it > 0 }) {
            "All collection lengths must be positive values"
        }
    }

    override fun iterator() = IndexSelectionIterator(collectionLengths)
}

/**
 * An [Iterator] that generates the next set of selected indices.
 */
internal class IndexSelectionIterator(private val collectionLengths: List<Int>) : Iterator<List<Int>> {
    private val currentSelections = mutableListOf<Int>()

    // done when all the last element of every collection is selected
    override fun hasNext() = currentSelections.isEmpty()
            || currentSelections.anyIndexed { i, selection -> selection < collectionLengths[i] - 1 }

    override fun next(): List<Int> {
        if (currentSelections.isEmpty()) {
            initializeSelections()
        } else {
            advanceSelections()
        }
        return currentSelections.toList()
    }

    private fun initializeSelections() {
        // start with choosing the first element of every collection
        for (i in collectionLengths.indices) {
            currentSelections.add(0)
        }
    }

    private fun advanceSelections() {
        for (i in collectionLengths.indices) {
            if (currentSelections[i] < collectionLengths[i] - 1) {
                currentSelections[i]++
                break
            } else {
                currentSelections[i] = 0
            }
        }
    }
}

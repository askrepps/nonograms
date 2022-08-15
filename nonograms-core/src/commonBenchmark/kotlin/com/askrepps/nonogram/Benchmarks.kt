/*
 * MIT License
 *
 * Copyright (c) 2022 Andrew Krepps
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

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Scope
import kotlinx.benchmark.State

// sample puzzles for benchmarking were obtained from https://webpbn.com/survey/puzzles/
// (only puzzles marked as freely redistributable with attribution are included below)

@Suppress("unused")
@State(Scope.Benchmark)
open class Benchmarks {
    @Benchmark
    open fun benchmarkWebPbn00001(): PuzzleSolution {
        // "Dancer" by Jan Wolter (https://webpbn.com/play.cgi?id=1)
        return runSolverBenchmark(
            rowHints = listOf(
                listOf(2),
                listOf(2, 1),
                listOf(1, 1),
                listOf(3),
                listOf(1, 1),
                listOf(1, 1),
                listOf(2),
                listOf(1, 1),
                listOf(1, 2),
                listOf(2)
            ),
            columnHints = listOf(
                listOf(2, 1),
                listOf(2, 1, 3),
                listOf(7),
                listOf(1, 3),
                listOf(2, 1)
            )
        )
    }

    @Benchmark
    open fun benchmarkWebPbn00006(): PuzzleSolution {
        // "Scardy Cat" by Jan Wolter (https://webpbn.com/play.cgi?id=6)
        return runSolverBenchmark(
            rowHints = listOf(
                listOf(2),
                listOf(2),
                listOf(1),
                listOf(1),
                listOf(1, 3),
                listOf(2, 5),
                listOf(1, 7, 1, 1),
                listOf(1, 8, 2, 2),
                listOf(1, 9, 5),
                listOf(2, 16),
                listOf(1, 17),
                listOf(7, 11),
                listOf(5, 5, 3),
                listOf(5, 4),
                listOf(3, 3),
                listOf(2, 2),
                listOf(2, 1),
                listOf(1, 1),
                listOf(2, 2),
                listOf(2, 2)
            ),
            columnHints = listOf(
                listOf(5),
                listOf(5, 3),
                listOf(2, 3, 4),
                listOf(1, 7, 2),
                listOf(8),
                listOf(9),
                listOf(9),
                listOf(8),
                listOf(7),
                listOf(8),
                listOf(9),
                listOf(10),
                listOf(13),
                listOf(6, 2),
                listOf(4),
                listOf(6),
                listOf(6),
                listOf(5),
                listOf(6),
                listOf(6)
            )
        )
    }

    @Benchmark
    open fun benchmarkWebPbn00021(): PuzzleSolution {
        // "Slippery Conditions" by Jan Wolter (https://webpbn.com/play.cgi?id=21)
        return runSolverBenchmark(
            rowHints = listOf(
                listOf(9),
                listOf(1, 1),
                listOf(1, 1, 1),
                listOf(1, 3, 1),
                listOf(13),
                listOf(13),
                listOf(13),
                listOf(13),
                listOf(2, 2),
                listOf(2, 2),
                listOf(0),
                listOf(2, 2),
                listOf(2, 2),
                listOf(2, 2),
                listOf(2, 2),
                listOf(2, 2),
                listOf(2, 2),
                listOf(2, 2),
                listOf(2, 2),
                listOf(2, 2),
                listOf(2, 2),
                listOf(2, 2),
                listOf(2, 2),
                listOf(2, 2),
                listOf(2, 2)
            ),
            columnHints = listOf(
                listOf(2),
                listOf(4, 6),
                listOf(9, 4, 4, 2),
                listOf(1, 6, 2, 6),
                listOf(1, 5, 2),
                listOf(1, 6),
                listOf(1, 5),
                listOf(1, 4),
                listOf(1, 4),
                listOf(1, 4, 2),
                listOf(1, 4, 6),
                listOf(1, 6, 4, 4, 2),
                listOf(9, 2, 6),
                listOf(4, 2)
            )
        )
    }

    private fun runSolverBenchmark(rowHints: List<List<Int>>, columnHints: List<List<Int>>): PuzzleSolution {
        val puzzle = PuzzleDefinition(rowHints, columnHints)
        return puzzle.solve()
    }
}

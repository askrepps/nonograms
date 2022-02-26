# Nonograms
Simple project for solving nonogram puzzles using Kotlin

## Modules

- `nonograms-core` - multi-platform library for creating puzzle definitions and running the solver algorithm
- `nonograms-web` - example Kotlin/JS library usage to create a simple webpage for a user to enter puzzles to solve

## Library Setup Instructions

1. Install the `nonograms-core` library to your local Maven repository:

```
./gradlew publishToMavenLocal
```

2. Add the `nonograms-core` library as a dependency in your project:

```groovy
repositories {
    mavenCentral()
    mavenLocal()  // add your local maven repository
}

dependencies {
    implementation "com.askrepps:nonograms-core:1.0.0"
}
```

## Library API Documentation

1. Generate the library's API documentation:

```
./gradlew dokkaHtml
```

2. Open `nonograms-core/build/dokka/html/index.html` in a web browser.

## Sample Library Usage

Here is an example showing how to create a puzzle definition and run the solver:

```kotlin
// create puzzle
val puzzle = PuzzleDefinition(
    rows = 8,
    columns = 8,
    rowHints = listOf(
        listOf(0),
        listOf(1, 1),
        listOf(1, 1),
        listOf(1, 1),
        listOf(0),
        listOf(1, 1),
        listOf(4),
        listOf(0)
    ),
    columnHints = listOf(
        listOf(0),
        listOf(1),
        listOf(3, 1),
        listOf(1),
        listOf(1),
        listOf(3, 1),
        listOf(1),
        listOf(0)
    )
)

// run solver
val solution = puzzle.solve()

// access solved puzzle state
for (row in solution.state.cellGrid) {
    println(
        row.joinToString(separator = " ") { cell ->
            when (cell) {
                CellContents.FILLED -> "█"
                CellContents.X -> "X"
                CellContents.OPEN -> " "
            }
        }
    )
}
```

The above example generates the following output:
```
X X X X X X X X
X X █ X X █ X X
X X █ X X █ X X
X X █ X X █ X X
X X X X X X X X
X █ X X X X █ X
X X █ █ █ █ X X
X X X X X X X X
```

<hr>
<sub>Copyright 2020-2022 © Andrew Krepps</sub>

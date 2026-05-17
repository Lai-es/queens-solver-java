# Queens Solver

A Java-based solver for the [Queens puzzle](https://www.playqueensgame.com/) — a logic game where you place queens on a colored grid such that each row, column, and color region contains exactly one queen, with no two queens touching (even diagonally).

## How it works

- **Fetcher** — scrapes up to 300 8×8 puzzles from [playqueensgame.com](https://www.playqueensgame.com) using Selenium and saves them locally
- **Parser** — reads puzzle files into a board representation
- **Solver** — solves the puzzle using a backtracking algorithm
- **Printer** — prints the board and solution to the console

## Prerequisites

- **Java 25+** — check with `java --version`
- **Google Chrome** — required for the `fetch-database` command (Selenium drives it automatically)

## Setup

```bash
git clone git@github.com:Lai-es/queens-solver.git
cd queens-solver
```

## Usage

### Fetch puzzle database
Downloads up to 300 8×8 puzzles and saves them to `./puzzles/8x8/`:
```bash
./gradlew run --args="fetch-database"
```
> Ideomatically, run this once before using `solve-random`. But theres also a puzzle database uploaded.

### Solve a random puzzle
Picks a random puzzle from the local database and prints the solution:
```bash
./gradlew run --args="solve-random"
```

Example output:
```
0 0 1 1 2 2 3 3
Q 0 1 1 2 2 3 3
0 0 Q 1 2 2 3 3
0 0 1 1 Q 2 3 3
0 0 1 1 2 2 Q 3
0 0 1 1 2 2 3 Q
0 Q 1 1 2 2 3 3
0 0 1 Q 2 2 3 3
```

## Project structure

```
queens-solver/
└── app/src/main/java/com/queens/
    ├── Main.java                        # CLI entry point
    ├── model/
    │   ├── Board.java                   # grid + color region map
    │   └── Queen.java                   # position (row, col)
    ├── solver/
    │   └── BacktrackingSolver.java      # recursive backtracking algorithm
    ├── validator/
    │   └── PlacementValidator.java      # constraint checker
    └── io/
        ├── QueensFetcher.java           # Selenium-based puzzle downloader
        ├── PuzzleParser.java            # reads puzzle files into Board
        └── BoardPrinter.java            # prints board and solution
```

## Rules

1. Each **row** must contain exactly one queen
2. Each **column** must contain exactly one queen
3. Each **color region** must contain exactly one queen
4. No two queens may **touch** — not even diagonally

# Why am i doing this?

- to learn how gradle projects are structured
- to recap recursion
- to learn how Webscrapers work and use one for the first time
- to build my first java app
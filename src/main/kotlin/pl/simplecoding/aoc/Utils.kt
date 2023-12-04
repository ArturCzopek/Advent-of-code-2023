package pl.simplecoding.aoc

import kotlin.io.path.Path
import kotlin.io.path.readLines

fun readInput(name: String) = Path("src/main/resources$name.txt").readLines()

fun Any?.println() = println(this)
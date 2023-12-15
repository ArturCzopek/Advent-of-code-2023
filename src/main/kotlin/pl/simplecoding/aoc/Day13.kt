package pl.simplecoding.aoc

fun main() {
    Day13a.solve(readInput("Day13")).println()
//    Day13b.solve(readInput("Day13")).println()
}

abstract class Day13 {

    abstract fun solve(input: List<String>): Int

    internal fun parseLavaMaps(input: List<String>): List<LavaMap> {
        val entities = mutableListOf<LavaMap>()
        var currentEntityRows = mutableListOf<String>()

        for (line in input) {
            if (line.isBlank()) {
                val currentEntityColumns = getCurrentEntityColumns(currentEntityRows)
                entities.add(LavaMap(currentEntityRows, currentEntityColumns))
                currentEntityRows.clear()
            } else {
                // Map each row horizontally
                currentEntityRows.add(line)
            }
        }

        // last line
        if (currentEntityRows.isNotEmpty()) {
            val currentEntityColumns = getCurrentEntityColumns(currentEntityRows)
            entities.add(LavaMap(currentEntityRows, currentEntityColumns))
        }

        return entities.toList()
    }

    private fun getCurrentEntityColumns(currentEntityRows: MutableList<String>) =
        (0..<currentEntityRows[0].length)
            .map { columnIdx ->
                (0..<currentEntityRows.size)
                    .map { rowIdx -> currentEntityRows[rowIdx][columnIdx] }.joinToString("")
            }
}

object Day13a : Day13() {
    override fun solve(input: List<String>) = parseLavaMaps(input).sumOf { lavaMap ->
        val verticalReflectionIdx = getReflectionStartIdx(lavaMap.columns)
        if (verticalReflectionIdx != null) {
            verticalReflectionIdx + 1
        }

        val horizontalReflectionIdx = getReflectionStartIdx(lavaMap.rows)

        if (horizontalReflectionIdx != null) {
            (horizontalReflectionIdx + 1) * 100
        } else {
            println("NO MAP")
            0
        }
    }


    private fun getReflectionStartIdx(source: List<String>): Int? {
        val searchIdxs = mutableListOf(source.size / 2, source.size / 2 - 1, source.size / 2 + 1)
        searchIdxs.addAll(source.indices)
        searchIdxs.remove(source.size - 1)
        return searchIdxs.map { startIdx ->
            var diff = 0
            var isReflection: Boolean
            do {
                isReflection = source[startIdx - diff] == source[startIdx + 1 + diff]
                if (isReflection) diff++
            } while (isReflection && diff <= startIdx && startIdx + 1 + diff < source.size)

            startIdx to diff
        }
            .filter { it.second > 0 }
            .maxByOrNull { it.second }
            ?.first
    }
}

object Day13b : Day13() {
    override fun solve(input: List<String>): Int {
        TODO("Not yet implemented")
    }
}

data class LavaMap(
    val rows: List<String>,
    val columns: List<String>
)

package pl.simplecoding.aoc

fun main() {
    Day05a.solve(readInput("Day05")).println()
    Day05b.solve(readInput("Day05")).println()
}

abstract class Day05 {
    private val mapRegex = Regex("""(\w+)-to-(\w+) map:""")
    private val mapSuffix = " map:"
    internal val seedsSuffix = "seeds: "

    abstract fun solve(input: List<String>): Long

    internal fun mapSeed(seed: Long, mappingsList: List<Mappings>): Long {
        var mappedSeed = seed
        mappingsList.forEach { mappings ->
            val mapping = mappings.valueMappings
                .firstOrNull { values -> mappedSeed >= values.sourceStart && mappedSeed <= values.sourceEnd }
            mappedSeed = if (mapping != null) mappedSeed + mapping.offset else mappedSeed
        }
        return mappedSeed
    }

    internal fun parseMappings(mapsInput: List<String>): List<Mappings> {
        val mappings = mutableListOf<Mappings>()
        var currentMapping: Mappings? = null

        for (line in mapsInput) {
            if (line.isNotBlank()) {
                if (line.endsWith(mapSuffix)) {
                    val matches = mapRegex.findAll(line)
                    for (match in matches) {
                        currentMapping = Mappings(match.groupValues[1], match.groupValues[2])
                    }
                } else {
                    val numbers = line.split(" ").map { it.toLong() }
                    if (numbers.isNotEmpty() && currentMapping != null) {
                        val destinationStart = numbers[0]
                        val sourceStart = numbers[1]
                        val rangeLength = numbers[2]

                        currentMapping + ValueMapping(
                            sourceStart = sourceStart,
                            sourceEnd = sourceStart + rangeLength - 1,
                            offset = destinationStart - sourceStart,
                        )
                    }
                }
            } else {
                currentMapping?.let { mappings += it }
            }

        }
        // for last group there is no empty line at the end
        currentMapping?.let { mappings += it }

        return mappings
    }
}

object Day05a : Day05() {

    override fun solve(input: List<String>): Long {
        val seeds = parseSeeds(input.first())
        val mappingsList = parseMappings(input.subList(2, input.size))
        return seeds.minOf { seed -> mapSeed(seed, mappingsList) }
    }

    private fun parseSeeds(line: String) = line.removePrefix("seeds: ")
        .split(" ")
        .map { it.toLong() }
        .toSet()
}

object Day05b : Day05() {

    override fun solve(input: List<String>): Long {
        val seeds = parseSeeds(input.first())
        val mappingsList = parseMappings(input.subList(2, input.size))
        return seeds.minOf { seedRange ->
            seedRange.minOf {
                seed -> mapSeed(seed, mappingsList)
            }
        }
    }

    private fun parseSeeds(line: String): List<LongRange> {
        val values = line.removePrefix(seedsSuffix)
            .split(" ")
            .map { it.toLong() }
            .toList()

        return values
            .filterIndexed { index, _ -> index % 2 == 0 } // is even
            .mapIndexed { index, value ->
                (value..<value + values[index * 2 + 1]) // multiply index by 2 because we have only half of values and we want next value to that one to end range
            }

    }
}

data class Mappings(
    val from: String,
    val to: String,
    val valueMappings: MutableList<ValueMapping> = mutableListOf()
) {
    operator fun plus(valueMapping: ValueMapping) {
        this.valueMappings.add(valueMapping)
    }
}

data class ValueMapping(
    val sourceStart: Long,
    val sourceEnd: Long,
    val offset: Long
)

package pl.simplecoding.aoc

fun main() {
    Day05a.solve(readInput("Day05")).println()
//    Day05b.solve(readInput("Day05")).println()
}

abstract class Day05 {
    private val mapRegex = Regex("""(\w+)-to-(\w+) map:""")
    private val mapSuffix = " map:"

    abstract fun solve(input: List<String>): Long

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
                        currentMapping + ValueMapping(
                            destinationStart = numbers[0],
                            sourceStart = numbers[1],
                            rangeLength = numbers[2]
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

    internal fun parseSeeds(line: String) = line.removePrefix("seeds: ")
        .split(" ")
        .map { it.toLong() }
}

object Day05a : Day05() {
    override fun solve(input: List<String>): Long {
        val seeds = parseSeeds(input.first())
        val mappingsInput = input.subList(2, input.size)
        val mappingsList = parseMappings(mappingsInput)
        return seeds.minOf { seed ->
            var mappedSeed = seed
            mappingsList.forEach { mappings ->
                val mapping =
                    mappings.valueMappings.firstOrNull { values -> mappedSeed >= values.sourceStart && mappedSeed <= values.sourceEnd }
                mappedSeed = if (mapping != null) mappedSeed + mapping.offset else mappedSeed
            }
            mappedSeed
        }
    }
}

object Day05b : Day05() {
    override fun solve(input: List<String>): Long {
        TODO("Not yet implemented")
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
    val destinationStart: Long,
    val destinationEnd: Long,
    val rangeLength: Long,
    val offset: Long
) {
    constructor(
        destinationStart: Long,
        sourceStart: Long,
        rangeLength: Long
    ) : this(
        sourceStart,
        sourceStart + rangeLength - 1,
        destinationStart,
        destinationStart + rangeLength - 1,
        rangeLength,
        destinationStart - sourceStart
    )
}
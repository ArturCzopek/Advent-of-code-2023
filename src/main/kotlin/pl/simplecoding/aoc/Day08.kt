package pl.simplecoding.aoc

const val LAST_STEP = "ZZZ"
const val START_STEP = "AAA"
const val LEFT_ACTION = 'L'

fun main() {
    Day08a.solve(readInput("Day08")).println()
//    Day08b.solve(readInput("Day08")).println()
}

abstract class Day08 {
    abstract fun solve(input: List<String>): Long

    fun parseGuide(input: List<String>): Guide {
        val lines = input.map { it.trim() }
        val instruction = lines[0]
        val steps = mutableMapOf<String, Step>()

        (2..<lines.size).forEach {
            val line = lines[it]
            val (id, rest) = line.split("=").map(String::trim)
            val (leftId, rightId) = rest.substring(1, rest.length - 1).split(",").map(String::trim)
            steps[id] = Step(id, leftId, rightId)
        }

        return Guide(instruction, steps)
    }
}


object Day08a : Day08() {
    override fun solve(input: List<String>): Long {
        val guide = parseGuide(input)
        var currentStep = guide.steps[START_STEP]!!
        var stepsDone = 0L
        val stepsInGuide = guide.instructions.length
        do {
            val nextAction = guide.instructions[(stepsDone % stepsInGuide).toInt()]
            currentStep = if (nextAction == LEFT_ACTION) guide.steps[currentStep.leftId]!! else guide.steps[currentStep.rightId]!!
            stepsDone++
        } while (currentStep.id != LAST_STEP)
        return stepsDone
    }

}

data class Guide(
    val instructions: String,
    val steps: Map<String, Step>
)

class Step(
    val id: String,
    val leftId: String,
    val rightId: String
)
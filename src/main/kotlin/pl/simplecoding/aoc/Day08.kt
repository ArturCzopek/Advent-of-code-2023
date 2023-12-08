package pl.simplecoding.aoc

import kotlin.math.abs

const val END_STEP = "ZZZ"
const val START_STEP = "AAA"

const val START_STEP_SUFFIX = 'A'
const val END_STEP_SUFFIX = 'Z'

const val LEFT_ACTION = 'L'

fun main() {
    Day08a.solve(readInput("Day08")).println()
    Day08b.solve(readInput("Day08")).println()
}

abstract class Day08 {
    fun solve(input: List<String>) = parseGuide(input).let { guide ->
        guide.steps
            .filter(startStepCondition)
            .map { it.value }
            .map { getStepsAmount(guide, it, endStepCondition) }
            .let(finalModifier)
    }

    abstract val startStepCondition: (Map.Entry<String, Step>) -> Boolean
    abstract val endStepCondition: (Step) -> Boolean
    abstract val finalModifier: Iterable<Long>.() -> Long

    private fun parseGuide(input: List<String>): Guide {
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

    private fun getStepsAmount(guide: Guide, startStep: Step, endCondition: (Step) -> Boolean): Long {
        var currentStep = startStep
        var stepsDone = 0L
        val stepsInGuide = guide.instructions.length
        do {
            val nextAction = guide.instructions[(stepsDone % stepsInGuide).toInt()]
            currentStep =
                if (nextAction == LEFT_ACTION) guide.steps[currentStep.left]!! else guide.steps[currentStep.right]!!
            stepsDone++
        } while (!endCondition.invoke(currentStep))
        return stepsDone
    }
}

object Day08a : Day08() {
    override val startStepCondition: (Map.Entry<String, Step>) -> Boolean
        get() = { it.value.id == START_STEP }
    override val endStepCondition: (Step) -> Boolean
        get() = { it.id == END_STEP }

    override val finalModifier: Iterable<Long>.() -> Long
        get() = Iterable<Long>::sum
}

object Day08b : Day08() {
    override val startStepCondition: (Map.Entry<String, Step>) -> Boolean
        get() = { it.value.id.endsWith(START_STEP_SUFFIX) }
    override val endStepCondition: (Step) -> Boolean
        get() = { it.id.endsWith(END_STEP_SUFFIX) }
    override val finalModifier: Iterable<Long>.() -> Long
        get() = Iterable<Long>::findLcm
}

fun gcd(a: Long, b: Long): Long {
    return if (b == 0L) abs(a) else gcd(b, a % b)
}

fun lcm(a: Long, b: Long): Long {
    return if (a == 0L || b == 0L) 0 else abs(a * b) / gcd(a, b)
}

private fun Iterable<Long>.findLcm(): Long {
    val asList = this.toList()
    var result = asList[0]

    for (i in 1..<asList.size) {
        result = lcm(result, asList[i])
    }

    return result
}

data class Guide(
    val instructions: String,
    val steps: Map<String, Step>
)

data class Step(
    val id: String,
    val left: String,
    val right: String
)
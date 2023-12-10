package pl.simplecoding.aoc

fun main() {
    Day10a.solve(readInput("Day10")).println()
    Day10b.solve(readInput("Day10")).println()
}

// for further refactoring / development, I would handle distinguishing which directions pipe has. Also, PipeMap should contain this signs instead of chars only
// for another development, this map might be prettier, not this weird signs

abstract class Day10 {
    abstract fun solve(input: List<String>): Long

    private fun parseInput(input: List<String>) = PipeMap(
        input.mapIndexed { y, line -> line.mapIndexed { x, sign -> PipeData(sign, Coordinates(x, y)) }.toList() }
    )

    internal fun generateFilledInPipeMap(input: List<String>): PipeMap {
        val pipeMap = parseInput(input)
        val startCoord = getStartCoords(pipeMap)
        pipeMap.setLoopPipe(startCoord)
        var currentPipe: Pipe = StartPipe(startCoord, startCoord)

        do {
            currentPipe = currentPipe.getNextPipe(pipeMap)
            pipeMap.setLoopPipe(currentPipe.current)
            pipeMap.setRangePipe(currentPipe.current, currentPipe.isRangePipe)
        } while (currentPipe !is StartPipe)

        // start pipe again
        pipeMap.setLoopPipe(currentPipe.current)
        pipeMap.setRangePipe(currentPipe.current, currentPipe.checkIfRangePipe(pipeMap))
        return pipeMap
    }

    private fun getStartCoords(pipeMap: PipeMap): Coordinates {
        val startY = pipeMap.rows.indexOfFirst { 'S' in it.map { r -> r.sign } }
        val startX = pipeMap.rows[startY].indexOfFirst { 'S' == it.sign }
        return Coordinates(startX, startY)
    }
}

object Day10a : Day10() {
    override fun solve(input: List<String>): Long {
        val pipeMap = generateFilledInPipeMap(input)
        val loopPipes = pipeMap.countLoopPipes()
        return if (loopPipes % 2 == 0) loopPipes / 2L else (loopPipes / 2L) + 1
    }
}

object Day10b : Day10() {
    override fun solve(input: List<String>) = with(generateFilledInPipeMap(input)) {
        this.rows
            .map { row -> row.filter { it.rangePipe && it.loopPipe } }
            .sumOf { row ->
                (0..<(row.size - 1) step 2)
                    .map { row[it].coords to row[it + 1].coords } // all ranges
                    .sumOf { range -> // count non-loop pipes within range
                        val startX = range.first.x
                        val endX = range.second.x
                        val y = range.first.y
                        (startX..endX)
                            .map { this[Coordinates(it, y)] }
                            .count { !it.loopPipe }
                    }
            }.toLong()
    }
}

data class PipeData(
    val sign: Char,
    val coords: Coordinates,
    var loopPipe: Boolean = false,
    var rangePipe: Boolean = false
)

data class PipeMap(
    val rows: List<List<PipeData>>
) {
    operator fun get(coords: Coordinates) = rows[coords.y][coords.x]

    fun setLoopPipe(coords: Coordinates, value: Boolean = true) {
        rows[coords.y][coords.x].loopPipe = value
    }

    fun setRangePipe(coords: Coordinates, value: Boolean = true) {
        rows[coords.y][coords.x].rangePipe = value
    }

    fun countLoopPipes() = rows.sumOf { row ->
        row.count { it.loopPipe }
    }
}

data class Coordinates(
    val x: Int,
    val y: Int
) {
    fun isValid() = x >= 0 && y >= 0 // south-east boundaries not handled
}

sealed class Pipe(
    val origin: Coordinates,
    val current: Coordinates,
    val nextStep: (Coordinates, Coordinates) -> Coordinates // origin, current -> next
) {

    abstract val isRangePipe: Boolean

    open fun getNextPipe(pipeMap: PipeMap) = with(nextStep(origin, current)) {
        when (pipeMap[this].sign) {
            VerticalPipe.sign -> VerticalPipe(current, this)
            HorizontalPipe.sign -> HorizontalPipe(current, this)
            Ne90Pipe.sign -> Ne90Pipe(current, this)
            Nw90Pipe.sign -> Nw90Pipe(current, this)
            Sw90Pipe.sign -> Sw90Pipe(current, this)
            Se90Pipe.sign -> Se90Pipe(current, this)
            StartPipe.sign -> StartPipe(current, this)
            else -> NoPipe(current, this)
        }
    }
}

class VerticalPipe(
    origin: Coordinates,
    current: Coordinates
) : Pipe(
    origin = origin,
    current = current,
    nextStep = { o, c -> if (o.y + 1 == c.y) c.copy(y = c.y + 1) else c.copy(y = c.y - 1) }
) {
    override val isRangePipe: Boolean
        get() = true

    companion object {
        const val sign = '|'
    }
}

class HorizontalPipe(
    origin: Coordinates,
    current: Coordinates
) : Pipe(
    origin = origin,
    current = current,
    nextStep = { o, c -> if (o.x + 1 == c.x) c.copy(x = c.x + 1) else c.copy(x = c.x - 1) }
) {
    override val isRangePipe: Boolean
        get() = false

    companion object {
        const val sign = '-'
    }
}

class Ne90Pipe(
    origin: Coordinates,
    current: Coordinates
) : Pipe(
    origin = origin,
    current = current,
    nextStep = { o, c -> if (o.y + 1 == c.y) c.copy(x = c.x + 1) else c.copy(y = c.y - 1) }
) {
    override val isRangePipe: Boolean
        get() = false

    companion object {
        const val sign = 'L'
    }
}

class Nw90Pipe(
    origin: Coordinates,
    current: Coordinates
) : Pipe(
    origin = origin,
    current = current,
    nextStep = { o, c -> if (o.y + 1 == c.y) c.copy(x = c.x - 1) else c.copy(y = c.y - 1) }
) {
    override val isRangePipe: Boolean
        get() = false

    companion object {
        const val sign = 'J'
    }
}

class Sw90Pipe(
    origin: Coordinates,
    current: Coordinates
) : Pipe(
    origin = origin,
    current = current,
    nextStep = { o, c -> if (o.y - 1 == c.y) c.copy(x = c.x - 1) else c.copy(y = c.y + 1) }
) {
    override val isRangePipe: Boolean
        get() = true

    companion object {
        const val sign = '7'
    }
}

class Se90Pipe(
    origin: Coordinates,
    current: Coordinates
) : Pipe(
    origin = origin,
    current = current,
    nextStep = { o, c -> if (o.y - 1 == c.y) c.copy(x = c.x + 1) else c.copy(y = c.y + 1) }
) {
    override val isRangePipe: Boolean
        get() = true

    companion object {
        const val sign = 'F'
    }
}

class NoPipe(
    origin: Coordinates,
    current: Coordinates
) : Pipe(
    origin = origin,
    current = current,
    nextStep = { _, _ -> throw UnsupportedOperationException("No next pipe for no pipe") }
) {
    override val isRangePipe: Boolean
        get() = false
}

class StartPipe(
    origin: Coordinates,
    current: Coordinates
) : Pipe(
    origin = origin,
    current = current,
    nextStep = { _, _ -> throw UnsupportedOperationException("No next pipe for start pipe, use #getNextPipe instead") }
) {
    override val isRangePipe: Boolean
        get() = false

    companion object {
        const val sign = 'S'
    }

    // so much logic for "data" class

    override fun getNextPipe(pipeMap: PipeMap): Pipe {
        val rightCoord = Coordinates(current.x + 1, current.y)
        var firstPipe = when (pipeMap[rightCoord].sign) {
            HorizontalPipe.sign -> HorizontalPipe(current, rightCoord)
            Nw90Pipe.sign -> Nw90Pipe(current, rightCoord)
            Sw90Pipe.sign -> Sw90Pipe(current, rightCoord)
            else -> null
        }

        if (firstPipe != null) {
            return firstPipe
        }

        val downCoord = Coordinates(current.x, current.y + 1)
        firstPipe = when (pipeMap[downCoord].sign) {
            VerticalPipe.sign -> VerticalPipe(current, downCoord)
            Ne90Pipe.sign -> Ne90Pipe(current, downCoord)
            Nw90Pipe.sign -> Nw90Pipe(current, downCoord)
            else -> null
        }

        if (firstPipe != null) {
            return firstPipe
        }

        val leftCoord = Coordinates(current.x - 1, current.y)
        return when (pipeMap[leftCoord].sign) {
            HorizontalPipe.sign -> HorizontalPipe(current, leftCoord)
            Ne90Pipe.sign -> Ne90Pipe(current, leftCoord)
            Se90Pipe.sign -> Se90Pipe(current, leftCoord)
            else -> throw IllegalArgumentException("Wrong map provided")
        }

        // start has two connections, three directions to check is enough
    }

    fun checkIfRangePipe(pipeMap: PipeMap): Boolean {
        val rightCoord = Coordinates(current.x + 1, current.y)
        val downCoord = Coordinates(current.x, current.y + 1)
        val leftCoord = Coordinates(current.x - 1, current.y)
        val upCoord = Coordinates(current.x, current.y - 1)

        val possibleUpDownConn =
            upCoord.isValid() && downCoord.isValid() && pipeMap[upCoord].loopPipe && pipeMap[downCoord].loopPipe
        val possibleDownRightConn =
            downCoord.isValid() && rightCoord.isValid() && pipeMap[downCoord].loopPipe && pipeMap[rightCoord].loopPipe
        val possibleDownLeftConn =
            downCoord.isValid() && leftCoord.isValid() && pipeMap[downCoord].loopPipe && pipeMap[leftCoord].loopPipe

        if (possibleUpDownConn &&
            pipeMap[upCoord].sign in listOf(Sw90Pipe.sign, VerticalPipe.sign, Se90Pipe.sign) &&
            pipeMap[downCoord].sign in listOf(Nw90Pipe.sign, VerticalPipe.sign, Ne90Pipe.sign)
        ) {
            return true
        }

        if (possibleDownRightConn &&
            pipeMap[downCoord].sign in listOf(Nw90Pipe.sign, VerticalPipe.sign, Ne90Pipe.sign) &&
            pipeMap[rightCoord].sign in listOf(Sw90Pipe.sign, HorizontalPipe.sign, Nw90Pipe.sign)
        ) {
            return true
        }

        return possibleDownLeftConn &&
                pipeMap[downCoord].sign in listOf(Nw90Pipe.sign, VerticalPipe.sign, Ne90Pipe.sign) &&
                pipeMap[leftCoord].sign in listOf(Se90Pipe.sign, HorizontalPipe.sign, Ne90Pipe.sign)
    }
}

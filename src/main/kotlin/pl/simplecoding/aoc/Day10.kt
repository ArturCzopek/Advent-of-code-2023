package pl.simplecoding.aoc

fun main() {
    Day10a.solve(readInput("Day10")).println()
//    Day10b.solve(readInput("Day10")).println()
}

abstract class Day10 {
    abstract fun solve(input: List<String>): Long
}

object Day10a : Day10() {
    override fun solve(input: List<String>): Long {
        val startY = input.indexOfFirst { 'S' in it }
        val startX = input[startY].indexOfFirst { 'S' == it }
        val startCoord = Coordinates(startX, startY)
        var currentPipe: Pipe = StartPipe(startCoord, startCoord).getFirstPipe(input)
        var steps = 1
        do {
            currentPipe = currentPipe.getNextPipe(input)
            steps++
        } while (currentPipe !is StartPipe)

        return if (steps % 2 == 0) steps / 2L else (steps / 2L) + 1
    }

}

data class Coordinates(val x: Int, val y: Int)

sealed class Pipe(
    val origin: Coordinates,
    val current: Coordinates,
    val nextStep: (Coordinates, Coordinates) -> Coordinates // origin, current -> next
) {

    fun getNextPipe(pipeMap: List<String>) = with(nextStep(origin, current)) {
        when (pipeMap[this.y][this.x]) {
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
    companion object {
        const val sign ='|'
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
    companion object {
        const val sign ='-'
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
    companion object {
        const val sign ='L'
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
    companion object {
        const val sign ='J'
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
    companion object {
        const val sign ='7'
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
    companion object {
        const val sign ='F'
    }
}

class NoPipe(
    origin: Coordinates,
    current: Coordinates
) : Pipe(
    origin = origin,
    current = current,
    nextStep = { _, _ -> throw UnsupportedOperationException("No next pipe for no pipe") }
)

class StartPipe(
    origin: Coordinates,
    current: Coordinates
) : Pipe(
    origin = origin,
    current = current,
    nextStep = { _, _ -> throw UnsupportedOperationException("No next pipe for start pipe, use #getFirstPipe instead") }
) {
    companion object {
        const val sign ='S'
    }

    // corner case not handled - start point on the border
    fun getFirstPipe(pipeMap: List<String>): Pipe {
        val rightCoord = Coordinates(current.x + 1, current.y)
        var firstPipe = when (pipeMap[rightCoord.y][rightCoord.x]) {
            HorizontalPipe.sign -> HorizontalPipe(current, rightCoord)
            Nw90Pipe.sign -> Nw90Pipe(current, rightCoord)
            Sw90Pipe.sign -> Sw90Pipe(current, rightCoord)
            else -> null
        }

        if (firstPipe != null) {
            return firstPipe
        }

        val downCoord = Coordinates(current.x, current.y + 1)
        firstPipe = when (pipeMap[downCoord.y][downCoord.x]) {
            VerticalPipe.sign -> VerticalPipe(current, downCoord)
            Ne90Pipe.sign -> Ne90Pipe(current, downCoord)
            Nw90Pipe.sign -> Nw90Pipe(current, downCoord)
            else -> null
        }

        if (firstPipe != null) {
            return firstPipe
        }

        val leftCoord = Coordinates(current.x - 1, current.y)
        return when (pipeMap[leftCoord.y][leftCoord.x]) {
            HorizontalPipe.sign -> HorizontalPipe(current, leftCoord)
            Ne90Pipe.sign -> Ne90Pipe(current, leftCoord)
            Se90Pipe.sign -> Se90Pipe(current, leftCoord)
            else -> throw IllegalArgumentException("Wrong map provided")
        }

        // start has two connections, three directions to check is enough
    }
}

package pl.simplecoding.aoc

import org.junit.jupiter.api.Test

class Day06Test {

    @Test
    fun `should solve quiz`() {
        // given
        val input = """
            Time:      7  15   30
            Distance:  9  40  200
        """.trimIndent()

        // when
//        val resultA = Day06a.solve(input.lines())
        val resultB = Day06b.solve(input.lines())

        // then
//        assert(288L == resultA)
        assert(71503L == resultB)
    }
}
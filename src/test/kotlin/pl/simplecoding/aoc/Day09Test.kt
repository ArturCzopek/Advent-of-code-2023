package pl.simplecoding.aoc

import org.junit.jupiter.api.Test

class Day09Test {

    @Test
    fun `should solve quiz`() {
        // given
        val input = """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
        """.trimIndent()

        // when
        val resultA = Day09a.solve(input.lines())
//        val resultB = Day09b.solve(input.lines())

        // then
        assert(114L == resultA)
//        assert(5905L == resultB)
    }
}
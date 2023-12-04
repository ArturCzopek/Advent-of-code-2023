package pl.simplecoding.aoc

import org.junit.jupiter.api.Test

class Day01Test {

    @Test
    fun `should solve quiz`() {
        // given
        val input = """
            sq5fivetwothree1
            six5gc
            txb3qfzsbzbxlzslfourone1vqxgfive
        """.trimIndent()

        // when
        val resultA = Day01a.solve(input.lines())
        val resultB = Day01b.solve(input.lines())

        // then
        assert(137 == resultA)
        assert(151 == resultB)
    }
}
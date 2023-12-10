package pl.simplecoding.aoc

import org.junit.jupiter.api.Test

class Day10Test {

    @Test
    fun `should solve quiz`() {
        // given
        val inputA1 = """
            .....
            .S-7.
            .|.|.
            .L-J.
            .....
        """.trimIndent()

        val inputA2 = """
            ..F7.
            .FJ|.
            SJ.L7
            |F--J
            LJ...
        """.trimIndent()

        // when
        val resultA1 = Day10a.solve(inputA1.lines())
        val resultA2 = Day10a.solve(inputA2.lines())
//        val resultB = Day10b.solve(input.lines())

        // then
        assert(4L == resultA1)
        assert(8L == resultA2)
//        assert(2L == resultB)
    }
}
package pl.simplecoding.aoc

import org.junit.jupiter.api.Test

class Day03Test {

    @Test
    fun `should solve quiz`() {
        // given
        val input = """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..
        """.trimIndent()

        //when
        val resultA = Day03a.solve(input.lines())
        val resultB = Day03b.solve(input.lines())

        // then
        assert(4361L == resultA)
        assert(467835L == resultB)
    }
}
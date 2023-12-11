package pl.simplecoding.aoc

import org.junit.jupiter.api.Test

class Day11Test {

    @Test
    fun `should solve quiz`() {
        // given
        val input = """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....
        """.trimIndent()

        // when
        val resultA = Day11a.solve(input.lines())
        val resultB = Day11b.solve(input.lines())

        // then
        assert(374L == resultA)
//        assert(1030L == resultB)
    }
}
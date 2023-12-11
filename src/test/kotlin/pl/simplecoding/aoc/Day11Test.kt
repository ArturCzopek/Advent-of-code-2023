package pl.simplecoding.aoc

import org.junit.jupiter.api.Test

class Day11Test {

    @Test
    fun `should solve quiz`() {
        // given
        val inputA = """
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
        val resultA = Day11a.solve(inputA.lines())
//        val resultB = Day11a.solve(inputA.lines())

        // then
        assert(374L == resultA)
//        assert(1030L == resultB)
    }
}
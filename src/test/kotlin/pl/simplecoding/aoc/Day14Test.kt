package pl.simplecoding.aoc

import org.junit.jupiter.api.Test

class Day14Test {

    @Test
    fun `should solve quiz`() {
        // given
        val inputA1 = """
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....
        """.trimIndent()


        // when
        val resultA1 = Day14a.solve(inputA1.lines())

        // then
        assert(136L == resultA1)
    }
}
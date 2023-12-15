package pl.simplecoding.aoc

import org.junit.jupiter.api.Test

class Day13Test {

    @Test
    fun `should solve quiz`() {
        // given
        val inputA1 = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.
            
            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#
        """.trimIndent()

        val inputA2 = """
            #...##.#.##
            #...##.#.##
            .###..#.#.#
            ##.###..#..
            ..#...##..#
            ....##.####
            ##.###.#.#.
            ##.###...#.
            ....##.####
            ..#...##..#
            ##.###..#..
            .###..#.#.#
            #...##.#.##
        """.trimIndent()



        // when
        val resultA1 = Day13a.solve(inputA1.lines())
        val resultA2 = Day13a.solve(inputA2.lines())

        // then
        assert(405 == resultA1)
        assert(100 == resultA2)
    }
}
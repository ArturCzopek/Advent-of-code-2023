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

        val inputB1 = """
            ...........
            .F-------7.
            .|F-----7|.
            .||.....||.
            .||.....||.
            .|L-7.F-J|.
            .|..S.|..|.
            .L--J.L--J.
            ...........
        """.trimIndent()

        val inputB2 = """
            FF7F7F7F7F7F7F7F---7
            L|LJ||||||||||||F--J
            FL-7LJLJ||||||LJL-77
            F--JF--7||LJLJ7F7FJ-
            L---JF-JLS.||-FJLJJ7
            |F|F-JF---7F7-L7L|7|
            |FFJF7L7F-JF7|JL---7
            7-L-JL7||F7|L7F-7F7|
            L.L7LFJ|||||FJL7||LJ
            L7JLJL-JLJLJL--JLJ.L
        """.trimIndent()

        // when
        val resultA1 = Day10a.solve(inputA1.lines())
        val resultA2 = Day10a.solve(inputA2.lines())
        val resultB1 = Day10b.solve(inputB1.lines())
        val resultB2 = Day10b.solve(inputB2.lines())

        // then
        assert(4L == resultA1)
        assert(8L == resultA2)
        assert(4L == resultB1)
        assert(10L == resultB2)
    }
}
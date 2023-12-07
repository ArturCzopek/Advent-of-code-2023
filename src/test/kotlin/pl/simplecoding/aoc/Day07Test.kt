package pl.simplecoding.aoc

import org.junit.jupiter.api.Test

class Day07Test {

    @Test
    fun `should solve quiz`() {
        // given
        val input = """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
        """.trimIndent()

        // when
        val resultA = Day07a.solve(input.lines())
//        val resultB = Day07b.solve(input.lines())

        // then
        assert(6440L == resultA)
//        assert(71503L == resultB)
    }
}
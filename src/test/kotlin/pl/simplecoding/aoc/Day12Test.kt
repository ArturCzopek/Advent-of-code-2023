package pl.simplecoding.aoc

import org.junit.jupiter.api.Test

class Day12Test {

    @Test
    fun `should solve quiz`() {
        // given
        val input = """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
        """.trimIndent()

        // when
        val resultA = Day12a.solve(input.lines())
//        val resultB = Day12b.solve(input.lines())

        // then
        assert(21L == resultA)
//        assert(1030L == resultB)
    }
}
package pl.simplecoding.aoc

import org.junit.jupiter.api.Test

class Day08Test {

    @Test
    fun `should solve quiz`() {
        // given
        val input1 = """
            RL
            
            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent()
        val input2 = """
            LLR
            
            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent()


        // when
        val resultA1 = Day08a.solve(input1.lines())
        val resultA2 = Day08a.solve(input2.lines())
//        val resultB = Day08b.solve(input.lines())

        // then
        assert(2L == resultA1)
        assert(6L == resultA2)
//        assert(5905L == resultB)
    }
}
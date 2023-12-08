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
        val input3 = """
            LR

            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
        """.trimIndent()


        // when
        val resultA1 = Day08a.solve(input1.lines())
        val resultA2 = Day08a.solve(input2.lines())
        val resultB = Day08b.solve(input3.lines())

        // then
        assert(2L == resultA1)
        assert(6L == resultA2)
        assert(6L == resultB)
    }
}
package pl.simplecoding.aoc

import org.junit.jupiter.api.Test

class AoC03bTest {

    @Test
    fun `should solve quiz`() {
        // given
        val fileContent = """
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

        val result = AoC03b.solve(fileContent)

        assert(467835L == result)
    }
}
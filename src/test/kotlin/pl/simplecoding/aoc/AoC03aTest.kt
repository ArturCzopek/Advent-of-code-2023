package pl.simplecoding.aoc

import org.junit.jupiter.api.Test

class AoC03aTest {

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

        val result = AoC03a.solve(fileContent)

        assert(4361 == result)
    }
}
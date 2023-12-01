package pl.simplecoding.aoc

import org.junit.jupiter.api.Test

class AoC01aTest {

    @Test
    fun `should solve quiz`() {
        // given
        val fileContent = """
            sq5fivetwothree1
            six5gc
            txb3qfzsbzbxlzslfourone1vqxgfive
        """.trimIndent()

        val result = AoC01a.solve(fileContent)

        assert(137 == result)
    }
}
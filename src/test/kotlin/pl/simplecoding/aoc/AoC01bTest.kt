package pl.simplecoding.aoc

import org.junit.jupiter.api.Test

class AoC01bTest {

    @Test
    fun `should solve quiz`() {
        // given
        val fileContent = """
            sq5fivetwothree1
            six5gc
            txb3qfzsbzbxlzslfourone1vqxgfive
        """.trimIndent()

        val result = AoC01b.solve(fileContent)

        assert(151 == result)
    }
}
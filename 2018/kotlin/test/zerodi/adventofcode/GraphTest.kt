package zerodi.adventofcode

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import kotlin.test.assertEquals

internal class GraphTest {
    lateinit var subject: Graph

    @BeforeEach
    fun setup() {
        subject = Graph()
    }

    @Test
    fun letterValue() {
        assertEquals(61, subject.letterValue("A"))
        assertEquals(62, subject.letterValue("B"))
        assertEquals(63, subject.letterValue("C"))
        assertEquals(86, subject.letterValue("Z"))
    }
}
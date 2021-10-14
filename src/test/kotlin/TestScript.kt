import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.test.assertEquals
import kotlin.test.*

internal class TestScript {
    private val standardOut = System.out
    private val stream = ByteArrayOutputStream()

    @BeforeTest
    fun setUp() {
        System.setOut(PrintStream(stream))
    }

    @AfterTest
    fun tearDown() {
        System.setOut(standardOut)
    }

    private fun databaseSystemTest(scriptPath : String, resPath : String) {
        script(scriptPath)
        val actualRes = stream.toString().trim().lines()
        val expectedRes = File(resPath).readLines()
        assertEquals(expectedRes, actualRes)
    }

    @Test
    fun databaseSystemShortTest() {
        databaseSystemTest("TestData/DatabaseSystemShortTest", "TestData/DatabaseSystemShortTest.result")
    }
}
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

/* There I will use those redactions: A - assign, R - remove, C - contains, V - value, D - delete */

fun testDatabase(database: DatabaseInterface, commandsPath : String) {
    require(File(commandsPath).exists())
    require(File("${commandsPath}.result").exists())

    val commands = File(commandsPath).readLines()
    val result = File("${commandsPath}.result").readLines()
    var iInRes = 0
    var iInCom = 0
    while (iInCom < commands.size) {
        when (commands[iInCom]) {
            "assign" -> {
                database.assignValue(commands[iInCom + 1], commands[iInCom + 2])
                iInCom += 3
            }
            "contains" -> {
                assertEquals(result[iInRes++], database.contains(commands[iInCom + 1]).toString())
                iInCom += 2
            }
            "value" -> {
                assertEquals(result[iInRes++], database.getValue(commands[iInCom + 1]).toString())
                iInCom += 2
            }
            "remove" -> {
                assertEquals(result[iInRes++], database.removeElement(commands[iInCom + 1]).toString())
                iInCom += 2
            }
            "allContent" -> {
                val content = database.allContent()
                for (i in 0 until 2 * content.size)
                    assertEquals(result[iInRes++], content[i / 2].split('\n')[i % 2])
                iInCom++
            }
            "clear" -> {
                database.clear()
                iInCom++
            }
            else -> throw Exception("Wrong test: line $iInCom(${commands[iInCom]})")
        }
    }
}

internal class TestArrayDatabase {
    private val database = ArrayDatabase("TestData/test.db")

    @Test
    fun testAC() {
        testDatabase(database, "TestData/AC")
    }

    @Test
    fun testAV() {
        testDatabase(database, "TestData/AV")
    }

    @Test
    fun testAVRC() {
        testDatabase(database, "TestData/AVRC")
    }

    @Test
    fun testAll() {
        testDatabase(database, "TestData/All")
    }
}

internal class TestMapDatabase {
    private val database = MapDatabase("TestData/test.db")

    @Test
    fun testAC() {
        testDatabase(database, "TestData/AC")
    }

    @Test
    fun testAV() {
        testDatabase(database, "TestData/AV")
    }

    @Test
    fun testAVRC() {
        testDatabase(database, "TestData/AVRC")
    }

    @Test
    fun testAll() {
        testDatabase(database, "TestData/All")
    }
}
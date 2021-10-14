import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

fun testDatabase(database: Database, commandsPath : String) {
    require(File(commandsPath).exists())
    require(File("${commandsPath}.result").exists())

    val commands = File(commandsPath).readLines()
    val result = File("${commandsPath}.result").readLines()
    var iInRes = 0
    var iInCom = 0
    while (iInCom < commands.size) {
        when (commands[iInCom]) {
            Commands.ASSIGN.command -> {
                database.assignValue(commands[iInCom + 1], commands[iInCom + 2])
                iInCom += 3
            }
            Commands.CONTAINS.command -> {
                assertEquals(result[iInRes++], database.contains(commands[iInCom + 1]).toString())
                iInCom += 2
            }
            Commands.VALUE.command -> {
                assertEquals(result[iInRes++], database.getValue(commands[iInCom + 1]).toString())
                iInCom += 2
            }
            Commands.REMOVE.command -> {
                assertEquals(result[iInRes++], database.removeElement(commands[iInCom + 1]).toString())
                iInCom += 2
            }
            Commands.ALL_CONTENT.command -> {
                val content = database.allContent()
                for (i in content.indices) {
                    assertEquals(result[iInRes++], content[i].first)
                    assertEquals(result[iInRes++], content[i].second)
                }
                iInCom++
            }
            Commands.CLEAR.command -> {
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
    fun testAssignContains() {
        testDatabase(database, "TestData/AC")
    }

    @Test
    fun testAssignValue() {
        testDatabase(database, "TestData/AV")
    }

    @Test
    fun testAssignValueRemoveContains() {
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
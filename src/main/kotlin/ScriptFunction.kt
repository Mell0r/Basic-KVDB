import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.system.exitProcess

val innerCommands = listOf("assign", "contains", "value", "remove", "allContent", "clear", "save", "close",
    "lastActions", "allActions")

/** Prints message about error in line 'ind' with value 'str' in script */
fun printScriptErrorMessageAndExit(ind : Int, str : String) {
    println("Error. Script is incorrect: line $ind(${str})")
    exitProcess(-1)
}

/** Asks user for path to script and calls script on it */
fun scriptUI() {
    print("Enter path to script or 'back': ")
    var input = readLine()
    while (input == null || input == "" || (input != "back" && !File(input).exists())) {
        when (input) {
            null, "" -> print("Empty input. Please, try again: ")
            else -> print("Script with this name doesn't exist. Please, try again: ")
        }
        input = readLine()
    }
    if (input == "back")
        return
    script(input)
}

/** Execute script with given path. Commands from here repeat commands from 'main',
 * without UI and with correctness check */
fun script(scriptPath : String) {
    val script = File(scriptPath).readLines().map{ str -> str.filter{ it != ' ' } }
    var ind = 0
    while (ind < script.size) {
        if (innerCommands.contains(script[ind]) && currentDatabase == null)
            printScriptErrorMessageAndExit(ind, script[ind])
        when (script[ind]) {
            "assign" -> {
                if (ind + 2 >= script.size)
                    printScriptErrorMessageAndExit(ind, script[ind])
                currentDatabase!!.assignValue(script[ind + 1], script[ind + 2])
                ind += 3
            }
            "contains" -> {
                if (ind + 1 >= script.size)
                    printScriptErrorMessageAndExit(ind, script[ind])
                println(currentDatabase!!.contains(script[ind + 1]))
                ind += 2
            }
            "value" -> {
                if (ind + 1 >= script.size)
                    printScriptErrorMessageAndExit(ind, script[ind])
                println(currentDatabase!!.getValue(script[ind + 1]))
                ind += 2
            }
            "remove" -> {
                if (ind + 1 >= script.size)
                    printScriptErrorMessageAndExit(ind, script[ind])
                println(currentDatabase!!.removeElement(script[ind + 1]))
                ind += 2
            }
            "allContent" -> {
                val content = currentDatabase!!.allContent()
                for (elem in content)
                    println(elem)
                ind++
            }
            "clear" -> {
                currentDatabase!!.clear()
                ind++
            }
            "lastActions" -> {
                if (ind + 1 >= script.size)
                    printScriptErrorMessageAndExit(ind, script[ind])
                if (script[ind + 1].toIntOrNull() == null)
                    printScriptErrorMessageAndExit(ind + 1, script[ind + 1])
                val quantity = script[ind + 1]
                val journal = File(currentDatabase!!.journalPath()).readLines()
                for (i in max(0, journal.size - quantity.toInt()) until journal.size) {
                    println(journal[i])
                }
                ind += 2
            }
            "allActions" -> {
                val journal = File(currentDatabase!!.journalPath()).readLines()
                for (elem in journal)
                    println(elem)
                ind++
            }
            "create" -> {
                if (currentDatabase != null)
                    closeDatabase()
                if (ind + 1 >= script.size)
                    printScriptErrorMessageAndExit(ind, script[ind])
                val path = pathByName(script[ind + 1])
                if (path == "" || File(path).exists())
                    printScriptErrorMessageAndExit(ind + 1, script[ind + 1])

                File(path).createNewFile()
                File("${path}Journal").createNewFile()
                val currentTime = SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date())
                File("${path}Journal").writeText("Created: $currentTime\n")

                ind += 2
            }
            "open" -> {
                if (currentDatabase != null)
                    closeDatabase()
                if (ind + 2 >= script.size)
                    printScriptErrorMessageAndExit(ind, script[ind])
                val path = pathByName(script[ind + 1])
                if (path == "" || !File(path).exists())
                    printScriptErrorMessageAndExit(ind + 1, script[ind + 1])
                if (script[ind + 2] != "a" && script[ind + 2] != "m")
                    printScriptErrorMessageAndExit(ind + 2, script[ind + 2])

                when (script[ind + 2]) {
                    "a" -> currentDatabase = ArrayDatabase(path)
                    "m" -> currentDatabase = MapDatabase(path)
                }
                printActionToJournal("Opened")

                ind += 3
            }
            "delete" -> {
                if (currentDatabase != null)
                    closeDatabase()
                if (ind + 1 >= script.size)
                    printScriptErrorMessageAndExit(ind, script[ind])
                val path = pathByName(script[ind + 1])
                if (path == "" || !File(path).exists())
                    printScriptErrorMessageAndExit(ind + 1, script[ind + 1])

                File(path).delete()
                File("${path}Journal").delete()

                ind += 2
            }
            "save" -> {
                currentDatabase?.save()
                ind++
            }
            "close" -> {
                closeDatabase()
                ind++
            }
            "exit" -> {
                closeDatabase()
                exitProcess(0)
            }
            else -> {
                println("Error. Script is incorrect: line $ind(${script[ind]})")
                exitProcess(-1)
            }
        }
    }
}

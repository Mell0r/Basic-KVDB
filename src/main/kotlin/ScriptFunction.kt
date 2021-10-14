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
    throw Exception("Wrong script")
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
            Commands.ASSIGN.command -> {
                if (ind + 2 >= script.size)
                    printScriptErrorMessageAndExit(ind, script[ind])
                currentDatabase!!.assignValue(script[ind + 1], script[ind + 2])
                ind += 3
            }
            Commands.CONTAINS.command -> {
                if (ind + 1 >= script.size)
                    printScriptErrorMessageAndExit(ind, script[ind])
                println(currentDatabase!!.contains(script[ind + 1]))
                ind += 2
            }
            Commands.VALUE.command -> {
                if (ind + 1 >= script.size)
                    printScriptErrorMessageAndExit(ind, script[ind])
                println(currentDatabase!!.getValue(script[ind + 1]))
                ind += 2
            }
            Commands.REMOVE.command -> {
                if (ind + 1 >= script.size)
                    printScriptErrorMessageAndExit(ind, script[ind])
                println(currentDatabase!!.removeElement(script[ind + 1]))
                ind += 2
            }
            Commands.ALL_CONTENT.command -> {
                val content = currentDatabase!!.allContent()
                for (elem in content)
                    println(elem)
                ind++
            }
            Commands.CLEAR.command -> {
                currentDatabase!!.clear()
                ind++
            }
            Commands.LAST_ACTIONS.command -> {
                if (ind + 1 >= script.size)
                    printScriptErrorMessageAndExit(ind, script[ind])
                if (script[ind + 1].toIntOrNull() == null)
                    printScriptErrorMessageAndExit(ind + 1, script[ind + 1])
                lastActions(script[ind + 1].toInt()).forEach { println(it) }
                ind += 2
            }
            Commands.ALL_ACTIONS.command -> {
                allActions().forEach { print(it)}
                ind++
            }
            Commands.CREATE.command -> {
                if (ind + 1 >= script.size)
                    printScriptErrorMessageAndExit(ind, script[ind])
                try { createDatabase(script[ind + 1]) }
                catch(e : Exception) { printScriptErrorMessageAndExit(ind + 1, script[ind + 1]) }
                ind += 2
            }
            Commands.OPEN.command -> {
                if (ind + 2 >= script.size)
                    printScriptErrorMessageAndExit(ind, script[ind])
                try { openDatabase(script[ind + 1], script[ind + 2]) }
                catch(e : Exception) { printScriptErrorMessageAndExit(ind, script[ind]) }
                ind += 3
            }
            Commands.DELETE.command -> {
                if (ind + 1 >= script.size)
                    printScriptErrorMessageAndExit(ind, script[ind])
                try { deleteDatabase(script[ind + 1]) }
                catch(e : Exception) { printScriptErrorMessageAndExit(ind, script[ind]) }
                ind += 2
            }
            Commands.SAVE.command -> {
                currentDatabase?.save()
                ind++
            }
            Commands.CLOSE.command -> {
                closeDatabase()
                ind++
            }
            Commands.EXIT.command -> {
                closeDatabase()
                exitProcess(0)
            }
            else -> printScriptErrorMessageAndExit(ind, script[ind])
        }
    }
}

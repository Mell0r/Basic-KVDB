import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max

class Element(val key : String, val value: String)

var currentDatabase : DatabaseInterface? = null

fun printManual() {
    println("Available commands:")
    println("* 'create' - create new database wth given name.")
    println("* 'open' - open database by given mode. Processing mods are 'a'(Array) or 'm'(Map). Chosen mod " +
            "defines how data will store in RAM. Enter this command before work with database.")
    println("* 'assign' - assign given value to given key in the opened database. If element  with given key " +
            "already exists, it's value rewrites.")
    println("* 'remove' - remove element from the database. If element doesn't exist, nothing happens.")
    println("* 'value' - get value by the key. If element with this key doesn't exist, print error message.")
    println("* 'contains' - print 'true', then database contains given element, and 'false' otherwise.")
    println("* 'allContent' - print all content in the open database.")
    println("* 'clear' - clear opened database.")
    println("* 'script' - complete all commands in the given text file.")
    println("* 'save' - save changes in the opened database in the file 'data.txt'.")
    println("* 'close' - save and close the opened database.")
    println("* 'lastActions' - print given number of previous actions with the opened database.")
    println("* 'allActions' - print all previous actions with the opened database")
    println("* 'exit' - close the utility. Please, do not close the utility by any other " +
            "methods, otherwise all unsaved changes will be lost")
}

fun readString(name : String) : String {
    print("Enter $name: ")
    var s = readLine()
    while (s == null) {
        print("Wrong input. Please, try again: ")
        s = readLine()
    }
    return s
}

fun checkNotOpened() : Boolean {
    return if (currentDatabase == null) {
        println("Nothing opened now. Print 'open' first.")
        true
    }
    else false
}

fun printActionToJournal(action : String) {
    if (currentDatabase == null)
        return
    val currentTime = SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date())
    File(currentDatabase!!.journalName()).appendText("$action: $currentTime\n")
}

fun createDatabase() {
    print("Enter database name or 'back': ")
    var input = readLine()
    while (input == null || input == "" || (input != "back" && File(input).exists())) {
        when (input) {
            "", null -> print("Empty input. Please, try again: ")
            else -> print("Database with this name already exists. Please, try again: ")
        }
        input = readLine()
    }
    if (input == "back")
        return
    File(input).createNewFile()
    File("${input}Journal").createNewFile()
    val currentTime = SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date())
    File("${input}Journal").writeText("Created: $currentTime\n")
}

fun openDatabase() {
    if (currentDatabase != null)
        close()
    print("Enter database name or 'back': ")
    var input = readLine()
    while (input == null || input == "" || (input != "back" && !File(input).exists())) {
        when (input) {
            null, "" -> print("Empty input. Please, try again: ")
            else -> print("Database with this name doesn't exist. Please, try again: ")
        }
        input = readLine()
    }
    if (input == "back")
        return

    print("Enter processing mod ('a' for array or 'm' for map): ")
    var processingMode = readLine()
    while (processingMode != "a" && processingMode != "m") {
        print("Wrong input. Please, try again: ")
        processingMode = readLine()
    }
    when (processingMode) {
        "a" -> currentDatabase = ArrayDatabase(input)
        "m" -> currentDatabase = MapDatabase(input)
    }
    printActionToJournal("Opened")
}

fun assignValue() {
    if (checkNotOpened())
        return
    val key = readString("key")
    val value = readString("value")
    currentDatabase?.assignValue(Element(key, value))
}

fun removeElement() {
    if (checkNotOpened())
        return
    val key = readString("key")
    val value = currentDatabase?.removeElement(key)
    if (value == null)
        println("Error! Element with this key doesn't exist.")
    else
        println("Removed value: $value")
}

fun getValue() {
    if (checkNotOpened())
        return
    val key = readString("key")
    val value = currentDatabase?.getValue(key)
    if (value == null)
        println("Error! Element with this key doesn't exist.")
    else
        println(value)
}

fun contains() {
    if (checkNotOpened())
        return
    val key = readString("key")
    println(currentDatabase?.contains(key))
}

fun scriptInterface() {
    val path = readString("path to script")
    TODO()
}

fun lastActions() {
    if (currentDatabase == null) {
        println("Nothing opened now. Print 'open' first.")
        return
    }
    print("Enter actions quantity: ")
    var quantity = readLine()
    while (quantity == null || quantity.toIntOrNull() == null) {
        print("Wrong input. Please, try again: ")
        quantity = readLine()
    }
    val journal = File(currentDatabase!!.journalName()).readLines()
    for (i in max(0, journal.size - quantity.toInt()) until journal.size) {
        println(journal[i])
    }
}

fun printAllActions() {
    if (checkNotOpened())
        return
    File(currentDatabase!!.journalName()).readLines().forEach {
        println(it)
    }
}

fun save() {
    if (checkNotOpened())
        return
    currentDatabase?.save()
}

fun printAllContent() {
    if (checkNotOpened())
        return
    currentDatabase?.printAllContent()
}

fun clear() {
    if (checkNotOpened())
        return
    currentDatabase?.clear()
}

fun close() {
    if (checkNotOpened())
        return
    currentDatabase?.save()
    currentDatabase?.clear()
    printActionToJournal("Closed")
}

fun exit() {
    close()
}

fun main() {
    println("Hello User! To see help enter 'help'.")
    var command = readLine()
    while (command != null && command != "exit") {
        when (command) {
            "help" -> printManual()
            "create" -> createDatabase()
            "open" -> openDatabase()
            "assign" -> assignValue()
            "remove" -> removeElement()
            "value" -> getValue()
            "script" -> scriptInterface()
            "contains" -> contains()
            "allContent" -> printAllContent()
            "save" -> save()
            "clear" -> clear()
            "close" -> {
                close()
                currentDatabase = null
            }
            "lastActions" -> lastActions()
            "allActions" -> printAllActions()
            "exit" -> exit()
            else -> println("Wrong command, please try again")
        }
        command = readLine()
    }
    return
}
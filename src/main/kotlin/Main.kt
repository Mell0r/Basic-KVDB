import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max

var currentDatabase : DatabaseInterface? = null //current opened database. It is null, if none is open.

/** Prints short manual */
fun printManual() {
    println("Available commands:")
    println("* 'createDb' - create new database wth given name.")
    println("* 'openDb' - open database with given name by given mode. Processing mods are 'a'(Array) or 'm'(Map). Chosen mod " +
            "defines how data will store in RAM. Enter this command before work with database.")
    println("* 'deleteDb' - delete database with given name.")
    println("* 'clearDb' - clear opened database.")
    println("* 'saveDb' - save changes in the opened database in the file 'data.txt'.")
    println("* 'closeDb' - save and close the opened database.")
    println("* 'assign' - assign given value to given key in the opened database. If element  with given key " +
            "already exists, its value rewrites.")
    println("* 'remove' - remove element from the database. If element doesn't exist, nothing happens.")
    println("* 'value' - get value by the key. If element with this key doesn't exist, print error message.")
    println("* 'contains' - print 'true', then database contains given element, and 'false' otherwise.")
    println("* 'allContent' - print all content in the open database.")
    println("* 'script' - complete all commands in the given text file.")
    println("* 'lastActions' - print given number of previous actions with the opened database.")
    println("* 'allActions' - print all previous actions with the opened database")
    println("* 'exit' - close the utility. Please, do not close the utility by any other " +
            "methods, otherwise all unsaved changes will be lost")
}

/** Interacting with user function. Asks user for string with given name with null assertion */
fun readString(name : String) : String {
    print("Enter $name: ")
    var s = readLine()
    while (s == null) {
        print("Wrong input. Please, try again: ")
        s = readLine()
    }
    return s
}

/** Returns path to all data by its name */
fun pathByName(name : String) = "Data\\$name"

/** Returns true and prints notification, if none is opened now */
fun checkNotOpened() : Boolean {
    return if (currentDatabase == null) {
        println("Nothing opened now. Print 'open' first.")
        true
    }
    else false
}

/** Prints given action and current time to opened database journal */
fun printActionToJournal(action : String) {
    if (currentDatabase == null)
        return
    val currentTime = SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date())
    File(currentDatabase!!.journalPath()).appendText("$action: $currentTime\n")
}

/** Interacting with user function. Asks the name to the new database and creates it */
fun createDatabase() {
    print("Enter database name or 'back': ")
    var input = readLine()
    while (input == null || input == "" || (input != "back" && File(pathByName(input)).exists())) {
        when (input) {
            "", null -> print("Empty input. Please, try again: ")
            else -> print("Database with this name already exists. Please, try again: ")
        }
        input = readLine()
    }
    if (input == "back")
        return
    File(pathByName(input)).createNewFile()
    File("${pathByName(input)}Journal").createNewFile()
    val currentTime = SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date())
    File("${pathByName(input)}Journal").writeText("Created: $currentTime\n")
}

/** Interacting with user function. Closes current database, if it is open, then
 * asks the name of the database and opens it, assigning it to 'currentDatabase' */
fun openDatabase() {
    if (currentDatabase != null)
        closeDatabase()
    print("Enter database name or 'back': ")
    var input = readLine()
    while (input == null || input == "" || (input != "back" && !File(pathByName(input)).exists())) {
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
        "a" -> currentDatabase = ArrayDatabase(pathByName(input))
        "m" -> currentDatabase = MapDatabase(pathByName(input))
    }
    printActionToJournal("Opened")
}

/** Interacting with user function. Asks the name of the database and delete it */
fun deleteDatabase() {
    print("Enter database name or 'back': ")
    var input = readLine()
    while (input == null || input == "" || (input != "back" && !File(pathByName(input)).exists())) {
        when (input) {
            null, "" -> print("Empty input. Please, try again: ")
            else -> print("Database with this name doesn't exist. Please, try again: ")
        }
        input = readLine()
    }
    if (input == "back")
        return

    File(pathByName(input)).delete()
    File("${pathByName(input)}Journal").delete()
}

/** Checks database by 'checkNotOpened', asks user for key and value by 'readString' and calls 'assignValue'
 * of the current database, if it is opened */
fun assignValue() {
    if (checkNotOpened())
        return
    val key = readString("key")
    val value = readString("value")
    currentDatabase?.assignValue(key, value)
}

/** Checks database by 'checkNotOpened', asks user for key by 'readString' and calls 'removeElement'
 * of the current database, if it is opened */
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

/** Checks database by 'checkNotOpened', asks user for key by 'readString' and calls 'getValue'
 * of the current database, if it is opened */
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

/** Checks database by 'checkNotOpened', asks user for key by 'readString' and calls 'contains'
 *  of the current database, if it is opened */
fun contains() {
    if (checkNotOpened())
        return
    val key = readString("key")
    println(currentDatabase?.contains(key))
}

fun script() {
    TODO()
}

/** Interacting with user function. Checks database by 'checkNotOpened', asks user for quantity
 * and prints given number of actions with database, if it ts opened */
fun lastActions() {
    if (checkNotOpened())
        return
    print("Enter actions quantity: ")
    var quantity = readLine()
    while (quantity == null || quantity.toIntOrNull() == null) {
        print("Wrong input. Please, try again: ")
        quantity = readLine()
    }
    val journal = File(currentDatabase!!.journalPath()).readLines()
    for (i in max(0, journal.size - quantity.toInt()) until journal.size) {
        println(journal[i])
    }
}

/** Checks database by 'checkNotOpened' and print all actions with database, if it is open */
fun printAllActions() {
    if (checkNotOpened())
        return
    File(currentDatabase!!.journalPath()).readLines().forEach {
        println(it)
    }
}

/** Checks database by 'checkNotOpened' and calls 'save' for it, if it is open */
fun save() {
    if (checkNotOpened())
        return
    currentDatabase?.save()
}

/** Checks database by 'checkNotOpened' and call 'printAllContent' for it, if it is open */
fun printAllContent() {
    if (checkNotOpened())
        return
    val content = currentDatabase!!.allContent()
    for (elem in content) {
        println("Key: ${elem.split('\n')[0]}")
        println("Value: ${elem.split('\n')[1]}")
    }
}

/** Checks database by 'checkNotOpened' and call 'clear' for it, if it is open */
fun clearDatabase() {
    if (checkNotOpened())
        return
    currentDatabase?.clear()
}

/** Checks database by 'checkNotOpened' and call 'save' for it, if it is open, then assign null to currentDatabase */
fun closeDatabase() {
    if (checkNotOpened())
        return
    currentDatabase?.save()
    currentDatabase = null
    printActionToJournal("Closed")
}

fun main() {
    File("Data").mkdir()
    println("Hello User! To see help enter 'help'.")
    var command = readLine()
    while (command != null && command != "exit") {
        when (command) {
            "help" -> printManual()
            "createDb" -> createDatabase()
            "deleteDb" -> deleteDatabase()
            "openDb" -> openDatabase()
            "saveDb" -> save()
            "clearDb" -> clearDatabase()
            "closeDb" -> closeDatabase()
            "assign" -> assignValue()
            "remove" -> removeElement()
            "value" -> getValue()
            "script" -> script()
            "contains" -> contains()
            "allContent" -> printAllContent()
            "lastActions" -> lastActions()
            "allActions" -> printAllActions()
            "exit" -> closeDatabase()
            else -> println("Wrong command, please try again")
        }
        command = readLine()
    }
    return
}
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.max
import kotlin.system.exitProcess

var currentDatabase : Database? = null //current opened database. It is null, if none is open.

/** Prints short manual */
fun printManual() {
    println("""Available commands:
    * 'create' - create new database wth given name.
    * 'open' - open database with given name by given mode. Processing mods are 'a'(Array) or 'm'(Map). 
        Chosen mod defines how data will store in RAM. Enter this command before work with database.
    * 'delete' - delete database with given name.
    * 'save' - save changes in the opened database.
    * 'close' - save and close the opened database.
    * 'assign' - assign given value to given key in the opened database. 
        If element  with given key already exists, its value rewrites.
    * 'remove' - remove element from the database. If element doesn't exist, nothing happens.
    * 'value' - get value by the key. If element with this key doesn't exist, print error message.
    * 'contains' - print 'true', then database contains given element, and 'false' otherwise.
    * 'clear' - clear opened database.
    * 'allContent' - print all content in the open database.
    * 'script' - complete all commands in the given text file.
    * 'lastActions' - print given number of previous actions with the opened database.
    * 'allActions' - print all previous actions with the opened database.
    * 'script' - execute all commands line by line from given file. For more information, check README."
    * 'exit' - close the utility. Please, do not close the utility by any other methods, 
        otherwise all unsaved changes will be lost.""")
}

enum class Commands(val command : String) {
    HELP("help"),
    CREATE("create"),
    OPEN("open"),
    DELETE("delete"),
    SAVE("save"),
    CLOSE("close"),
    ASSIGN("assign"),
    REMOVE("remove"),
    VALUE("value"),
    CONTAINS("contains"),
    CLEAR("clear"),
    SCRIPT("script"),
    ALL_CONTENT("allContent"),
    LAST_ACTIONS("lastActions"),
    ALL_ACTIONS("allActions"),
    EXIT("exit")
}

/** Asks user for string with given name with null assertion */
fun readString(name : String) : String {
    print("Enter $name: ")
    var s = readLine()
    while (s == null) {
        print("Wrong input. Please, try again: ")
        s = readLine()
    }
    return s
}

/** Returns true and prints notification, if none is opened now */
fun checkNotOpened() : Boolean {
    return if (currentDatabase == null) {
        println("Nothing opened now. Print 'open' first.")
        true
    }
    else false
}

/** Asks the name to the new database */
fun readNotExistingDatabaseName() : String {
    print("Enter database name or 'back': ")
    var input = readLine()
    while (input == null || input == "" || (input != "back" && File(pathByName(input)).exists())) {
        when (input) {
            "", null -> print("Empty input. Please, try again: ")
            else -> print("Database with this name already exists. Please, try again: ")
        }
        input = readLine()
    }
    return input
}

/** Asks the name to the existing database */
fun readExistingDatabaseName() : String {
    print("Enter database name or 'back': ")
    var input = readLine()
    while (input == null || input == "" || (input != "back" && !File(pathByName(input)).exists())) {
        when (input) {
            null, "" -> print("Empty input. Please, try again: ")
            else -> print("Database with this name doesn't exist. Please, try again: ")
        }
        input = readLine()
    }
    return input
}

/** Asks the processing mode to the database */
fun readProcessingMode() : String {
    print("Enter processing mod ('a' for array or 'm' for map): ")
    var processingMode = readLine()
    while (processingMode != "a" && processingMode != "m") {
        print("Wrong input. Please, try again: ")
        processingMode = readLine()
    }
    return processingMode
}

fun createDatabaseUI() {
    val name = readNotExistingDatabaseName()
    if (name != "back")
        createDatabase(name)
}

/** Asks the name of the database and calls 'deleteDatabase' by it */
fun deleteDatabaseUI() {
    val name = readExistingDatabaseName()
    if (name != "back")
        deleteDatabase(name)
}

/** Asks the name of the database and the processingMode and calls 'openDatabase' by it */
fun openDatabaseUI() {
    val name = readExistingDatabaseName()
    if (name != "back")
        openDatabase(name, readProcessingMode())
}

/** Checks database by 'checkNotOpened', asks user for key and value by 'readString' and calls 'assignValue'
 * of the current database, if it is opened */
fun assignValueUI() {
    if (checkNotOpened())
        return
    val key = readString("key")
    val value = readString("value")
    currentDatabase?.assignValue(key, value)
}

/** Checks database by 'checkNotOpened', asks user for key by 'readString' and calls 'removeElement'
 * of the current database, if it is opened */
fun removeElementUI() {
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
fun getValueUI() {
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
fun containsUI() {
    if (checkNotOpened())
        return
    val key = readString("key")
    println(currentDatabase?.contains(key))
}

/** Interacting with user function. Checks database by 'checkNotOpened', asks user for quantity
 * and prints given number of actions with database, if it ts opened */
fun lastActionsUI() {
    if (checkNotOpened())
        return
    print("Enter actions quantity: ")
    var quantity = readLine()
    while (quantity == null || quantity.toIntOrNull() == null) {
        print("Wrong input. Please, try again: ")
        quantity = readLine()
    }
    lastActions(quantity.toInt()).forEach {
        print(it)
    }
}

/** Checks database by 'checkNotOpened' and print all actions with database, if it is open */
fun printAllActions() {
    if (checkNotOpened())
        return
    allActions().forEach{ print(it) }
}

/** Checks database by 'checkNotOpened' and calls 'save' for it, if it is open */
fun saveUI() {
    if (checkNotOpened())
        return
    currentDatabase?.save()
}

/** Checks database by 'checkNotOpened' and call 'printAllContent' for it, if it is open */
fun printAllContentUI() {
    if (checkNotOpened())
        return
    val content = currentDatabase!!.allContent()
    for (elem in content) {
        println("Key: ${elem.first}")
        println("Value: ${elem.second}")
    }
}

/** Checks database by 'checkNotOpened' and call 'clear' for it, if it is open */
fun clearDatabaseUI() {
    if (checkNotOpened())
        return
    currentDatabase?.clear()
}

/** Checks database by 'checkNotOpened' and call 'save' for it, if it is open, then assign null to currentDatabase */
fun closeDatabaseUI() {
    if (checkNotOpened())
        return
    closeDatabase()
}

fun main() {
    File("Data").mkdir()
    println("Hello User! To see help enter 'help'.")
    var command = readLine()
    while (command != null && command != "exit") {
        when (command) {
            Commands.HELP.command -> printManual()
            Commands.CREATE.command -> createDatabaseUI()
            Commands.DELETE.command -> deleteDatabaseUI()
            Commands.OPEN.command -> openDatabaseUI()
            Commands.SAVE.command -> saveUI()
            Commands.CLEAR.command -> clearDatabaseUI()
            Commands.CLOSE.command -> closeDatabaseUI()
            Commands.ASSIGN.command -> assignValueUI()
            Commands.REMOVE.command -> removeElementUI()
            Commands.VALUE.command -> getValueUI()
            Commands.SCRIPT.command -> scriptUI()
            Commands.CONTAINS.command -> containsUI()
            Commands.ALL_CONTENT.command -> printAllContentUI()
            Commands.LAST_ACTIONS.command -> lastActionsUI()
            Commands.ALL_ACTIONS.command -> printAllActions()
            Commands.EXIT.command -> closeDatabaseUI()
            else -> println("Wrong command, please try again")
        }
        command = readLine()
    }
    return
}
import java.io.File
import kotlin.math.max

fun printManual() {
    println("Available commands:")
    println("* 'init' - initialize database. Processing mods are 'a'(Array) or 'm'(Map). Chosen mod defines how data " +
            "will store in RAM. Enter this command before work with database.")
    println("* 'add' - add element in the database. If element already exists, it's value rewrites.")
    println("* 'remove' - remove element from the database. If element doesn't exist, nothing happens.")
    println("* 'value' - get value by the key. If element with this key doesn't exist, print error message.")
    println("* 'contains' - print 'true', then database contains given element, and 'false' otherwise.")
    println("* 'allContent' - print all content in the open database.")
    println("* 'script' - complete all commands in the given text file.")
    println("* 'save' - save changes in the file 'data.txt'.")
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

fun initDatabaseInterface() : String {
    print("Enter processing mod ('a' for array or 'm' for map): ")
    var processingMode = readLine()
    while (processingMode != "a" && processingMode != "m") {
        print("Wrong input. Please, try again: ")
        processingMode = readLine()
    }
    initDatabase(processingMode)
    return processingMode
}

fun addElementInterface(processingMode : String) {
    if (processingMode == NOT_INITIALIZED) {
        println("Not yet initialized. Print 'init' first.")
        return
    }
    val key = readString("new key")
    val value = readString("new value")
    addElement(Element(key, value), processingMode)
}

fun removeElementInterface(processingMode : String) {
    if (processingMode == NOT_INITIALIZED) {
        println("Not yet initialized. Print 'init' first.")
        return
    }
    val key = readString("key")
    val value = removeElement(key, processingMode)
    if (value == null)
        println("Error! Element with this key doesn't exist.")
    else
        println("Removed value: $value")
}

fun getValueInterface(processingMode : String) {
    if (processingMode == NOT_INITIALIZED) {
        println("Not yet initialized. Print 'init' first.")
        return
    }
    val key = readString("key")
    val value = getValue(key, processingMode)
    if (value == null)
        println("Error! Element with this key doesn't exist.")
    else
        println(value)
}

fun containsInterface(processingMode : String) {
    if (processingMode == NOT_INITIALIZED) {
        println("Not yet initialized. Print 'init' first.")
        return
    }
    val key = readString("key")
    println(contains(key, processingMode))
}

fun scriptInterface(processingMode : String) {
    val path = readString("path to script")
    TODO()
}

fun lastActionsInterface(processingMode: String) {
    if (processingMode == NOT_INITIALIZED) {
        println("Not yet initialized. Print 'init' first.")
        return
    }
    print("Enter actions quantity: ")
    var quantity = readLine()
    while (quantity == null || quantity.toIntOrNull() == null) {
        print("Wrong input. Please, try again: ")
        quantity = readLine()
    }
    val journal = File(JOURNAL_NAME).readLines()
    for (i in max(0, journal.size - quantity.toInt()) until journal.size) {
        println(journal[i])
    }
}

fun printAllActionsInterface(processingMode: String) {
    if (processingMode == NOT_INITIALIZED) {
        println("Not yet initialized. Print 'init' first.")
        return
    }
    File(JOURNAL_NAME).readLines().forEach {
        println(it)
    }
}

fun saveInterface(processingMode: String) {
    if (processingMode == NOT_INITIALIZED) {
        println("Not yet initialized. Print 'init' first.")
        return
    }
    save(processingMode)
}

fun printAllContentInterface(processingMode: String) {
    if (processingMode == NOT_INITIALIZED) {
        println("Not yet initialized. Print 'init' first.")
        return
    }
    printAllContent(processingMode)
}

fun clearInterface(processingMode: String) {
    if (processingMode == NOT_INITIALIZED) {
        println("Not yet initialized. Print 'init' first.")
        return
    }
    clear(processingMode)
}

fun closeInterface(processingMode: String) {
    if (processingMode == NOT_INITIALIZED) {
        println("Not yet initialized. Print 'init' first.")
        return
    }
    close(processingMode)
}

fun main() {
    println("Hello User! To see help enter 'help'.")
    var command = readLine()
    var processingMode = NOT_INITIALIZED
    while (command != null && command != "exit") {
        when (command) {
            "help" -> printManual()
            "init" -> processingMode = initDatabaseInterface()
            "add" -> addElementInterface(processingMode)
            "remove" -> removeElementInterface(processingMode)
            "getValue" -> getValueInterface(processingMode)
            "script" -> scriptInterface(processingMode)
            "contains" -> containsInterface(processingMode)
            "allContent" -> printAllContentInterface(processingMode)
            "save" -> saveInterface(processingMode)
            "clear" -> clearInterface(processingMode)
            "close" -> {
                closeInterface(processingMode)
                processingMode = NOT_INITIALIZED
            }
            "lastActions" -> lastActionsInterface(processingMode)
            "allActions" -> printAllActionsInterface(processingMode)
            "exit" -> exit(processingMode)
            else -> println("Wrong command, please try again")
        }
        command = readLine()
    }
    return
}
fun printManual() {
    println("Available commands:")
    println("* 'init' - initialize database. Enter it before work with base.")
    println("* 'add' - add element in the database. If element already exists, it's value rewrites.")
    println("* 'remove' - remove element from the database. If element doesn't exist, nothing happens.")
    println("* 'value' - get value by the key. If element with this key doesn't exist, print error message")
    println("* 'exit' - save changes in the file 'data.txt' and exit. Please, do not close the utility by any other " +
            "methods, otherwise all your changes will be lost")
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

fun addElementInterface() {
    val key = readString("new key")
    val value = readString("new value")
    addElement(Element(key, value))
}

fun removeElementInterface() {
    val key = readString("key")
    val value = removeElement(key)
    if (value == null)
        println("Element with this key doesn't exist")
    else
        println("Removed value: $value")
}

fun getValueInterface() {
    val key = readString("key")
    val value = getValue(key)
    if (value == null)
        println("Error! Element with this key doesn't exist.")
    else
        println(value)
}

fun main() {
    println("Hello User! To see help enter 'help'.")
    var command = readLine()
    while (command != null && command != "exit") {
        when (command) {
            "help" -> printManual()
            "init" -> initDatabase()
            "add" -> addElementInterface()
            "remove" -> removeElementInterface()
            "getValue" -> getValueInterface()
            "exit" -> saveOperation()
            else -> println("Wrong command, please try again")
        }
        command = readLine()
    }
    return
}
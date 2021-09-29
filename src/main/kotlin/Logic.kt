import java.io.File
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.Date

const val DATABASE_NAME = "data.txt"
const val JOURNAL_NAME = "changelog.txt"
const val NOT_INITIALIZED = "not initialized"

class Element(val key : String, val value: String)

var openedArrayImpl = ArrayDatabaseImplementation()
var openedMapImpl = MapDatabaseImplementation()

fun initDatabase(processingMode : String) {
    if (!File(DATABASE_NAME).exists())
        File(DATABASE_NAME).createNewFile()
    val size = File(DATABASE_NAME).readLines().size
    if (size % 2 == 1) {
        println("Data is damaged! Clear all data")
        File(DATABASE_NAME).writeText("")
        File(JOURNAL_NAME).writeText("")
        return
    }
    when (processingMode) {
        "a" -> openedArrayImpl.initDatabase()
        "m" -> openedMapImpl.initDatabase()
    }
    val currentTime = SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date())
    File(JOURNAL_NAME).appendText("Opened: $currentTime\n")
}

fun addElement(newElem : Element, processingMode: String) {
    when (processingMode) {
        "a" -> openedArrayImpl.addElement(newElem)
        "m" -> openedMapImpl.addElement(newElem)
    }
}

fun removeElement(key : String, processingMode: String) : String? {
    return when (processingMode) {
        "a" -> openedArrayImpl.removeElement(key)
        "m" -> openedMapImpl.removeElement(key)
        else -> null
    }
}

fun getValue(key : String, processingMode: String) : String? {
    return when (processingMode) {
        "a" -> openedArrayImpl.getValue(key)
        "m" -> openedMapImpl.getValue(key)
        else -> null
    }
}

fun contains(key: String, processingMode: String) : Boolean {
    return when (processingMode) {
        "a" -> openedArrayImpl.contains(key)
        "m" -> openedMapImpl.contains(key)
        else -> false
    }
}

fun save(processingMode: String) {
    when (processingMode) {
        "a" -> openedArrayImpl.save()
        "m" -> openedMapImpl.save()
    }
}

fun printAllContent(processingMode: String) {
    when (processingMode) {
        "a" -> openedArrayImpl.printAllContent()
        "m" -> openedMapImpl.printAllContent()
    }
}

fun clear(processingMode: String) {
    when (processingMode) {
        "a" -> openedArrayImpl.clear()
        "m" -> openedMapImpl.clear()
    }
}

fun close(processingMode: String) {
    save(processingMode)
    clear(processingMode)
    val currentTime = SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date())
    File(JOURNAL_NAME).appendText("Closed: $currentTime\n")
}

fun exit(processingMode: String) {
    if (processingMode != "not initialized")
        close(processingMode)
}
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.max

/** Returns path to all data by name */
fun pathByName(name : String) = "Data/$name"

/** Creates database and its journal by name */
fun createDatabase(name: String) {
    closeDatabase()
    val path = pathByName(name)
    File(path.replaceAfterLast('/', "")).mkdirs()
    File(path).createNewFile()
    File("${path}Journal").createNewFile()
    val currentTime = SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date())
    File("${path}Journal").writeText("Created: $currentTime\n")
}

/** Deletes database and its journal by given name */
fun deleteDatabase(name : String) {
    closeDatabase()
    val path = pathByName(name)
    File(path).delete()
    File("${path}Journal").delete()
}

/** Opens database by name and processing mode. Trows Exceptions, if processing mode isn't 'a' or 'm',
 * or if database with given name doesn't exist. */
fun openDatabase(name: String, processingMode: String) {
    closeDatabase()
    currentDatabase = when (processingMode) {
        "a" -> ArrayDatabase(pathByName(name))
        "m" -> MapDatabase(pathByName(name))
        else -> throw Exception("Wrong processing mode")
    }
    printActionToJournal("Opened")
}

/** Saves and closes current database */
fun closeDatabase() {
    currentDatabase?.save()
    printActionToJournal("Closed")
    currentDatabase = null
}

/** Return list with given quantity of last actions with current database. Requires any database to be opened. */
fun lastActions(quantity : Int) : List<String> {
    require(currentDatabase != null)
    val journal = File(currentDatabase!!.journalPath()).readLines()
    return journal.subList(max(0, journal.size - quantity), journal.size)
}

/** Return list with all actions with current database. Requires any database to be opened. */
fun allActions() : List<String> {
    require(currentDatabase != null)
    return File(currentDatabase!!.journalPath()).readLines()
}

/** Prints given action and current time to opened database journal */
fun printActionToJournal(action : String) {
    currentDatabase ?: return
    val currentTime = SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date())
    File(currentDatabase!!.journalPath()).appendText("$action: $currentTime\n")
}
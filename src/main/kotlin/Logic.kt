import java.io.File
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.Date

const val DATABASE_NAME = "data.txt"
const val CHANGELOG_NAME = "changelog.txt"

class Element(val key : String, val value: String)

val data = arrayListOf<Element>()

fun writeToFile(filename : String, text : String) {
    val writer = PrintWriter(filename)
    writer.append(text)
    writer.close()
}

fun initDatabase() {
    if (!File(DATABASE_NAME).exists())
        File(DATABASE_NAME).createNewFile()
    val allDataInFile = File(DATABASE_NAME).readLines()
    if (allDataInFile.size % 2 == 1) {
        println("Data is damaged! Clear all data")
        return
    }
    for (i in allDataInFile.indices step 2)
        data += Element(allDataInFile[i], allDataInFile[i + 1])
}

fun addElement(newElem : Element) {
    data += newElem
}

fun removeElement(key : String) : String? {
    for (ind in data.indices) {
        if (data[ind].key == key) {
            val res = data[ind]
            data[ind] = data.last()
            data.removeLast()
            return res.value
        }
    }
    return null
}

fun getValue(key : String) : String? {
    for (elem in data) {
        if (elem.key == key)
            return elem.value
    }
    return null
}

fun saveOperation() {
    val text = StringBuilder()
    for (elem in data) {
        text.append("${elem.key}\n")
        text.append("${elem.value}\n")
    }
    File(DATABASE_NAME).writeText(text.toString())
    val currentDate = SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date())
    writeToFile(CHANGELOG_NAME, currentDate)
    println("Good bye! Come back soon.")
}
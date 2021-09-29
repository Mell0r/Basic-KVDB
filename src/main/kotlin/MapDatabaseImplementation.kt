import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MapDatabaseImplementation {
    private val data = mutableMapOf<String, String>()

    fun initDatabase() {
        val allDataInFile = File(DATABASE_NAME).readLines()
        for (i in allDataInFile.indices step 2)
            data[allDataInFile[i]] = allDataInFile[i + 1]
    }

    fun addElement(newElem : Element) {
        data[newElem.key] = newElem.value
    }

    fun removeElement(key : String) : String? {
        val res = data[key]
        data.remove(key)
        return res
    }

    fun getValue(key : String) : String? {
        data.forEach { elem ->
            if (elem.key == key)
                return elem.value
        }
        return null
    }

    fun contains(key: String) : Boolean {
        return data.containsKey(key)
    }

    fun save() {
        val text = StringBuilder()
        data.forEach { elem ->
            text.append("${elem.key}\n")
            text.append("${elem.value}\n")
        }
        File(DATABASE_NAME).writeText(text.toString())
    }

    fun printAllContent() {
        data.forEach { elem ->
            println("Key: ${elem.key}")
            println("Value: ${elem.value}")
        }
    }

    fun clear() {
        data.clear()
    }
}
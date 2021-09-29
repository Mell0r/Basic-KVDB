import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ArrayDatabaseImplementation {
    private val data = arrayListOf<Element>()

    fun initDatabase() {
        data.clear()
        val allDataInFile = File(DATABASE_NAME).readLines()
        for (i in allDataInFile.indices step 2)
            data += Element(allDataInFile[i], allDataInFile[i + 1])
    }

    fun addElement(newElem : Element) {
        if (contains(newElem.key))
            return
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
        data.forEach { elem ->
            if (elem.key == key)
                return elem.value
        }
        return null
    }

    fun contains(key: String) : Boolean {
        return getValue(key) != null
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
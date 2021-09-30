import java.io.File

class ArrayDatabase(databaseName : String) : DatabaseInterface {
    private val data = arrayListOf<Element>()
    private val name = databaseName

    init {
        data.clear()
        val allDataInFile = File(name).readLines()
        for (i in allDataInFile.indices step 2)
            data += Element(allDataInFile[i], allDataInFile[i + 1])
    }

    override fun journalName() : String {
        return "${name}Journal"
    }

    override fun assignValue(newElem : Element) {
        if (!contains(newElem.key)) {
            data += newElem
            return
        }
        for (ind in data.indices)
            if (data[ind].key == newElem.key)
                data[ind] = newElem
    }

    override fun removeElement(key : String) : String? {
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

    override fun getValue(key : String) : String? {
        data.forEach { elem ->
            if (elem.key == key)
                return elem.value
        }
        return null
    }

    override fun contains(key: String) : Boolean {
        return getValue(key) != null
    }

    override fun save() {
        val text = StringBuilder()
        data.forEach { elem ->
            text.append("${elem.key}\n")
            text.append("${elem.value}\n")
        }
        File(name).writeText(text.toString())
    }

    override fun printAllContent() {
        data.forEach { elem ->
            println("Key: ${elem.key}")
            println("Value: ${elem.value}")
        }
    }

    override fun clear() {
        data.clear()
    }
}
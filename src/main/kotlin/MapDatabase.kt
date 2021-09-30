import java.io.File

class MapDatabase(databaseName : String) : DatabaseInterface {
    private val data = mutableMapOf<String, String>()
    private val name = databaseName

    init {
        data.clear()
        val allDataInFile = File(name).readLines()
        for (i in allDataInFile.indices step 2)
            data[allDataInFile[i]] = allDataInFile[i + 1]
    }

    override fun journalName() : String {
        return "${name}Journal"
    }

    override fun assignValue(newElem : Element) {
        data[newElem.key] = newElem.value
    }

    override fun removeElement(key : String) : String? {
        val res = data[key]
        data.remove(key)
        return res
    }

    override fun getValue(key : String) : String? {
        data.forEach { elem ->
            if (elem.key == key)
                return elem.value
        }
        return null
    }

    override fun contains(key: String) : Boolean {
        return data.containsKey(key)
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
import java.io.File

/** Implementation of DatabaseInterface by map. Initializes by path to database. Path must be correct. */
class MapDatabase(databasePath : String) : DatabaseInterface {
    private val data = mutableMapOf<String, String>()
    private val path = databasePath

    init {
        require(File(path).exists()) {"File with given path must exists, was $path"}
        val allDataInFile = File(path).readLines()
        for (i in allDataInFile.indices step 2)
            data[allDataInFile[i]] = allDataInFile[i + 1]
    }

    override fun journalPath() : String {
        return "${path}Journal"
    }

    override fun assignValue(key : String, value : String) {
        data[key] = value
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
        File(path).writeText(text.toString())
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
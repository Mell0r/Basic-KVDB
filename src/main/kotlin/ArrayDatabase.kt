import java.io.File

/** Inner element of ArrayDatabase */
class Element(val key : String, val value: String)

/** Array implementation of DatabaseInterface. Initializes by path to database. Path must be correct. */
class ArrayDatabase(pathToDatabase : String) : Database {
    private val data = arrayListOf<Element>()
    private val path = pathToDatabase

    init {
        require(File(path).exists()) {"File with given path must exists, was $path"}
        val allDataInFile = File(path).readLines()
        for (i in allDataInFile.indices step 2)
            data += Element(allDataInFile[i], allDataInFile[i + 1])
    }

    override fun journalPath() : String {
        return "${path}Journal"
    }

    override fun assignValue(key : String, value : String) {
        val ind = data.indexOfFirst { it.key == key }
        if (ind >= 0)
            data[ind] = Element(key, value)
        else
            data.add( Element(key, value))
    }

    override fun removeElement(key : String) : String? {
        val ind = data.indexOfFirst { it.key == key }
        if (ind >= 0) {
            val res = data[ind]
            data[ind] = data.last()
            data.removeLast()
            return res.value
        }
        return null
    }

    override fun getValue(key : String) : String? = data.find { it.key == key }?.value

    override fun contains(key: String) : Boolean {
        return getValue(key) != null
    }

    override fun save() {
        val text = StringBuilder()
        data.forEach { elem ->
            text.append("${elem.key}\n")
            text.append("${elem.value}\n")
        }
        File(path).writeText(text.toString())
    }

    override fun allContent() : List<Pair<String, String>> = data.map { elem -> Pair(elem.key, elem.value) }

    override fun clear() {
        data.clear()
    }
}
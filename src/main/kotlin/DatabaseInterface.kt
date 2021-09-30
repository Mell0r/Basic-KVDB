interface DatabaseInterface {
    fun journalName() : String = ""

    fun assignValue(newElem : Element) {}

    fun removeElement(key : String) : String? = null

    fun getValue(key : String) : String? = null

    fun contains(key: String) : Boolean = false

    fun save() {}

    fun printAllContent() {}

    fun clear() {}
}
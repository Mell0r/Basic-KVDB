/**
 * Interface, containing frame functions for database.
 */
interface Database {
    /** Returns the path to journal of this database. */
    fun journalPath() : String = ""

    /** Assigns the given value to the given key. If key haven't been in the database, creates new element. */
    fun assignValue(key : String, value : String) {}

    /** Removes element with the given key from the database and returns its value or null,
     * if this key isn't in database. */
    fun removeElement(key : String) : String? = null

    /** Returns value of element with the given key or null, if this key isn't in the database. */
    fun getValue(key : String) : String? = null

    /** Returns 'true' if element with the given key is in database, and 'false' otherwise. */
    fun contains(key: String) : Boolean = false

    /** Saves the database in the file. */
    fun save() {}

    /** Returns all content in database as List<String>, where each string is "*elem.key*\n*elem.value" for each
     * database element. */
    fun allContent() : List<Pair<String, String>> = listOf()

    /** Deletes all elements from the database. */
    fun clear() {}
}
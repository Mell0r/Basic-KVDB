/**
 * Interface, containing frame functions for database.
 */
interface DatabaseInterface {
    /** Returns the path to journal of this database. */
    fun journalPath() : String = ""

    /** Assigns the given value to the given key. If key haven't been in the database, creates new element. */
    fun assignValue(key : String, value : String) {}

    /** Removes element with the given key from the database and returns its value or null, if this key isn't in database. */
    fun removeElement(key : String) : String? = null

    /** Returns value of element with the given key or null, if this key isn't in the database. */
    fun getValue(key : String) : String? = null

    /** Returns 'true' if element with the given key is in database, and 'false' otherwise. */
    fun contains(key: String) : Boolean = false

    /** Saves the database in the file. */
    fun save() {}

    /** Print all content in the database. */
    fun printAllContent() {}

    /** Delete all elements from the database. */
    fun clear() {}
}
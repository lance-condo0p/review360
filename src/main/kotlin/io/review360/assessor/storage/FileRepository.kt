package io.review360.assessor.storage

/**
 * In memory storage of unique items of class T
 */
interface FileRepository<T> {
    /**
     * Load data from JSON file to RAM
     */
    fun init()

    /**
     * Get all data from RAM
     */
    fun getAll(): Set<T>
}
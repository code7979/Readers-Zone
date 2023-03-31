package com.code7979.readerszone.data

import java.util.*
import kotlin.collections.HashMap

class TempUUID {
    private val table: MutableMap<String, UUID>

    init {
        table = HashMap()
    }

    fun addUUIDInDb(key: String, workUUId: UUID) {
        table[key] = workUUId
    }

    fun getUUIDbyId(key: String): UUID? {
        return table[key]
    }

    companion object {
        @Volatile
        private var instance: TempUUID? = null

        fun getInstance(): TempUUID {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = TempUUID()
                    }
                }
            }
            return instance!!
        }
    }
}
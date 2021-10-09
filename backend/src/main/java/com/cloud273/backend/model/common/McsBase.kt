package com.cloud273.backend.model.common

import java.util.*

abstract class McsBase() {

    var id: Int? = null
    var createdAt: Date? = null
    var updatedAt: Date? = null
    var deletedAt: Date? = null

    open fun validCreated() {
        // verify fields and force close if not pass
    }

    open fun validUpdate() {
        // verify fields and force close if not pass
    }

    open fun validPartialUpdate() {
        // verify fields and force close if not pass
    }

}
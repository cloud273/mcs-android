package com.cloud273.backend.util

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.util.*

class DayTypeAdapter: TypeAdapter<Date>() {

    override fun write(out: JsonWriter?, value: Date?) {
        if (value != null) {
            out!!.value(value.toApiDateString())
        }
    }

    override fun read(`in`: JsonReader?): Date? {
        val input = `in`!!
        val text = input.nextString()
        return text.toDateApiDateFormat()
    }
}

class DateTimeTypeAdapter: TypeAdapter<Date>() {

    override fun write(out: JsonWriter?, value: Date?) {
        if (value != null) {
            out!!.value(value.toApiDateTimeString())
        }
    }

    override fun read(`in`: JsonReader?): Date? {
        val input = `in`!!
        val text = input.nextString()
        return text.toDateApiDateTimeFormat()
    }
}
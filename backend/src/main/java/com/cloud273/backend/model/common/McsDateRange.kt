package com.cloud273.backend.model.common

import com.cloud273.backend.util.DayTypeAdapter
import com.google.gson.annotations.JsonAdapter
import java.util.*

class McsDateRange (@JsonAdapter(DayTypeAdapter::class) var from: Date,
                    @JsonAdapter(DayTypeAdapter::class) var to: Date)
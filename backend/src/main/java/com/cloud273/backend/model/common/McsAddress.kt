package com.cloud273.backend.model.common

import com.google.gson.annotations.SerializedName


class McsAddress(@SerializedName("country") var countryCode: String,
                 @SerializedName("state") var stateCode: String,
                 @SerializedName("city") var cityCode: String,
                 var line: String,
                 var longitude: Double?,
                 var latitude: Double?) {

    companion object {

        fun create(countryCode: String, stateCode: String, cityCode: String, line: String, longitude: Double?, latitude: Double?): McsAddress {
            return McsAddress(countryCode, stateCode, cityCode, line, longitude, latitude)
        }

        fun update(countryCode: String, stateCode: String, cityCode: String, line: String, longitude: Double?, latitude: Double?): McsAddress {
            return McsAddress(countryCode, stateCode, cityCode, line, longitude, latitude)
        }

    }
}
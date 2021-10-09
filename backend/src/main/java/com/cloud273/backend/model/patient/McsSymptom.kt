package com.cloud273.backend.model.patient

class McsSymptom(var name: String,
                 var note: String) {

    companion object {

        fun create(name : String, note : String): McsSymptom {
            return McsSymptom(name, note)
        }

    }

}
package com.cloud273.mcs.model

import com.dungnguyen.qdcore.model.TextInterface
import com.cloud273.backend.model.common.McsValue

class McsValueModel (val value: McsValue): TextInterface {

    override fun getText(): String {
        return value.name
    }

}
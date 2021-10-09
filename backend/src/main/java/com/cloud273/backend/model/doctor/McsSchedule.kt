package com.cloud273.backend.model.doctor

import com.cloud273.backend.model.common.McsBase
import com.cloud273.backend.model.common.McsDateRange
import com.cloud273.backend.model.common.McsTimeRange

class McsSchedule(var duration: McsDateRange,
                  var monday: List<McsTimeRange>,
                  var tuesday: List<McsTimeRange>,
                  var wednesday: List<McsTimeRange>,
                  var thursday: List<McsTimeRange>,
                  var friday: List<McsTimeRange>,
                  var saturday: List<McsTimeRange>,
                  var sunday: List<McsTimeRange>) : McsBase() {

}
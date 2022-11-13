package org.drekorik.tswriter.model

import software.amazon.awssdk.services.timestreamwrite.model.MeasureValueType

class Value(
    var name: String,
    var type: MeasureValueType,
    var value: String
) {
}
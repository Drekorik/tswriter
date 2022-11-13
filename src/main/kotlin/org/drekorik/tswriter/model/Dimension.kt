package org.drekorik.tswriter.model

import software.amazon.awssdk.services.timestreamwrite.model.DimensionValueType

class Dimension(
    var name: String,
    var type: DimensionValueType = DimensionValueType.VARCHAR,
    var value: String
) {
}
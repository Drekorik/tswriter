package org.drekorik.tswriter.service

import org.drekorik.tswriter.model.JsonFile
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.timestreamwrite.TimestreamWriteClient
import software.amazon.awssdk.services.timestreamwrite.model.*

@Service
class TsWriter(
    val timestreamWriteClient: TimestreamWriteClient,
) {

    companion object Constants {
        private val log = LoggerFactory.getLogger(FileReaderService::class.java)
    }

    fun write(jsonFile: JsonFile) {
        jsonFile.data.forEach { data ->
            val dimensions: List<Dimension> = data.dimensions
                .map { dimension ->
                    Dimension.builder()
                        .dimensionValueType(dimension.type)
                        .name(dimension.name)
                        .value(dimension.value)
                        .build()
                }
            val common = Record.builder()
                .dimensions(dimensions)
                .build()

            val values: List<MeasureValue> = data.values
                .map { value ->
                    MeasureValue.builder()
                        .name(value.name)
                        .value(value.value)
                        .type(value.type)
                        .build()
                }

            val awsRecord: Record = Record.builder()
                .measureName(data.measureName)
                .measureValueType(MeasureValueType.MULTI)
                .measureValues(values)
                .time(System.currentTimeMillis().toString())
                .timeUnit(TimeUnit.MILLISECONDS)
                .build()

            val writeRecordsRequest: WriteRecordsRequest = WriteRecordsRequest.builder()
                .databaseName(jsonFile.connection.database)
                .tableName(jsonFile.connection.table)
                .commonAttributes(common)
                .records(awsRecord)
                .build()

            try {
                timestreamWriteClient.writeRecords(writeRecordsRequest)
            } catch (e: Exception) {
                log.error("Failed to write dummy values", e)
                throw RuntimeException("Failed to write data to Timestream")
            }
        }
    }
}
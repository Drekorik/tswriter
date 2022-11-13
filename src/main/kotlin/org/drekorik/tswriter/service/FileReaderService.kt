package org.drekorik.tswriter.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.drekorik.tswriter.model.JsonFile
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.nio.file.Paths

@Service
class FileReaderService(
    val objectMapper: ObjectMapper
) {

    companion object Constants{
        private val log = LoggerFactory.getLogger(FileReaderService::class.java)
    }

    fun readFile(path: String = "ts.json"): JsonFile {
        val path = Paths.get(path)
        val absolutePath = path.toAbsolutePath().toString()
        println("chosen file: $absolutePath", )

        return objectMapper.readValue(path.toFile(), JsonFile::class.java)
    }
}
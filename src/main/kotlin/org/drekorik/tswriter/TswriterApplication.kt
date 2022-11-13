package org.drekorik.tswriter

import org.drekorik.tswriter.service.FileReaderService
import org.drekorik.tswriter.service.TsWriter
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import java.util.*

@SpringBootApplication
class TswriterApplication(
	val fileReaderService: FileReaderService,
	val tsWriter: TsWriter,
) : CommandLineRunner {

    override fun run(vararg args: String?) {

        var file = "ts.json"

        val argIterator = args.iterator()
        while (argIterator.hasNext()) {
            when (argIterator.next()) {
                "-f" -> {
                    file = argIterator.next()!!
                }
            }
        }

        val json = fileReaderService.readFile(file)
        tsWriter.write(json)
    }
}

fun main(args: Array<String>) {
    val properties = Properties()
    properties["spring.main.banner-mode"] = "OFF"

    val argIterator = args.iterator()

    while (argIterator.hasNext()) {
        when (argIterator.next()) {
            "-h" -> {
                println("-h for help")
                println("-p for setting aws profile, default profile is 'default'")
                println("-f for setting file location, default file is 'ts.json' in run directory")
                return
            }
            "-p" -> {
                properties["aws.profile"] = argIterator.next()
            }
        }
    }

//	SpringApplication.run(TswriterApplication::class.java, *args)
//		.environment.propertySources.addFirst(runtimePropertySource)

    SpringApplicationBuilder(TswriterApplication::class.java)
        .properties(properties)
        .build()
        .run(*args)
}

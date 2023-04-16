package glol.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GlolApplication

fun main(args: Array<String>) {
	runApplication<GlolApplication>(*args)
}

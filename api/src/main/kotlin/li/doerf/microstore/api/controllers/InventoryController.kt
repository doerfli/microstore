package li.doerf.microstore.api.controllers

import li.doerf.microstore.TOPIC_INVENTORY
import li.doerf.microstore.dto.kafka.InventoryOrderItems
import li.doerf.microstore.services.KafkaService
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/inventory")
class InventoryController @Autowired constructor(
        private val kafkaService: KafkaService
) {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    @PostMapping
    fun orderItems(): ResponseEntity<String> {
        log.debug("ordering items")
        kafkaService.sendEvent(TOPIC_INVENTORY, InventoryOrderItems(UUID.randomUUID().toString()), UUID.randomUUID().toString())
        return ResponseEntity.ok().body("")
    }
}
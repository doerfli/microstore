package li.doerf.microstore.api.listeners

import li.doerf.microstore.TOPIC_CUSTOMERS
import li.doerf.microstore.dto.kafka.CustomerCreated
import li.doerf.microstore.utils.getLogger
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CountDownLatch

@Component
class CustomersListener {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    private val waitingLatches = mutableMapOf<String, CountDownLatch>()
    private val results = mutableMapOf<String, CustomerCreated>()

    @KafkaListener(topics = [TOPIC_CUSTOMERS])
    @Transactional
    fun receive(record: ConsumerRecord<String, Any>) {
        log.debug("received: $record")
        val correlationId =
                String(record.headers().headers(KafkaHeaders.CORRELATION_ID).first().value())
        val event = record.value() as? CustomerCreated ?: return
        results[correlationId] = event
        val latch = waitingLatches.remove(correlationId)
        if (latch == null) {
            log.error("unknown correlation id: $correlationId")
            return
        }
        log.debug("countdown latch")
        latch.countDown()
    }

    fun registerCorrelationIdForResponse(correlationId: String): CompletableFuture<CustomerCreated> {
        return CompletableFuture.supplyAsync<CustomerCreated> {
            val latch = CountDownLatch(1)
            waitingLatches[correlationId] = latch
            latch.await()
            results.getOrDefault(correlationId, null)
        }
    }

}
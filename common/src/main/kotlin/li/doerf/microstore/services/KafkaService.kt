package li.doerf.microstore.services

import li.doerf.microstore.TOPIC_CUSTOMERS
import li.doerf.microstore.utils.getLogger
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.internals.RecordHeader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.stereotype.Service
import org.springframework.util.concurrent.ListenableFutureCallback

@Service
open class KafkaService @Autowired constructor(private val kafkaTemplate: KafkaTemplate<String, Any>) {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    open fun sendEvent(event: Any, correlationId: String) {
        log.debug("sending event $event")
        val record = ProducerRecord<String, Any>(TOPIC_CUSTOMERS, event)
        record.headers().add(RecordHeader(KafkaHeaders.CORRELATION_ID, correlationId.toByteArray()))

        val sendFuture = kafkaTemplate.send(record)
        sendFuture.addCallback(object: ListenableFutureCallback<Any> {
            override fun onSuccess(result: Any?) {
                log.debug("onSuccess")
            }

            override fun onFailure(ex: Throwable) {
                log.error("onFailure", ex)
            }
        })
    }
}
package li.doerf.microstore.listeners

import li.doerf.microstore.utils.getLogger
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.listener.ConsumerSeekAware
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.transaction.annotation.Transactional

abstract class ReplayingRecordsListener : ConsumerSeekAware {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    /**
     * Map with key partition and value initial offset
     */
    private val initialOffsets: MutableMap<Int,Long> = mutableMapOf()

    @Transactional
    @KafkaHandler(isDefault = true)
    open fun receive(record: ConsumerRecord<String, Any>) {
        log.debug("received: $record")
        val correlationId =
                String(record.headers().headers(KafkaHeaders.CORRELATION_ID).first().value())
        val event = record.value()
        val eventResponse = applyEventToStore(event, correlationId)
        if(isReplaying(record.partition(), record.offset())) {
            return
        }
        processCommand(event, correlationId, eventResponse)
    }

    protected abstract fun applyEventToStore(event: Any?, correlationId: String): Any?

    protected abstract fun processCommand(event: Any, correlationId: String, eventResponse: Any?)

    override fun onIdleContainer(assignments: MutableMap<TopicPartition, Long>?, callback: ConsumerSeekAware.ConsumerSeekCallback?) {
    }

    override fun onPartitionsAssigned(assignments: MutableMap<TopicPartition, Long>?, callback: ConsumerSeekAware.ConsumerSeekCallback?) {
        log.debug("onPartitionsAssigned")
        assignments?.forEach { t, offset ->
            run {
                log.debug("before seekToBeginning ${t.topic()} / ${t.partition()} / $offset")
                initialOffsets[t.partition()] = offset
                callback?.seekToBeginning(t.topic(), t.partition())
            }
        }
    }

    override fun registerSeekCallback(callback: ConsumerSeekAware.ConsumerSeekCallback?) {
    }

    private fun isReplaying(partition: Int, offset: Long): Boolean {
        val initialOffset = initialOffsets.getOrDefault(partition, 0)
        return offset < initialOffset
    }

}
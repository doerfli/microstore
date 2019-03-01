package li.doerf.microstore.listeners

import li.doerf.microstore.utils.getLogger
import org.apache.kafka.common.TopicPartition
import org.springframework.kafka.listener.ConsumerSeekAware

open class ReplayingRecordsListener : ConsumerSeekAware {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    /**
     * Map with key partition and value initial offset
     */
    private val initialOffsets: MutableMap<Int,Long> = mutableMapOf()

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

    fun isReplaying(partition: Int, offset: Long): Boolean {
        val initialOffset = initialOffsets.getOrDefault(partition, 0)
        return offset < initialOffset
    }

}
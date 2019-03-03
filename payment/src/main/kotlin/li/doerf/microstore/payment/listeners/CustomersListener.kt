package li.doerf.microstore.payment.listeners

import li.doerf.microstore.TOPIC_CUSTOMERS
import li.doerf.microstore.dto.kafka.CustomerCreated
import li.doerf.microstore.dto.kafka.CustomerIncreaseLimit
import li.doerf.microstore.listeners.ReplayingRecordsListener
import li.doerf.microstore.payment.services.CreditLimitService
import li.doerf.microstore.payment.services.CustomerService
import li.doerf.microstore.services.KafkaService
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.math.BigDecimal

@KafkaListener(topics = [TOPIC_CUSTOMERS])
@Component
class CustomersListener @Autowired constructor(
        private val creditLimitService: CreditLimitService,
        private val customerService: CustomerService,
        private val kafkaService: KafkaService
) : ReplayingRecordsListener() {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    override fun applyEventToStore(event: Any?, correlationId: String): Any? {
        when(event) {
            is CustomerCreated -> return customerService.store(event)
            is CustomerIncreaseLimit -> return creditLimitService.increaseLimit(event)
        }
        return null
    }

    override fun processCommand(event: Any, correlationId: String, eventResponse: Any?) {
        log.debug("processCommand")
        when(event) {
            is CustomerCreated -> {
                val n = CustomerIncreaseLimit(
                        event.id,
                        BigDecimal(1000)
                )
                kafkaService.sendEvent(TOPIC_CUSTOMERS, n, correlationId)
            }
        }
    }



}
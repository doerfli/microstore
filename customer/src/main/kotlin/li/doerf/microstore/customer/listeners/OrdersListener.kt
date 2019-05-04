package li.doerf.microstore.customer.listeners

import li.doerf.microstore.TOPIC_ORDERS
import li.doerf.microstore.customer.services.CustomerService
import li.doerf.microstore.customer.services.OrderService
import li.doerf.microstore.dto.kafka.OrderCustomerExists
import li.doerf.microstore.dto.kafka.OrderEndState
import li.doerf.microstore.dto.kafka.OrderFinished
import li.doerf.microstore.dto.kafka.OrderOpened
import li.doerf.microstore.listeners.ReplayingRecordsListener
import li.doerf.microstore.services.KafkaService
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.math.BigDecimal

@KafkaListener(topics = [TOPIC_ORDERS])
@Component
class OrdersListener @Autowired constructor(
        val kafkaService: KafkaService,
        val customerService: CustomerService,
        val orderService: OrderService
) : ReplayingRecordsListener() {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    override fun applyEventToStore(event: Any?, correlationId: String): Any? {
        when(event) {
//            is OrderOpened -> return orderService.open(event)
        }
        return null
    }

    override fun handleBusinessLogic(event: Any, correlationId: String, eventResponse: Any?) {
        log.debug("handleBusinessLogic")
        when(event) {
            is OrderOpened -> {
                val customerExists = customerService.hasCustomer(event.customerId)
                if(customerExists) {
                    val orderId = orderService.getOrderId(event.customerId)
                    kafkaService.sendEvent(
                        TOPIC_ORDERS,
                        event.id,
                        OrderCustomerExists(
                                event.id,
                                event.customerId,
                                orderId
                        ),
                        correlationId)
                } else {
                    kafkaService.sendEvent(
                            TOPIC_ORDERS,
                            event.id,
                            OrderFinished(
                                    event.id,
                                    event.customerId,
                                    BigDecimal.ZERO,
                                    0,
                                    OrderEndState.CUSTOMER_NOT_FOUND,
                                    "customerId ${event.customerId} does not exist}"
                            ),
                            correlationId)
                }
            }
        }
    }



}
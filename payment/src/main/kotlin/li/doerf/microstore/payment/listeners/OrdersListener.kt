package li.doerf.microstore.payment.listeners

import li.doerf.microstore.TOPIC_ORDERS
import li.doerf.microstore.dto.kafka.*
import li.doerf.microstore.listeners.ReplayingRecordsListener
import li.doerf.microstore.payment.services.CreditLimitService
import li.doerf.microstore.payment.services.OrderService
import li.doerf.microstore.services.KafkaService
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.math.BigDecimal

@KafkaListener(topics = [TOPIC_ORDERS])
@Component
class OrdersListener @Autowired constructor(
        private val kafkaService: KafkaService,
        private val orderService: OrderService,
        private val creditLimitService: CreditLimitService
) : ReplayingRecordsListener() {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    override fun applyEventToStore(event: Any?, correlationId: String): Any? {
        when(event) {
            is OrderOpened -> return orderService.open(event)
            is OrderItemsReserved -> return creditLimitService.payOrder(event)
            is OrderFinished -> return orderService.delete(event.id)
        }
        return null
    }

    override fun handleBusinessLogic(event: Any, correlationId: String, eventResponse: Any?) {
        log.debug("handleBusinessLogic")
        when(event) {
//            is OrderCreate -> {
//                val oid = UUID.randomUUID().toString()
//                kafkaService.sendEvent(
//                        TOPIC_ORDERS,
//                        oid,
//                        OrderOpened(
//                                oid,
//                                event.customerId,
//                                event.itemsIds
//                        ),
//                        correlationId)
//            }
            is OrderItemsReserved -> {
                val paymentSuccessful = eventResponse as Boolean
                if (paymentSuccessful) {
                    kafkaService.sendEvent(
                            TOPIC_ORDERS,
                            event.id,
                            OrderPaymentSuccessful(
                                    event.id
                            ),
                            correlationId
                    )
                } else {
                    val order = orderService.get(event.id)
                    kafkaService.sendEvent(
                            TOPIC_ORDERS,
                            event.id,
                            OrderFinished(
                                    event.id,
                                    order.customerId,
                                    BigDecimal.ZERO,
                                    0,
                                    OrderEndState.PAYMENT_FAILED,
                                    "customer ${order.customerId} payment limit exceeded}"
                            ),
                            correlationId)
                }
            }
        }
    }



}
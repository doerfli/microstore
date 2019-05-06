package li.doerf.microstore.payment.services

import li.doerf.microstore.dto.kafka.CustomerIncreaseLimit
import li.doerf.microstore.dto.kafka.OrderItemsReserved
import li.doerf.microstore.payment.repositories.CustomerRepository
import li.doerf.microstore.payment.repositories.OrderRepository
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CreditLimitService @Autowired constructor(
        val customerRepository: CustomerRepository,
        val orderRepository: OrderRepository
){
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    fun increaseLimit(input: CustomerIncreaseLimit) {
        val customer = customerRepository.findById(input.id)
        if(customer.isEmpty) {
            throw IllegalArgumentException("customer with id ${input.id} unknown")
        }
        customer.get().creditLimit = customer.get().creditLimit.add(input.amount)
        log.info("credit limit increased. $customer")
        customerRepository.save(customer.get())
    }

    fun payOrder(event: OrderItemsReserved): Boolean {
        val customerId = orderRepository.findById(event.id).orElseThrow().customerId // TODO handle throw
        val customer = customerRepository.findById(customerId).orElseThrow() // TODO handle throw
        if (event.totalAmount > customer.creditLimit) {
            log.warn("not enough credit available: $customer")
            return false
        }
        customer.creditLimit = customer.creditLimit.subtract(event.totalAmount)
        customerRepository.save(customer)
        log.info("reduced customer credit limit by ${event.totalAmount} to ${customer.creditLimit}")
        return true
    }
}
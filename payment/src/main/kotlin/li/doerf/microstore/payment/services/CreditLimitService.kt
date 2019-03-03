package li.doerf.microstore.payment.services

import li.doerf.microstore.dto.kafka.CustomerIncreaseLimit
import li.doerf.microstore.payment.repositories.CustomerRepository
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CreditLimitService @Autowired constructor(
        val customerRepository: CustomerRepository
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
}
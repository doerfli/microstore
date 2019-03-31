package li.doerf.microstore.customer.services

import li.doerf.microstore.customer.entities.Customer
import li.doerf.microstore.customer.repositories.CustomerRepository
import li.doerf.microstore.dto.kafka.CustomerCreated
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CustomerService @Autowired constructor(
        private val customerRepository: CustomerRepository
) {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    fun store(event: CustomerCreated) {
        val customer = Customer(
                event.id,
                event.email,
                event.firstname,
                event.lastname
        )
        customerRepository.save(customer)
        log.debug("customer created $customer")
    }

    fun hasCustomer(customerId: String): Boolean {
        val exists = !customerRepository.findById(customerId).isEmpty
        log.debug("customerid $customerId exists? $exists")
        return exists
    }

}
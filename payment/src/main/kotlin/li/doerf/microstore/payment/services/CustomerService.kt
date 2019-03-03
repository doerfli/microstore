package li.doerf.microstore.payment.services

import li.doerf.microstore.dto.CustomerCreated
import li.doerf.microstore.entities.Customer
import li.doerf.microstore.payment.repositories.CustomerRepository
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CustomerService @Autowired constructor(
        val customerRepository: CustomerRepository
){
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

}
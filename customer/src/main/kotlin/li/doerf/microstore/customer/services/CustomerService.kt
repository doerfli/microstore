package li.doerf.microstore.customer.services

import li.doerf.microstore.customer.repositories.CustomerRepository
import li.doerf.microstore.dto.CustomerCreate
import li.doerf.microstore.entities.Customer
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerService @Autowired constructor(
        val customerRepository: CustomerRepository
) {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    fun create(customerEvent: CustomerCreate): Customer {
        val customer = Customer(
                UUID.randomUUID().toString(),
                customerEvent.email,
                customerEvent.firstname,
                customerEvent.lastname
        )
        customerRepository.save(customer)
        log.info("Customer $customer saved")
        return customer
    }

}
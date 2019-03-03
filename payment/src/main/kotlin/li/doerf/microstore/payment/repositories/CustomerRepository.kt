package li.doerf.microstore.payment.repositories

import li.doerf.microstore.payment.entities.Customer
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository : CrudRepository<Customer, String>
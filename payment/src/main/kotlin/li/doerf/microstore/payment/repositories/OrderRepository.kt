package li.doerf.microstore.payment.repositories

import li.doerf.microstore.payment.entities.Order
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : CrudRepository<Order, String>
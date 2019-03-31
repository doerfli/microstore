package li.doerf.microstore.order.repositories

import li.doerf.microstore.order.entities.Order
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : CrudRepository<Order, String>
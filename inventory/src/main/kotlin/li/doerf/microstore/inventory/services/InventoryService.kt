package li.doerf.microstore.inventory.services

import com.github.javafaker.Faker
import li.doerf.microstore.TOPIC_INVENTORY
import li.doerf.microstore.dto.kafka.InventoryItemAdd
import li.doerf.microstore.dto.kafka.InventoryItemIncreaseQuantity
import li.doerf.microstore.dto.kafka.InventoryItemReserve
import li.doerf.microstore.inventory.entities.Item
import li.doerf.microstore.inventory.repositories.ItemRepository
import li.doerf.microstore.services.KafkaService
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class InventoryService @Autowired constructor(
        private val kafkaService: KafkaService,
        private val itemRepository: ItemRepository
) {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    fun orderItems() {
        if (itemRepository.count() == 0L) {
            initializeItems()
        } else {
            incrementQuantity()
        }
    }

    private fun initializeItems() {
        log.info("no items in repository. creating empty stock")
        val names = mutableListOf<String>()
        repeat(50) { names.add(Faker().beer().name()) }
        names.forEach {
            log.debug("adding item $it")
            val id = UUID.randomUUID().toString()
            kafkaService.sendEvent(
                    TOPIC_INVENTORY,
                    id,
                    InventoryItemAdd(
                            id,
                            it,
                            BigDecimal.valueOf(Random().nextDouble() * 50),
                            Random().nextInt(10)
                    ),
                    UUID.randomUUID().toString()
            )
        }
    }

    private fun incrementQuantity() {
        itemRepository.findAll().forEach {item ->
            val id = item.id
            kafkaService.sendEvent(
                    TOPIC_INVENTORY,
                    id,
                    InventoryItemIncreaseQuantity(
                            id,
                            Random().nextInt(10)
                    ),
                    UUID.randomUUID().toString()
            )
        }
    }

    fun addItem(event: InventoryItemAdd): Item {
        log.info("adding item to stock - $event")
        val item = Item(
                event.id,
                event.name,
                event.price,
                event.initialQuantity,
                0
        )
        itemRepository.save(item)
        log.debug("item saved $item")
        return item
    }

    fun increaseQuantity(event: InventoryItemIncreaseQuantity): Item {
        log.info("increasing item ${event.id} quantity by ${event.quantity}")
        val itemO = itemRepository.findById(event.id)
        if (itemO.isEmpty) {
            throw IllegalArgumentException("unknown item with id ${event.id}")
        }
        val item = itemO.get()
        item.quantity += event.quantity
        itemRepository.save(item)
        log.debug("updated item $item")
        return item
    }

    fun reserveItems(itemsIds: Collection<String>): BigDecimal {
        log.debug("reserving items")
        val totalAmount = BigDecimal.ZERO
        itemsIds.forEach {itemId ->
            val item = itemRepository.findById(itemId).orElseThrow()
            log.debug("sending reserve item for $item")
            if(item.quantity == 0 || item.quantityReserved + 1 > item.quantity) {
                throw IllegalStateException("not enough items available: $item")
            }
            totalAmount.add(item.price)
            // TODO store itemid reference temporarily for orderid until being shipped (not contains in further messages)
            kafkaService.sendEvent(
                    TOPIC_INVENTORY,
                    item.id,
                    InventoryItemReserve(
                            item.id,
                            1
                    ),
                    UUID.randomUUID().toString()
            )
            // TODO wait for item reserved confirmation
        }

        log.info("total amount to pay: $totalAmount")
        return totalAmount
    }

    fun reserveItem(event: InventoryItemReserve) {
        val item = itemRepository.findById(event.id).orElseThrow()
        log.debug("reserving item $item")
        if(item.quantity == 0 || item.quantityReserved + event.quantity > item.quantity) {
            throw IllegalStateException("not enough items available: $item")
        }
        item.quantityReserved = item.quantityReserved + event.quantity
        itemRepository.save(item)
        log.info("reserved item: $item")
    }
}
package li.doerf.microstore.order.entities

enum class OrderStatus {
    OPENED,
    ITEMS_RESERVED,
    PAYMENT_SUCCESSFUL,
    ITEMS_SHIPPED,
    FINISHED
}

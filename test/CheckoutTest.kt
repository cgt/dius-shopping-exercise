import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CheckoutTest {
    @Test
    fun `sell zero items`() {
        val checkout = Checkout()
        assertEquals(0, checkout.total())
    }

    @Test
    fun `sell one item`() {
        val checkout = Checkout()
        checkout.scan("ipd")
        assertEquals(54999, checkout.total())
    }

    @Test
    fun `sell a different item`() {
        val checkout = Checkout()
        checkout.scan("mbp")
        assertEquals(139999, checkout.total())
    }
}

class Checkout {
    private var lastScanned: String? = null

    fun total(): Int {
        if (lastScanned == "ipd") {
            return 54999
        } else if (lastScanned == "mbp") {
            return 139999
        }
        return 0
    }

    fun scan(sku: String) {
        lastScanned = sku
    }
}

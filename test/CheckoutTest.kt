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
}

class Checkout {
    private var lastScanned: String? = null

    fun total(): Int {
        if (lastScanned != null) {
            return 54999
        }
        return 0
    }

    fun scan(sku: String) {
        lastScanned = sku
    }
}

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

    @Test
    fun `sell multiple items`() {
        val checkout = Checkout()

        checkout.scan("ipd")
        checkout.scan("mbp")

        val ipdPriceInCents = 54999
        val mbpPriceInCents = 139999
        val expectedTotal = ipdPriceInCents + mbpPriceInCents
        assertEquals(expectedTotal, checkout.total())
    }
}

class Checkout {
    private val scanned = ArrayList<String>()

    fun total(): Int {
        return scanned
            .map { item ->
                if (item == "ipd") {
                    54999
                } else if (item == "mbp") {
                    139999
                } else {
                    0
                }
            }
            .sum()
    }

    fun scan(sku: String) {
        scanned.add(sku)
    }
}

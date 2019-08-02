import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CheckoutTest {
    @Test
    fun `sell zero items`() {
        assertEquals(0, checkout.total())
    }

    @Test
    fun `sell one item`() {
        checkout.scan("ipd")
        assertEquals(54999, checkout.total())
    }

    @Test
    fun `sell a different item`() {
        checkout.scan("mbp")
        assertEquals(139999, checkout.total())
    }

    @Test
    fun `sell multiple items`() {
        checkout.scan("ipd")
        checkout.scan("mbp")

        val ipdPriceInCents = 54999
        val mbpPriceInCents = 139999
        val expectedTotal = ipdPriceInCents + mbpPriceInCents
        assertEquals(expectedTotal, checkout.total())
    }

    private val priceInCentsBySku = mapOf(
        "ipd" to 54999,
        "mbp" to 139999
    )

    private val checkout = Checkout(priceInCentsBySku)

    @Test
    fun `item prices come from catalog`() {
        checkout.scan("ipd")
        assertEquals(priceInCentsBySku["ipd"], checkout.total())
    }
}

class Checkout(private val priceInCentsBySku: Map<String, Int>) {
    private val scanned = ArrayList<String>()

    fun total(): Int {
        return scanned
            .map { item -> priceInCentsBySku[item] ?: TODO() }
            .sum()
    }

    fun scan(sku: String) {
        scanned.add(sku)
    }
}

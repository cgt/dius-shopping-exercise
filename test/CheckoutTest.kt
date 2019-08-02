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
        val sku = "ipd"
        val priceInCents = 54999
        val checkout = Checkout(mapOf(sku to priceInCents))
        checkout.scan(sku)
        assertEquals(priceInCents, checkout.total())
    }

    @Test
    fun `sell a different item`() {
        val sku = "mbp"
        val priceInCents = 139999
        val checkout = Checkout(mapOf(sku to priceInCents))
        checkout.scan(sku)
        assertEquals(priceInCents, checkout.total())
    }

    @Test
    fun `sell multiple items`() {
        val ipdSku = "ipd"
        val mbpSku = "mbp"
        val ipdPriceInCents = 54999
        val mbpPriceInCents = 139999

        val checkout = Checkout(
            mapOf(
                ipdSku to ipdPriceInCents,
                mbpSku to mbpPriceInCents
            )
        )

        checkout.scan(ipdSku)
        checkout.scan(mbpSku)

        val expectedTotal = ipdPriceInCents + mbpPriceInCents
        assertEquals(expectedTotal, checkout.total())
    }

    @Test
    fun `sell one item from catalog`() {
        val priceInCentsBySku = mapOf(
            "atv" to 10950
        )
        val checkout = Checkout(priceInCentsBySku)

        checkout.scan("atv")

        assertEquals(priceInCentsBySku["atv"], checkout.total())
    }
}

class Checkout(
    private val priceInCentsBySku: Map<String, Int> = emptyMap()
) {
    private val scanned = ArrayList<String>()

    fun total(): Int {
        return scanned
            .map { item ->
                priceInCentsBySku.getOrDefault(item, 0)
            }
            .sum()
    }

    fun scan(sku: String) {
        scanned.add(sku)
    }
}

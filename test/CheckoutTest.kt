import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(PER_CLASS)
class CheckoutTest {
    private val priceInCentsBySku = mapOf(
        "atv" to 10950,
        "ipd" to 54999,
        "mbp" to 139999
    )

    @Test
    fun `sell zero items`() {
        val checkout = Checkout(priceInCentsBySku)
        assertEquals(0, checkout.total())
    }

    @ParameterizedTest
    @MethodSource("itemProvider")
    fun `sell any one item`(item: Pair<String, Int>) {
        val (sku, priceInCents) = item
        val checkout = Checkout(priceInCentsBySku)

        checkout.scan(sku)

        assertEquals(priceInCents, checkout.total())
    }

    fun itemProvider(): Stream<Pair<String, Int>> {
        return priceInCentsBySku
            .entries
            .map { kv -> kv.toPair() }
            .stream()
    }

    @Test
    fun `sell one item`() {
        val sku = "ipd"
        val priceInCents = priceInCentsBySku.getValue(sku)
        val checkout = Checkout(priceInCentsBySku)
        checkout.scan(sku)
        assertEquals(priceInCents, checkout.total())
    }

    @Test
    fun `sell a different item`() {
        val sku = "mbp"
        val priceInCents = priceInCentsBySku.getValue(sku)
        val checkout = Checkout(priceInCentsBySku)
        checkout.scan(sku)
        assertEquals(priceInCents, checkout.total())
    }

    @Test
    fun `sell yet another different item`() {
        val sku = "atv"
        val priceInCents = priceInCentsBySku[sku]
        val checkout = Checkout(priceInCentsBySku)
        checkout.scan(sku)
        assertEquals(priceInCents, checkout.total())
    }

    @Test
    fun `sell multiple items`() {
        val ipdSku = "ipd"
        val mbpSku = "mbp"
        val ipdPriceInCents = priceInCentsBySku.getValue(ipdSku)
        val mbpPriceInCents = priceInCentsBySku.getValue(mbpSku)

        val checkout = Checkout(priceInCentsBySku)

        checkout.scan(ipdSku)
        checkout.scan(mbpSku)

        val expectedTotal = ipdPriceInCents + mbpPriceInCents
        assertEquals(expectedTotal, checkout.total())
    }
}

class Checkout(
    private val priceInCentsBySku: Map<String, Int>
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

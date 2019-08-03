package name.cgt.dius.shopping

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
        "mbp" to 139999,
        "vga" to 3000
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

    @Test
    fun `given scanned mbp, vga, deduct price of vga from total`() {
        val expectedTotal = priceInCentsBySku.getValue("mbp")
        val checkout = Checkout(priceInCentsBySku)

        checkout.scan("mbp")
        checkout.scan("vga")

        assertEquals(expectedTotal, checkout.total())
    }
}
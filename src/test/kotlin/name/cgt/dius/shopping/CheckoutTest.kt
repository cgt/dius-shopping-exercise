package name.cgt.dius.shopping

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(PER_CLASS)
class CheckoutTest {

    private val ATV = "atv"
    private val IPD = "ipd"
    private val MBP = "mbp"
    private val VGA = "vga"

    private val priceInCentsBySku = mapOf(
        ATV to 10950,
        IPD to 54999,
        MBP to 139999,
        VGA to 3000
    )

    private val catalog = Catalog(priceInCentsBySku)

    private fun price(sku: String) = priceInCentsBySku.getValue(sku)

    private lateinit var checkout: Checkout

    @BeforeEach
    fun setUp() {
        checkout = Checkout(catalog)
    }

    @Test
    fun `sell zero items`() {
        checkout.assertTotal(0)
    }

    @ParameterizedTest
    @MethodSource("itemProvider")
    fun `sell any one item`(item: Pair<String, Int>) {
        val (sku, priceInCents) = item

        checkout.scan(sku)

        checkout.assertTotal(priceInCents)
    }

    fun itemProvider(): Stream<Pair<String, Int>> {
        return priceInCentsBySku
            .entries
            .map { kv -> kv.toPair() }
            .stream()
    }

    @Test
    fun `sell multiple items`() {
        val ipdPriceInCents = price(IPD)
        val mbpPriceInCents = price(MBP)

        checkout.scan(IPD)
        checkout.scan(MBP)

        val expectedTotal = ipdPriceInCents + mbpPriceInCents
        checkout.assertTotal(expectedTotal)
    }

    @Test
    fun `given scanned mbp, vga, deduct price of vga from total`() {
        val expectedTotal = price(MBP)

        checkout.scan(MBP)
        checkout.scan(VGA)

        checkout.assertTotal(expectedTotal)
    }

    @Test
    fun `given scanned mbp and 2 x vga, only deduct price of one vga from total`() {
        val expectedTotal = price(MBP) + price(VGA)

        checkout.scan(MBP)
        checkout.scan(VGA)
        checkout.scan(VGA)

        checkout.assertTotal(expectedTotal)
    }

    @Test
    fun `given scanned 2 x mbp and 2 x vga, deduct price of two vga from total`() {
        val totalWithoutDiscount = (price(MBP) + price(VGA)) * 2
        val expectedTotal = totalWithoutDiscount - 2 * price(VGA)

        checkout.scan(MBP)
        checkout.scan(MBP)
        checkout.scan(VGA)
        checkout.scan(VGA)

        checkout.assertTotal(expectedTotal)
    }
}

private fun Checkout.assertTotal(expectedCents: Int) {
    assertEquals(expectedCents, total())
}
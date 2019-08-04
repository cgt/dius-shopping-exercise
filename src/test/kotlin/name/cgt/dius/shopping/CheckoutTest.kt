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
    private val priceInCentsBySku = mapOf(
        "atv" to 10950,
        "ipd" to 54999,
        "mbp" to 139999,
        "vga" to 3000
    )

    private lateinit var checkout: Checkout

    @BeforeEach
    fun setUp() {
        checkout = Checkout(priceInCentsBySku)
    }

    @Test
    fun `sell zero items`() {
        assertEquals(0, checkout.total())
    }

    @ParameterizedTest
    @MethodSource("itemProvider")
    fun `sell any one item`(item: Pair<String, Int>) {
        val (sku, priceInCents) = item

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


        checkout.scan(ipdSku)
        checkout.scan(mbpSku)

        val expectedTotal = ipdPriceInCents + mbpPriceInCents
        assertEquals(expectedTotal, checkout.total())
    }

    @Test
    fun `given scanned mbp, vga, deduct price of vga from total`() {
        val expectedTotal = priceInCentsBySku.getValue("mbp")

        checkout.scan("mbp")
        checkout.scan("vga")

        assertEquals(expectedTotal, checkout.total())
    }

    @Test
    fun `given scanned mbp and 2 x vga, only deduct price of one vga from total`() {
        val expectedTotal = priceInCentsBySku.getValue("mbp") + priceInCentsBySku.getValue("vga")

        checkout.scan("mbp")
        checkout.scan("vga")
        checkout.scan("vga")

        assertEquals(expectedTotal, checkout.total())
    }

    @Test
    fun `given scanned 2 x mbp and 2 x vga, deduct price of two vga from total`() {
        val mbpPrice = priceInCentsBySku.getValue("mbp")
        val vgaPrice = priceInCentsBySku.getValue("vga")
        val totalWithoutDiscount = (mbpPrice + vgaPrice) * 2
        val expectedTotal = totalWithoutDiscount - 2 * vgaPrice

        checkout.scan("mbp")
        checkout.scan("mbp")
        checkout.scan("vga")
        checkout.scan("vga")

        assertEquals(expectedTotal, checkout.total())
    }
}
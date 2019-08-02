import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CheckoutTest {
    @Test
    fun `sell zero items`() {
        val checkout = Checkout()
        assertEquals(0, checkout.total())
    }
}

class Checkout {
    fun total(): Int {
        return 0
    }
}

package name.cgt.dius.shopping

import kotlin.math.min

class Checkout(
    private val catalog: Catalog
) {
    private val scanned = ArrayList<String>()

    fun total(): Int {
        var total = scanned
            .map { item ->
                catalog.price(item)
            }
            .sum()
        total -= catalog.freeVGAWithPurchaseOfMBPDiscount(scanned)
        return total
    }

    fun scan(sku: String) {
        scanned.add(sku)
    }
}

class Catalog(
    private val priceInCentsBySku: Map<String, Int>
) {
    fun price(sku: String): Int = priceInCentsBySku.getOrDefault(sku, 0)

    fun freeVGAWithPurchaseOfMBPDiscount(items: List<String>): Int {
        val mbpQuantity = items.count { it == "mbp" }
        val vgaQuantity = items.count { it == "vga" }
        val freeVGAs = min(mbpQuantity, vgaQuantity)
        val discount = freeVGAs * priceInCentsBySku.getValue("vga")
        return discount
    }

}
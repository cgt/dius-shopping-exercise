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
        total -= freeVGAWithPurchaseOfMBPDiscount(scanned, catalog.priceInCentsBySku)
        return total
    }

    fun scan(sku: String) {
        scanned.add(sku)
    }
}

class Catalog(
    val priceInCentsBySku: Map<String, Int>
) {
    fun price(sku: String): Int = priceInCentsBySku.getOrDefault(sku, 0)
}

private fun freeVGAWithPurchaseOfMBPDiscount(items: ArrayList<String>, catalog: Map<String, Int>): Int {
    val mbpQuantity = items.count { it == "mbp" }
    val vgaQuantity = items.count { it == "vga" }
    val freeVGAs = min(mbpQuantity, vgaQuantity)
    val discount = freeVGAs * catalog.getValue("vga")
    return discount
}
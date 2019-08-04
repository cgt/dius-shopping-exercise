package name.cgt.dius.shopping

import kotlin.math.min

class Checkout(
    private val priceInCentsBySku: Map<String, Int>
) {
    private val scanned = ArrayList<String>()

    fun total(): Int {
        var total = scanned
            .map { item ->
                priceInCentsBySku.getOrDefault(item, 0)
            }
            .sum()
        total -= freeVGAWithPurchaseOfMBPDiscount(scanned, priceInCentsBySku)
        return total
    }

    fun scan(sku: String) {
        scanned.add(sku)
    }
}

private fun freeVGAWithPurchaseOfMBPDiscount(items: ArrayList<String>, catalog: Map<String, Int>): Int {
    val mbpQuantity = items.count { it == "mbp" }
    val vgaQuantity = items.count { it == "vga" }
    val freeVGAs = min(mbpQuantity, vgaQuantity)
    val discount = freeVGAs * catalog.getValue("vga")
    return discount
}
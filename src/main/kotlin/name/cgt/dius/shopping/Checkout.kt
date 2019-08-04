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
        val mbpQuantity = scanned.count { it == "mbp" }
        val vgaQuantity = scanned.count { it == "vga" }
        val freeVGAs = min(mbpQuantity, vgaQuantity)
        total -= freeVGAs * priceInCentsBySku.getValue("vga")
        return total
    }

    fun scan(sku: String) {
        scanned.add(sku)
    }
}
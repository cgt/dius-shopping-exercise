package name.cgt.dius.shopping

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
        if (scanned.contains("mbp") && scanned.contains("vga")) {
            total -= priceInCentsBySku.getValue("vga")
        }
        return total
    }

    fun scan(sku: String) {
        scanned.add(sku)
    }
}
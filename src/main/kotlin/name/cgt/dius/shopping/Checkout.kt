package name.cgt.dius.shopping

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
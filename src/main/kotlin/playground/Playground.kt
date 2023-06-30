fun main() {
    val map = mapOf('A' to "Alex", 'B' to "Bob")
    println(map.size) // 2
    println(map.isEmpty()) // false
    val map2 = mapOf<Char, String>()
    println(map2.size) // 0
    println(map2.isEmpty()) // true
}
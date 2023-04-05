package playground

import java.io.File


fun main() {
    println(0b0101 and 0b0001) // 1, that is 0b0001
    println(0b0101 or 0b0001)  // 5, that is 0b0101
    println(0b0101 xor 0b0001) // 4, that is 0b0100
    println(0b0101 shl 1) // 10, that is 0b1010
    println(0b0101 shr 1) // 2,  that is 0b0010
}

//fun main(): Unit {
//    val files = File("/Users/marcinmoskala/Projekty/kotlin_for_developers_book/manuscript").listFiles()!!
////    val files = File("manuscript").listFiles()!!
//    files
//        .filter { it.isFile }
//        .map {
//            it.readText()
//                .replace(Regex("```(kotlin|java)\\n([\\s\\S]*?)```"), "")
//                .replace(Regex("\\{crop-start: \\d+(, crop-end: \\d+)?}"), "")
//                .replace(Regex("\\{sample: (true|false)"), "")
//                .replace(Regex("\\{width: \\d+%}"), "")
//                .replace(Regex("!\\[\\]\\([\\w\\._]+\\)"), "")
//                .replace("#", "")
//                .replace("--", "")
//                .replace("|", "")
//                .replace("  ", "")
//        }
//        .sumOf {
//            it.filter { !it.isWhitespace() || it == ' ' }.length
//        }
//        .let { print(it) }
//}

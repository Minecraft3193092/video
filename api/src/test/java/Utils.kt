import org.junit.Test
import kotlin.math.max

fun printResult(result: Any) {
    val title = "|Test Result|"
    val rs = result.toString().split("\n")
    var maxWidth = 25
    rs.forEach {
        maxWidth = max(maxWidth, it.length)
    }
    val wl = (maxWidth - title.length) / 2
    val wr = maxWidth - wl - title.length
    print("=" * wl)
    print(title)
    print("=" * wr + "\n")
    rs.forEach {
        println(it)
    }
    println("=" * maxWidth)
}

fun printQr(t: Any) {
    Runtime.getRuntime().exec("qrencode -m 2 -t utf8").run {
        outputStream.write("$t".toByteArray())
        outputStream.close()
        inputStream.copyTo(System.out)
    }
}

private operator fun String.times(i: Int): String {
    val sb = StringBuilder()
    (0 until i).forEach {
        sb.append(this)
    }
    return sb.toString()
}

class UtilsTest {

    @Test
    fun testPrintResult() {
        printResult("")
        printResult("test")
        printResult("""
        l1
                                                    l2
        l3
    """.trimIndent())
    }
}

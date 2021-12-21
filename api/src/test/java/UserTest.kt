import io.github.duzhaokun123.bilibili.api.BilibiliService
import kotlinx.coroutines.runBlocking
import org.junit.Test

class UserTest {
    @Test
    fun testNav() {
        val r = runBlocking {
            runCatching {
                BilibiliService.userApi.nav().await()
            }
        }
        printResult(r)
    }
}
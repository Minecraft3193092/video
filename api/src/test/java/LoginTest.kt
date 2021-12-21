import io.github.duzhaokun123.bilibili.api.BilibiliService
import io.github.duzhaokun123.bilibili.api.Cookies
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

class LoginTest {
//    @Before
//    fun init() {
//        BilibiliService.setLogLevel(HttpLoggingInterceptor.Level.BODY)
//    }

    @Test
    fun textWebLogin() {
        val r = runBlocking {
            BilibiliService.loginApi.webLogin(
                username = "test",
                password = "test",
                key = "test",
                challenge = "test",
                validate = "test",
                seccode = "test"
            ).await()
        }
        printResult(r)
    }

    @Test
    fun testGetLoginUrl() {
        printResult(runBlocking {
            BilibiliService.loginApi.getLoginUrl().await()
        })
    }

    @Test
    fun testQrLogin() {
        val (key, url) = runBlocking { BilibiliService.loginApi.getLoginUrl().await().data }
        printResult("key: $key\nurl: $url")
        printQr(url)
        runBlocking {
            var t = 0
            delay(2000)
            while (true) {
                t++
                delay(2000)
                val loginInfo = runCatching { BilibiliService.loginApi.getLoginInfo(key).await() }
                printResult("t: $t\n${loginInfo}")
                if (loginInfo.isSuccess && loginInfo.getOrThrow().body()?.data !is Double) {
                    val cookies = Cookies.fromResponse(loginInfo.getOrThrow())
                    printResult(cookies)
                    BilibiliService.cookies = cookies
                    break
                }
            }
            printResult(BilibiliService.userApi.nav().await())
        }
    }
}
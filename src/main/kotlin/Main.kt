import client.Credentials
import client.GDClient
import client.GDClientApi
import client.GJP2

// TODO: Once this is actually more closer to being finished delete this temporary main function
@OptIn(GDClientApi::class)
private fun main() {
    val username = System.getenv("USERNAME")!!
    val password = System.getenv("PASSWORD")!!

    val client = GDClient(Credentials(username, GJP2.create(password)))
//    client.getUserInfo(14350205)
}
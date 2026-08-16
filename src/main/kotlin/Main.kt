import client.GDClient
import client.GJP2

// TODO: Once this is actually more closer to being finished delete this temporary main function
private fun main() {
    val username = System.getenv("USERNAME")!!
    val password = System.getenv("PASSWORD")!!

    val client = GDClient(username, GJP2.create(password))
}
package fr.geming400.gddotkt.client

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Request

open class GDClient(val username: String, val gjp2: GJP2, val url: HttpUrl = DEFAULT_URL) {
    companion object {
        val DEFAULT_URL = "https://www.boomlings.com/database".toHttpUrl()
    }

    protected fun createRequest(): Request.Builder =
        Request.Builder()
            .url(this.url)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("User-Agent", "")

    fun a() {
        GDClient("Geming400", GJP2.create("5"))
    }
}
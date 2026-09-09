package client

import okhttp3.HttpUrl

@GDClientApi
enum class Endpoint(val endpoint: String) {
    GET_USER_INFO("getGJUserInfo20");

    fun resolve(url: HttpUrl): HttpUrl =
        url.resolve("database/" + this.endpoint + ".php")!!
}
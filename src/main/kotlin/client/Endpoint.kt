package client

import okhttp3.HttpUrl

enum class Endpoint(val endpoint: String) {
    GET_USER_INFO("getGJUserInfo20");

    fun resolve(url: HttpUrl): HttpUrl =
        url.resolve("database/" + this.endpoint + ".php")!!
}
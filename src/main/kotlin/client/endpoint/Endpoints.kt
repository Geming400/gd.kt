package client.endpoint

import client.GDClientApi
import client.ResponseHandlers

@GDClientApi
object Endpoints {
    // User
    val GET_USER_INFO = Endpoint("getGJUserInfo20")

    // Account
    val LOGIN = Endpoint("accounts/loginGJAccount")

    // Comments
    val UPLOAD_COMMENT = Endpoint("uploadGJComment21", ResponseHandlers.COMMENT)
    val UPLOAD_ACCOUNT_COMMENT = Endpoint("uploadGJAccComment20", ResponseHandlers.COMMENT)
}
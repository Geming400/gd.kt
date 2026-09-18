package client

import client.endpoint.Endpoint
import client.endpoint.Endpoints
import client.struct.ServerStructure
import client.struct.ServerStructureCompanion
import client.struct.UserInfo
import editor.rawstring.serializing.Parsable
import editor.rawstring.serializing.Serializers
import exceptions.GdDotKtException
import exceptions.InvalidRawStringException
import exceptions.ServerErrorException
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.apache.commons.codec.digest.DigestUtils
import utils.cyclicXor
import utils.nonNull
import utils.remember
import utils.toFormRequestBody
import java.io.IOException
import java.util.*
import kotlin.io.encoding.Base64
import kotlin.random.Random

@GDClientApi
typealias ResponseHandler = (body: String) -> Unit

@GDClientApi
abstract class AbstractGDClient(
    val credentials: Credentials? = null,
    val url: HttpUrl = DEFAULT_URL,

    val gameVersion: UInt = GAME_VERSION,
    val binaryVersion: UInt = BINARY_VERSION,
    val platform: Platform = Platform.get()
) {
    companion object {
        val DEFAULT_URL = "https://www.boomlings.com/".toHttpUrl()
        const val GAME_VERSION = 22u
        const val BINARY_VERSION = 47u

        /**
         * Generates an [UDID](https://en.wikipedia.org/wiki/UDID) according to [boomlings.dev](https://boomlings.dev/topics/encryption/id#udid)
         */
        fun generateUDID(rngSource: Random = Random): String {
            val parts = ArrayList<String>(4)
            for (i in 0..4)
                parts.add(rngSource.nextInt(100_000, 100_000_000).toString())

            return "S15" + parts.joinToString(separator = "")
        }

        fun createCHK(values: List<Any>, key: String, salt: String? = null): String {
            val valuesToCompute = values
                .map { it.toString() }
                .toMutableList()

            if (salt != null)
                valuesToCompute.add(salt)

            val stringToCompute = valuesToCompute.joinToString()

            val hashed = DigestUtils.sha1Hex(stringToCompute)
            val xored = hashed.cyclicXor(key)
            return Base64.UrlSafe.encode(xored.toByteArray())
        }
    }

    protected val client = OkHttpClient()

    private var internalAccountInfo: Array<String>? = null

    /**
     * The client's account ID.
     * If not logged in, `null` will be returned.
     *
     * This info is always fetched **synchronously**
     * @see getAccountIdOrThrow
     */
    val accountID: UInt?
        get() {
            if (!this.isLoggedIn())
                return null

            if (this.internalAccountInfo == null) {
                this.internalAccountInfo = this.getAccountInfo()
                return this.internalAccountInfo!![0].toUInt()
            } else {
                return this.internalAccountInfo!![0].toUInt()
            }
        }

    /**
     * The client's player ID.
     * If not logged in, `null` will be returned.
     *
     * This info is always fetched **synchronously**
     * @see getPlayerIdOrThrow
     */
    val playerID: UInt?
        get() {
            if (!this.isLoggedIn())
                return null

            if (this.internalAccountInfo == null) {
                this.internalAccountInfo = this.getAccountInfo()
                return this.internalAccountInfo!![1].toUInt()
            } else {
                return this.internalAccountInfo!![1].toUInt()
            }
        }

    /**
     * The [UDID](https://en.wikipedia.org/wiki/UDID) linked to this client.
     * Each client generates its own UDID
     * @see generateUDID
     */
    val udid: String = generateUDID()

    protected fun resolveURL(endpoint: Endpoint): HttpUrl =
        endpoint.resolve(this.url)

    protected open fun createRequest(endpoint: Endpoint): Request.Builder =
        Request.Builder()
            .url(this.resolveURL(endpoint))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("User-Agent", "")

    protected fun <T : ServerStructure> executeRequest(
        serverStructureCompanion: ServerStructureCompanion<T>,
        endpoint: Endpoint,
        data: Map<Any, Any>,
        secret: Secret = Secret.COMMON,
        asyncCallback: CallbackWithData<T>? = null,
        postProcessingReqBuilder: (Request.Builder) -> Request.Builder = { it },
        responseHandler: ResponseHandler = endpoint.responseHandler
    ): Result<T> =
        this.executeRequest(
            endpoint,
            data,
            secret,
            asyncCallback,
            postProcessingReqBuilder,
            { rawString: String, client: AbstractGDClient -> serverStructureCompanion.parse(rawString, client) },
            responseHandler
        )

    protected fun <T> executeRequest(
        serializer: Parsable<T>,
        endpoint: Endpoint,
        data: Map<Any, Any>,
        secret: Secret = Secret.COMMON,
        asyncCallback: CallbackWithData<T>? = null,
        postProcessingReqBuilder: (Request.Builder) -> Request.Builder = { it },
        responseHandler: ResponseHandler = endpoint.responseHandler
    ): Result<T> =
        this.executeRequest(
            endpoint,
            data,
            secret,
            asyncCallback,
            postProcessingReqBuilder,
            { rawString: String, client: AbstractGDClient -> serializer.parse(rawString) },
            responseHandler
        )

    protected open fun <T> executeRequest(
        endpoint: Endpoint,
        data: Map<Any, Any>,
        secret: Secret = Secret.COMMON,
        asyncCallback: CallbackWithData<T>? = null,
        postProcessingReqBuilder: (Request.Builder) -> Request.Builder = { it },
        parser: (rawString: String, client: AbstractGDClient) -> T,
        responseHandler: ResponseHandler = {}
    ): Result<T> {
        val reqBuilder = postProcessingReqBuilder(
            this.createRequest(endpoint)
                .post(data.toFormRequestBodyWithClientInfo(this, secret))
        )

        val call = this.client.newCall(reqBuilder.build())
        if (asyncCallback == null) {
            // if asyncCallback == null then we do synchronous requests
            try {
                call.execute().use { response ->
                    val body = response.body.string()
                    responseHandler(body)

                    val res = Result.success(parser(body, this))
                    return res
                }
            } catch (e: IOException) {
                return Result.failure(e)
            } catch (e: Exception) {
                return Result.failure(e)
            }
        } else {
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) =
                    asyncCallback.onNetworkFailure(call, e)

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val body = response.body.string()
                        responseHandler(body)

                        asyncCallback.onResponse(call, response, parser(body, this@AbstractGDClient))
                    } catch (e: InvalidRawStringException) {
                        asyncCallback.onParsingFailure(call, e)
                    }
                }
            })

            return Result.failure(NullPointerException("Cannot get a return value on async requests"))
        }
    }

    protected fun getAccountInfo(): Array<String> {
        val req =
            this.createRequest(Endpoints.LOGIN)
                .post(mapOf<Any, Any>(
                    Pair("userName", this.credentials!!.username),
                    Pair("udid", this.udid)
                ).toFormRequestBodyWithClientInfo(this, Secret.ACCOUNT))
                .build()

        this.client.newCall(req).execute().use { response ->
            val body = response.body.string()
            when (body) {
                "-1" -> throw ServerErrorException.genericError()
                "-11" -> throw ServerErrorException(-11, "Login failed. Incorrect credentials")
                "-12" -> throw ServerErrorException(-12, "Account has been disabled")
            }

            return body.split(",").toTypedArray()
        }
    }

    fun getAccountIdOrThrow(): UInt =
        Objects.requireNonNull(this.accountID, "Cannot get the client's accountID since no credentials were entered")!!

    fun getPlayerIdOrThrow(): UInt =
        Objects.requireNonNull(this.playerID, "Cannot get the client's accountID since no credentials were entered")!!

    /**
     * If this client is logged in. This doesn't check if the credentials are valid
     * @see credentials
     */
    fun isLoggedIn(): Boolean =
        this.credentials != null

    fun throwIfLoggedOut() {
        if (!this.isLoggedIn())
            throw GdDotKtException("This client must be logged in, but it isn't")
    }
}

@GDClientApi
class GDClient(
    credentials: Credentials? = null,
    url: HttpUrl = DEFAULT_URL,

    gameVersion: UInt = GAME_VERSION,
    binaryVersion: UInt = BINARY_VERSION,
    platform: Platform = Platform.get()
) : AbstractGDClient(credentials, url, gameVersion, binaryVersion, platform) {
    fun getUserInfo(accountID: Int): Result<UserInfo> =
        this.executeRequest(
            UserInfo,
            Endpoints.GET_USER_INFO,
            mapOf(
                Pair("targetAccountID", accountID)
            ),
            asyncCallback = null
        )

    /**
     * @return the ID of the sent comment
     */
    fun postAccountComment(message: String): Result<Int> {
        this.throwIfLoggedOut()
        return this.executeRequest(
            Serializers.INT,
            Endpoints.UPLOAD_ACCOUNT_COMMENT,
            mapOf(
                Pair("comment", Base64.UrlSafe.encode(message.toByteArray())),
                Pair("accountID", this.accountID!!)
            ),
            asyncCallback = null
        )
    }
}

@GDClientApi
class AsyncGDClient(
    credentials: Credentials? = null,
    url: HttpUrl = DEFAULT_URL,

    gameVersion: UInt = GAME_VERSION,
    binaryVersion: UInt = BINARY_VERSION,
    platform: Platform = Platform.get()
) : AbstractGDClient(credentials, url, gameVersion, binaryVersion, platform) {
    fun getUserInfo(accountID: Int, asyncCallback: CallbackWithData<UserInfo>) {
        this.executeRequest(
            UserInfo,
            Endpoints.LOGIN,
            mapOf(
                Pair("targetAccountID", accountID)
            ),
            asyncCallback = asyncCallback
        )
    }

    /**
     * @return the ID of the sent comment
     */
    fun postAccountComment(message: String, asyncCallback: CallbackWithData<Int>) {
        this.throwIfLoggedOut()
        this.executeRequest(
            Serializers.INT,
            Endpoints.UPLOAD_ACCOUNT_COMMENT,
            mapOf(
                Pair("comment", Base64.UrlSafe.encode(message.toByteArray())),
                Pair("accountID", this.accountID!!)
            ),
            asyncCallback = asyncCallback
        )
    }
}

// The 'Any' upper bound is to prevent null types
@GDClientApi
fun <K : Any, V : Any> Map<K, V>.toFormRequestBodyWithClientInfo(client: AbstractGDClient, secret: Secret): FormBody {
    val bodyBuilder = this.toFormRequestBody()
    bodyBuilder.add("secret", secret.secret)
    bodyBuilder.add("gameVersion", client.gameVersion.toString())
    bodyBuilder.add("gameVersion", client.binaryVersion.toString())
    bodyBuilder.add("dvs", client.platform.value.toString())
    if (client.credentials != null)
        bodyBuilder.add("gjp2", client.credentials.gjp2.encryptedPassword)
    bodyBuilder.add("uuid", UUID.randomUUID().toString())

    return bodyBuilder.build()
}

@GDClientApi
interface CallbackWithData<T> {
    @Throws(IOException::class)
    fun onNetworkFailure(
        call: Call,
        e: IOException,
    )

    fun onParsingFailure(
        call: Call,
        e: InvalidRawStringException,
    ) {}

    fun onGdServerException(
        call: Call,
        e: ServerErrorException
    ) {}

    @Throws(IOException::class)
    fun onResponse(
        call: Call,
        response: Response,
        data: T
    )
}

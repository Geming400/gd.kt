package fr.geming400.gddotkt.client

import org.apache.commons.codec.digest.DigestUtils

@JvmInline
value class GJP2 internal constructor(val encryptedPassword: String) {
    companion object {
        const val SALT = "mI29fmAnxgTs"

        fun encryptPassword(password: String): String {
            return DigestUtils.sha1Hex(password + SALT)
        }

        fun create(password: String): GJP2 =
            GJP2(encryptPassword(password))

        fun fromGJP2(gjp2: String): GJP2 =
            GJP2(gjp2)
    }
}
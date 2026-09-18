package client

import org.apache.commons.lang3.SystemUtils

/**
 * An enum containing all platforms geometry dash builds support.
 * You can get your local platform by using [get]
 */
enum class Platform(val value: Int) {
    IOS(1),
    ANDROID(2),
    WINDOWS(3),
    MACOS(8);

    companion object {
        /**
         * The default [Platform] value when this device's
         * OS cannot be found/is unknown
         * @see get
         */
        inline val DEFAULT
            get() = WINDOWS

        /**
         * Gets the current platform from this device's OS.
         * If the user is not on `Windows`, `MacOS` or `Android` then
         * [DEFAULT] is returned
         */
        fun get(): Platform {
            return if (SystemUtils.IS_OS_WINDOWS) {
                WINDOWS
            } else if (SystemUtils.IS_OS_MAC) {
                MACOS
            } else if (SystemUtils.IS_OS_ANDROID) {
                ANDROID
            } else {
                DEFAULT
            }
        }
    }
}
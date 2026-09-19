import utils.cyclicXor
import utils.staticXor

/**
 * An enum containing all xor keys Geometry Dash uses.
 * See [boomlings.dev](https://boomlings.dev/topics/encryption/xor#xor-keys)
 */
enum class XorKey(val key: Int, val type: XorType = XorType.CYCLIC) {
    PLAYER_SAVE_DATA(11, XorType.STATIC),
    PLAYER_MESSAGES(14251),
    VAULT_CODES(19283),
    DAILY_CHALLENGES(19847),
    LEVEL_PASSWORD(26364),
    COMMENT_INTEGRITY(29481),
    ACCOUNT_PASSWORD(37526),
    LEVEL_LEADERBOARD_INTEGRITY(39673),
    LEVEL_INTEGRITY(41274),
    LOAD_DATA(48291),
    MULTIPLAYER(52832),
    MUSIC_SFX_LIBRARY_SECRET(57709),
    RATING_INTEGRITY(58281),
    CHEST_REWARDS(59182),
    STAT_SUBMISSION_INTEGRITY(85271);

    infix fun applyXor(input: String): String =
        this.type.xorer(input, this.key)
}

enum class XorType(val xorer: String.(Int) -> String) {
    STATIC(String::staticXor),
    CYCLIC(String::cyclicXor);
}
package client.struct

import client.GDClient
import client.GDClientApi
import client.enums.Gamemode
import editor.objects.ObjectParser
import editor.rawstring.RawStringFactory
import editor.rawstring.id
import editor.rawstring.property.BoolProperty
import editor.rawstring.property.EnumProperty
import editor.rawstring.property.GdEnum
import editor.rawstring.property.IntProperty
import editor.rawstring.property.UIntProperty
import editor.rawstring.property.UnencodedStringProperty
import editor.rawstring.serializing.Serializer

enum class MessageState(override val value: Int) : GdEnum {
    ALL(0),
    FRIENDS_ONLY(1),
    NONE(2),
}

enum class FriendsState(override val value: Int) : GdEnum {
    ALL(0),
    NONE(1),
}

enum class Special(override val value: Int) : GdEnum {
    GLOW_DISABLED(0),
    ENABLED(2),
}

enum class FriendState(override val value: Int) : GdEnum {
    UNFRIENDED(0),
    FRIENDED(1),
    FRIEND_REQUEST_SENT(3),
    FRIEND_REQUEST_RECEIVED(4),
}

enum class ModLevel(override val value: Int) : GdEnum {
    NONE(0),
    NORMAL_MOD(1),
    ELDER_MOD(2),
    LEADERBOARD_MOD(3),
}

enum class CommentHistoryState(override val value: Int) : GdEnum {
    ALL(0),
    FRIENDS_ONLY(1),
    NONE(2),
}

// TODO: breakdown
@GDClientApi
open class UserStructure(override val client: GDClient) : ServerStructure {
    companion object : ServerStructureCompanion<UserStructure> {
        override val separator: Char = ':'

        override fun parse(rawString: String, client: GDClient): UserStructure =
            ObjectParser.parse(rawString, UserStructure(client))
    }

    override val rawStringFactory: RawStringFactory = RawStringFactory.create(this)

    val userName = UnencodedStringProperty(1.id, defaultValue = null)
    val userID = UIntProperty(2.id, defaultValue = null)
    val stars = UIntProperty(3.id, defaultValue = null)
    val demonCount = UIntProperty(4.id, defaultValue = null)
    val creatorPoints = UIntProperty(8.id, defaultValue = null)
    val color = UIntProperty(10.id, defaultValue = null)
    val color2 = UIntProperty(11.id, defaultValue = null)
    val shipID = UIntProperty(12.id, defaultValue = null)
    val secretCoins = UIntProperty(13.id, defaultValue = null)
    val accountID = UIntProperty(16.id, defaultValue = null)
    val userCoins = UIntProperty(17.id, defaultValue = null)
}

@GDClientApi
class UserScore(client: GDClient) : UserStructure(client) {
    val ranking = IntProperty(6.id, defaultValue = null)
    /**
     * The player's account ID, or the device ID for unregistered players.
     * Used for highlighting unregistered players on the leaderboards.
     * For registered players, the game instead checks if the accountID field matches the current account.
     *
     * Only has a value when viewing yourself on a leaderboard
     * */
    val accountHighlight = UnencodedStringProperty(7.id, defaultValue = null)
    val iconType = EnumProperty(14.id, Serializer.enum(Gamemode.entries))
    val iconID = UIntProperty(9.id, defaultValue = null)
    val special = EnumProperty(15.id, Serializer.enum(Special.entries))
}

@GDClientApi
class UserInfo(client: GDClient) : UserStructure(client) {
    val ranking = IntProperty(6.id, defaultValue = null)
    val iconType = EnumProperty(14.id, Serializer.enum(Gamemode.entries))
    val iconID = UIntProperty(9.id, defaultValue = null)
    val special = EnumProperty(15.id, Serializer.enum(Special.entries))
    val messageState = EnumProperty(18.id, Serializer.enum(MessageState.entries))
    val friendsState = EnumProperty(19.id, Serializer.enum(FriendsState.entries))
    val youTube = UnencodedStringProperty(20.id, defaultValue = null)
    val accIcon = UIntProperty(21.id, defaultValue = null)
    val accShip = UIntProperty(22.id, defaultValue = null)
    val accBall = UIntProperty(23.id, defaultValue = null)
    val accBird = UIntProperty(24.id, defaultValue = null)
    val accDart = UIntProperty(25.id, defaultValue = null)
    val accRobot = UIntProperty(26.id, defaultValue = null)
    val accGlow = BoolProperty(28.id, defaultValue = null)
    val isRegistered = BoolProperty(29.id, defaultValue = null)
    val globalRank = UIntProperty(30.id, defaultValue = null)
    val friendState = EnumProperty(31.id, Serializer.enum(FriendState.entries))
    /** Only has a value when the player sent you a friend request */
    val friendRequestID = UIntProperty(32.id, defaultValue = null)
    /** Only has a value when the player sent you a friend request */
    val friendRequestMessage = UnencodedStringProperty(35.id, defaultValue = null)
    /** Only has a value when the player sent you a friend request */
    val friendRequestAge = UnencodedStringProperty(37.id, defaultValue = null)
    /** Only has a value when logged in and when viewing your own profile */
    val messages = UIntProperty(38.id, defaultValue = null)
    val friendRequests = UIntProperty(39.id, defaultValue = null)
    val newFriends = UIntProperty(40.id, defaultValue = null)
    val accSpider = UIntProperty(43.id, defaultValue = null)
    val twitter = UnencodedStringProperty(44.id, defaultValue = null)
    val twitch = UnencodedStringProperty(45.id, defaultValue = null)
    val diamonds = UIntProperty(46.id, defaultValue = null)
    val accExplosion = UIntProperty(48.id, defaultValue = null)
    val mod = EnumProperty(49.id, Serializer.enum(ModLevel.entries))
    val commentHistoryState = EnumProperty(50.id, Serializer.enum(CommentHistoryState.entries))
    val color3 = UIntProperty(51.id, defaultValue = null)
    val moons = UIntProperty(52.id, defaultValue = null)
    val accSwing = UIntProperty(53.id, defaultValue = null)
    val accJetpack = UIntProperty(54.id, defaultValue = null)
    val demons = UnencodedStringProperty(55.id, defaultValue = null)
    val classicLevels = UnencodedStringProperty(56.id, defaultValue = null)
    val platformerLevels = UnencodedStringProperty(57.id, defaultValue = null)
    val discord = UnencodedStringProperty(58.id, defaultValue = null)
    val instagram = UnencodedStringProperty(59.id, defaultValue = null)
    val tiktok = UnencodedStringProperty(60.id, defaultValue = null)
    /** The player's custom one-time authentication token */
    val custom = UnencodedStringProperty(61.id, defaultValue = null)
}

@GDClientApi
class LeaderboardUser(client: GDClient) : UserStructure(client) {
    /** The time since you submitted a levelScore */
    val age = UnencodedStringProperty(42.id, defaultValue = null)
}

@GDClientApi
class FriendRequestUser(client: GDClient) : UserStructure(client) {
    val newFriendRequest = BoolProperty(41.id, defaultValue = null)
}

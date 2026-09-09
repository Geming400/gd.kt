package client.enums

import editor.rawstring.property.GdEnum

enum class Gamemode(override val value: Int) : GdEnum {
    CUBE(0),
    SHIP(1),
    BALL(2),
    UFO(3),
    WAVE(4),
    ROBOT(5),
    SPIDER(6),
    SWING(7)
}
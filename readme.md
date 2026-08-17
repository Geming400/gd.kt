# gd.kt

*(This is actually the lib I've remade the most)*

This is the 3rd gd.lang lib I've made (it's predecessor is [gddotpy v2](https://github.com/Geming400/gddotpy-v2)).
This is mostly just to do a bit more kotlin and learn it more deeply.

It can be used, but do not really expect anything, though the difference with most geometry dash libraries is that this one allows you to create objects using an easy to use property system.

There is not much java interoperability because for example `UInt`s cannot get created on java and fully used.

## Installation

You can install this lib via JitPack:
```kts
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.Geming400:gd.kt:<version tag>")
}
```

## Usage

### Objects

`gd.kt` allows you to create objects.
You can look in the `fr.geming400.gddotkt.editor.objects` package or see the inheritors of `SimpleObject`.

You can also create your own object instances by extending the different open classes, like:
- SimpleObject
- ComplexObject
- TriggerObject

To add properties, you can look at the [gd info explorer](https://flowvix.github.io/gd-info-explorer/props) website
to know their ids, then you can add properties. For example:
```kt
class MyObjClass : ComplexObject {
    val myIntProp = IntProperty(1.id, defaultValue = 2)
    val myEnumProp = EnumProperty(1.id, Serializer.enum(MyEnum.entries))
}
```

And then you can get something called a "raw string" which is the raw
representation of the object via:
```kt
MyObjClass().asRawString()
```

### Parsing 

Raw strings can get parses into any classes.
Using the `ObjectParser` object you can input a raw string and get an output
TODO
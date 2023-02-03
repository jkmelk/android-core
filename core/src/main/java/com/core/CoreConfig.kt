package com.core


class CoreConfig private constructor(builder: Builder) {

    val mainContainer: Int
    val baseUrlDev: String
    val baseUrlQa: String
    val baseUrlLive: String
    val versionName: String
    val versionCode: Int
    val scale: String
    val appId: String

    init {
        this.mainContainer = builder.mainContainer
        this.baseUrlDev = builder.baseUrlDev
        this.baseUrlQa = builder.baseUrlQa
        this.baseUrlLive = builder.baseUrlLive
        this.versionName = builder.versionName
        this.versionCode = builder.versionCode
        this.scale = builder.scale
        this.appId = builder.appId
    }

    class Builder {
        var mainContainer: Int = 0
            private set
        var baseUrlDev: String = ""
            private set
        var baseUrlQa: String = ""
            private set
        var baseUrlLive: String = ""
            private set
        var versionName: String = ""
            private set
        var versionCode: Int = 0
            private set
        var scale: String = ""
            private set
        var appId: String = ""
            private set

        fun mainContainer(mainContainer: Int) = apply { this.mainContainer = mainContainer }
        fun baseUrlDev(baseUrlDev: String) = apply { this.baseUrlDev = baseUrlDev }
        fun baseUrlQa(baseUrlQa: String) = apply { this.baseUrlQa = baseUrlQa }
        fun baseUrlLive(baseUrlLive: String) = apply { this.baseUrlLive = baseUrlLive }
        fun versionName(versionName: String) = apply { this.versionName = versionName }
        fun versionCode(versionCode: Int) = apply { this.versionCode = versionCode }
        fun scale(scale: String) = apply { this.scale = scale }
        fun appId(appId: String) = apply { this.appId = appId }
        fun build() = CoreConfig(this)
    }
}
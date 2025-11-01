package malta.pedro.speedometer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
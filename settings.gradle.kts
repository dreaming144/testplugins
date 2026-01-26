dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

val disabled = listOf<String>()  // Add folder names here to disable, e.g., listOf("ExampleProvider")

rootDir.listFiles()?.filter { it.isDirectory && !disabled.contains(it.name) && it.name != "gradle" && it.name != ".github" && it.name != ".git" && !it.name.startsWith(".") }?.forEach {
    include(":${it.name}")
}
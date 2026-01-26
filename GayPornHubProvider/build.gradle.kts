plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.lagradost.cloudstream3.gradle")
}

cloudstream {
    pluginId = "com.lagradost.cloudstream3.gaypornhubprovider"
    pluginName = "Gay PornHub"
    pluginDescription = "Gay-focused PornHub provider for CloudStream"
    pluginVersion = 1
}

android {
    namespace = "com.lagradost.cloudstream3.gaypornhubprovider"
}
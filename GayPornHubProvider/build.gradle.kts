// Optional additional dependencies if needed (e.g., for UI or specific libs)
dependencies {
    // Example: implementation("com.google.android.material:material:1.12.0")
}

// Use an integer for version numbers
version = 1

cloudstream {
    // All of these properties are optional, you can safely remove any of them.
    description = "Gay-focused PornHub provider for CloudStream"
    authors = listOf("Grok")  // Or your name/community
    /**
     * Status int as one of the following:
     * 0: Down
     * 1: Ok
     * 2: Slow
     * 3: Beta-only
    **/
    status = 1 // Will be 3 if unspecified
    tvTypes = listOf("NSFW")  // Or "Others" for adult content
    language = "en"
    // Optional icon URL for the extension
    iconUrl = "https://www.pornhub.com/static/img/ph-logo.png"  // Replace with a suitable icon if needed
}

android {
    namespace = "com.lagradost.cloudstream3.gaypornhubprovider"
    buildFeatures {
        buildConfig = true
        viewBinding = true  // If needed for any UI, else remove
    }
}
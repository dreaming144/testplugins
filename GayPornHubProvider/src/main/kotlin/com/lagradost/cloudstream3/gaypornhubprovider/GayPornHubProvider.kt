package com.lagradost.cloudstream3.gaypornhubprovider

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import org.jsoup.nodes.Document

class GayPornHubProvider : MainAPI() {
    override var mainUrl = "https://www.pornhub.com/gay"
    override var name = "Gay PornHub"
    override val hasQuickSearch = false
    override val hasMainPage = true
    override val supportedTypes = setOf(TvType.NSFW)
    override var lang = "en"

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val homePages = arrayListOf<HomePageList>()

        // Popular Gay Videos
        val popularUrl = "$mainUrl/video?page=$page"
        val popularDoc = app.get(popularUrl).document
        val popularItems = popularDoc.select("li.pcVideoListItem").mapNotNull { toSearchResponse(it) }
        if (popularItems.isNotEmpty()) homePages.add(HomePageList("Popular Gay Videos", popularItems))

        // Gay-Specific Categories
        val categoriesUrl = "$mainUrl/categories"
        val categoriesDoc = app.get(categoriesUrl).document
        val categoryItems = categoriesDoc.select("div.category-wrapper").mapNotNull {
            val catName = it.select("a span").text().trim()
            val catUrl = fixUrl(it.select("a").attr("href"))
            if (catName.isNotEmpty() && catUrl.contains("/gay")) {
                newMovieSearchResponse(catName, catUrl, TvType.NSFW) {}
            } else null
        }
        if (categoryItems.isNotEmpty()) homePages.add(HomePageList("Gay Categories", categoryItems, true))

        return newHomePageResponse(homePages)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val searchUrl = "$mainUrl/video/search?search=${query.replace(" ", "+")}&gay=1" // Gay search filter
        val doc = app.get(searchUrl).document
        return doc.select("li.pcVideoListItem").mapNotNull { toSearchResponse(it) }
    }

    override suspend fun load(url: String): LoadResponse {
        val doc = app.get(url).document
        val title = doc.select("span.inlineFree").text().trim()
        val poster = doc.select("img#videoElementPoster").attr("src")
        val description = doc.select("div.videoInfo .description").text().trim()
        val tags = doc.select("div.categoriesWrapper a").map { it.text() }

        return newMovieLoadResponse(title, url, TvType.NSFW, url) {
            this.posterUrl = poster
            this.plot = description
            this.tags = tags
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val doc = app.get(data).document
        val script = doc.select("script[type=application/ld+json]").html()
        val videoUrl = Regex("\"url\":\"(.*?)\"").find(script)?.groupValues?.get(1)?.replace("\\", "") ?: return false

        if (videoUrl.isNotEmpty()) {
            callback.invoke(
                newExtractorLink(
                    source = this.name,
                    name = "",
                    url = videoUrl,
                    referer = mainUrl,
                    quality = Qualities.Unknown.value,
                    isM3u8 = videoUrl.contains(".m3u8")
                )
            )
        }

        loadExtractor(data, subtitleCallback, callback)

        return true
    }

    private fun toSearchResponse(element: org.jsoup.nodes.Element?): SearchResponse? {
        val titleElem = element?.select("span.title a") ?: return null
        val title = titleElem.text().trim()
        val href = fixUrl(titleElem.attr("href"))
        val poster = element.select("img").attr("data-src") ?: element.select("img").attr("src")

        return newMovieSearchResponse(title, href, TvType.NSFW) {
            this.posterUrl = poster
        }
    }

    private fun fixUrl(url: String): String {
        return if (url.startsWith("/")) mainUrl + url else url
    }
}
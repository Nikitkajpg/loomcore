package com.njpg.loomcore.core

import com.njpg.loomcore.model.update.GithubLatestReleaseDto
import com.njpg.loomcore.model.update.UpdateInfo
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class UpdateChecker(
    private val client: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }
) {
    suspend fun checkForUpdate(): Result<UpdateInfo?> = runCatching {
        val url = "https://api.github.com/repos/${AppInfo.GITHUB_OWNER}/${AppInfo.GITHUB_REPO}/releases/latest"

        val response = client.get(url) {
            header("Accept", "application/vnd.github+json")
            header("X-GitHub-Api-Version", "2022-11-28")
        }

        if (!response.status.isSuccess()) {
            if (response.status.value == 404) return@runCatching null
            error("GitHub API error: ${response.status}")
        }

        val release = response.body<GithubLatestReleaseDto>()

        val info = UpdateInfo(
            latestVersion = release.tagName, releaseUrl = release.htmlUrl, releaseNotes = release.body
        )

        info.takeIf { it.isUpdateAvailable }
    }
}
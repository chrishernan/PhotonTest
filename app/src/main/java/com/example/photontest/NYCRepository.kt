package com.example.photontest

class NYCRepository(private val service: NYCApiService) {

    internal suspend fun fetch() = runCatching {
        service.fetch()
    }
}
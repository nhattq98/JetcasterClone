package com.example.clonejetcaster

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory

class JetcasterApplication : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            // Disable `Cache-Control` header support as some podcast images disable disk caching.
            .respectCacheHeaders(false)
            .build()
    }
}

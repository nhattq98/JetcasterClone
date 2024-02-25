package com.example.clonejetcaster.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clonejetcaster.Graph
import com.example.clonejetcaster.ui.data.PodcastWithExtraInfo
import com.example.clonejetcaster.ui.data.repository.PodcastsRepository
import com.example.clonejetcaster.ui.data.store.PodcastStore
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeViewModel(
    private val podcastsRepository: PodcastsRepository = Graph.podcastRepository,
    private val podcastStore: PodcastStore = Graph.podcastStore,
) : ViewModel() {
    private val selectedCategory = MutableStateFlow(HomeCategory.Discover)
    private val categories = MutableStateFlow(HomeCategory.values().asList())
    private val _state = MutableStateFlow(HomeViewState())
    private val refreshing = MutableStateFlow(false)
    val state: StateFlow<HomeViewState>
        get() = _state

    init {
        viewModelScope.launch {
            // combines the latest value from each of the flows
            combine(
                categories,
                selectedCategory,
                podcastStore.followedPodcastSortedByLastEpisode(limit = 20),
                refreshing
            ) { categories, selectedCategory, podcasts, refreshing ->
                HomeViewState(
                    homeCategories = categories,
                    selectedHomeCategory = selectedCategory,
                    featuredPodcasts = podcasts.toPersistentList(),
                    refreshing = refreshing,
                    errorMessage = null
                )
            }.catch {
                // emit error
                throw it
            }.collect {
                _state.value = it
            }
        }

        refresh(force = false)
    }

    private fun refresh(force: Boolean) {
        viewModelScope.launch {
            runCatching {
                refreshing.value = true
                podcastsRepository.updatePodcasts(force)
            }
            // TODO: look at result of runCatching and show any errors

            refreshing.value = false
        }
    }

    fun onHomeCategorySelected(category: HomeCategory) {
        selectedCategory.value = category
    }

    fun onPodcastUnfollowed(podcastUri: String) {
        viewModelScope.launch {
            podcastStore.unfollowPodcast(podcastUri)
        }
    }
}

data class HomeViewState(
    val featuredPodcasts: PersistentList<PodcastWithExtraInfo> = persistentListOf(),
    val refreshing: Boolean = false,
    val selectedHomeCategory: HomeCategory = HomeCategory.Discover,
    val homeCategories: List<HomeCategory> = emptyList(),
    val errorMessage: String? = null,
)
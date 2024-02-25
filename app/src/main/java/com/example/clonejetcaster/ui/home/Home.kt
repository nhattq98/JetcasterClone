package com.example.clonejetcaster.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clonejetcaster.R
import com.example.clonejetcaster.ui.data.PodcastWithExtraInfo
import com.example.clonejetcaster.utils.verticalGradientScrim
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun Home(
    navigateToPlayer: ((String) -> Unit)? = null,
    viewModel: HomeViewModel = viewModel(),
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    Surface(Modifier.fillMaxSize()) {
        Log.d("tahn_check", "$viewState")
        HomeContent(
            featuredPodcasts = viewState.featuredPodcasts,
            isRefreshing = viewState.refreshing,
            homeCategories = viewState.homeCategories,
            selectedHomeCategory = viewState.selectedHomeCategory,
            onCategorySelected = viewModel::onHomeCategorySelected,
            onPodcastUnfollowed = viewModel::onPodcastUnfollowed,
            navigateToPlayer = navigateToPlayer,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun HomeContent(
    featuredPodcasts: PersistentList<PodcastWithExtraInfo> = persistentListOf(),
    isRefreshing: Boolean = false,
    selectedHomeCategory: HomeCategory,
    homeCategories: List<HomeCategory> = listOf(HomeCategory.Library, HomeCategory.Discover),
    modifier: Modifier = Modifier,
    onPodcastUnfollowed: ((String) -> Unit)? = null,
    onCategorySelected: (HomeCategory) -> Unit,
    navigateToPlayer: ((String) -> Unit)? = null,
) {
    val surfaceColor = MaterialTheme.colorScheme.surface
    val appBarColor = surfaceColor.copy(alpha = 0.87f)

    Column(Modifier.windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Horizontal))) {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalGradientScrim(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
                    startYPercentage = 1f,
                    endYPercentage = 0f
                )
        ) {
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .background(color = appBarColor)
                    .windowInsetsTopHeight(WindowInsets.statusBars)
            )

            HomeAppBar(backgroundColor = appBarColor, modifier = Modifier.fillMaxWidth())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(
    backgroundColor: Color,
    modifier: Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Row {
                Image(
                    painter = painterResource(R.drawable.ic_logo),
                    contentDescription = null
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_text_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .heightIn(max = 24.dp)
                )
            }
        },
        actions = {
            Row {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = null)
                }
            }
        }
    )
}

@Composable
fun HomeCategoryTab(
    categories: List<HomeCategory>,
    selectedCategory: HomeCategory,
    onCategorySelected: (HomeCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedIndex = categories.indexOfFirst { it == selectedCategory }
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        HomeCategoryTabIndicator(
            Modifier.tabIndicatorOffset(tabPositions[selectedIndex])
        )
    }

    TabRow(
        indicator = indicator,
        selectedTabIndex = selectedIndex,
        modifier = modifier
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                modifier = Modifier.heightIn(min = 50.dp),
                selected = selectedIndex == index,
                onClick = {
                    onCategorySelected.invoke(category)
                }) {
                Text(
                    text = when (category) {
                        HomeCategory.Library -> stringResource(R.string.home_library)
                        HomeCategory.Discover -> stringResource(R.string.home_discover)
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun HomeCategoryTabIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
) {
    Spacer(
        modifier
            .padding(horizontal = 24.dp)
            .height(4.dp)
            .background(color, RoundedCornerShape(topStartPercent = 100, topEndPercent = 100))
    )
}
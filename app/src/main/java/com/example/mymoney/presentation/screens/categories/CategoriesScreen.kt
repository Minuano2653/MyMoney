package com.example.mymoney.presentation.screens.categories


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mymoney.R
import com.example.mymoney.presentation.components.CustomTopAppBar
import com.example.mymoney.presentation.components.Divider
import com.example.mymoney.presentation.components.EmojiIcon
import com.example.mymoney.presentation.components.ListItemComponent
import com.example.mymoney.presentation.components.SearchField
import com.example.mymoney.presentation.theme.MyMoneyTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    onNavigateToCategoriesDetail: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        contentWindowInsets = WindowInsets(bottom = 0.dp),
        topBar = {
            CustomTopAppBar(titleRes = R.string.top_bar_title_categories)
        }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        CategoriesScreenContent(
            uiState = uiState,
            onEvent = viewModel::handleEvent,
            modifier = modifier.padding(paddingValues)
        )
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is CategoriesSideEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is CategoriesSideEffect.NavigateToCategoryDetails -> {
                    /*onNavigateToCategoriesDetail()*/
                }
            }
        }
    }
}

@Composable
fun CategoriesScreenContent(
    modifier: Modifier = Modifier,
    uiState: CategoriesUiState,
    onEvent: (CategoriesEvent) -> Unit,
) {
    if (uiState.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            SearchField(
                query = uiState.searchQuery,
                onQueryChange = { searchQuery -> onEvent(CategoriesEvent.OnSearchQueryChanged(searchQuery)) },
                placeholderRes = R.string.search_field_placeholder
            )
            Divider()
            LazyColumn {
                itemsIndexed(uiState.categories) { _, category ->
                    ListItemComponent(
                        title = category.name,
                        leadingIcon = {
                            EmojiIcon(emoji = category.emoji)
                        },
                        onClick = {onEvent(CategoriesEvent.OnCategoryClicked)}
                    )
                    Divider()
                }
            }
        }
    }
}

@Preview
@Composable
fun CategoriesScreenPreview() {
    MyMoneyTheme {
        CategoriesScreenContent(
            uiState = CategoriesUiState(),
            onEvent = {}
        )
    }
}

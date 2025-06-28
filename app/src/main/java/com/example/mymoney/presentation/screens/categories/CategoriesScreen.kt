package com.example.mymoney.presentation.screens.categories


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mymoney.R
import com.example.mymoney.presentation.components.Divider
import com.example.mymoney.presentation.components.EmojiIcon
import com.example.mymoney.presentation.components.ListItemComponent
import com.example.mymoney.presentation.components.SearchField
import com.example.mymoney.presentation.components.model.FabState
import com.example.mymoney.presentation.components.model.TopAppBarState
import com.example.mymoney.presentation.theme.MyMoneyTheme
import kotlinx.coroutines.flow.collectLatest

@Preview
@Composable
fun CategoriesScreenPreview() {
    MyMoneyTheme {
        CategoriesScreen(
            onUpdateTopAppBar = {},
            onUpdateFabState = {},
            onShowSnackbar = {}
        )
    }
}

/**
 * Контейнерный компонент экрана статей.
 *
 * Обрабатывает состояния, события и сайд-эффекты из ViewModel.
 *
 * @param onUpdateTopAppBar Функция для обновления состояния верхней панели.
 * @param onUpdateFabState Функция для обновления состояния FAB.
 * @param onShowSnackbar Функция для отображения snackbar с сообщением.
 * @param viewModel ViewModel экрана категорий (по умолчанию внедряется через Hilt).
 */
@Composable
fun CategoriesScreen(
    onUpdateTopAppBar: (TopAppBarState) -> Unit,
    onUpdateFabState: (FabState) -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        onUpdateTopAppBar(
            TopAppBarState(
                titleRes = R.string.top_bar_title_categories
            )
        )
        onUpdateFabState(
            FabState(
                isVisible = false
            )
        )
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is CategoriesSideEffect.ShowError -> {
                    onShowSnackbar(effect.message)
                }
                is CategoriesSideEffect.NavigateToCategoryDetails -> {
                    //TODO: пока не понятно куда навигация :)
                }
            }
        }
    }

    CategoriesScreenContent(
        uiState = uiState,
        onEvent = viewModel::handleEvent
    )
}

/**
 * UI-содержимое экрана категорий.
 *
 * Отображает список категорий с поисковым полем.
 *
 * @param modifier Модификатор для настройки внешнего вида.
 * @param uiState Текущее состояние UI, предоставляемое ViewModel.
 * @param onEvent Обработчик пользовательских событий.
 */
@Composable
fun CategoriesScreenContent(
    modifier: Modifier = Modifier,
    uiState: CategoriesUiState,
    onEvent: (CategoriesEvent) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            SearchField(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                placeholderRes = R.string.search_field_placeholder
            )
            Divider()
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
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

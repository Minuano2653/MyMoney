package com.example.mymoney.presentation.screens.categories


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mymoney.presentation.components.EmojiIcon
import com.example.mymoney.presentation.components.ListItemComponent
import com.example.mymoney.R
import com.example.mymoney.presentation.components.TrailingIcon
import com.example.mymoney.domain.entity.Category
import com.example.mymoney.presentation.navigation.FabState
import com.example.mymoney.presentation.navigation.TopAppBarState
import com.example.mymoney.ui.theme.MyMoneyTheme

@Preview
@Composable
fun CategoriesScreenPreview() {
    MyMoneyTheme {
        CategoriesScreen(
            onUpdateTopAppBar = {},
            onUpdateFabState = {}
        )
    }
}

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    onUpdateTopAppBar: (TopAppBarState) -> Unit,
    onUpdateFabState: (FabState) -> Unit

) {
    LaunchedEffect(Unit) {
        onUpdateTopAppBar(
            TopAppBarState(
                title = "Статьи",
            )
        )
        onUpdateFabState(
            FabState(
                isVisible = false,
                onClick = null
            )
        )
    }
    var searchQuery by remember { mutableStateOf("") }
    val uiState = getMockCategories()

    Column(modifier = modifier.fillMaxSize()) {
        SearchField(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            itemsIndexed(uiState.categories) { _, category ->
                ListItemComponent(
                    title = category.name,
                    leadingIcon = {
                        EmojiIcon(emoji = category.emoji)
                    }
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}

@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Найти статью"
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        trailingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = "Поиск"
            )
        },
        shape = RectangleShape,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        singleLine = true
    )
}

private fun getMockCategories(): CategoriesUiState = CategoriesUiState(
    listOf (
        Category(
            id = 1,
            name = "Аренда квартиры",
            emoji = "\uD83C\uDFE1",
            isIncome = false
        ),
        Category(
            id = 2,
            name = "Одежда",
            emoji = "\uD83D\uDC57",
            isIncome = false,
        ),
        Category(
            id = 3,
            name = "На собачку",
            isIncome = false,
            emoji = "\uD83D\uDC36",
        ),
        Category(
            id = 4,
            name = "Ремонт квартиры",
            isIncome = false,
            emoji = "РК",
        ),
        Category(
            id = 5,
            name = "Продукты",
            isIncome = false,
            emoji = "\uD83C\uDF6D",
        ),
        Category(
            id = 6,
            name = "Спортзал",
            isIncome = false,
            emoji = "\uD83C\uDFCB\uFE0F",
        ),
        Category(
            id = 7,
            name = "Медицина",
            isIncome = false,
            emoji = "\uD83D\uDC8A",
        )

    )
)



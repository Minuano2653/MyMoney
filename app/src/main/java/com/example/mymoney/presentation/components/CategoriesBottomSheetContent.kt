package com.example.mymoney.presentation.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import com.example.core.domain.entity.Category
import com.example.core.ui.components.Divider
import com.example.core.ui.components.EmojiIcon
import com.example.core.ui.components.ListItemComponent

@Composable
fun CategoriesBottomSheetContent(
    onCategoryClicked: (Category) -> Unit,
    categories: List<Category>
) {
    LazyColumn {
        itemsIndexed(
            items = categories,
            key = { _, category -> category.id }
        ) { _, category ->
            ListItemComponent(
                title = category.name,
                leadingIcon = {
                    EmojiIcon(emoji = category.emoji)
                },
                onClick = { onCategoryClicked(category) }
            )
            Divider()
        }
    }
}
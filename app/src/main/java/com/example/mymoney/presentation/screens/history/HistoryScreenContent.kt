package com.example.mymoney.presentation.screens.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mymoney.presentation.components.Divider
import com.example.mymoney.presentation.components.EmojiIcon
import com.example.mymoney.presentation.components.ListItemComponent
import com.example.mymoney.presentation.components.TrailingIcon
import com.example.mymoney.utils.DateUtils
import com.example.mymoney.utils.formatAmount
import com.example.mymoney.R

/**
 * Контент экрана истории транзакций.
 *
 * Отображает:
 * - Период фильтрации с выбором даты начала и конца.
 * - Итоговую сумму за выбранный период.
 * - Список транзакций с информацией о категории, комментарии, сумме и дате.
 * - Индикатор загрузки при загрузке данных.
 *
 * @param modifier модификатор для настройки внешнего вида компонента.
 * @param uiState текущее состояние UI с данными транзакций и параметрами фильтра.
 * @param onEvent лямбда для обработки событий UI, таких как выбор даты.
 */
@Composable
fun HistoryScreenContent(
    modifier: Modifier = Modifier,
    uiState: HistoryUiState,
    onEvent: (HistoryEvent) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        ListItemComponent(
            title = stringResource(R.string.list_item_text_start),
            trailingText = uiState.startDate,
            itemHeight = 56.dp,
            backgroundColor = MaterialTheme.colorScheme.secondary,
            onClick = { onEvent(HistoryEvent.OnStartDateClicked) }
        )
        Divider()
        ListItemComponent(
            title = stringResource(R.string.list_item_text_end),
            trailingText = uiState.endDate,
            itemHeight = 56.dp,
            backgroundColor = MaterialTheme.colorScheme.secondary,
            onClick = { onEvent(HistoryEvent.OnEndDateClicked) }
        )
        Divider()
        ListItemComponent(
            title = stringResource(R.string.list_item_text_sum),
            trailingText = uiState.total.formatAmount(),
            itemHeight = 56.dp,
            backgroundColor = MaterialTheme.colorScheme.secondary,
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(uiState.transactions) { _, transaction ->
                    ListItemComponent(
                        title = transaction.category.name,
                        subtitle = transaction.comment,
                        trailingText = transaction.amount.formatAmount(),
                        trailingSubText = DateUtils.formatIsoToDayMonth(transaction.transactionDate),
                        leadingIcon = {
                            EmojiIcon(emoji = transaction.category.emoji)
                        },
                        trailingIcon = {
                            TrailingIcon()
                        }
                    )
                    Divider()
                }
            }
        }
    }
}

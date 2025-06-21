package com.example.mymoney.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mymoney.ui.theme.MyMoneyTheme

@Composable
fun ListItemComponent(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    trailingText: String? = null,
    trailingSubText: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    itemHeight: Dp = 68.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    subtitleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: (() -> Unit)? = null
) {
    val clickableModifier = if (onClick != null) {
        Modifier.clickable { onClick() }
    } else {
        Modifier
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .height(itemHeight)
            .background(backgroundColor)
            .then(clickableModifier)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            leadingIcon?.invoke()

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = contentColor,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                subtitle?.let {
                    Text(
                        text = it,
                        color = subtitleColor,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(horizontalAlignment = Alignment.End) {
                trailingText?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        color = contentColor
                    )
                }
                trailingSubText?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        color = contentColor
                    )
                }
            }

            trailingIcon?.invoke()
        }
    }
}

@Preview
@Composable
fun ListItemExamples() {
    MyMoneyTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            // Полный вариант
            ListItemComponent(
                title = "На собачку",
                subtitle = "Джек",
                trailingText = "1500₽",
                trailingSubText = "25 февраля",
                leadingIcon = { EmojiIcon("\uD83D\uDC36") },
                trailingIcon = { TrailingIcon() }
            )

            // Без иконки слева
            ListItemComponent(
                title = "Покупка продуктов",
                subtitle = "Магазин у дома",
                trailingText = "2 500 ₽",
                trailingIcon = { TrailingIcon() }
            )

            // Без subtitle
            ListItemComponent(
                title = "Зарплата",
                trailingText = "50 000 ₽",
                leadingIcon = { EmojiIcon("💰") },
                trailingIcon = { TrailingIcon() }
            )

            // Без иконки справа
            ListItemComponent(
                title = "Перевод другу",
                subtitle = "Александр И.",
                trailingText = "1 000 ₽",
                leadingIcon = { EmojiIcon("👤") }
            )

            // Минимальный вариант
            ListItemComponent(
                title = "Покупочка",
                backgroundColor = MaterialTheme.colorScheme.secondary,
                itemHeight = 56.dp,
                trailingText = "100 000 ₽"
            )

            // С кликом
            ListItemComponent(
                title = "Кликабельный элемент",
                subtitle = "Нажми меня",
                leadingIcon = { EmojiIcon("👆") },
                onClick = { }
            )
        }
    }
}
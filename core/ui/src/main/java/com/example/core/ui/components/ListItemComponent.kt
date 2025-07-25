package com.example.core.ui.components

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Компонент списка с заголовком, необязательным подзаголовком, иконками и дополнительным текстом.
 *
 * @param title Основной заголовок элемента списка.
 * @param modifier Модификатор для внешнего оформления компонента.
 * @param subtitle Необязательный подзаголовок.
 * @param trailingText Текст, отображаемый справа сверху.
 * @param trailingSubText Текст, отображаемый справа снизу.
 * @param leadingIcon composable лямбда для иконки слева (необязательный).
 * @param trailingIcon composable лямбда для иконки справа (необязательный).
 * @param itemHeight Высота элемента списка (по умолчанию 68.dp).
 * @param backgroundColor Цвет фона элемента (по умолчанию [androidx.compose.material3.MaterialTheme.colorScheme.surface]).
 * @param contentColor Цвет основного текста (по умолчанию [androidx.compose.material3.MaterialTheme.colorScheme.onSurface]).
 * @param subtitleColor Цвет подзаголовка (по умолчанию [androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant]).
 * @param onClick Обработчик нажатия на элемент (необязательный).
 */
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
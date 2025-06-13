package com.example.mymoney

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mymoney.presentation.screens.MainScreen
import com.example.mymoney.ui.theme.MyMoneyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyMoneyTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun ListItemComponent(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    trailText: String? = null,
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
            trailText?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    color = contentColor
                )
            }
            trailingIcon?.invoke()
        }
    }
}

@Composable
fun EmojiIcon(
    emoji: String,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
    size: Dp = 24.dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(backgroundColor, CircleShape)
            .size(size)
    ) {
        Text(text = emoji)
    }
}

@Composable
fun TrailingIcon(iconId: Int = R.drawable.ic_more_vert) {
    Image(
        painter = painterResource(id = iconId),
        contentDescription = null
    )
}


// Примеры использования:
@Preview
@Composable
fun ListItemExamples() {
    MyMoneyTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            // Полный вариант (как оригинал)
            ListItemComponent(
                title = "На собачку",
                subtitle = "Джек",
                trailText = "1500₽",
                leadingIcon = { EmojiIcon("\uD83D\uDC36") },
                trailingIcon = { TrailingIcon() }
            )

            // Без иконки слева
            ListItemComponent(
                title = "Покупка продуктов",
                subtitle = "Магазин у дома",
                trailText = "2 500 ₽",
                trailingIcon = { TrailingIcon() }
            )

            // Без subtitle
            ListItemComponent(
                title = "Зарплата",
                trailText = "50 000 ₽",
                leadingIcon = { EmojiIcon("💰") },
                trailingIcon = { TrailingIcon() }
            )

            // Без иконки справа
            ListItemComponent(
                title = "Перевод другу",
                subtitle = "Александр И.",
                trailText = "1 000 ₽",
                leadingIcon = { EmojiIcon("👤") }
            )

            // Минимальный вариант
            ListItemComponent(
                title = "Покупочка",
                backgroundColor = MaterialTheme.colorScheme.secondary,
                itemHeight = 56.dp,
                trailText = "100 000 ₽"
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

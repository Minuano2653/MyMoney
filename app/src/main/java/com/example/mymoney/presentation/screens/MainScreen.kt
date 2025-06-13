package com.example.mymoney.presentation.screens

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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mymoney.R
import com.example.mymoney.presentation.navigation.BottomNavGraph
import com.example.mymoney.presentation.navigation.BottomNavItem
import com.example.mymoney.presentation.navigation.Screen
import com.example.mymoney.ui.theme.MyMoneyTheme

@Preview
@Composable
fun MainScreenPreview() {
    MyMoneyTheme {
        MainScreen()
    }
}

@Composable
fun MainScreen() {
    val navHostController = rememberNavController()

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentScreen = when (currentRoute) {
        Screen.Expenses.route -> Screen.Expenses
        Screen.Incomes.route -> Screen.Incomes
        Screen.Account.route -> Screen.Account
        Screen.Categories.route -> Screen.Categories
        Screen.Settings.route -> Screen.Settings
        else -> Screen.Expenses
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            CustomTopAppBar(title = currentScreen.title, trailingIconRes = currentScreen.trailingIconRes, onTrailingClick = {})
        },

        bottomBar = {
            val navigationList = listOf(
                BottomNavItem.Expenses,
                BottomNavItem.Incomes,
                BottomNavItem.Account,
                BottomNavItem.Categories,
                BottomNavItem.Settings
            )
            NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
                navigationList.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.screen.route,
                        onClick = {
                            navHostController.navigate(item.screen.route) {
                                popUpTo(navHostController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(item.iconRes),
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (currentRoute == item.screen.route) FontWeight.W600 else FontWeight.Medium
                                )
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedTextColor = MaterialTheme.colorScheme.onSurface,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                }
            }
        },

        floatingActionButton = {
            if (currentScreen.hasFloatingActionButton) {
                FloatingActionButton(
                    onClick = {},
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = "Add"
                    )
                }
            }
        }
    ) { paddingValues ->
        BottomNavGraph(
            navHostController = navHostController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    leadingIconRes: Int? = null,
    trailingIconRes: Int? = null,
    onLeadingClick: (() -> Unit)? = null,
    onTrailingClick: (() -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (leadingIconRes != null && onLeadingClick != null) {
                IconButton(onClick = onLeadingClick) {
                    Icon(
                        painter = painterResource(leadingIconRes),
                        contentDescription = null
                    )
                }
            }
        },
        actions = {
            if (trailingIconRes != null && onTrailingClick != null) {
                IconButton(onClick = onTrailingClick) {
                    Icon(
                        painter = painterResource(trailingIconRes),
                        contentDescription = null
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun ListItemComponent(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    trailingText: String? = null,
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
            trailingText?.let {
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

@Composable
fun MyTopAppBarPreview() {
    MyMoneyTheme {
        CustomTopAppBar(
            title = "Расходы сегодня",
            trailingIconRes = R.drawable.ic_history,
            onTrailingClick = {}
        )
    }
}

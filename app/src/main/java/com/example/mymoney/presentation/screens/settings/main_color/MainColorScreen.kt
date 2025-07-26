package com.example.mymoney.presentation.screens.settings.main_color

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.ui.components.CustomTopAppBar
import com.example.core.ui.components.Divider
import com.example.mymoney.R
import com.example.mymoney.presentation.daggerViewModel
import com.example.mymoney.presentation.screens.settings.theme.ThemeEvent
import com.example.mymoney.presentation.screens.settings.theme.ThemeSideEffect
import com.example.mymoney.presentation.screens.settings.theme.ThemeUiState
import com.example.mymoney.presentation.screens.settings.theme.ThemeViewModel
import com.example.mymoney.presentation.theme.ColorTheme
import com.example.mymoney.presentation.theme.blueDarkBackground
import com.example.mymoney.presentation.theme.blueDarkPrimary
import com.example.mymoney.presentation.theme.blueLightBackground
import com.example.mymoney.presentation.theme.blueLightPrimary
import com.example.mymoney.presentation.theme.greenDarkPrimary
import com.example.mymoney.presentation.theme.greenLightPrimary
import com.example.mymoney.presentation.theme.orangeDarkBackground
import com.example.mymoney.presentation.theme.orangeDarkPrimary
import com.example.mymoney.presentation.theme.orangeLightBackground
import com.example.mymoney.presentation.theme.orangeLightPrimary
import com.example.mymoney.presentation.theme.purpleDarkBackground
import com.example.mymoney.presentation.theme.purpleDarkPrimary
import com.example.mymoney.presentation.theme.purpleLightBackground
import com.example.mymoney.presentation.theme.purpleLightPrimary
import kotlinx.coroutines.flow.collectLatest


@Composable
fun MainColorScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: ThemeViewModel = daggerViewModel()
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        contentWindowInsets = WindowInsets(bottom = 0.dp),
        topBar = {
            CustomTopAppBar(
                titleRes = R.string.settings_item_primary_color,
                leadingIconRes = R.drawable.ic_arrow_back,
                onLeadingClick = onNavigateBack
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        ColorThemeScreenContent(
            uiState = uiState,
            onColorThemeSelected = { colorTheme ->
                viewModel.handleEvent(ThemeEvent.ChangeColorTheme(colorTheme))
            },
            modifier = modifier.padding(paddingValues)
        )
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is ThemeSideEffect.ShowError -> {
                    snackbarHostState.showSnackbar(context.getString(effect.message))
                }
            }
        }
    }
}

@Composable
fun ColorThemeScreenContent(
    uiState: ThemeUiState,
    onColorThemeSelected: (ColorTheme) -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            ColorTheme.entries.forEach { colorTheme ->
                ColorThemeListItem(
                    colorTheme = colorTheme,
                    isSelected = uiState.currentTheme.colorTheme == colorTheme,
                    isDarkMode = uiState.currentTheme.isDarkMode,
                    onSelected = { onColorThemeSelected(colorTheme) }
                )
                Divider()
            }
        }
    }
}

// 7. Элемент списка с цветовой темой
@Composable
private fun ColorThemeListItem(
    colorTheme: ColorTheme,
    isSelected: Boolean,
    isDarkMode: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        modifier = modifier
            .height(72.dp)
            .clickable { onSelected() },
        headlineContent = {
            Text(
                text = stringResource(colorTheme.displayNameRes),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        leadingContent = {
            ColorThemePreview(
                colorTheme = colorTheme,
                isDarkMode = isDarkMode
            )
        },
        trailingContent = {
            RadioButton(
                selected = isSelected,
                onClick = { onSelected() }
            )
        }
    )
}

@Composable
private fun ColorThemePreview(
    colorTheme: ColorTheme,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val primaryColor = getThemePreviewColor(colorTheme, isDarkMode)
    val backgroundColor = if (isDarkMode) {
        getThemePreviewBackgroundColor(colorTheme, true)
    } else {
        getThemePreviewBackgroundColor(colorTheme, false)
    }

    Box(
        modifier = modifier
            .size(40.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(
                    color = primaryColor,
                    shape = CircleShape
                )
        )
    }
}

private fun getThemePreviewColor(colorTheme: ColorTheme, isDark: Boolean): Color {
    return when (colorTheme) {
        ColorTheme.DEFAULT -> if (isDark) greenDarkPrimary else greenLightPrimary
        ColorTheme.BLUE -> if (isDark) blueDarkPrimary else blueLightPrimary
        ColorTheme.PURPLE -> if (isDark) purpleDarkPrimary else purpleLightPrimary
        ColorTheme.ORANGE -> if (isDark) orangeDarkPrimary else orangeLightPrimary
    }
}

private fun getThemePreviewBackgroundColor(colorTheme: ColorTheme, isDark: Boolean): Color {
    return when (colorTheme) {
        ColorTheme.DEFAULT -> if (isDark) Color(0xFF121212) else Color.White
        ColorTheme.BLUE -> if (isDark) blueDarkBackground else blueLightBackground
        ColorTheme.PURPLE -> if (isDark) purpleDarkBackground else purpleLightBackground
        ColorTheme.ORANGE -> if (isDark) orangeDarkBackground else orangeLightBackground
    }
}

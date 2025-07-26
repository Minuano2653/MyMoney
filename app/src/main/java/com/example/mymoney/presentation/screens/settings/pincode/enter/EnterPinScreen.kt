package com.example.mymoney.presentation.screens.settings.pincode.enter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mymoney.R
import com.example.mymoney.presentation.daggerViewModel
import com.example.mymoney.presentation.screens.settings.pincode.create.NumericKeypad
import com.example.mymoney.presentation.screens.settings.pincode.create.PinDotsIndicator
import com.example.mymoney.presentation.screens.settings.pincode.create.PinScreenContent


@Composable
fun EnterPinScreen(
    onPinVerified: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EnterPinViewModel = daggerViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                EnterPinSideEffect.PinVerified -> onPinVerified()
                is EnterPinSideEffect.ShowError -> {
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        EnterPinContent(
            uiState = state,
            onDigitClick = { viewModel.handleEvent(EnterPinEvent.PinDigitAdded(it)) },
            onBackspaceClick = { viewModel.handleEvent(EnterPinEvent.PinDigitRemoved) },
            onClearClick = { viewModel.handleEvent(EnterPinEvent.PinCleared) },
            modifier = modifier
        )
    }
}

@Composable
fun EnterPinContent(
    uiState: EnterPinUiState,
    onDigitClick: (String) -> Unit,
    onBackspaceClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                text = stringResource(R.string.enter_password_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            PinDotsIndicator(
                pinLength = uiState.currentPin.length,
                maxLength = 4
            )

            Spacer(modifier = Modifier.height(16.dp))

            NumericKeypad(
                onDigitClick = onDigitClick,
                onBackspaceClick = onBackspaceClick,
                onClearClick = onClearClick
            )
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
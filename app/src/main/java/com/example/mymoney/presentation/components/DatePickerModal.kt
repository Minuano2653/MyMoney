package com.example.mymoney.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mymoney.R

/**
 * Диалоговое окно выбора даты с кнопками подтверждения, отмены и очистки выбора.
 *
 * @param initialSelectedDateMillis Начальное выбранное значение даты в миллисекундах (или null).
 * @param onDateSelected Колбэк, вызываемый при подтверждении выбора даты, передаёт выбранное время в миллисекундах или null.
 * @param onDismiss Колбэк для закрытия диалогового окна.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    initialSelectedDateMillis: Long?,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDateMillis
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis = null
                }) {
                    Text(
                        text = stringResource(R.string.date_picker_clear_button),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = stringResource(R.string.date_picker_dismiss_button),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    TextButton(onClick = {
                        onDateSelected(datePickerState.selectedDateMillis)
                        onDismiss()
                    }) {
                        Text(
                            text = stringResource(R.string.date_picker_ok_button),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.secondary,
                subheadContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                navigationContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                weekdayContentColor = MaterialTheme.colorScheme.onSurface,
                dayContentColor = MaterialTheme.colorScheme.onSurface,
                selectedDayContentColor = MaterialTheme.colorScheme.onSurface
            ),
            title = null,
            headline = null,
            showModeToggle = false
        )
    }
}

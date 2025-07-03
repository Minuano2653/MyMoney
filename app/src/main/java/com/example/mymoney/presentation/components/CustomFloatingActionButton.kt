package com.example.mymoney.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.mymoney.R

@Composable
fun CustomFloatingActionButton(
    onClick: () -> Unit,
    shape: Shape = CircleShape,
    @DrawableRes iconRes: Int = R.drawable.ic_add,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    FloatingActionButton(
        onClick = onClick,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = stringResource(R.string.fab_description)
        )
    }
}

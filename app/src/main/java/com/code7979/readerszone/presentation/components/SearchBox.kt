package com.code7979.readerszone.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchBox(
    value: String,
    onValueChange: (String) -> Unit,
    backgroundColor: Color,
    leadingIcon: @Composable () -> Unit,
    roundedCorner: Dp = 5.dp,
    onFocusChanged: (FocusState) -> Unit,
    isSingleLine: Boolean = true
) {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp),
        shape = RoundedCornerShape(
            topStart = roundedCorner,
            topEnd = roundedCorner,
            bottomStart = roundedCorner,
            bottomEnd = roundedCorner
        ),
        elevation = CardDefaults.cardElevation()
    ) {
        BasicTextField(
            modifier = Modifier.onFocusChanged { onFocusChanged(it) },
            value = value,
            onValueChange = onValueChange,
            singleLine = isSingleLine,
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.background(backgroundColor)
                ) {
                    leadingIcon()
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value == TextFieldValue("").text) {
                            Text("Search Books", fontSize = 12.sp)
                        }
                        innerTextField()
                    }
                }
            },
        )
    }
}

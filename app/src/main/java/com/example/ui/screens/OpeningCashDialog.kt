package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.FinanceCalculationService
import com.example.domain.VendigoStrings
import com.example.ui.theme.RendeskBorderSoftGray
import com.example.ui.theme.RendeskButtonBlack
import com.example.ui.theme.RendeskCardWhite
import com.example.ui.theme.RendeskPastelPink
import com.example.ui.theme.RendeskPrimaryText
import com.example.ui.theme.RendeskSecondaryText

@Composable
fun OpeningCashDialog(
    currentOpeningPaise: Long,
    isTamil: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    var textValue by remember {
        mutableStateOf(if (currentOpeningPaise > 0) (currentOpeningPaise / 100.0).toString().replace(".0", "") else "")
    }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = VendigoStrings.openingCash(isTamil),
                fontWeight = FontWeight.Black,
                fontSize = 19.sp,
                color = RendeskPrimaryText
            )
        },
        text = {
            Column {
                Text(
                    text = if (isTamil)
                        "இன்று காலை கடையைத் திறக்கும்போது பணப்பெட்டியில் இருக்கும் சில்லறை / ரொக்கப் பணத்தை உள்ளிடவும். இது பழைய பணம், இன்றைய விற்பனை அல்ல."
                    else
                        "Enter the starting cash drawer balance for today. Opening cash represents existing float money, not today's revenue or profit.",
                    fontSize = 13.sp,
                    color = RendeskSecondaryText
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = textValue,
                    onValueChange = {
                        textValue = it
                        error = null
                    },
                    label = { Text(VendigoStrings.enterAmount(isTamil)) },
                    prefix = { Text("₹ ", fontWeight = FontWeight.Bold, color = RendeskPrimaryText) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    isError = error != null,
                    supportingText = {
                        if (error != null) Text(error!!, color = MaterialTheme.colorScheme.error)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RendeskPastelPink,
                        unfocusedBorderColor = RendeskBorderSoftGray,
                        focusedContainerColor = RendeskCardWhite,
                        unfocusedContainerColor = RendeskCardWhite
                    ),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_opening_cash")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val clean = textValue.trim()
                    val paise = if (clean.isEmpty()) 0L else FinanceCalculationService.parseRupeeToPaise(clean)
                    if (paise == null && clean.isNotEmpty()) {
                        error = if (isTamil) "சரியான தொகையை உள்ளிடவும்" else "Invalid amount"
                    } else {
                        onConfirm(paise ?: 0L)
                    }
                },
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RendeskButtonBlack),
                modifier = Modifier.testTag("btn_save_opening_cash")
            ) {
                Text(VendigoStrings.saveTransaction(isTamil), color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(22.dp),
                border = BorderStroke(1.dp, RendeskBorderSoftGray)
            ) {
                Text(VendigoStrings.cancel(isTamil), color = RendeskPrimaryText, fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(26.dp),
        containerColor = RendeskCardWhite
    )
}

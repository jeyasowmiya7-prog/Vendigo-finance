package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.FinanceCalculationService
import com.example.domain.VendigoStrings
import com.example.ui.theme.FinanceLossBg
import com.example.ui.theme.FinanceProfitBg
import com.example.ui.theme.RendeskBorderSoftGray
import com.example.ui.theme.RendeskButtonBlack
import com.example.ui.theme.RendeskCardWhite
import com.example.ui.theme.RendeskLavender
import com.example.ui.theme.RendeskLightPurpleTint
import com.example.ui.theme.RendeskPastelPink
import com.example.ui.theme.RendeskPrimaryText
import com.example.ui.theme.RendeskSecondaryText
import com.example.ui.theme.RendeskSoftCoral

@Composable
fun ReconciliationDialog(
    expectedClosingPaise: Long,
    previouslyCountedPaise: Long?,
    isTamil: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (countedPaise: Long, notes: String) -> Unit
) {
    var textValue by remember {
        mutableStateOf(
            if (previouslyCountedPaise != null) {
                (previouslyCountedPaise / 100.0).toString().replace(".0", "")
            } else ""
        )
    }

    var notesText by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val countedPaise = FinanceCalculationService.parseRupeeToPaise(textValue)
    val discrepancy = countedPaise?.let { it - expectedClosingPaise }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = VendigoStrings.reconcileCash(isTamil),
                fontWeight = FontWeight.Black,
                fontSize = 19.sp,
                color = RendeskPrimaryText
            )
        },
        text = {
            Column {
                Text(
                    text = if (isTamil)
                        "இன்று மாலை வியாபார முடிவில் உங்கள் பணப்பையில் உள்ள உண்மையான ரொக்கத்தை எண்ணி உள்ளிடவும்."
                    else
                        "Count the actual physical cash notes & coins in your drawer at the end of the day and enter below.",
                    fontSize = 13.sp,
                    color = RendeskSecondaryText
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Expected closing box
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = FinanceProfitBg,
                    border = BorderStroke(1.dp, RendeskPastelPink.copy(alpha = 0.6f)),
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = if (isTamil) "கணக்கிடப்பட்ட கடைசி ரொக்கம் (கணக்குப்படி)" else "Expected Closing Cash (from records)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = RendeskSecondaryText
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = FinanceCalculationService.formatPaiseToRupee(expectedClosingPaise),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = RendeskPrimaryText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = textValue,
                    onValueChange = {
                        textValue = it
                        error = null
                    },
                    label = { Text(VendigoStrings.countedCash(isTamil)) },
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
                        .testTag("input_counted_cash")
                )

                // Live difference preview
                if (discrepancy != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    val discColor: Color = when {
                        discrepancy == 0L -> RendeskLavender
                        discrepancy > 0L -> RendeskSoftCoral
                        else -> Color(0xFFE11D48)
                    }
                    val discIcon: ImageVector = when {
                        discrepancy == 0L -> Icons.Default.CheckCircle
                        discrepancy > 0L -> Icons.Default.Info
                        else -> Icons.Default.Warning
                    }
                    val discText: String = when {
                        discrepancy == 0L -> VendigoStrings.cashMatched(isTamil)
                        discrepancy > 0L -> "${VendigoStrings.cashSurplus(isTamil)}: +${FinanceCalculationService.formatPaiseToRupee(discrepancy)}"
                        else -> "${VendigoStrings.cashShort(isTamil)}: ${FinanceCalculationService.formatPaiseToRupee(discrepancy)}"
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = discIcon, contentDescription = null, tint = discColor, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = discText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = discColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = notesText,
                    onValueChange = { notesText = it },
                    label = { Text(if (isTamil) "குறிப்புகள் (விருப்பத்தேர்வு)" else "Notes (optional)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RendeskPastelPink,
                        unfocusedBorderColor = RendeskBorderSoftGray,
                        focusedContainerColor = RendeskCardWhite,
                        unfocusedContainerColor = RendeskCardWhite
                    ),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val paise = FinanceCalculationService.parseRupeeToPaise(textValue)
                    if (paise == null) {
                        error = if (isTamil) "சரியான தொகையை உள்ளிடவும்" else "Please enter a valid counted cash amount"
                    } else {
                        onConfirm(paise, notesText.trim())
                    }
                },
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RendeskButtonBlack),
                modifier = Modifier.testTag("btn_confirm_reconcile")
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

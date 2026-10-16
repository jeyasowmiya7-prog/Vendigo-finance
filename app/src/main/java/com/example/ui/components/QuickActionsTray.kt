package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.VendigoStrings
import com.example.ui.theme.RendeskBorderSoftGray
import com.example.ui.theme.RendeskButtonBlack
import com.example.ui.theme.RendeskLavender
import com.example.ui.theme.RendeskPastelPink
import com.example.ui.theme.RendeskPrimaryText
import com.example.ui.theme.RendeskSoftCoral

@Composable
fun QuickActionsTray(
    isTamil: Boolean,
    onAddCashSale: () -> Unit,
    onAddUpiSale: () -> Unit,
    onAddExpense: () -> Unit,
    onViewDailyReport: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("quick_actions_tray")
    ) {
        Text(
            text = if (isTamil) "விரைவு நடவடிக்கைகள்" else "Quick Actions",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = RendeskPrimaryText
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Row 1: Add Cash Sale & Add UPI Sale (Rendesk Black Pill Buttons)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onAddCashSale,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("btn_add_cash_sale"),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RendeskButtonBlack),
                contentPadding = PaddingValues(horizontal = 8.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Payments,
                        contentDescription = null,
                        tint = RendeskPastelPink,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = VendigoStrings.addCashSale(isTamil),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Button(
                onClick = onAddUpiSale,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("btn_add_upi_sale"),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RendeskButtonBlack),
                contentPadding = PaddingValues(horizontal = 8.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = null,
                        tint = RendeskLavender,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = VendigoStrings.addUpiSale(isTamil),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Row 2: Add Expense & View Report (Secondary Buttons: White with subtle border)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onAddExpense,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("btn_add_expense"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                border = BorderStroke(1.dp, RendeskBorderSoftGray),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = null,
                        tint = RendeskSoftCoral,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = VendigoStrings.addExpense(isTamil),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = RendeskPrimaryText
                    )
                }
            }

            OutlinedButton(
                onClick = onViewDailyReport,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("btn_view_report"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                border = BorderStroke(1.dp, RendeskBorderSoftGray),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Assessment,
                        contentDescription = null,
                        tint = RendeskLavender,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = VendigoStrings.viewReport(isTamil),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = RendeskPrimaryText
                    )
                }
            }
        }
    }
}

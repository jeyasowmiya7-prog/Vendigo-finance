package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyFinancialSummary
import com.example.domain.FinanceCalculationService
import com.example.domain.VendigoStrings
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
fun CashDrawerCard(
    summary: DailyFinancialSummary,
    isTamil: Boolean,
    onEditOpeningCash: () -> Unit,
    onReconcileCash: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("cash_drawer_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = RendeskCardWhite),
        border = BorderStroke(1.dp, RendeskBorderSoftGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(RendeskPastelPink.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = RendeskSoftCoral,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = VendigoStrings.physicalCashDrawer(isTamil),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = RendeskPrimaryText
                        )
                        Text(
                            text = "Closing = Opening + Cash In - Cash Out",
                            fontSize = 11.sp,
                            color = RendeskSecondaryText
                        )
                    }
                }

                IconButton(
                    onClick = onEditOpeningCash,
                    modifier = Modifier.testTag("edit_opening_cash_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit opening cash",
                        tint = RendeskPrimaryText,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4-Stage Equation row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CashFlowMetric(
                    label = VendigoStrings.openingCash(isTamil),
                    amount = summary.openingCashPaise,
                    color = RendeskPrimaryText
                )
                Text("+", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = RendeskSecondaryText, modifier = Modifier.align(Alignment.CenterVertically))
                CashFlowMetric(
                    label = VendigoStrings.cashReceived(isTamil),
                    amount = summary.cashSalesPaise,
                    color = RendeskSoftCoral
                )
                Text("-", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = RendeskSecondaryText, modifier = Modifier.align(Alignment.CenterVertically))
                CashFlowMetric(
                    label = VendigoStrings.cashSpent(isTamil),
                    amount = summary.cashExpensesPaise,
                    color = RendeskSecondaryText
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Calculated Closing Cash in hand
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = FinanceProfitBg,
                border = BorderStroke(1.dp, RendeskPastelPink.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = VendigoStrings.closingCash(isTamil),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RendeskSoftCoral
                        )
                        Text(
                            text = FinanceCalculationService.formatPaiseToRupee(summary.expectedClosingCashPaise),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = RendeskPrimaryText
                        )
                    }

                    Button(
                        onClick = onReconcileCash,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RendeskButtonBlack),
                        modifier = Modifier.testTag("reconcile_cash_button")
                    ) {
                        Text(
                            text = if (summary.countedClosingCashPaise != null) VendigoStrings.cashDiscrepancy(isTamil) else VendigoStrings.reconcileCash(isTamil),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Discrepancy indicator if counted
            if (summary.countedClosingCashPaise != null) {
                Spacer(modifier = Modifier.height(10.dp))
                val discrepancy = summary.cashDiscrepancyPaise ?: 0L
                val (discColor, discIcon, discText) = when {
                    discrepancy == 0L -> Triple(RendeskLavender, Icons.Default.Check, VendigoStrings.cashMatched(isTamil))
                    discrepancy > 0L -> Triple(RendeskSoftCoral, Icons.Default.Info, "${VendigoStrings.cashSurplus(isTamil)}: +${FinanceCalculationService.formatPaiseToRupee(discrepancy)}")
                    else -> Triple(Color(0xFFE11D48), Icons.Default.Warning, "${VendigoStrings.cashShort(isTamil)}: ${FinanceCalculationService.formatPaiseToRupee(discrepancy)}")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = discIcon, contentDescription = null, tint = discColor, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$discText (${if (isTamil) "எண்ணியது" else "Counted"}: ${FinanceCalculationService.formatPaiseToRupee(summary.countedClosingCashPaise)})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = discColor
                    )
                }
            }
        }
    }
}

@Composable
fun UpiSummaryCard(
    summary: DailyFinancialSummary,
    isTamil: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("upi_summary_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = RendeskCardWhite),
        border = BorderStroke(1.dp, RendeskBorderSoftGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(RendeskLightPurpleTint, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = null,
                        tint = RendeskLavender,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = VendigoStrings.upiRecorded(isTamil),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = RendeskPrimaryText
                    )
                    Text(
                        text = "Net UPI = UPI Sales - UPI Expenses",
                        fontSize = 11.sp,
                        color = RendeskSecondaryText
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CashFlowMetric(
                    label = VendigoStrings.upiSales(isTamil),
                    amount = summary.upiSalesPaise,
                    color = RendeskLavender
                )
                Text("-", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = RendeskSecondaryText, modifier = Modifier.align(Alignment.CenterVertically))
                CashFlowMetric(
                    label = VendigoStrings.upiExpenses(isTamil),
                    amount = summary.upiExpensesPaise,
                    color = RendeskSoftCoral
                )
                Text("=", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = RendeskSecondaryText, modifier = Modifier.align(Alignment.CenterVertically))
                CashFlowMetric(
                    label = if (isTamil) "நிகர யூபிஐ" else "Net UPI",
                    amount = summary.netUpiRecordedPaise,
                    color = RendeskPrimaryText
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Disclaimer banner
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = RendeskLightPurpleTint.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, RendeskBorderSoftGray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "ℹ️ ${VendigoStrings.upiDisclaimer(isTamil)}",
                    fontSize = 11.sp,
                    color = RendeskSecondaryText,
                    lineHeight = 15.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
private fun CashFlowMetric(
    label: String,
    amount: Long,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = RendeskSecondaryText
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = FinanceCalculationService.formatPaiseToRupee(amount),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

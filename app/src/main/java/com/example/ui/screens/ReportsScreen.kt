package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.FinanceCalculationService
import com.example.domain.VendigoStrings
import com.example.ui.theme.FinanceBreakEvenBg
import com.example.ui.theme.FinanceLossBg
import com.example.ui.theme.FinanceProfitBg
import com.example.ui.theme.HeroCardGradient
import com.example.ui.theme.RendeskBorderSoftGray
import com.example.ui.theme.RendeskButtonBlack
import com.example.ui.theme.RendeskCardWhite
import com.example.ui.theme.RendeskLavender
import com.example.ui.theme.RendeskLightPurpleTint
import com.example.ui.theme.RendeskPastelPink
import com.example.ui.theme.RendeskPrimaryText
import com.example.ui.theme.RendeskSecondaryText
import com.example.ui.theme.RendeskSoftCoral
import com.example.ui.viewmodel.ReportPeriod
import com.example.ui.viewmodel.VendigoViewModel

@Composable
fun ReportsScreen(
    viewModel: VendigoViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isTamil by viewModel.isTamil.collectAsStateWithLifecycle()
    val businessName by viewModel.businessName.collectAsStateWithLifecycle()
    val reportPeriod by viewModel.reportPeriod.collectAsStateWithLifecycle()
    val reportSummary by viewModel.reportSummary.collectAsStateWithLifecycle()
    val allTransactions by viewModel.allTransactions.collectAsStateWithLifecycle()

    // Calculate transactions in selected period
    val today = FinanceCalculationService.currentEpochDay()
    val periodTransactions = when (reportPeriod) {
        ReportPeriod.TODAY -> allTransactions.filter { it.dateEpochDay == today }
        ReportPeriod.THIS_WEEK -> allTransactions.filter { it.dateEpochDay >= (today - 6) && it.dateEpochDay <= today }
        ReportPeriod.THIS_MONTH -> allTransactions.filter { it.dateEpochDay >= (today - 29) && it.dateEpochDay <= today }
    }

    val categoryBreakdown = FinanceCalculationService.calculateCategoryBreakdown(periodTransactions)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("reports_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Text(
                text = VendigoStrings.tabReports(isTamil),
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = RendeskPrimaryText,
                letterSpacing = (-0.5).sp
            )
        }

        // Period Toggle (Today, This Week, This Month) - Rendesk Style Pills
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ReportPeriod.entries.forEach { period ->
                    val label = when (period) {
                        ReportPeriod.TODAY -> VendigoStrings.filterToday(isTamil)
                        ReportPeriod.THIS_WEEK -> VendigoStrings.filterThisWeek(isTamil)
                        ReportPeriod.THIS_MONTH -> VendigoStrings.filterThisMonth(isTamil)
                    }
                    FilterChip(
                        selected = reportPeriod == period,
                        onClick = { viewModel.reportPeriod.value = period },
                        label = { Text(label, fontWeight = if (reportPeriod == period) FontWeight.Bold else FontWeight.Medium) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RendeskButtonBlack,
                            selectedLabelColor = Color.White,
                            containerColor = RendeskCardWhite,
                            labelColor = RendeskPrimaryText
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = reportPeriod == period,
                            borderColor = if (reportPeriod == period) RendeskButtonBlack else RendeskBorderSoftGray
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.weight(1f).testTag("report_chip_${period.name}")
                    )
                }
            }
        }

        // Net Profit / Loss Highlight Card - Signature Gradient Look
        item {
            val isProfit = reportSummary.isProfit
            val isLoss = reportSummary.isLoss
            val bannerBg = when {
                isProfit -> FinanceProfitBg
                isLoss -> FinanceLossBg
                else -> FinanceBreakEvenBg
            }
            val bannerTextColor = when {
                isProfit -> RendeskSoftCoral
                isLoss -> Color(0xFFE11D48)
                else -> RendeskSecondaryText
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = RendeskCardWhite),
                border = BorderStroke(1.dp, RendeskPastelPink.copy(alpha = 0.6f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(HeroCardGradient)
                        .padding(20.dp)
                ) {
                    Text(
                        text = if (isProfit) VendigoStrings.todayProfit(isTamil) else if (isLoss) VendigoStrings.todayLoss(isTamil) else VendigoStrings.breakEven(isTamil),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RendeskSecondaryText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = FinanceCalculationService.formatPaiseToRupee(reportSummary.profitLossPaise),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        color = RendeskPrimaryText,
                        letterSpacing = (-0.5).sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = VendigoStrings.totalSales(isTamil), fontSize = 11.sp, fontWeight = FontWeight.Medium, color = RendeskSecondaryText)
                            Text(
                                text = FinanceCalculationService.formatPaiseToRupee(reportSummary.totalSalesPaise),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = RendeskPrimaryText
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = VendigoStrings.totalExpenses(isTamil), fontSize = 11.sp, fontWeight = FontWeight.Medium, color = RendeskSecondaryText)
                            Text(
                                text = FinanceCalculationService.formatPaiseToRupee(reportSummary.totalExpensesPaise),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = RendeskSoftCoral
                            )
                        }
                    }
                }
            }
        }

        // Cash vs UPI Sales Comparison (Pastel Pink vs Lavender)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = RendeskCardWhite),
                border = BorderStroke(1.dp, RendeskBorderSoftGray),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = if (isTamil) "விற்பனை முறை ஒப்பீடு (ரொக்கம் vs யூபிஐ)" else "Sales Split (Cash vs UPI)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = RendeskPrimaryText
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val totalSales = reportSummary.totalSalesPaise
                    val cashPct = if (totalSales > 0) (reportSummary.cashSalesPaise.toFloat() / totalSales) else 0.5f
                    val upiPct = if (totalSales > 0) (reportSummary.upiSalesPaise.toFloat() / totalSales) else 0.5f

                    // Two-color progress bar in pastel pink & lavender
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .clip(RoundedCornerShape(7.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(cashPct.coerceAtLeast(0.01f))
                                .background(RendeskPastelPink)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(upiPct.coerceAtLeast(0.01f))
                                .background(RendeskLavender)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(10.dp).background(RendeskPastelPink, CircleShape))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${VendigoStrings.filterCash(isTamil)}: ${FinanceCalculationService.formatPaiseToRupee(reportSummary.cashSalesPaise)} (${(cashPct * 100).toInt()}%)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RendeskPrimaryText
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(10.dp).background(RendeskLavender, CircleShape))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${VendigoStrings.filterUpi(isTamil)}: ${FinanceCalculationService.formatPaiseToRupee(reportSummary.upiSalesPaise)} (${(upiPct * 100).toInt()}%)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RendeskPrimaryText
                            )
                        }
                    }
                }
            }
        }

        // Expense Category Breakdown (Coral & Lavender Coordinated Colors)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = RendeskCardWhite),
                border = BorderStroke(1.dp, RendeskBorderSoftGray),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = VendigoStrings.expenseBreakdown(isTamil),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = RendeskPrimaryText
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (categoryBreakdown.isEmpty()) {
                        Text(
                            text = if (isTamil) "செலவு பதிவுகள் இல்லை" else "No expenses recorded for this period",
                            fontSize = 13.sp,
                            color = RendeskSecondaryText
                        )
                    } else {
                        categoryBreakdown.forEach { catSummary ->
                            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = VendigoStrings.categoryName(catSummary.category, isTamil),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = RendeskPrimaryText
                                    )
                                    Text(
                                        text = "${FinanceCalculationService.formatPaiseToRupee(catSummary.totalPaise)} (${catSummary.percentage.toInt()}%)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RendeskPrimaryText
                                    )
                                }
                                Spacer(modifier = Modifier.height(5.dp))
                                LinearProgressIndicator(
                                    progress = { catSummary.percentage / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = RendeskSoftCoral,
                                    trackColor = RendeskLightPurpleTint
                                )
                            }
                        }
                    }
                }
            }
        }

        // Share Daily Report Button - Rendesk Luxury Black Pill
        item {
            Button(
                onClick = {
                    val reportText = FinanceCalculationService.generateShareableReport(businessName, reportSummary, isTamil)
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, "Vendigo Finance Report")
                        putExtra(Intent.EXTRA_TEXT, reportText)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Report via"))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("btn_share_report"),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RendeskButtonBlack)
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = VendigoStrings.shareReport(isTamil),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

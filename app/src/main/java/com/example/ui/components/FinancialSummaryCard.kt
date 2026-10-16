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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyFinancialSummary
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
import com.example.ui.theme.SignaturePastelGradient

@Composable
fun FinancialSummaryCard(
    summary: DailyFinancialSummary,
    isTamil: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("financial_summary_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = RendeskCardWhite),
        border = BorderStroke(1.dp, RendeskBorderSoftGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // Question 3 Answer: Net Profit / Loss Banner - Signature Pastel Gradient Style
            val profitBgColor = when {
                summary.isProfit -> FinanceProfitBg
                summary.isLoss -> FinanceLossBg
                else -> FinanceBreakEvenBg
            }
            val profitTextColor = when {
                summary.isProfit -> RendeskSoftCoral
                summary.isLoss -> Color(0xFFE11D48)
                else -> RendeskSecondaryText
            }
            val profitIcon = when {
                summary.isProfit -> Icons.Default.TrendingUp
                summary.isLoss -> Icons.Default.TrendingDown
                else -> Icons.Default.CheckCircle
            }
            val profitTitle = when {
                summary.isProfit -> VendigoStrings.todayProfit(isTamil)
                summary.isLoss -> VendigoStrings.todayLoss(isTamil)
                else -> VendigoStrings.breakEven(isTamil)
            }

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.Transparent,
                border = BorderStroke(1.dp, RendeskPastelPink.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(HeroCardGradient)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .background(RendeskPastelPink.copy(alpha = 0.45f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = profitIcon,
                                contentDescription = null,
                                tint = RendeskPrimaryText,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = profitTitle,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RendeskSecondaryText
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (summary.isBreakEven) "₹0" else FinanceCalculationService.formatPaiseToRupee(summary.profitLossPaise),
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Black,
                                    color = RendeskPrimaryText,
                                    letterSpacing = (-0.5).sp
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                // Signature cursor accent bar in coral
                                Box(
                                    modifier = Modifier
                                        .width(2.5.dp)
                                        .height(22.dp)
                                        .background(RendeskSoftCoral, RoundedCornerShape(1.dp))
                                )
                            }
                        }
                    }

                    // Sleek pill badge
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = RendeskButtonBlack,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = if (summary.isProfit) "Active" else "Daily",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Questions 1 & 2 Answers: Total Sales & Total Expenses side by side
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Total Sales Box
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFFBF9FA),
                    border = BorderStroke(1.dp, RendeskBorderSoftGray)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(RendeskLightPurpleTint, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowUpward,
                                    contentDescription = null,
                                    tint = RendeskLavender,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = VendigoStrings.totalSales(isTamil),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RendeskPrimaryText
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = FinanceCalculationService.formatPaiseToRupee(summary.totalSalesPaise),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = RendeskPrimaryText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "💵 ${FinanceCalculationService.formatPaiseToRupee(summary.cashSalesPaise)}  •  📱 ${FinanceCalculationService.formatPaiseToRupee(summary.upiSalesPaise)}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = RendeskSecondaryText
                        )
                    }
                }

                // Total Expenses Box
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFFBF9FA),
                    border = BorderStroke(1.dp, RendeskBorderSoftGray)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(FinanceProfitBg, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDownward,
                                    contentDescription = null,
                                    tint = RendeskSoftCoral,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = VendigoStrings.totalExpenses(isTamil),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RendeskPrimaryText
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = FinanceCalculationService.formatPaiseToRupee(summary.totalExpensesPaise),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = RendeskSoftCoral
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "💵 ${FinanceCalculationService.formatPaiseToRupee(summary.cashExpensesPaise)}  •  📱 ${FinanceCalculationService.formatPaiseToRupee(summary.upiExpensesPaise)}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = RendeskSecondaryText
                        )
                    }
                }
            }
        }
    }
}

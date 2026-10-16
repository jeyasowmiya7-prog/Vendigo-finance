package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.model.PaymentMethod
import com.example.data.model.TransactionType
import com.example.domain.FinanceCalculationService
import com.example.domain.VendigoStrings
import com.example.ui.components.CashDrawerCard
import com.example.ui.components.FinancialSummaryCard
import com.example.ui.components.QuickActionsTray
import com.example.ui.components.TransactionItemCard
import com.example.ui.components.UpiSummaryCard
import com.example.ui.theme.RendeskBorderSoftGray
import com.example.ui.theme.RendeskButtonBlack
import com.example.ui.theme.RendeskCardWhite
import com.example.ui.theme.RendeskLavender
import com.example.ui.theme.RendeskLightPurpleTint
import com.example.ui.theme.RendeskPastelPink
import com.example.ui.theme.RendeskPrimaryText
import com.example.ui.theme.RendeskSecondaryText
import com.example.ui.theme.RendeskSoftCoral
import com.example.ui.viewmodel.ScreenTab
import com.example.ui.viewmodel.VendigoViewModel

@Composable
fun HomeScreen(
    viewModel: VendigoViewModel,
    modifier: Modifier = Modifier
) {
    val isTamil by viewModel.isTamil.collectAsStateWithLifecycle()
    val businessName by viewModel.businessName.collectAsStateWithLifecycle()
    val todaySummary by viewModel.todaySummary.collectAsStateWithLifecycle()
    val todayTransactions by viewModel.todayTransactions.collectAsStateWithLifecycle()
    val selectedEpochDay by viewModel.selectedEpochDay.collectAsStateWithLifecycle()

    val dateFormatted = FinanceCalculationService.formatDisplayDate(selectedEpochDay, isTamil)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Header Banner - Rendesk Minimalist Aesthetic
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(RendeskButtonBlack),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_vendigo_logo_1792139661973),
                            contentDescription = "Vendigo Logo",
                            modifier = Modifier.size(42.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = businessName.ifBlank { VendigoStrings.appTitle(isTamil) },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = RendeskPrimaryText,
                            letterSpacing = (-0.3).sp
                        )
                        Text(
                            text = dateFormatted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RendeskSecondaryText
                        )
                    }
                }

                // In-app language pill (White with subtle soft gray border)
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = RendeskCardWhite,
                    border = BorderStroke(1.dp, RendeskBorderSoftGray),
                    onClick = { viewModel.toggleLanguage(!isTamil) },
                    modifier = Modifier.testTag("quick_lang_toggle")
                ) {
                    Text(
                        text = if (isTamil) "தமிழ்" else "English",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = RendeskPrimaryText,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                    )
                }
            }
        }

        // 2. Financial Summary Card (The 3 Core Questions)
        item {
            FinancialSummaryCard(
                summary = todaySummary,
                isTamil = isTamil
            )
        }

        // 3. Cash Drawer Card (Physical float & reconciliation)
        item {
            CashDrawerCard(
                summary = todaySummary,
                isTamil = isTamil,
                onEditOpeningCash = { viewModel.isOpeningCashDialogOpen.value = true },
                onReconcileCash = { viewModel.isReconciliationDialogOpen.value = true }
            )
        }

        // 4. UPI Summary Card
        item {
            UpiSummaryCard(
                summary = todaySummary,
                isTamil = isTamil
            )
        }

        // 5. Quick Actions Tray
        item {
            QuickActionsTray(
                isTamil = isTamil,
                onAddCashSale = { viewModel.openAddTransaction(TransactionType.SALE, PaymentMethod.CASH) },
                onAddUpiSale = { viewModel.openAddTransaction(TransactionType.SALE, PaymentMethod.UPI) },
                onAddExpense = { viewModel.openAddTransaction(TransactionType.EXPENSE, PaymentMethod.CASH) },
                onViewDailyReport = { viewModel.selectTab(ScreenTab.REPORTS) }
            )
        }

        // 6. Recent Transactions Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isTamil) "இன்றைய பதிவுகள் (${todayTransactions.size})" else "Today's Transactions (${todayTransactions.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = RendeskPrimaryText
                )

                if (todayTransactions.isNotEmpty()) {
                    TextButton(
                        onClick = { viewModel.selectTab(ScreenTab.TRANSACTIONS) },
                        modifier = Modifier.testTag("view_all_transactions_button")
                    ) {
                        Text(
                            text = if (isTamil) "அனைத்தும் காண்" else "View All",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = RendeskPrimaryText
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = RendeskPrimaryText, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        // Empty state or list
        if (todayTransactions.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = RendeskCardWhite,
                    border = BorderStroke(1.dp, RendeskBorderSoftGray),
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .testTag("empty_transactions_banner")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(26.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(RendeskLightPurpleTint, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = null,
                                tint = RendeskLavender,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = VendigoStrings.noTransactions(isTamil),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RendeskPrimaryText
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.openAddTransaction(TransactionType.SALE, PaymentMethod.CASH) },
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = RendeskButtonBlack),
                            modifier = Modifier.testTag("btn_add_first_tx")
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = VendigoStrings.addFirstTransaction(isTamil), fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        } else {
            val previewList = todayTransactions.take(5)
            items(previewList, key = { it.id }) { tx ->
                TransactionItemCard(
                    transaction = tx,
                    isTamil = isTamil,
                    onEdit = { viewModel.openEditTransaction(it) },
                    onDelete = { viewModel.transactionToDelete.value = it }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

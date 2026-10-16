package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.TransactionType
import com.example.domain.FinanceCalculationService
import com.example.domain.VendigoStrings
import com.example.ui.components.TransactionItemCard
import com.example.ui.theme.FinanceLossBg
import com.example.ui.theme.FinanceProfitBg
import com.example.ui.theme.RendeskBorderSoftGray
import com.example.ui.theme.RendeskButtonBlack
import com.example.ui.theme.RendeskCardWhite
import com.example.ui.theme.RendeskLavender
import com.example.ui.theme.RendeskLightPurpleTint
import com.example.ui.theme.RendeskPastelPink
import com.example.ui.theme.RendeskPrimaryBg
import com.example.ui.theme.RendeskPrimaryText
import com.example.ui.theme.RendeskSecondaryBg
import com.example.ui.theme.RendeskSecondaryText
import com.example.ui.theme.RendeskSoftCoral
import com.example.ui.viewmodel.DateFilter
import com.example.ui.viewmodel.PaymentFilter
import com.example.ui.viewmodel.TypeFilter
import com.example.ui.viewmodel.VendigoViewModel

@Composable
fun TransactionsScreen(
    viewModel: VendigoViewModel,
    modifier: Modifier = Modifier
) {
    val isTamil by viewModel.isTamil.collectAsStateWithLifecycle()
    val transactions by viewModel.filteredTransactions.collectAsStateWithLifecycle()
    val dateFilter by viewModel.dateFilter.collectAsStateWithLifecycle()
    val typeFilter by viewModel.typeFilter.collectAsStateWithLifecycle()
    val paymentFilter by viewModel.paymentFilter.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    // Aggregate totals for the current filtered list
    val totalIn = transactions.filter { it.type.equals(TransactionType.SALE.value, ignoreCase = true) }
        .sumOf { it.amountPaise }
    val totalOut = transactions.filter { it.type.equals(TransactionType.EXPENSE.value, ignoreCase = true) }
        .sumOf { it.amountPaise }
    val net = totalIn - totalOut

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("transactions_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Screen Header
        item {
            Text(
                text = VendigoStrings.tabTransactions(isTamil),
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = RendeskPrimaryText,
                letterSpacing = (-0.5).sp
            )
        }

        // Search Field - Rendesk Rounded Pill Style
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.searchQuery.value = it },
                label = { Text(VendigoStrings.searchHint(isTamil), color = RendeskSecondaryText) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = RendeskSecondaryText)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = RendeskPrimaryText)
                        }
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RendeskPastelPink,
                    unfocusedBorderColor = RendeskBorderSoftGray,
                    focusedContainerColor = RendeskCardWhite,
                    unfocusedContainerColor = RendeskCardWhite
                ),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_search_transactions")
            )
        }

        // Date Filter Chips Row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DateFilterChip(
                    title = VendigoStrings.filterAll(isTamil),
                    selected = dateFilter == DateFilter.ALL,
                    onClick = { viewModel.dateFilter.value = DateFilter.ALL },
                    tag = "filter_date_all"
                )
                DateFilterChip(
                    title = VendigoStrings.filterToday(isTamil),
                    selected = dateFilter == DateFilter.TODAY,
                    onClick = { viewModel.dateFilter.value = DateFilter.TODAY },
                    tag = "filter_date_today"
                )
                DateFilterChip(
                    title = VendigoStrings.filterYesterday(isTamil),
                    selected = dateFilter == DateFilter.YESTERDAY,
                    onClick = { viewModel.dateFilter.value = DateFilter.YESTERDAY },
                    tag = "filter_date_yesterday"
                )
                DateFilterChip(
                    title = VendigoStrings.filterThisWeek(isTamil),
                    selected = dateFilter == DateFilter.THIS_WEEK,
                    onClick = { viewModel.dateFilter.value = DateFilter.THIS_WEEK },
                    tag = "filter_date_week"
                )
                DateFilterChip(
                    title = VendigoStrings.filterThisMonth(isTamil),
                    selected = dateFilter == DateFilter.THIS_MONTH,
                    onClick = { viewModel.dateFilter.value = DateFilter.THIS_MONTH },
                    tag = "filter_date_month"
                )
            }
        }

        // Type & Payment Filter Chips Row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Type Filters
                TypeFilter.entries.forEach { t ->
                    val label = when (t) {
                        TypeFilter.ALL -> VendigoStrings.filterAll(isTamil)
                        TypeFilter.SALES -> "📈 ${VendigoStrings.filterSales(isTamil)}"
                        TypeFilter.EXPENSES -> "📉 ${VendigoStrings.filterExpenses(isTamil)}"
                    }
                    FilterChip(
                        selected = typeFilter == t,
                        onClick = { viewModel.typeFilter.value = t },
                        label = { Text(label, fontSize = 12.sp, fontWeight = if (typeFilter == t) FontWeight.Bold else FontWeight.Medium) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RendeskButtonBlack,
                            selectedLabelColor = Color.White,
                            containerColor = RendeskCardWhite,
                            labelColor = RendeskPrimaryText
                        ),
                        border = BorderStroke(1.dp, if (typeFilter == t) RendeskButtonBlack else RendeskBorderSoftGray),
                        shape = RoundedCornerShape(18.dp)
                    )
                }

                // Payment Filters
                PaymentFilter.entries.filter { it != PaymentFilter.ALL }.forEach { p ->
                    val label = when (p) {
                        PaymentFilter.CASH -> "💵 ${VendigoStrings.filterCash(isTamil)}"
                        PaymentFilter.UPI -> "📱 ${VendigoStrings.filterUpi(isTamil)}"
                        else -> ""
                    }
                    FilterChip(
                        selected = paymentFilter == p,
                        onClick = {
                            viewModel.paymentFilter.value = if (paymentFilter == p) PaymentFilter.ALL else p
                        },
                        label = { Text(label, fontSize = 12.sp, fontWeight = if (paymentFilter == p) FontWeight.Bold else FontWeight.Medium) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RendeskButtonBlack,
                            selectedLabelColor = Color.White,
                            containerColor = RendeskCardWhite,
                            labelColor = RendeskPrimaryText
                        ),
                        border = BorderStroke(1.dp, if (paymentFilter == p) RendeskButtonBlack else RendeskBorderSoftGray),
                        shape = RoundedCornerShape(18.dp)
                    )
                }
            }
        }

        // Summary Bar for filtered view - Rendesk Glass Card with subtle shadow & 20dp corners
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = RendeskCardWhite),
                border = BorderStroke(1.dp, RendeskBorderSoftGray),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${VendigoStrings.tabTransactions(isTamil)}: ${transactions.size}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RendeskSecondaryText
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Net: ${FinanceCalculationService.formatPaiseToRupee(net, showSign = true)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = if (net >= 0) RendeskSoftCoral else Color(0xFFE11D48)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "In (+)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = RendeskSoftCoral)
                            Text(
                                text = FinanceCalculationService.formatPaiseToRupee(totalIn),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = RendeskPrimaryText
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Out (-)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = RendeskSecondaryText)
                            Text(
                                text = FinanceCalculationService.formatPaiseToRupee(totalOut),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = RendeskSecondaryText
                            )
                        }
                    }
                }
            }
        }

        // Transaction list items
        if (transactions.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = RendeskCardWhite,
                    border = BorderStroke(1.dp, RendeskBorderSoftGray),
                    shadowElevation = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isTamil) "பொருத்தமான பதிவுகள் எதுவும் இல்லை" else "No matching transactions found",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = RendeskSecondaryText
                        )
                    }
                }
            }
        } else {
            items(transactions, key = { it.id }) { tx ->
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

@Composable
private fun DateFilterChip(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    tag: String
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(title, fontSize = 12.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = RendeskButtonBlack,
            selectedLabelColor = Color.White,
            containerColor = RendeskCardWhite,
            labelColor = RendeskPrimaryText
        ),
        border = BorderStroke(1.dp, if (selected) RendeskButtonBlack else RendeskBorderSoftGray),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.testTag(tag)
    )
}

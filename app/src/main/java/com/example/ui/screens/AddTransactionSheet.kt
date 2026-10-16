package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
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
import com.example.data.model.ExpenseCategory
import com.example.data.model.PaymentMethod
import com.example.data.model.TransactionEntity
import com.example.data.model.TransactionType
import com.example.domain.FinanceCalculationService
import com.example.domain.VendigoStrings
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddTransactionSheet(
    sheetState: SheetState,
    initialType: TransactionType,
    initialPayment: PaymentMethod,
    editingTransaction: TransactionEntity?,
    isTamil: Boolean,
    onDismiss: () -> Unit,
    onSave: (
        type: TransactionType,
        amountPaise: Long,
        paymentMethod: PaymentMethod,
        category: String,
        description: String
    ) -> Unit
) {
    var selectedType by remember(editingTransaction, initialType) {
        mutableStateOf(
            if (editingTransaction != null) TransactionType.fromValue(editingTransaction.type) else initialType
        )
    }

    var selectedPayment by remember(editingTransaction, initialPayment) {
        mutableStateOf(
            if (editingTransaction != null) PaymentMethod.fromValue(editingTransaction.paymentMethod) else initialPayment
        )
    }

    var amountText by remember(editingTransaction) {
        mutableStateOf(
            if (editingTransaction != null) {
                (editingTransaction.amountPaise / 100.0).toString().replace(".0", "")
            } else ""
        )
    }

    var selectedCategory by remember(editingTransaction, selectedType) {
        mutableStateOf(
            if (editingTransaction != null) editingTransaction.category
            else if (selectedType == TransactionType.SALE) "GENERAL_SALE"
            else ExpenseCategory.STOCK.id
        )
    }

    var descriptionText by remember(editingTransaction) {
        mutableStateOf(editingTransaction?.description ?: "")
    }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        containerColor = RendeskCardWhite,
        modifier = Modifier.testTag("add_transaction_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (editingTransaction != null) VendigoStrings.editTransaction(isTamil) else VendigoStrings.newTransaction(isTamil),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = RendeskPrimaryText,
                    letterSpacing = (-0.3).sp
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = RendeskPrimaryText)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 1. Transaction Type Selector (Sale vs Expense) - Rendesk Pill Segment
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilterChip(
                    selected = selectedType == TransactionType.SALE,
                    onClick = {
                        selectedType = TransactionType.SALE
                        selectedCategory = "GENERAL_SALE"
                    },
                    label = {
                        Text(
                            text = "📈 " + VendigoStrings.typeSale(isTamil),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = RendeskButtonBlack,
                        selectedLabelColor = Color.White,
                        containerColor = RendeskSecondaryBg,
                        labelColor = RendeskPrimaryText
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.weight(1f).height(48.dp).testTag("chip_type_sale")
                )

                FilterChip(
                    selected = selectedType == TransactionType.EXPENSE,
                    onClick = {
                        selectedType = TransactionType.EXPENSE
                        if (selectedCategory == "GENERAL_SALE") {
                            selectedCategory = ExpenseCategory.STOCK.id
                        }
                    },
                    label = {
                        Text(
                            text = "📉 " + VendigoStrings.typeExpense(isTamil),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = RendeskSoftCoral,
                        selectedLabelColor = Color.White,
                        containerColor = RendeskSecondaryBg,
                        labelColor = RendeskPrimaryText
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.weight(1f).height(48.dp).testTag("chip_type_expense")
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 2. Amount Input Field - Large Rendesk Numeric Style
            OutlinedTextField(
                value = amountText,
                onValueChange = {
                    amountText = it
                    errorMessage = null
                },
                label = { Text(VendigoStrings.enterAmount(isTamil)) },
                prefix = {
                    Text(
                        text = "₹ ",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        color = RendeskPrimaryText
                    )
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = RendeskPrimaryText,
                    letterSpacing = (-0.5).sp
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                isError = errorMessage != null,
                supportingText = {
                    if (errorMessage != null) {
                        Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RendeskPastelPink,
                    unfocusedBorderColor = RendeskBorderSoftGray,
                    focusedContainerColor = RendeskCardWhite,
                    unfocusedContainerColor = RendeskCardWhite
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_transaction_amount")
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Quick add amount pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(10, 20, 50, 100, 200, 500).forEach { addVal ->
                    SuggestionChip(
                        onClick = {
                            val current = amountText.toDoubleOrNull() ?: 0.0
                            val newVal = current + addVal
                            amountText = if (newVal % 1 == 0.0) newVal.toLong().toString() else newVal.toString()
                            errorMessage = null
                        },
                        label = { Text("+₹$addVal", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = RendeskPrimaryText) },
                        shape = RoundedCornerShape(20.dp),
                        colors = SuggestionChipDefaults.suggestionChipColors(containerColor = RendeskLightPurpleTint),
                        border = BorderStroke(1.dp, RendeskBorderSoftGray)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Payment Method: Cash vs UPI
            Text(
                text = VendigoStrings.paymentMethod(isTamil),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = RendeskPrimaryText
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilterChip(
                    selected = selectedPayment == PaymentMethod.CASH,
                    onClick = { selectedPayment = PaymentMethod.CASH },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Payments, contentDescription = null, tint = RendeskSoftCoral, modifier = Modifier.size(18.dp))
                    },
                    label = { Text("💵 ${VendigoStrings.filterCash(isTamil)}", fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = FinanceProfitBg,
                        selectedLabelColor = RendeskPrimaryText,
                        containerColor = RendeskSecondaryBg,
                        labelColor = RendeskPrimaryText
                    ),
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, if (selectedPayment == PaymentMethod.CASH) RendeskSoftCoral else RendeskBorderSoftGray),
                    modifier = Modifier.weight(1f).height(48.dp).testTag("chip_pay_cash")
                )

                FilterChip(
                    selected = selectedPayment == PaymentMethod.UPI,
                    onClick = { selectedPayment = PaymentMethod.UPI },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = null, tint = RendeskLavender, modifier = Modifier.size(18.dp))
                    },
                    label = { Text("📱 ${VendigoStrings.filterUpi(isTamil)}", fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = RendeskLightPurpleTint,
                        selectedLabelColor = RendeskPrimaryText,
                        containerColor = RendeskSecondaryBg,
                        labelColor = RendeskPrimaryText
                    ),
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, if (selectedPayment == PaymentMethod.UPI) RendeskLavender else RendeskBorderSoftGray),
                    modifier = Modifier.weight(1f).height(48.dp).testTag("chip_pay_upi")
                )
            }

            // 4. Categories (Relevant for Expenses)
            if (selectedType == TransactionType.EXPENSE) {
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = VendigoStrings.selectCategory(isTamil),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = RendeskPrimaryText
                )

                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ExpenseCategory.entries.forEach { cat ->
                        val isSelected = selectedCategory == cat.id
                        val icon = getCategoryIcon(cat)
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = cat.id },
                            leadingIcon = {
                                Icon(imageVector = icon, contentDescription = null, tint = if (isSelected) Color.White else RendeskSoftCoral, modifier = Modifier.size(16.dp))
                            },
                            label = {
                                Text(
                                    text = VendigoStrings.categoryName(cat.id, isTamil),
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = RendeskButtonBlack,
                                selectedLabelColor = Color.White,
                                containerColor = RendeskCardWhite
                            ),
                            border = BorderStroke(1.dp, RendeskBorderSoftGray),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.testTag("cat_chip_${cat.id}")
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 5. Description Field
            OutlinedTextField(
                value = descriptionText,
                onValueChange = { descriptionText = it },
                label = { Text(VendigoStrings.descriptionOptional(isTamil)) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RendeskPastelPink,
                    unfocusedBorderColor = RendeskBorderSoftGray,
                    focusedContainerColor = RendeskCardWhite,
                    unfocusedContainerColor = RendeskCardWhite
                ),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_transaction_desc")
            )

            Spacer(modifier = Modifier.height(26.dp))

            // 6. Action Buttons - Rendesk Luxury Black Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(26.dp),
                    border = BorderStroke(1.dp, RendeskBorderSoftGray),
                    modifier = Modifier.weight(1f).height(54.dp)
                ) {
                    Text(VendigoStrings.cancel(isTamil), color = RendeskPrimaryText, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        val paise = FinanceCalculationService.parseRupeeToPaise(amountText)
                        if (paise == null || paise <= 0L) {
                            errorMessage = if (isTamil) "சரியான தொகையை உள்ளிடவும்" else "Please enter a valid amount greater than ₹0"
                            return@Button
                        }
                        onSave(selectedType, paise, selectedPayment, selectedCategory, descriptionText.trim())
                    },
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RendeskButtonBlack
                    ),
                    modifier = Modifier.weight(1f).height(54.dp).testTag("btn_save_transaction")
                ) {
                    Text(
                        text = if (editingTransaction != null) VendigoStrings.updateTransaction(isTamil) else VendigoStrings.saveTransaction(isTamil),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

private fun getCategoryIcon(category: ExpenseCategory): ImageVector = when (category) {
    ExpenseCategory.STOCK -> Icons.Default.Inventory
    ExpenseCategory.INGREDIENTS -> Icons.Default.Fastfood
    ExpenseCategory.TRANSPORT -> Icons.Default.LocalShipping
    ExpenseCategory.PACKAGING -> Icons.Default.ShoppingBag
    ExpenseCategory.RENT -> Icons.Default.Home
    ExpenseCategory.ELECTRICITY -> Icons.Default.ElectricBolt
    ExpenseCategory.OTHER -> Icons.Default.Category
}

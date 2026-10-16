package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
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
import com.example.data.model.PaymentMethod
import com.example.data.model.TransactionEntity
import com.example.data.model.TransactionType
import com.example.domain.FinanceCalculationService
import com.example.domain.VendigoStrings
import com.example.ui.theme.RendeskBorderSoftGray
import com.example.ui.theme.RendeskCardWhite
import com.example.ui.theme.RendeskLavender
import com.example.ui.theme.RendeskLightPurpleTint
import com.example.ui.theme.RendeskPastelPink
import com.example.ui.theme.RendeskPrimaryText
import com.example.ui.theme.RendeskSecondaryText
import com.example.ui.theme.RendeskSoftCoral

@Composable
fun TransactionItemCard(
    transaction: TransactionEntity,
    isTamil: Boolean,
    onEdit: (TransactionEntity) -> Unit,
    onDelete: (TransactionEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSale = transaction.type.equals(TransactionType.SALE.value, ignoreCase = true)
    val isCash = transaction.paymentMethod.equals(PaymentMethod.CASH.value, ignoreCase = true)

    val typeColor = if (isSale) RendeskSoftCoral else RendeskLavender
    val typeBg = if (isSale) RendeskPastelPink.copy(alpha = 0.45f) else RendeskLightPurpleTint
    val typeIcon = if (isSale) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onEdit(transaction) }
            .testTag("transaction_item_${transaction.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = RendeskCardWhite),
        border = BorderStroke(1.dp, RendeskBorderSoftGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Type Icon Circle
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(typeBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = typeIcon,
                    contentDescription = null,
                    tint = typeColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details Column
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isSale) {
                            if (transaction.description.isNotBlank()) transaction.description else VendigoStrings.typeSale(isTamil)
                        } else {
                            VendigoStrings.categoryName(transaction.category, isTamil)
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = RendeskPrimaryText
                    )
                }

                if (transaction.description.isNotBlank() && !isSale) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = transaction.description,
                        fontSize = 12.sp,
                        color = RendeskSecondaryText
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Badges row: Payment method + time
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isCash) RendeskPastelPink.copy(alpha = 0.5f) else RendeskLightPurpleTint
                    ) {
                        Text(
                            text = if (isCash) "💵 ${VendigoStrings.filterCash(isTamil)}" else "📱 ${VendigoStrings.filterUpi(isTamil)}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RendeskPrimaryText,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }

                    if (transaction.timeFormatted.isNotBlank()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = transaction.timeFormatted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = RendeskSecondaryText
                        )
                    }
                }
            }

            // Amount Column + Actions
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = (if (isSale) "+" else "-") + FinanceCalculationService.formatPaiseToRupee(transaction.amountPaise),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isSale) RendeskPrimaryText else RendeskSecondaryText
                )

                Row {
                    IconButton(
                        onClick = { onEdit(transaction) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = RendeskSecondaryText,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(
                        onClick = { onDelete(transaction) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete",
                            tint = RendeskSoftCoral,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

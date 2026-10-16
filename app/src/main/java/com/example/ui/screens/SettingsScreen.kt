package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.FinanceCalculationService
import com.example.domain.VendigoStrings
import com.example.ui.theme.RendeskBorderSoftGray
import com.example.ui.theme.RendeskButtonBlack
import com.example.ui.theme.RendeskCardWhite
import com.example.ui.theme.RendeskLavender
import com.example.ui.theme.RendeskLightPurpleTint
import com.example.ui.theme.RendeskPastelPink
import com.example.ui.theme.RendeskPrimaryText
import com.example.ui.theme.RendeskSecondaryBg
import com.example.ui.theme.RendeskSecondaryText
import com.example.ui.theme.RendeskSoftCoral
import com.example.ui.viewmodel.VendigoViewModel

@Composable
fun SettingsScreen(
    viewModel: VendigoViewModel,
    modifier: Modifier = Modifier
) {
    val isTamil by viewModel.isTamil.collectAsStateWithLifecycle()
    val businessName by viewModel.businessName.collectAsStateWithLifecycle()
    val vendorName by viewModel.vendorName.collectAsStateWithLifecycle()
    val todaySummary by viewModel.todaySummary.collectAsStateWithLifecycle()

    var editBusinessName by remember(businessName) { mutableStateOf(businessName) }
    var editVendorName by remember(vendorName) { mutableStateOf(vendorName) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("settings_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Text(
                text = VendigoStrings.tabSettings(isTamil),
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = RendeskPrimaryText,
                letterSpacing = (-0.5).sp
            )
        }

        // 1. Language Setting Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = RendeskCardWhite),
                border = BorderStroke(1.dp, RendeskBorderSoftGray),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = RendeskPrimaryText)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = VendigoStrings.appLanguage(isTamil),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = RendeskPrimaryText
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FilterChip(
                            selected = !isTamil,
                            onClick = { viewModel.toggleLanguage(false) },
                            label = { Text("English", fontWeight = if (!isTamil) FontWeight.Bold else FontWeight.Medium) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = RendeskButtonBlack,
                                selectedLabelColor = Color.White,
                                containerColor = RendeskSecondaryBg,
                                labelColor = RendeskPrimaryText
                            ),
                            border = BorderStroke(1.dp, if (!isTamil) RendeskButtonBlack else RendeskBorderSoftGray),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.weight(1f).testTag("chip_lang_en")
                        )

                        FilterChip(
                            selected = isTamil,
                            onClick = { viewModel.toggleLanguage(true) },
                            label = { Text("தமிழ் (Tamil)", fontWeight = if (isTamil) FontWeight.Bold else FontWeight.Medium) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = RendeskButtonBlack,
                                selectedLabelColor = Color.White,
                                containerColor = RendeskSecondaryBg,
                                labelColor = RendeskPrimaryText
                            ),
                            border = BorderStroke(1.dp, if (isTamil) RendeskButtonBlack else RendeskBorderSoftGray),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.weight(1f).testTag("chip_lang_ta")
                        )
                    }
                }
            }
        }

        // 2. Business Profile Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = RendeskCardWhite),
                border = BorderStroke(1.dp, RendeskBorderSoftGray),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Store, contentDescription = null, tint = RendeskPrimaryText)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = VendigoStrings.businessProfile(isTamil),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = RendeskPrimaryText
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = editBusinessName,
                        onValueChange = { editBusinessName = it },
                        label = { Text(VendigoStrings.businessName(isTamil), color = RendeskSecondaryText) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RendeskPastelPink,
                            unfocusedBorderColor = RendeskBorderSoftGray,
                            focusedContainerColor = RendeskCardWhite,
                            unfocusedContainerColor = RendeskCardWhite
                        ),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth().testTag("input_business_name")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = editVendorName,
                        onValueChange = { editVendorName = it },
                        label = { Text(VendigoStrings.vendorName(isTamil), color = RendeskSecondaryText) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RendeskPastelPink,
                            unfocusedBorderColor = RendeskBorderSoftGray,
                            focusedContainerColor = RendeskCardWhite,
                            unfocusedContainerColor = RendeskCardWhite
                        ),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth().testTag("input_vendor_name")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.updateBusinessProfile(editBusinessName.trim(), editVendorName.trim()) },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RendeskButtonBlack),
                        modifier = Modifier.fillMaxWidth().height(50.dp).testTag("btn_save_profile")
                    ) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = VendigoStrings.saveTransaction(isTamil), fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        // 3. Opening Cash Management
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = RendeskCardWhite),
                border = BorderStroke(1.dp, RendeskBorderSoftGray),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = VendigoStrings.openingCash(isTamil),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = RendeskPrimaryText
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${if (isTamil) "தற்போதைய ஆரம்ப இருப்பு" else "Current float"}: ${FinanceCalculationService.formatPaiseToRupee(todaySummary.openingCashPaise)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = RendeskSecondaryText
                        )
                    }

                    OutlinedButton(
                        onClick = { viewModel.isOpeningCashDialogOpen.value = true },
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, RendeskBorderSoftGray),
                        modifier = Modifier.testTag("btn_manage_opening_cash")
                    ) {
                        Text(if (isTamil) "மாற்று" else "Edit", color = RendeskPrimaryText, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 4. Sample Demo Data Seeder
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = RendeskCardWhite),
                border = BorderStroke(1.dp, RendeskBorderSoftGray),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = VendigoStrings.loadSampleData(isTamil),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = RendeskPrimaryText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = VendigoStrings.loadSampleDataDesc(isTamil),
                        fontSize = 12.sp,
                        color = RendeskSecondaryText
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedButton(
                        onClick = { viewModel.loadSampleData() },
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.dp, RendeskBorderSoftGray),
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("btn_load_sample_data")
                    ) {
                        Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = RendeskPrimaryText, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = VendigoStrings.loadSampleData(isTamil), fontWeight = FontWeight.Bold, color = RendeskPrimaryText)
                    }
                }
            }
        }

        // 5. Reset All Data
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = RendeskCardWhite),
                border = BorderStroke(1.dp, RendeskBorderSoftGray),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = VendigoStrings.resetData(isTamil),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = RendeskSoftCoral
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isTamil) "அனைத்து விற்பனை மற்றும் செலவு பதிவுகளையும் அகற்றும்" else "Deletes all transactions and restores clean state",
                        fontSize = 12.sp,
                        color = RendeskSecondaryText
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.isResetConfirmOpen.value = true },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RendeskSoftCoral),
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("btn_reset_all_data")
                    ) {
                        Icon(imageVector = Icons.Default.DeleteForever, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = VendigoStrings.resetData(isTamil), fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        // 6. Security and Offline Pledge
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = RendeskLightPurpleTint.copy(alpha = 0.6f),
                border = BorderStroke(1.dp, RendeskBorderSoftGray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = RendeskLavender, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = VendigoStrings.offlineGuarantee(isTamil),
                        fontSize = 11.sp,
                        color = RendeskSecondaryText,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

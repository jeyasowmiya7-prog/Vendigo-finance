package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.PaymentMethod
import com.example.data.model.TransactionType
import com.example.domain.VendigoStrings
import com.example.ui.screens.AddTransactionSheet
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OpeningCashDialog
import com.example.ui.screens.ReconciliationDialog
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TransactionsScreen
import com.example.ui.theme.AmbientBackgroundGradient
import com.example.ui.theme.RendeskButtonBlack
import com.example.ui.theme.RendeskCardWhite
import com.example.ui.theme.RendeskPastelPink
import com.example.ui.theme.RendeskPrimaryText
import com.example.ui.theme.RendeskSecondaryText
import com.example.ui.theme.RendeskSoftCoral
import com.example.ui.theme.VendigoTheme
import com.example.ui.viewmodel.ScreenTab
import com.example.ui.viewmodel.VendigoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VendigoTheme {
                VendigoApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendigoApp(
    viewModel: VendigoViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val isTamil by viewModel.isTamil.collectAsStateWithLifecycle()
    val todaySummary by viewModel.todaySummary.collectAsStateWithLifecycle()

    val isAddSheetOpen by viewModel.isAddSheetOpen.collectAsStateWithLifecycle()
    val editingTransaction by viewModel.editingTransaction.collectAsStateWithLifecycle()
    val defaultEntryType by viewModel.defaultEntryType.collectAsStateWithLifecycle()
    val defaultPaymentMethod by viewModel.defaultPaymentMethod.collectAsStateWithLifecycle()

    val isOpeningCashDialogOpen by viewModel.isOpeningCashDialogOpen.collectAsStateWithLifecycle()
    val isReconciliationDialogOpen by viewModel.isReconciliationDialogOpen.collectAsStateWithLifecycle()
    val transactionToDelete by viewModel.transactionToDelete.collectAsStateWithLifecycle()
    val isResetConfirmOpen by viewModel.isResetConfirmOpen.collectAsStateWithLifecycle()
    val userNoticeMessage by viewModel.userNoticeMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Handle user notices / toasts
    LaunchedEffect(userNoticeMessage) {
        userNoticeMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearNotice()
        }
    }

    // Predictive Back Handling: Return to Home if on secondary tabs
    if (currentTab != ScreenTab.HOME) {
        BackHandler {
            viewModel.selectTab(ScreenTab.HOME)
        }
    }

    // Rendesk Soft Ambient Pastel Glow Background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmbientBackgroundGradient)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            floatingActionButton = {
                if (currentTab == ScreenTab.HOME || currentTab == ScreenTab.TRANSACTIONS) {
                    FloatingActionButton(
                        onClick = { viewModel.openAddTransaction(TransactionType.SALE, PaymentMethod.CASH) },
                        containerColor = RendeskButtonBlack,
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier.testTag("fab_add_transaction")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Transaction", modifier = Modifier.size(28.dp))
                    }
                }
            },
            bottomBar = {
                NavigationBar(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("bottom_navigation_bar"),
                    containerColor = RendeskCardWhite.copy(alpha = 0.96f),
                    tonalElevation = 2.dp
                ) {
                    val navItemColors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RendeskPrimaryText,
                        indicatorColor = RendeskPastelPink,
                        selectedTextColor = RendeskPrimaryText,
                        unselectedIconColor = RendeskSecondaryText,
                        unselectedTextColor = RendeskSecondaryText
                    )

                    NavigationBarItem(
                        selected = currentTab == ScreenTab.HOME,
                        onClick = { viewModel.selectTab(ScreenTab.HOME) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == ScreenTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                                contentDescription = "Home"
                            )
                        },
                        label = { Text(VendigoStrings.tabHome(isTamil), fontSize = 12.sp, fontWeight = if (currentTab == ScreenTab.HOME) FontWeight.Bold else FontWeight.Normal) },
                        colors = navItemColors,
                        modifier = Modifier.testTag("nav_tab_home")
                    )

                    NavigationBarItem(
                        selected = currentTab == ScreenTab.TRANSACTIONS,
                        onClick = { viewModel.selectTab(ScreenTab.TRANSACTIONS) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == ScreenTab.TRANSACTIONS) Icons.Filled.ReceiptLong else Icons.Outlined.ReceiptLong,
                                contentDescription = "Transactions"
                            )
                        },
                        label = { Text(VendigoStrings.tabTransactions(isTamil), fontSize = 12.sp, fontWeight = if (currentTab == ScreenTab.TRANSACTIONS) FontWeight.Bold else FontWeight.Normal) },
                        colors = navItemColors,
                        modifier = Modifier.testTag("nav_tab_transactions")
                    )

                    NavigationBarItem(
                        selected = currentTab == ScreenTab.REPORTS,
                        onClick = { viewModel.selectTab(ScreenTab.REPORTS) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == ScreenTab.REPORTS) Icons.Filled.BarChart else Icons.Outlined.BarChart,
                                contentDescription = "Reports"
                            )
                        },
                        label = { Text(VendigoStrings.tabReports(isTamil), fontSize = 12.sp, fontWeight = if (currentTab == ScreenTab.REPORTS) FontWeight.Bold else FontWeight.Normal) },
                        colors = navItemColors,
                        modifier = Modifier.testTag("nav_tab_reports")
                    )

                    NavigationBarItem(
                        selected = currentTab == ScreenTab.SETTINGS,
                        onClick = { viewModel.selectTab(ScreenTab.SETTINGS) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == ScreenTab.SETTINGS) Icons.Filled.Settings else Icons.Outlined.Settings,
                                contentDescription = "Settings"
                            )
                        },
                        label = { Text(VendigoStrings.tabSettings(isTamil), fontSize = 12.sp, fontWeight = if (currentTab == ScreenTab.SETTINGS) FontWeight.Bold else FontWeight.Normal) },
                        colors = navItemColors,
                        modifier = Modifier.testTag("nav_tab_settings")
                    )
                }
            }
        ) { innerPadding ->
            when (currentTab) {
                ScreenTab.HOME -> HomeScreen(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                ScreenTab.TRANSACTIONS -> TransactionsScreen(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                ScreenTab.REPORTS -> ReportsScreen(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                ScreenTab.SETTINGS -> SettingsScreen(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
            }
        }
    }

    // Modal Bottom Sheet: Add or Edit Transaction
    if (isAddSheetOpen) {
        AddTransactionSheet(
            sheetState = sheetState,
            initialType = defaultEntryType,
            initialPayment = defaultPaymentMethod,
            editingTransaction = editingTransaction,
            isTamil = isTamil,
            onDismiss = { viewModel.closeAddSheet() },
            onSave = { type, amountPaise, paymentMethod, category, description ->
                viewModel.saveTransaction(type, amountPaise, paymentMethod, category, description)
            }
        )
    }

    // Dialog: Enter Opening Cash
    if (isOpeningCashDialogOpen) {
        OpeningCashDialog(
            currentOpeningPaise = todaySummary.openingCashPaise,
            isTamil = isTamil,
            onDismiss = { viewModel.isOpeningCashDialogOpen.value = false },
            onConfirm = { paise -> viewModel.setOpeningCash(paise) }
        )
    }

    // Dialog: Reconcile Closing Cash
    if (isReconciliationDialogOpen) {
        ReconciliationDialog(
            expectedClosingPaise = todaySummary.expectedClosingCashPaise,
            previouslyCountedPaise = todaySummary.countedClosingCashPaise,
            isTamil = isTamil,
            onDismiss = { viewModel.isReconciliationDialogOpen.value = false },
            onConfirm = { countedPaise, notes -> viewModel.setCountedClosingCash(countedPaise, notes) }
        )
    }

    // Dialog: Delete Transaction Confirmation
    transactionToDelete?.let { tx ->
        AlertDialog(
            onDismissRequest = { viewModel.transactionToDelete.value = null },
            title = { Text(VendigoStrings.delete(isTamil), fontWeight = FontWeight.Bold, color = RendeskPrimaryText) },
            text = { Text(VendigoStrings.deleteConfirm(isTamil), color = RendeskSecondaryText) },
            confirmButton = {
                Button(
                    onClick = { viewModel.deleteTransaction(tx) },
                    colors = ButtonDefaults.buttonColors(containerColor = RendeskSoftCoral),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(VendigoStrings.delete(isTamil), color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { viewModel.transactionToDelete.value = null },
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(VendigoStrings.cancel(isTamil), color = RendeskPrimaryText)
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = RendeskCardWhite
        )
    }

    // Dialog: Reset All Data Confirmation
    if (isResetConfirmOpen) {
        AlertDialog(
            onDismissRequest = { viewModel.isResetConfirmOpen.value = false },
            title = { Text(VendigoStrings.resetData(isTamil), fontWeight = FontWeight.Bold, color = RendeskSoftCoral) },
            text = { Text(VendigoStrings.resetConfirm(isTamil), color = RendeskSecondaryText) },
            confirmButton = {
                Button(
                    onClick = { viewModel.resetAllData() },
                    colors = ButtonDefaults.buttonColors(containerColor = RendeskSoftCoral),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(VendigoStrings.resetData(isTamil), color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { viewModel.isResetConfirmOpen.value = false },
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(VendigoStrings.cancel(isTamil), color = RendeskPrimaryText)
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = RendeskCardWhite
        )
    }
}

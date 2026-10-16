package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.ui.viewmodel.KitaViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read app name from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Kita", appName)
  }

  @Test
  fun `verify coin tracking and redemption logic`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = KitaViewModel(app)

    // Initial coin balance from design reference
    assertEquals(200, viewModel.coins.value)

    // Log dose adds 100 coins
    viewModel.logTodayDose()
    assertEquals(300, viewModel.coins.value)
    assertTrue(viewModel.todayLogged.value)
    assertEquals(2, viewModel.consecutiveDays.value)

    // Available products exist
    assertEquals(6, viewModel.availableProducts.size)
    val discountProduct = viewModel.availableProducts.first { it.id == "3" }
    assertEquals("10% Discount", discountProduct.title)
    assertEquals(500, discountProduct.coinCost)
  }
}

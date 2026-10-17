package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.CardRepository
import com.example.model.CardType
import com.example.viewmodel.GameViewModel
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("פשוט להיפגש", appName)
  }

  @Test
  fun `verify default cards count is 37 and includes authentic tasks`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = CardRepository(context)
    val cards = repository.getAllCards()
    assertEquals(37, cards.size)

    val staringContestCard = cards.find { it.title.contains("מבט בעיניים") }
    assertNotNull(staringContestCard)
    assertTrue(staringContestCard!!.challengeMode)
    assertEquals(CardType.SHARED, staringContestCard.type)
  }

  @Test
  fun `test custom card addition and persistence`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = CardRepository(context)

    val customTitle = "משימה זוגית מותאמת"
    val customText = "ספר/י לי זיכרון יפה"
    val card = com.example.model.CoupleCard(
      id = 9999L,
      type = CardType.INDIVIDUAL,
      title = customTitle,
      text = customText,
      isCustom = true
    )
    repository.saveCustomCard(card)

    val allCards = repository.getAllCards()
    assertTrue(allCards.any { it.title == customTitle && it.text == customText })
  }

  @Test
  fun `test game flow and firgun stone transfer`() {
    val app = ApplicationProvider.getApplicationContext<Application>()
    val vm = GameViewModel(app)

    // Set withDice = false for deterministic card flip without waiting for dice roll coroutine
    vm.updateSettings(withDice = false, withStones = true, soundEnabled = false)

    val initialBank = vm.uiState.value.bankStones
    assertEquals(20, initialBank)

    // Simulate individual task done
    vm.flipCard()
    assertTrue(vm.uiState.value.isCardFlipped)
    vm.onIndividualTaskDone()

    val stateAfterDone = vm.uiState.value
    assertEquals(19, stateAfterDone.bankStones)
    assertEquals(1, stateAfterDone.player1Stones.size)

    // Firgun transfer from P1 to P2
    vm.executeFirgun(1)
    val stateAfterFirgun = vm.uiState.value
    assertEquals(0, stateAfterFirgun.player1Stones.size)
    assertEquals(1, stateAfterFirgun.player2Stones.size)
  }
}

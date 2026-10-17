package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CardRepository
import com.example.data.SoundManager
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel(application: Application) : AndroidViewModel(application) {
  private val repository = CardRepository(application)
  val soundManager = SoundManager()

  private val _uiState = MutableStateFlow(GameState())
  val uiState: StateFlow<GameState> = _uiState.asStateFlow()

  init {
    loadInitialData()
  }

  private fun loadInitialData() {
    val settings = repository.loadSettings()
    soundManager.isEnabled = settings.soundEnabled
    val allCards = repository.getAllCards().shuffled()
    val (bank, p1Stones, p2Stones) = repository.loadSessionStones()

    _uiState.update {
      it.copy(
        settings = settings,
        bankStones = bank,
        player1Stones = p1Stones,
        player2Stones = p2Stones,
        deck = allCards,
        currentCardIndex = 0,
        activePlayer = 1,
        statusMessage = "הטילו קוביות כדי לגלות מי מרים קלף!"
      )
    }
  }

  fun rollDice() {
    val current = _uiState.value
    if (current.isRolling || current.isCardFlipped || !current.settings.withDice) return

    viewModelScope.launch {
      _uiState.update { it.copy(isRolling = true, activeFirgunPlayer = null) }
      soundManager.playDiceRoll()

      // 3D roll delay
      delay(700)

      val roll1 = Random.nextInt(1, 7)
      val roll2 = Random.nextInt(1, 7)

      if (roll1 == roll2) {
        _uiState.update {
          it.copy(
            isRolling = false,
            lastRollP1 = roll1,
            lastRollP2 = roll2,
            hasRolledThisTurn = false,
            statusMessage = "תיקו ($roll1 : $roll2)! זרקו שוב את הקוביות להכרעה 🎲"
          )
        }
      } else {
        // Whoever rolls the SMALLER number draws the card!
        val active = if (roll1 < roll2) 1 else 2
        val activeName = if (active == 1) current.settings.player1Name else current.settings.player2Name
        val lowScore = if (active == 1) roll1 else roll2
        val highScore = if (active == 1) roll2 else roll1

        _uiState.update {
          it.copy(
            isRolling = false,
            lastRollP1 = roll1,
            lastRollP2 = roll2,
            hasRolledThisTurn = true,
            activePlayer = active,
            statusMessage = "ל$activeName יצא $lowScore (נמוך מ-$highScore) – תורך להרים קלף!"
          )
        }
      }
    }
  }

  fun flipCard() {
    val current = _uiState.value
    if (current.isCardFlipped || current.isRolling) return

    // If withDice is active and haven't rolled yet, roll first!
    if (current.settings.withDice && !current.hasRolledThisTurn) {
      rollDice()
      return
    }

    soundManager.playCardFlip()
    val card = current.deck.getOrNull(current.currentCardIndex)
    val activeName = if (current.activePlayer == 1) current.settings.player1Name else current.settings.player2Name
    val message = if (card?.type == CardType.SHARED) {
      "משימה משותפת ל${current.settings.player1Name} ו${current.settings.player2Name}!"
    } else {
      "המשימה של $activeName – קראו ובצעו!"
    }

    _uiState.update {
      it.copy(
        isCardFlipped = true,
        activeFirgunPlayer = null,
        statusMessage = message
      )
    }
  }

  fun onIndividualTaskDone() {
    val current = _uiState.value
    if (!current.isCardFlipped) return

    val targetPlayer = current.activePlayer
    var newBank = current.bankStones
    val p1 = current.player1Stones.toMutableList()
    val p2 = current.player2Stones.toMutableList()

    if (current.settings.withStones && newBank > 0) {
      val gem = GemColor.fromIndex(20 - newBank)
      newBank--
      if (targetPlayer == 1) p1.add(gem) else p2.add(gem)
      soundManager.playGlassStoneClink(false)
      repository.saveSessionStones(newBank, p1, p2)
    }

    checkVictory(p1, p2)
    advanceCard(newBank, p1, p2)
  }

  fun onIndividualTaskSkip() {
    val current = _uiState.value
    if (!current.isCardFlipped) return

    val targetPlayer = current.activePlayer
    var newBank = current.bankStones
    val p1 = current.player1Stones.toMutableList()
    val p2 = current.player2Stones.toMutableList()

    if (current.settings.withStones) {
      val targetList = if (targetPlayer == 1) p1 else p2
      if (targetList.isNotEmpty()) {
        targetList.removeAt(targetList.size - 1)
        newBank = (newBank + 1).coerceAtMost(20)
        soundManager.playGlassStoneClink(false)
        repository.saveSessionStones(newBank, p1, p2)
      }
    }

    advanceCard(newBank, p1, p2)
  }

  fun onSharedTaskBothDone() {
    val current = _uiState.value
    if (!current.isCardFlipped) return

    var newBank = current.bankStones
    val p1 = current.player1Stones.toMutableList()
    val p2 = current.player2Stones.toMutableList()

    if (current.settings.withStones) {
      if (newBank > 0) {
        val gem1 = GemColor.fromIndex(20 - newBank)
        newBank--
        p1.add(gem1)
      }
      if (newBank > 0) {
        val gem2 = GemColor.fromIndex(20 - newBank)
        newBank--
        p2.add(gem2)
      }
      soundManager.playGlassStoneClink(true)
      repository.saveSessionStones(newBank, p1, p2)
    }

    checkVictory(p1, p2)
    advanceCard(newBank, p1, p2)
  }

  fun onSharedTaskWinner(winner: Int) {
    val current = _uiState.value
    if (!current.isCardFlipped) return

    var newBank = current.bankStones
    val p1 = current.player1Stones.toMutableList()
    val p2 = current.player2Stones.toMutableList()

    if (current.settings.withStones && newBank > 0) {
      val gem = GemColor.fromIndex(20 - newBank)
      newBank--
      if (winner == 1) p1.add(gem) else p2.add(gem)
      soundManager.playGlassStoneClink(false)
      repository.saveSessionStones(newBank, p1, p2)
    }

    checkVictory(p1, p2)
    advanceCard(newBank, p1, p2)
  }

  fun advanceCard(
    bank: Int = _uiState.value.bankStones,
    p1: List<GemColor> = _uiState.value.player1Stones,
    p2: List<GemColor> = _uiState.value.player2Stones
  ) {
    val current = _uiState.value
    soundManager.playCardSlide()

    val nextIndex = (current.currentCardIndex + 1) % current.deck.size
    val nextActive = if (!current.settings.withDice) {
      if (current.activePlayer == 1) 2 else 1
    } else {
      current.activePlayer
    }
    val nextActiveName = if (nextActive == 1) current.settings.player1Name else current.settings.player2Name

    val message = if (!current.settings.withDice) {
      "תור $nextActiveName – לחצו על הקלף"
    } else {
      "תור חדש! זרקו את הקוביות 🎲"
    }

    _uiState.update {
      it.copy(
        bankStones = bank,
        player1Stones = p1,
        player2Stones = p2,
        currentCardIndex = nextIndex,
        isCardFlipped = false,
        hasRolledThisTurn = false,
        lastRollP1 = null,
        lastRollP2 = null,
        activePlayer = nextActive,
        statusMessage = message
      )
    }
  }

  fun toggleFirgunPopover(player: Int) {
    _uiState.update {
      it.copy(activeFirgunPlayer = if (it.activeFirgunPlayer == player) null else player)
    }
  }

  fun closeFirgunPopover() {
    _uiState.update { it.copy(activeFirgunPlayer = null) }
  }

  fun executeFirgun(fromPlayer: Int) {
    val current = _uiState.value
    val p1 = current.player1Stones.toMutableList()
    val p2 = current.player2Stones.toMutableList()

    val fromList = if (fromPlayer == 1) p1 else p2
    val toList = if (fromPlayer == 1) p2 else p1

    if (fromList.isEmpty()) {
      closeFirgunPopover()
      return
    }

    val gem = fromList.removeAt(fromList.size - 1)
    toList.add(gem)

    soundManager.playGlassStoneClink(true)
    repository.saveSessionStones(current.bankStones, p1, p2)

    val giverName = if (fromPlayer == 1) current.settings.player1Name else current.settings.player2Name
    val receiverName = if (fromPlayer == 1) current.settings.player2Name else current.settings.player1Name

    checkVictory(p1, p2)

    _uiState.update {
      it.copy(
        player1Stones = p1,
        player2Stones = p2,
        activeFirgunPlayer = null,
        statusMessage = "איזה פרגון! $giverName העניק/ה אבן טובה ל$receiverName 🤍"
      )
    }
  }

  private fun checkVictory(p1: List<GemColor>, p2: List<GemColor>) {
    if (!_uiState.value.settings.withStones) return
    if (p1.size >= 10 && _uiState.value.winnerPlayer == null) {
      soundManager.playVictory()
      _uiState.update { it.copy(winnerPlayer = 1) }
    } else if (p2.size >= 10 && _uiState.value.winnerPlayer == null) {
      soundManager.playVictory()
      _uiState.update { it.copy(winnerPlayer = 2) }
    }
  }

  fun dismissVictory() {
    _uiState.update { it.copy(winnerPlayer = null) }
  }

  fun updatePlayerNames(p1Name: String, p2Name: String) {
    val name1 = p1Name.ifBlank { "שחקן 1" }
    val name2 = p2Name.ifBlank { "שחקן 2" }
    val newSettings = _uiState.value.settings.copy(player1Name = name1, player2Name = name2)
    repository.saveSettings(newSettings)
    _uiState.update { it.copy(settings = newSettings) }
  }

  fun updateSettings(
    withDice: Boolean,
    withStones: Boolean,
    soundEnabled: Boolean,
    seatingMode: SeatingMode = _uiState.value.settings.seatingMode
  ) {
    soundManager.isEnabled = soundEnabled
    val newSettings = _uiState.value.settings.copy(
      withDice = withDice,
      withStones = withStones,
      soundEnabled = soundEnabled,
      seatingMode = seatingMode
    )
    repository.saveSettings(newSettings)
    _uiState.update { it.copy(settings = newSettings) }
  }

  fun addCustomCard(title: String, text: String, type: CardType) {
    val card = CoupleCard(
      id = System.currentTimeMillis(),
      type = type,
      title = title.ifBlank { "קלף אישי מיוחד" },
      text = text,
      isCustom = true,
      theme = CardTheme.entries[Random.nextInt(CardTheme.entries.size)]
    )
    repository.saveCustomCard(card)
    val newDeck = _uiState.value.deck.toMutableList()
    newDeck.add(_uiState.value.currentCardIndex + 1, card)
    _uiState.update { it.copy(deck = newDeck) }
  }

  fun resetGame() {
    val allCards = repository.getAllCards().shuffled()
    repository.saveSessionStones(20, emptyList(), emptyList())
    _uiState.update {
      it.copy(
        bankStones = 20,
        player1Stones = emptyList(),
        player2Stones = emptyList(),
        deck = allCards,
        currentCardIndex = 0,
        activePlayer = 1,
        isCardFlipped = false,
        hasRolledThisTurn = false,
        lastRollP1 = null,
        lastRollP2 = null,
        winnerPlayer = null,
        activeFirgunPlayer = null,
        statusMessage = "המשחק אופס! הטילו קוביות כדי להתחיל 🎲"
      )
    }
  }
}

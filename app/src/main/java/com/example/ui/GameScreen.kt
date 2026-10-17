package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CardType
import com.example.model.GameState
import com.example.ui.components.*
import com.example.ui.dialogs.*
import com.example.ui.theme.*
import com.example.viewmodel.GameViewModel

@Composable
fun GameScreen(
  viewModel: GameViewModel,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsState()

  var showSplash by remember { mutableStateOf(true) }
  var showRulesDialog by remember { mutableStateOf(false) }
  var showSettingsDialog by remember { mutableStateOf(false) }
  var showAddCardDialog by remember { mutableStateOf(false) }

  // Set Hebrew Right-to-Left Direction
  CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .background(
          Brush.radialGradient(
            colors = listOf(BgCanvasCenter, BgCanvasLight, BgCanvasGradientEnd),
            radius = 1100f
          )
        )
    ) {
      Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
          TopGameBar(
            stonesCount = uiState.bankStones,
            withStones = uiState.settings.withStones,
            onSettingsClick = { showSettingsDialog = true },
            onRulesClick = { showRulesDialog = true }
          )
        },
        bottomBar = {
          BottomPlayersBar(
            uiState = uiState,
            onDieClick = { viewModel.rollDice() },
            onToggleFirgun = { viewModel.toggleFirgunPopover(it) },
            onConfirmFirgun = { viewModel.executeFirgun(it) }
          )
        }
      ) { innerPadding ->
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 16.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          // Turn status indicator pill
          StatusPill(
            message = uiState.statusMessage,
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
          )

          // Center Card Deck
          Box(
            modifier = Modifier
              .weight(1f)
              .fillMaxWidth(),
            contentAlignment = Alignment.Center
          ) {
            val currentCard = uiState.deck.getOrNull(uiState.currentCardIndex)
            val isReady = !uiState.settings.withDice || uiState.hasRolledThisTurn

            CircularCard(
              card = currentCard,
              isFlipped = uiState.isCardFlipped,
              cardNumber = uiState.currentCardIndex + 1,
              totalCards = uiState.deck.size,
              activePlayerName = if (uiState.activePlayer == 1) uiState.settings.player1Name else uiState.settings.player2Name,
              partnerName = if (uiState.activePlayer == 1) uiState.settings.player2Name else uiState.settings.player1Name,
              isReadyToFlip = isReady,
              onClick = { viewModel.flipCard() },
              cardDiameter = 295.dp
            )
          }

          // Task Action Buttons (Visible when card is flipped)
          AnimatedVisibility(
            visible = uiState.isCardFlipped,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
            modifier = Modifier.padding(bottom = 8.dp)
          ) {
            TaskActionButtons(
              uiState = uiState,
              onIndividualDone = { viewModel.onIndividualTaskDone() },
              onIndividualSkip = { viewModel.onIndividualTaskSkip() },
              onSharedBothDone = { viewModel.onSharedTaskBothDone() },
              onSharedWinner = { viewModel.onSharedTaskWinner(it) },
              onAdvanceCard = { viewModel.advanceCard() }
            )
          }
        }
      }

      // Splash Screen Overlay (Initial turn)
      AnimatedVisibility(
        visible = showSplash,
        enter = fadeIn(),
        exit = fadeOut() + scaleOut(targetScale = 1.05f)
      ) {
        SplashScreen(
          onEnterGame = {
            showSplash = false
          }
        )
      }

      // Dialogs
      if (showRulesDialog) {
        RulesDialog(onDismiss = { showRulesDialog = false })
      }

      if (showSettingsDialog) {
        SettingsDialog(
          settings = uiState.settings,
          onDismiss = { showSettingsDialog = false },
          onSaveNames = { p1, p2 -> viewModel.updatePlayerNames(p1, p2) },
          onSaveSettings = { dice, stones, sound -> viewModel.updateSettings(dice, stones, sound) },
          onOpenAddCard = {
            showSettingsDialog = false
            showAddCardDialog = true
          },
          onResetGame = { viewModel.resetGame() }
        )
      }

      if (showAddCardDialog) {
        AddCustomCardDialog(
          onDismiss = { showAddCardDialog = false },
          onSaveCard = { title, text, type -> viewModel.addCustomCard(title, text, type) }
        )
      }

      uiState.winnerPlayer?.let { winnerNum ->
        val winnerName = if (winnerNum == 1) uiState.settings.player1Name else uiState.settings.player2Name
        val partnerName = if (winnerNum == 1) uiState.settings.player2Name else uiState.settings.player1Name
        val winnerStones = if (winnerNum == 1) uiState.player1Stones else uiState.player2Stones

        VictoryDialog(
          winnerName = winnerName,
          partnerName = partnerName,
          stones = winnerStones,
          onContinue = { viewModel.dismissVictory() },
          onNewRound = {
            viewModel.dismissVictory()
            viewModel.resetGame()
          }
        )
      }
    }
  }
}

@Composable
private fun TopGameBar(
  stonesCount: Int,
  withStones: Boolean,
  onSettingsClick: () -> Unit,
  onRulesClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp, vertical = 6.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Settings Button (Right in RTL)
    Box(
      modifier = Modifier
        .size(42.dp)
        .shadow(4.dp, CircleShape)
        .clip(CircleShape)
        .background(
          Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFF6F2EB)))
        )
        .border(1.dp, Color(0x3378695A), CircleShape)
        .clickable(onClick = onSettingsClick)
        .testTag("settings_button"),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.Settings,
        contentDescription = "הגדרות",
        tint = Color(0xFF5C4E43),
        modifier = Modifier.size(20.dp)
      )
    }

    // Center Bank of 20 Stones
    if (withStones) {
      BankTray(stonesCount = stonesCount)
    } else {
      Spacer(modifier = Modifier.width(42.dp))
    }

    // Rules Button (Left in RTL)
    Box(
      modifier = Modifier
        .size(42.dp)
        .shadow(4.dp, CircleShape)
        .clip(CircleShape)
        .background(
          Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFF6F2EB)))
        )
        .border(1.dp, Color(0x3378695A), CircleShape)
        .clickable(onClick = onRulesClick)
        .testTag("rules_button"),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = "!",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Serif,
        color = Color(0xFF5C4E43)
      )
    }
  }
}

@Composable
private fun StatusPill(
  message: String,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = CircleShape,
    color = Color(0xEFFFFFFF),
    shadowElevation = 2.dp,
    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x33D1D5DB)),
    modifier = modifier
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Box(
        modifier = Modifier
          .size(8.dp)
          .clip(CircleShape)
          .background(AccentSky)
      )
      Text(
        text = message,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = InkDark,
        textAlign = TextAlign.Center
      )
    }
  }
}

@Composable
private fun TaskActionButtons(
  uiState: GameState,
  onIndividualDone: () -> Unit,
  onIndividualSkip: () -> Unit,
  onSharedBothDone: () -> Unit,
  onSharedWinner: (Int) -> Unit,
  onAdvanceCard: () -> Unit
) {
  val card = uiState.deck.getOrNull(uiState.currentCardIndex) ?: return

  if (!uiState.settings.withStones) {
    Button(
      onClick = onAdvanceCard,
      colors = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF2C2520)
      ),
      shape = RoundedCornerShape(20.dp),
      modifier = Modifier.testTag("action_next_card")
    ) {
      Text("המשך לקלף הבא ←", fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
    return
  }

  val activeName = if (uiState.activePlayer == 1) uiState.settings.player1Name else uiState.settings.player2Name

  if (card.type == CardType.INDIVIDUAL) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Button(
        onClick = onIndividualDone,
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFF059669)
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.testTag("action_task_done")
      ) {
        Text("עשיתי! (+ אבן ל$activeName) 💎", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }

      OutlinedButton(
        onClick = onIndividualSkip,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.outlinedButtonColors(
          contentColor = InkDark
        ),
        modifier = Modifier.testTag("action_task_skip")
      ) {
        Text("לא עשיתי (החזר אבן)", fontSize = 11.sp, fontWeight = FontWeight.Medium)
      }
    }
  } else {
    // Shared Task
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      Button(
        onClick = onSharedBothDone,
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFF0D9488)
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.testTag("action_shared_both_done")
      ) {
        Text("עשינו יחד! (+ אבן לשנינו) 💎💎", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }

      if (card.challengeMode) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          Button(
            onClick = { onSharedWinner(1) },
            colors = ButtonDefaults.buttonColors(containerColor = AccentSky),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text("${uiState.settings.player1Name} הצליח/ה (+1)", fontSize = 11.sp)
          }

          Button(
            onClick = { onSharedWinner(2) },
            colors = ButtonDefaults.buttonColors(containerColor = AccentSky),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text("${uiState.settings.player2Name} הצליח/ה (+1)", fontSize = 11.sp)
          }
        }
      }

      OutlinedButton(
        onClick = onAdvanceCard,
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
      ) {
        Text("דילוג", fontSize = 11.sp)
      }
    }
  }
}

@Composable
private fun BottomPlayersBar(
  uiState: GameState,
  onDieClick: () -> Unit,
  onToggleFirgun: (Int) -> Unit,
  onConfirmFirgun: (Int) -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp, vertical = 8.dp)
      .windowInsetsPadding(WindowInsets.navigationBars)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // Player 1 Zone (Right side in RTL)
      PlayerZone(
        playerNumber = 1,
        playerName = uiState.settings.player1Name,
        partnerName = uiState.settings.player2Name,
        isActiveTurn = (!uiState.settings.withDice || uiState.hasRolledThisTurn) && uiState.activePlayer == 1,
        lastRoll = uiState.lastRollP1,
        isRolling = uiState.isRolling,
        stones = uiState.player1Stones,
        withDice = uiState.settings.withDice,
        withStones = uiState.settings.withStones,
        isFirgunOpen = uiState.activeFirgunPlayer == 1,
        onDieClick = onDieClick,
        onStonePileClick = { onToggleFirgun(1) },
        onConfirmFirgun = { onConfirmFirgun(1) },
        modifier = Modifier.weight(1f)
      )

      // Player 2 Zone (Left side in RTL)
      PlayerZone(
        playerNumber = 2,
        playerName = uiState.settings.player2Name,
        partnerName = uiState.settings.player1Name,
        isActiveTurn = (!uiState.settings.withDice || uiState.hasRolledThisTurn) && uiState.activePlayer == 2,
        lastRoll = uiState.lastRollP2,
        isRolling = uiState.isRolling,
        stones = uiState.player2Stones,
        withDice = uiState.settings.withDice,
        withStones = uiState.settings.withStones,
        isFirgunOpen = uiState.activeFirgunPlayer == 2,
        onDieClick = onDieClick,
        onStonePileClick = { onToggleFirgun(2) },
        onConfirmFirgun = { onConfirmFirgun(2) },
        modifier = Modifier.weight(1f)
      )
    }
  }
}

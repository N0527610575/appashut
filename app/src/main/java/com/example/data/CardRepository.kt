package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.CardTheme
import com.example.model.CardType
import com.example.model.CoupleCard
import com.example.model.GameSettings
import com.example.model.GemColor
import org.json.JSONArray
import org.json.JSONObject

class CardRepository(context: Context) {
  private val prefs: SharedPreferences =
    context.getSharedPreferences("pashut_lehipagesh_prefs", Context.MODE_PRIVATE)

  val defaultCards: List<CoupleCard> = listOf(
    CoupleCard(
      id = 1,
      type = CardType.SHARED,
      challengeMode = true,
      title = "מבט בעיניים בלי לחייך",
      text = "מסתכלים אחד לשני בעיניים.. בלי לחייך. זה שהצליח מקבל אבן מהקופה!",
      theme = CardTheme.SUNFLOWER
    ),
    CoupleCard(
      id = 2,
      type = CardType.INDIVIDUAL,
      title = "ברכה מכל הלב",
      text = "הנח/י ידיים על הכתפיים או כפות הידיים של בן/בת הזוג, וברך/י אותו/ה בברכה אישית לחיים, לבריאות, לשמחה ולזוגיות שלכם.",
      theme = CardTheme.PEACH
    ),
    CoupleCard(
      id = 3,
      type = CardType.INDIVIDUAL,
      title = "עיסוי כתפיים וצוואר מפנק",
      text = "עסה/י לבן/בת הזוג את הצוואר והכתפיים במשך דקה שלמה בעדינות ובקשב מלא.",
      theme = CardTheme.MINT
    ),
    CoupleCard(
      id = 4,
      type = CardType.SHARED,
      title = "נשימה זוגית מסונכרנת",
      text = "משימה משותפת: שבו קרוב, אחזו ידיים ונשמו יחד 8 נשימות עמוקות ואיטיות בעיניים פקוחות.",
      theme = CardTheme.LILAC
    ),
    CoupleCard(
      id = 5,
      type = CardType.INDIVIDUAL,
      title = "שלושה דברים שאני מעריך/ה בך",
      text = "ספר/י לבן/בת הזוג על 3 דברים קטנים שהוא או היא עושים ביומיום ושאת/ה ממש מעריך/ה ומודה עליהם.",
      theme = CardTheme.SUNFLOWER
    ),
    CoupleCard(
      id = 6,
      type = CardType.INDIVIDUAL,
      title = "הגשה מלכותית",
      text = "קום/י והבא/י לבן/בת הזוג כוס מים, שתייה חמה או פינוק קטן מהמטבח בהגשה מלכותית עם חיוך.",
      theme = CardTheme.PEACH
    ),
    CoupleCard(
      id = 7,
      type = CardType.SHARED,
      title = "חיבוק דב של דקה",
      text = "משימה משותפת: התחבקו חיבוק חם, עוטף ושקט למשך דקה שלמה בלי לדבר ובלי להסתכל בטלפון.",
      theme = CardTheme.MINT
    ),
    CoupleCard(
      id = 8,
      type = CardType.INDIVIDUAL,
      title = "תכונה שמאירה אותך",
      text = "שתף/י תכונת אופי אחת של בן/בת הזוג שמרגשת אותך במיוחד, ואיך ראית אותה באה לידי ביטוי לאחרונה.",
      theme = CardTheme.LILAC
    ),
    CoupleCard(
      id = 9,
      type = CardType.INDIVIDUAL,
      title = "עיסוי כפות ידיים מרגיע",
      text = "קח/י את כפות הידיים של בן/בת הזוג ועסה/י בעדינות כל אצבע וכף יד בתנועות רכות.",
      theme = CardTheme.SUNFLOWER
    ),
    CoupleCard(
      id = 10,
      type = CardType.SHARED,
      title = "טלפתיה זוגית בספירה ל-3",
      text = "משימה משותפת: בספירה ל-3 אמרו יחד בקול מאכל או מקום בילוי ששניכם אוהבים. המשימה מזכה את שניכם באבן טובה!",
      theme = CardTheme.PEACH
    ),
    CoupleCard(
      id = 11,
      type = CardType.INDIVIDUAL,
      title = "רגע שבו עזרת לי",
      text = "הזכר/י לבן/בת הזוג רגע אחד שבו הוא או היא עזרו לך להירגע, לחייך או להרגיש בטוח/ה.",
      theme = CardTheme.MINT
    ),
    CoupleCard(
      id = 12,
      type = CardType.INDIVIDUAL,
      title = "הבטחה לשבוע הקרוב",
      text = "התחייב/י על מחווה אחת מוחשית שתעשה/י השבוע כדי לשמח או להקל על בן/בת הזוג.",
      theme = CardTheme.LILAC
    ),
    CoupleCard(
      id = 13,
      type = CardType.SHARED,
      title = "ריקוד סלואו שקט",
      text = "משימה משותפת: קומו יחד, התחבקו והתנועעו בריקוד סלואו איטי ושקט למשך 45 שניות.",
      theme = CardTheme.SUNFLOWER
    ),
    CoupleCard(
      id = 14,
      type = CardType.INDIVIDUAL,
      title = "נשיקה במקום מפתיע",
      text = "הענק/י לבן/בת הזוג נשיקה עדינה במצח, בגב כף היד או בקצה האף בליווי משפט אוהב.",
      theme = CardTheme.PEACH
    ),
    CoupleCard(
      id = 15,
      type = CardType.INDIVIDUAL,
      title = "משהו שלמדתי ממך",
      text = "שתף/י דבר אחד או הרגל טוב שלמדת מבן/בת הזוג מאז שאתם יחד.",
      theme = CardTheme.MINT
    ),
    CoupleCard(
      id = 16,
      type = CardType.SHARED,
      title = "זיכרון מהדייט הראשון",
      text = "משימה משותפת: כל אחד מספר פרט אחד שהוא זוכר מהפגישות הראשונות שלכם יחד.",
      theme = CardTheme.LILAC
    ),
    CoupleCard(
      id = 17,
      type = CardType.INDIVIDUAL,
      title = "מגע מרגיע בשיער ובקרקפת",
      text = "העבר/י אצבעות עדינות בשיער או בקרקפת של בן/בת הזוג למשך חצי דקה של רוגע.",
      theme = CardTheme.SUNFLOWER
    ),
    CoupleCard(
      id = 18,
      type = CardType.INDIVIDUAL,
      title = "תודה שעוד לא אמרתי",
      text = "אמור/אמרי תודה מיוחדת לבן/בת הזוג על משהו שעשה/תה עבורך או עבור הבית לאחרונה.",
      theme = CardTheme.PEACH
    ),
    CoupleCard(
      id = 19,
      type = CardType.SHARED,
      title = "חופשה חלומית משותפת",
      text = "משימה משותפת: תארו יחד ב-60 שניות לאן הייתם טסים או נוסעים מחר בבוקר אם הכל היה פתוח ואפשרי.",
      theme = CardTheme.MINT
    ),
    CoupleCard(
      id = 20,
      type = CardType.INDIVIDUAL,
      title = "מה הכי מצחיק אותי אצלך",
      text = "ספר/י לבן/בת הזוג מה הדבר שהכי מצחיק או משמח אותך כשהוא/היא עושה.",
      theme = CardTheme.LILAC
    ),
    CoupleCard(
      id = 21,
      type = CardType.INDIVIDUAL,
      title = "הקשבה נקייה מהלב",
      text = "שאל/י את בן/בת הזוג 'איך עבר עליך השבוע באמת?' והקשב/י במשך דקה שלמה בלי לקטוע.",
      theme = CardTheme.SUNFLOWER
    ),
    CoupleCard(
      id = 22,
      type = CardType.SHARED,
      title = "סוד הכוח שלנו",
      text = "משימה משותפת: כל אחד אומר במשפט אחד מה לדעתו החוזקה הכי גדולה של הזוגיות שלכם.",
      theme = CardTheme.PEACH
    ),
    CoupleCard(
      id = 23,
      type = CardType.INDIVIDUAL,
      title = "מחמאה על המראה",
      text = "הבט/הביטי בעיניים של בן/בת הזוג והחמא/י לו/לה על דבר אחד במראה שלו/ה שאת/ה אוהב/ת במיוחד.",
      theme = CardTheme.MINT
    ),
    CoupleCard(
      id = 24,
      type = CardType.INDIVIDUAL,
      title = "חיקוי מתוק ומלא חיבה",
      text = "עשה/י חיקוי משעשע ומלא אהבה של משפט קבוע או תנועה מוכרת של בן/בת הזוג.",
      theme = CardTheme.LILAC
    ),
    CoupleCard(
      id = 25,
      type = CardType.SHARED,
      title = "הרגל זוגי חדש",
      text = "משימה משותפת: בחרו יחד הרגל זוגי קטן אחד (כמו קפה יחד בבוקר או הליכה שבועית) שתרצו לאמץ החודש.",
      theme = CardTheme.SUNFLOWER
    ),
    CoupleCard(
      id = 26,
      type = CardType.INDIVIDUAL,
      title = "הרגע שבו הלב נפתח",
      text = "שתף/י ברגע מסוים מהקשר שלכם שבו הרגשת קרבה ואהבה גדולה במיוחד.",
      theme = CardTheme.PEACH
    ),
    CoupleCard(
      id = 27,
      type = CardType.INDIVIDUAL,
      title = "פינוק כפות רגליים",
      text = "הענק/י לבן/בת הזוג עיסוי קצר ומשחרר בכפות הרגליים או בשוקיים.",
      theme = CardTheme.MINT
    ),
    CoupleCard(
      id = 28,
      type = CardType.SHARED,
      title = "נשיקת אסקימוסים",
      text = "משימה משותפת: התקרבו וחככו קצה אף בקצה אף בחיוך למשך 10 שניות.",
      theme = CardTheme.LILAC
    ),
    CoupleCard(
      id = 29,
      type = CardType.INDIVIDUAL,
      title = "איך לשמח אותי כשאני עייף/ה",
      text = "גלה/י לבן/בת הזוג מה הדבר הקטן שהכי עוזר לך להירגע כשעובר עליך יום עמוס.",
      theme = CardTheme.SUNFLOWER
    ),
    CoupleCard(
      id = 30,
      type = CardType.INDIVIDUAL,
      title = "גאווה זוגית",
      text = "ספר/י לבן/בת הזוג על הישג או התמודדות שלו/שלה מהזמן האחרון שגרמו לך להיות גאה בו/בה.",
      theme = CardTheme.PEACH
    ),
    CoupleCard(
      id = 31,
      type = CardType.SHARED,
      title = "מילה טובה לכל אצבע",
      text = "משימה משותפת: הצמידו כף יד לכף יד, ועל כל אצבע אמרו לסירוגין מילה טובה אחד לשנייה.",
      theme = CardTheme.MINT
    ),
    CoupleCard(
      id = 32,
      type = CardType.INDIVIDUAL,
      title = "שיר שמזכיר לי אותך",
      text = "ספר/י איזה שיר מזכיר לך את בן/בת הזוג או את התקופה הראשונה שלכם יחד, וזמזם/י שורה ממנו.",
      theme = CardTheme.LILAC
    ),
    CoupleCard(
      id = 33,
      type = CardType.INDIVIDUAL,
      title = "הנחת ראש והקשבה ללב",
      text = "הנח/י את הראש על הכתף או החזה של בן/בת הזוג למשך 30 שניות של שקט ורוגע.",
      theme = CardTheme.SUNFLOWER
    ),
    CoupleCard(
      id = 34,
      type = CardType.SHARED,
      title = "אנחנו בעוד 5 שנים",
      text = "משימה משותפת: כל אחד משתף בחלום אחד שהייתם רוצים להגשים יחד בחמש השנים הקרובות.",
      theme = CardTheme.PEACH
    ),
    CoupleCard(
      id = 35,
      type = CardType.INDIVIDUAL,
      title = "מגע נוצה בעיניים עצומות",
      text = "בן/בת הזוג עוצם/ת עיניים – לטף/י בעדינות את פניו/ה בקצות האצבעות למשך 20 שניות.",
      theme = CardTheme.MINT
    ),
    CoupleCard(
      id = 36,
      type = CardType.INDIVIDUAL,
      title = "מה הכי כיף לי לעשות איתך",
      text = "שתף/י מה הבילוי או הרגע הפשוט בשגרה שהכי כיף לך לחלוק עם בן/בת הזוג.",
      theme = CardTheme.LILAC
    ),
    CoupleCard(
      id = 37,
      type = CardType.SHARED,
      title = "פשוט להיפגש",
      text = "משימה משותפת: הביטו בעיניים, אמרו יחד 'איזה כיף שאנחנו פשוט נפגשים!' וחתמו בחיבוק חם.",
      theme = CardTheme.SUNFLOWER
    )
  )

  fun getAllCards(): List<CoupleCard> {
    val customCards = getCustomCards()
    return defaultCards + customCards
  }

  fun getCustomCards(): List<CoupleCard> {
    val json = prefs.getString("custom_cards", "[]") ?: "[]"
    val list = mutableListOf<CoupleCard>()
    try {
      val arr = JSONArray(json)
      for (i in 0 until arr.length()) {
        val obj = arr.getJSONObject(i)
        list.add(
          CoupleCard(
            id = obj.optLong("id", System.currentTimeMillis()),
            type = if (obj.optString("type") == "SHARED") CardType.SHARED else CardType.INDIVIDUAL,
            title = obj.optString("title", "קלף אישי"),
            text = obj.optString("text", ""),
            isCustom = true,
            theme = CardTheme.entries[obj.optInt("themeIndex", 0) % CardTheme.entries.size]
          )
        )
      }
    } catch (_: Exception) {}
    return list
  }

  fun saveCustomCard(card: CoupleCard) {
    val current = getCustomCards().toMutableList()
    current.add(card)
    val arr = JSONArray()
    current.forEach {
      val obj = JSONObject()
      obj.put("id", it.id)
      obj.put("type", it.type.name)
      obj.put("title", it.title)
      obj.put("text", it.text)
      obj.put("themeIndex", it.theme.index)
      arr.put(obj)
    }
    prefs.edit().putString("custom_cards", arr.toString()).apply()
  }

  fun loadSettings(): GameSettings {
    val seatingStr = prefs.getString("seating_mode", com.example.model.SeatingMode.SIDE_BY_SIDE.name)
    val seatingMode = try {
      com.example.model.SeatingMode.valueOf(seatingStr ?: com.example.model.SeatingMode.SIDE_BY_SIDE.name)
    } catch (_: Exception) {
      com.example.model.SeatingMode.SIDE_BY_SIDE
    }
    return GameSettings(
      player1Name = prefs.getString("p1_name", "שחקן 1") ?: "שחקן 1",
      player2Name = prefs.getString("p2_name", "שחקן 2") ?: "שחקן 2",
      withDice = prefs.getBoolean("with_dice", true),
      withStones = prefs.getBoolean("with_stones", true),
      soundEnabled = prefs.getBoolean("sound_enabled", true),
      seatingMode = seatingMode
    )
  }

  fun saveSettings(settings: GameSettings) {
    prefs.edit()
      .putString("p1_name", settings.player1Name)
      .putString("p2_name", settings.player2Name)
      .putBoolean("with_dice", settings.withDice)
      .putBoolean("with_stones", settings.withStones)
      .putBoolean("sound_enabled", settings.soundEnabled)
      .putString("seating_mode", settings.seatingMode.name)
      .apply()
  }

  fun loadSessionStones(): Triple<Int, List<GemColor>, List<GemColor>> {
    val bank = prefs.getInt("bank_stones", 20)
    val p1Raw = prefs.getString("p1_stones", "") ?: ""
    val p2Raw = prefs.getString("p2_stones", "") ?: ""

    val p1 = if (p1Raw.isEmpty()) emptyList() else p1Raw.split(",").mapNotNull { name ->
      try { GemColor.valueOf(name) } catch (_: Exception) { null }
    }
    val p2 = if (p2Raw.isEmpty()) emptyList() else p2Raw.split(",").mapNotNull { name ->
      try { GemColor.valueOf(name) } catch (_: Exception) { null }
    }

    return Triple(bank, p1, p2)
  }

  fun saveSessionStones(bank: Int, p1: List<GemColor>, p2: List<GemColor>) {
    prefs.edit()
      .putInt("bank_stones", bank)
      .putString("p1_stones", p1.joinToString(",") { it.name })
      .putString("p2_stones", p2.joinToString(",") { it.name })
      .apply()
  }
}

package com.noorlearn.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Clear
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.ui.text.font.FontStyle
import com.noorlearn.ui.theme.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

data class DuaItem(
    val title: String,
    val arabic: String,
    val transliteration: String,
    val translation: String,
    val reference: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(
    navController: NavController,
    initialTab: Int = 0,
    viewModel: ToolsViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableIntStateOf(initialTab) }

    LaunchedEffect(selectedTab) {
        if (selectedTab == 1 || selectedTab == 0) {
            viewModel.completeAdhkarTask()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BeigeBackground).gridBackground()
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(PrimaryGreen, PrimaryGreenDark)
                    )
                )
        ) {
            Text(
                text = "Islamic Essentials",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 20.dp),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )

            // Tab Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(CardWhite, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ToolTabItem(title = "Tasbeeh", icon = Icons.Filled.Handyman, isActive = selectedTab == 0) { selectedTab = 0 }
                ToolTabItem(title = "Duas", icon = Icons.AutoMirrored.Filled.MenuBook, isActive = selectedTab == 1) { selectedTab = 1 }
                ToolTabItem(title = "Qibla", icon = Icons.Filled.Explore, isActive = selectedTab == 2) { selectedTab = 2 }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 220.dp)
        ) {
            when (selectedTab) {
                0 -> TasbeehContent()
                1 -> DuasContent()
                2 -> QiblaContent()
            }
        }
    }
}

@Composable
fun TasbeehContent() {
    var totalCounter by remember { mutableIntStateOf(0) }

    val target = 33
    val cycleCount = totalCounter % (target * 3)
    val displayCounter = totalCounter % target

    val phaseTitle = when {
        cycleCount < target -> "SubhanAllah"
        cycleCount < target * 2 -> "Alhamdulillah"
        else -> "Allahu Akbar"
    }

    val phaseArabic = when {
        cycleCount < target -> "سُبْحَانَ اللَّهِ"
        cycleCount < target * 2 -> "ٱلْحَمْدُ لِلَّٰهِ"
        else -> "اللَّهُ أَكْبَرُ"
    }

    val phaseNumber = when {
        cycleCount < target -> 1
        cycleCount < target * 2 -> 2
        else -> 3
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Phase indicator
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .width(if (index + 1 == phaseNumber) 32.dp else 12.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (index + 1 == phaseNumber) PrimaryGreen else DividerLight)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = phaseTitle,
            style = MaterialTheme.typography.titleMedium,
            color = GrayText
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = phaseArabic,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontFamily = ArabicFontFamily,
                fontSize = 32.sp
            ),
            color = PrimaryGreen
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Counter Circle
        Box(
            modifier = Modifier
                .size(240.dp)
                .background(PrimaryGreen, shape = CircleShape)
                .clickable { totalCounter++ },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = displayCounter.toString(),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 80.sp
                ),
                color = Color.White
            )

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 12.dp),
                shape = RoundedCornerShape(16.dp),
                color = CardWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, DividerLight)
            ) {
                Text(
                    text = "/ $target",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium.copy(color = GrayText)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Total: $totalCounter",
            style = MaterialTheme.typography.bodySmall.copy(color = GrayText)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ControlCircleButton(icon = Icons.Filled.Remove) { if (totalCounter > 0) totalCounter-- }
            Spacer(modifier = Modifier.width(32.dp))
            ControlCircleButton(icon = Icons.Filled.Refresh) { totalCounter = 0 }
            Spacer(modifier = Modifier.width(32.dp))
            ControlCircleButton(icon = Icons.Filled.Add) { totalCounter++ }
        }
    }
}

@Composable
fun DuasContent() {
        val KAGGLE_DUAS = listOf(
    DuaItem("Waking Up", "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ", "Alhamdu lillahil-ladhi ahyana ba'da ma amatana wa ilayhin-nushur", "All praise is for Allah who gave us life after having taken it from us and unto Him is the resurrection.", "6312"),
    DuaItem("Morning Protection", "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ", "Asbahna wa asbahal-mulku lillah, walhamdu lillah", "We have entered a new morning and with it all dominion belongs to Allah, and all praise is for Allah.", "2723"),
    DuaItem("Sayyid ul Istighfar", "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ", "Allahumma anta Rabbi la ilaha illa ant, khalaqtani wa ana abduk", "O Allah, You are my Lord, none has the right to be worshipped except You, You created me and I am Your servant.", "6306"),
    DuaItem("Evening Remembrance", "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ", "Amsayna wa amsal-mulku lillah, walhamdu lillah", "We have entered the evening and with it all dominion belongs to Allah, and all praise is for Allah.", "2723"),
    DuaItem("Evening Protection", "اللَّهُمَّ بِكَ أَمْسَيْنَا، وَبِكَ أَصْبَحْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوتُ وَإِلَيْكَ الْمَصِيرُ", "Allahumma bika amsayna, wa bika asbahna, wa bika nahya, wa bika namutu wa ilaykal masir", "O Allah, by You we enter the evening and by You we enter the morning, by You we live and by You we die, and to You is the final return.", "5068"),
    DuaItem("Before Sleeping", "بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا", "Bismika Allahumma amutu wa ahya", "In Your name O Allah, I die and I live.", "6324"),
    DuaItem("Ayat ul Kursi Before Sleep", "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ", "Allahu la ilaha illa huwal hayyul qayyum", "Allah — there is no deity except Him, the Ever-Living, the Sustainer of existence.", "2311"),
    DuaItem("Sleeping on Right Side", "اللَّهُمَّ قِنِي عَذَابَكَ يَوْمَ تَبْعَثُ عِبَادَكَ", "Allahumma qini adhabaka yawma tab'athu ibadak", "O Allah, protect me from Your punishment on the day You resurrect Your servants.", "5045"),
    DuaItem("Before Eating", "بِسْمِ اللَّهِ", "Bismillah", "In the name of Allah.", "5376"),
    DuaItem("After Eating", "الْحَمْدُ لِلَّهِ الَّذِي أَطْعَمَنِي هَذَا وَرَزَقَنِيهِ مِنْ غَيْرِ حَوْلٍ مِنِّي وَلَا قُوَّةٍ", "Alhamdu lillahil-ladhi at'amani hadha wa razaqanihi min ghayri hawlin minni wa la quwwah", "All praise is for Allah who fed me this and provided it for me without any might or power from myself.", "4023"),
    DuaItem("Forgot Bismillah", "بِسْمِ اللَّهِ أَوَّلَهُ وَآخِرَهُ", "Bismillahi awwalahu wa akhirah", "In the name of Allah at its beginning and at its end.", "3767"),
    DuaItem("After Drinking Milk", "اللَّهُمَّ بَارِكْ لَنَا فِيهِ وَزِدْنَا مِنْهُ", "Allahumma barik lana fihi wa zidna minh", "O Allah, bless it for us and give us more of it.", "3730"),
    DuaItem("Going to Mosque", "اللَّهُمَّ اجْعَلْ فِي قَلْبِي نُوراً، وَفِي لِسَانِي نُوراً", "Allahumma-j'al fi qalbi nuran, wa fi lisani nuran", "O Allah, place light in my heart, and on my tongue light.", "6316"),
    DuaItem("Entering Mosque", "اللَّهُمَّ افْتَحْ لِي أَبْوَابَ رَحْمَتِكَ", "Allahummaf-tah li abwaba rahmatik", "O Allah, open the gates of Your mercy for me.", "713"),
    DuaItem("Leaving Mosque", "اللَّهُمَّ إِنِّي أَسْأَلُكَ مِنْ فَضْلِكَ", "Allahumma inni as'aluka min fadlik", "O Allah, I ask You from Your favor.", "713"),
    DuaItem("After Adhan", "اللَّهُمَّ رَبَّ هَذِهِ الدَّعْوَةِ التَّامَّةِ وَالصَّلَاةِ الْقَائِمَةِ آتِ مُحَمَّداً الْوَسِيلَةَ وَالْفَضِيلَةَ", "Allahumma Rabba hadhihid-da'watit-tammah, was-salatil-qa'imah, ati Muhammadanil wasilata wal-fadilah", "O Allah, Lord of this perfect call and established prayer, grant Muhammad the intercession and honor.", "614"),
    DuaItem("Riding a Vehicle", "سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ وَإِنَّا إِلَى رَبِّنَا لَمُنْقَلِبُونَ", "Subhanal-ladhi sakhkhara lana hadha wa ma kunna lahu muqrinin, wa inna ila rabbina lamunqalibun", "Glory be to Him Who has subjected this to us, and we could not have it by our efforts. And to our Lord we shall certainly return.", "2602"),
    DuaItem("Entering a Town", "اللَّهُمَّ بَارِكْ لَنَا فِيهَا", "Allahumma barik lana fiha", "O Allah, bless us in it.", "2600"),
    DuaItem("Returning from Travel", "آيِبُونَ تَائِبُونَ عَابِدُونَ لِرَبِّنَا حَامِدُونَ", "Ayibuna, ta'ibuna, 'abiduna, lirabbina hamidun", "We return, repent, worship and praise our Lord.", "3085"),
    DuaItem("Protection from Evil Eye", "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ", "A'udhu bikalimatillahit-tammati min sharri ma khalaq", "I seek refuge in the perfect words of Allah from the evil of what He has created.", "2708"),
    DuaItem("Against Shaitan", "أَعُوذُ بِاللَّهِ مِنَ الشَّيْطَانِ الرَّجِيمِ", "A'udhu billahi minash-shaytanir-rajim", "I seek refuge with Allah from the accursed devil.", "3282"),
    DuaItem("Morning Protection (3x)", "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ", "Bismillahil-ladhi la yadurru ma'asmihi shay'un fil-ardi wa la fis-sama'i wa huwas-sami'ul 'alim", "In the name of Allah with Whose name nothing can harm, in the earth or in the heavens, and He is the All-Hearing, All-Knowing.", "5088"),
    DuaItem("Istighfar", "أَسْتَغْفِرُ اللَّهَ", "Astaghfirullah", "I seek the forgiveness of Allah.", "6307"),
    DuaItem("Complete Istighfar", "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ الَّذِي لَا إِلَهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ وَأَتُوبُ إِلَيْهِ", "Astaghfirullahil-'Azim alladhi la ilaha illa huwal Hayyul Qayyumu wa atubu ilyh", "I seek forgiveness from Allah, the Magnificent, besides Whom there is none worthy of worship, the Ever-Living, the Sustainer, and I repent to Him.", "1517"),
    DuaItem("Dua for Anxiety", "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَالْعَجْزِ وَالْكَسَلِ، وَالْبُخْلِ وَالْجُبْنِ، وَضَلَعِ الدَّيْنِ وَغَلَبَةِ الرِّجَالِ", "Allahumma inni a'udhu bika minal-hammi wal-hazan, wal-'ajzi wal-kasal, wal-bukhli wal-jubn, wa dhala'id-dayni wa ghalabatir-rijal", "O Allah, I seek refuge in You from grief and sadness, from weakness and from laziness, from miserliness and from cowardice, from being overcome by debt and overpowered by men.", "2893"),
    DuaItem("Dua of Prophet Yunus", "لَا إِلَهَ إِلَّا أَنْتَ سُبْحَانَكَ إِنِّي كُنْتُ مِنَ الظَّالِمِينَ", "La ilaha illa anta subhanaka inni kuntu minaz-zalimin", "There is no deity except You; exalted are You. Indeed, I have been of the wrongdoers.", "Al-Anbiya:87"),
    DuaItem("Hasbunallah", "حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ", "Hasbunallahu wa ni'mal wakil", "Allah is sufficient for us and He is the best disposer of affairs.", "Al-Imran:173"),
    DuaItem("Dua for Parents", "رَّبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا", "Rabbir-hamhuma kama rabbayani saghira", "My Lord, have mercy upon them as they brought me up when I was small.", "Al-Isra:24"),
    DuaItem("Dua for Righteous Children", "رَبِّ هَبْ لِي مِنَ الصَّالِحِينَ", "Rabbi hab li minas-salihin", "My Lord, grant me from among the righteous.", "As-Saffat:100"),
    DuaItem("Dua for Family", "رَبَّنَا هَبْ لَنَا مِنْ أَزْوَاجِنَا وَذُرِّيَّاتِنَا قُرَّةَ أَعْيُنٍ وَاجْعَلْنَا لِلْمُتَّقِينَ إِمَامًا", "Rabbana hab lana min azwajina wa dhurriyyatina qurrata a'yunin waj'alna lil-muttaqina imama", "Our Lord, grant us from among our wives and offspring comfort to our eyes and make us a leader for the righteous.", "Al-Furqan:74"),
    DuaItem("Dua for Rain", "اللَّهُمَّ اسْقِنَا غَيْثاً مُغِيثاً مَرِيئاً مَرِيعاً نَافِعاً غَيْرَ ضَارٍّ عَاجِلاً غَيْرَ آجِلٍ", "Allahummas-qina ghaythan mughithan mari'an mari'an nafi'an ghayra darrin 'ajilan ghayra ajil", "O Allah, shower upon us abundant rain, beneficial not harmful, swiftly and not delayed.", "1169"),
    DuaItem("When it Rains", "اللَّهُمَّ صَيِّباً نَافِعاً", "Allahumma sayyiban nafi'a", "O Allah, may it be a beneficial rain.", "1032"),
    DuaItem("When Hearing Thunder", "سُبْحَانَ الَّذِي يُسَبِّحُ الرَّعْدُ بِحَمْدِهِ", "Subhanal-ladhi yusabbihur-ra'du bihamdih", "Glory be to Him Whom the thunder glorifies with His praise.", "2:992"),
    DuaItem("Dua for Healing", "اللَّهُمَّ رَبَّ النَّاسِ، أَذْهِبِ الْبَأْسَ، اشْفِهِ وَأَنْتَ الشَّافِي، لَا شِفَاءَ إِلَّا شِفَاؤُكَ، شِفَاءً لَا يُغَادِرُ سَقَماً", "Allahumma Rabban-nas, adh-hibil-ba's, washfihi wa Antash-Shafi, la shifa'a illa shifa'uk, shifa'an la yughadiru saqama", "O Allah, Lord of mankind, remove the harm and heal him, for You are the Healer. There is no healing except Your healing, a healing that leaves no illness.", "5742"),
    DuaItem("Ruqyah on Oneself", "بِسْمِ اللَّهِ أَرْقِيكَ، مِنْ كُلِّ شَيْءٍ يُؤْذِيكَ، مِنْ شَرِّ كُلِّ نَفْسٍ أَوْ عَيْنٍ حَاسِدٍ، اللَّهُ يَشْفِيكَ", "Bismillahi arqik, min kulli shay'in yu'dhik, min sharri kulli nafsin aw 'aynin hasid, Allahu yashfik", "In the name of Allah I perform ruqyah on you, from everything that harms you, from the evil of every soul or envious eye, may Allah cure you.", "2186"),
    DuaItem("Entering Home", "اللَّهُمَّ إِنِّي أَسْأَلُكَ خَيْرَ الْمَوْلِجِ وَخَيْرَ الْمَخْرَجِ، بِسْمِ اللَّهِ وَلَجْنَا، وَبِسْمِ اللَّهِ خَرَجْنَا، وَعَلَى اللَّهِ رَبِّنَا تَوَكَّلْنَا", "Allahumma inni as'aluka khayral-mawliji wa khayral-makhraj, bismillahi walajna, wa bismillahi kharajna, wa 'alallahi rabbina tawakkalna", "O Allah, I ask You for the blessing of entering and leaving. In the name of Allah we enter, in the name of Allah we leave, and upon Allah our Lord we rely.", "5096"),
    DuaItem("Leaving Home", "بِسْمِ اللَّهِ، تَوَكَّلْتُ عَلَى اللَّهِ، وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ", "Bismillah, tawakkaltu 'alallah, wa la hawla wa la quwwata illa billah", "In the name of Allah, I place my trust in Allah, and there is no power or strength except with Allah.", "5095"),
    DuaItem("Entering Toilet", "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْخُبُثِ وَالْخَبَائِثِ", "Allahumma inni a'udhu bika minal-khubuthi wal-khaba'ith", "O Allah, I seek refuge with You from male and female evil spirits.", "142"),
    DuaItem("Leaving Toilet", "غُفْرَانَكَ", "Ghufranaka", "I seek Your forgiveness.", "30"),
    DuaItem("Entering Market", "لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ يُحْيِي وَيُمِيتُ وَهُوَ حَيٌّ لَا يَمُوتُ، بِيَدِهِ الْخَيْرُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ", "La ilaha illallahu wahdahu la sharika lah, lahul-mulku wa lahul-hamd, yuhyi wa yumitu wa huwa hayyun la yamut, biyadihil-khayr, wa huwa 'ala kulli shay'in qadir", "None has the right to be worshipped but Allah alone, Who has no partner. His is the dominion and His is the praise; He brings life and He causes death; He is Ever-Living and does not die; in His Hand is all good, and He is over all things Omnipotent.", "2235"),
    DuaItem("Dua for Knowledge", "رَّبِّ زِدْنِي عِلْمًا", "Rabbi zidni 'ilma", "My Lord, increase me in knowledge.", "Ta-Ha:114"),
    DuaItem("Before Studying", "اللَّهُمَّ انْفَعْنِي بِمَا عَلَّمْتَنِي وَعَلِّمْنِي مَا يَنْفَعُنِي وَزِدْنِي عِلْماً", "Allahumman-fa'ni bima 'allamtani wa 'allimni ma yanfa'uni wa zidni 'ilma", "O Allah, benefit me with what You have taught me, teach me that which will benefit me, and increase me in knowledge.", "251"),
    DuaItem("Alhamdulillah", "الْحَمْدُ لِلَّهِ", "Alhamdulillah", "All praise and thanks are for Allah.", "Al-Fatiha:2"),
    DuaItem("Reply to Sneeze", "يَرْحَمُكَ اللَّهُ", "Yarhamukallah", "May Allah have mercy upon you.", "6224"),
    DuaItem("SubhanAllah", "سُبْحَانَ اللَّهِ", "SubhanAllah", "Glory be to Allah.", "6406"),
    DuaItem("Allahu Akbar", "اللَّهُ أَكْبَرُ", "Allahu Akbar", "Allah is the Greatest.", "2992"),
    DuaItem("La ilaha illallah", "لَا إِلَهَ إِلَّا اللَّهُ", "La ilaha illallah", "There is no deity worthy of worship except Allah.", "6403"),
    DuaItem("Inna Lillahi", "إِنَّا لِلَّهِ وَإِنَّا إِلَيْهِ رَاجِعُونَ", "Inna lillahi wa inna ilayhi raji'un", "Indeed, to Allah we belong and to Allah we shall return.", "Al-Baqarah:156"),
    DuaItem("Mashallah", "مَا شَاءَ اللَّهُ", "Masha'Allah", "What Allah has willed.", "Al-Kahf:39"),
    DuaItem("Inshallah", "إِنْ شَاءَ اللَّهُ", "In sha'Allah", "If Allah wills.", "Al-Kahf:24"),
    DuaItem("Ayatul Kursi", "", "", "", ""),
    DuaItem("After Wudu", "أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ", "Ash-hadu an la ilaha illallahu wahdahu la sharika lah, wa ash-hadu anna Muhammadan abduhu wa rasuluh", "I bear witness that none has the right to be worshipped but Allah alone, Who has no partner; and I bear witness that Muhammad is His slave and His Messenger.", "234"),
    DuaItem("Intention for Fasting", "وَبِصَوْمِ غَدٍ نَوَيْتُ مِنْ شَهْرِ رَمَضَانَ", "Wa bisawmi ghadin nawaitu min shahri Ramadan", "I intend to fast tomorrow in the month of Ramadan.", "2454"),
    DuaItem("Breaking Fast (Iftar)", "اللَّهُمَّ لَكَ صُمْتُ، وَعَلَى رِزْقِكَ أَفْطَرْتُ", "Allahumma laka sumtu wa 'ala rizqika aftartu", "O Allah, for You I have fasted and upon Your provision I have broken my fast.", "2358"),
    DuaItem("Seeing the New Moon", "اللَّهُ أَكْبَرُ، اللَّهُمَّ أَهِلَّهُ عَلَيْنَا بِالْأَمْنِ وَالْإِيمَانِ، وَالسَّلَامَةِ وَالْإِسْلَامِ، وَالتَّوْفِيقِ لِمَا تُحِبُّ وَتَرْضَى", "Allahu Akbar, Allahumma ahillahu 'alayna bil-amni wal-iman, was-salamati wal-Islam, wat-tawfiqi lima tuhibbu wa tarda", "Allah is the Greatest. O Allah, let this moon appear on us with security and faith, with peace and Islam, and with guidance to what You love and pleases You.", "3451"),
    DuaItem("After Reading Quran", "سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ، لَا إِلَهَ إِلَّا أَنْتَ، أَسْتَغْفِرُكَ وَأَتُوبُ إِلَيْكَ", "Subhanakallahumma wa bihamdika, la ilaha illa anta, astaghfiruka wa atubu ilayk", "Glory and praise be to You, O Allah. There is none worthy of worship but You. I seek Your forgiveness and repent to You.", "1500"),
    DuaItem("Dua for Newlyweds", "بَارَكَ اللَّهُ لَكَ وَبَارَكَ عَلَيْكَ وَجَمَعَ بَيْنَكُمَا فِي خَيْرٍ", "Barakallahu laka wa baraka 'alayka wa jama'a baynakuma fi khayr", "May Allah bless you and send blessings upon you, and may He unite you both in goodness.", "2130"),
    DuaItem("Before Intimacy", "بِسْمِ اللَّهِ، اللَّهُمَّ جَنِّبْنَا الشَّيْطَانَ، وَجَنِّبِ الشَّيْطَانَ مَا رَزَقْتَنَا", "Bismillah, Allahumma jannibnas-shaytan, wa jannibishaytana ma razaqtana", "In the name of Allah. O Allah, keep us away from the devil and keep the devil away from what You bestow upon us.", "141"),
    DuaItem("Upon Hearing of Death", "إِنَّا لِلَّهِ وَإِنَّا إِلَيْهِ رَاجِعُونَ، اللَّهُمَّ أْجُرْنِي فِي مُصِيبَتِي وَأَخْلِفْ لِي خَيْرًا مِنْهَا", "Inna lillahi wa inna ilayhi raji'un, Allahumma'-jurni fi musibati wa akhlif li khayran minha", "Truly, to Allah we belong and truly, to Him we shall return. O Allah, reward me for my affliction and replace it for me with something better.", "918"),
    DuaItem("Dua for the Deceased", "اللَّهُمَّ اغْفِرْ لَهُ وَارْحَمْهُ وَعَافِهِ وَاعْفُ عَنْهُ", "Allahummaghfir lahu warhamhu wa 'afihi wa'fu 'anh", "O Allah, forgive him and have mercy on him, keep him safe and sound and pardon him.", "963"),
    DuaItem("At the Graveyard", "السَّلَامُ عَلَيْكُمْ أَهْلَ الدِّيَارِ مِنَ الْمُؤْمِنِينَ وَالْمُسْلِمِينَ، وَإِنَّا إِنْ شَاءَ اللَّهُ بِكُمْ لَاحِقُونَ", "As-salamu 'alaykum ahl ad-diyari minal mu'minina wal muslimin, wa inna in sha'Allahu bikum lahiqun", "Peace be upon you, O dwellers of this place from among the believers and Muslims. And indeed, Allah willing, we will follow you.", "975"),
    DuaItem("Istikhara Dua", "اللَّهُمَّ إِنِّي أَسْتَخِيرُكَ بِعِلْمِكَ، وَأَسْتَقْدِرُكَ بِقُدْرَتِكَ، وَأَسْأَلُكَ مِنْ فَضْلِكَ الْعَظِيمِ", "Allahumma inni astakhiruka bi'ilmika, wa astaqdiruka biqudratika, wa as'aluka min fadlikal-'azim", "O Allah, I seek Your guidance by virtue of Your knowledge, and I seek ability by virtue of Your power, and I ask You of Your great bounty.", "1166"),
    DuaItem("Dua for Deceased Parents", "رَبِّ اغْفِرْ لِي وَلِوَالِدَيَّ", "Rabbighfir li wa li walidayya", "My Lord, forgive me and my parents.", "Nuh:28"),
    DuaItem("Dua for Barakah in Rizq", "اللَّهُمَّ بَارِكْ لَنَا فِيمَا رَزَقْتَنَا وَقِنَا عَذَابَ النَّارِ", "Allahumma barik lana fima razaqtana wa qina 'adhaban-nar", "O Allah, bless us in what You have provided us and protect us from the punishment of the Fire.", "3730"),
    DuaItem("Dua to Repay Debt", "اللَّهُمَّ اكْفِنِي بِحَلَالِكَ عَنْ حَرَامِكَ، وَأَغْنِنِي بِفَضْلِكَ عَمَّنْ سِوَاكَ", "Allahummak-fini bihalaalika 'an haraamika, wa aghnini bifadlika 'amman siwak", "O Allah, suffice me with what You have allowed instead of what You have forbidden, and make me independent of all others besides You.", "3563"),
    DuaItem("Dua for Jannah", "اللَّهُمَّ إِنِّي أَسْأَلُكَ الْجَنَّةَ وَأَعُوذُ بِكَ مِنَ النَّارِ", "Allahumma inni as'alukal-jannah wa a'udhu bika minan-nar", "O Allah, I ask You for Paradise and I seek refuge in You from the Fire.", "792"),
    DuaItem("Dua for Good in Both Worlds", "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ", "Rabbana atina fid-dunya hasanatan wa fil-akhirati hasanatan wa qina 'adhaban-nar", "Our Lord, give us in this world that which is good and in the Hereafter that which is good, and save us from the torment of the Fire.", "Al-Baqarah:201"),
    DuaItem("For New Baby", "بَارَكَ اللَّهُ لَكَ فِي الْمَوْهُوبِ لَكَ، وَشَكَرْتَ الْوَاهِبَ، وَبَلَغَ أَشُدَّهُ، وَرُزِقْتَ بِرَّهُ", "Barakallahu laka fil-mawhubi lak, wa shakarta al-wahib, wa balagha ashuddahu, wa ruziqta birrah", "May Allah bless you with His gift to you, may you give thanks to the Giver, may the child reach maturity, and may you be granted its righteousness.", "177"),
    DuaItem("Salawat on Prophet ﷺ", "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ", "Allahumma salli 'ala Muhammadin wa 'ala ali Muhammad", "O Allah, send prayers upon Muhammad and upon the family of Muhammad.", "3370"),
    DuaItem("Dua in Last Hour of Friday", "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي", "Allahumma innaka 'afuwwun tuhibbul-'afwa fa'fu 'anni", "O Allah, You are Forgiving and love forgiveness, so forgive me.", "3850"),
    DuaItem("Complete Tawbah", "رَبَّنَا ظَلَمْنَا أَنفُسَنَا وَإِن لَّمْ تَغْفِرْ لَنَا وَتَرْحَمْنَا لَنَكُونَنَّ مِنَ الْخَاسِرِينَ", "Rabbana zalamna anfusana wa in lam taghfir lana wa tarhamna lanakunanna minal-khasirin", "Our Lord, we have wronged ourselves, and if You do not forgive us and have mercy upon us, we will surely be among the losers.", "Al-A'raf:23"),
    DuaItem("Night of Power Dua", "اللَّهُمَّ إِنَّكَ عَفُوٌّ كَرِيمٌ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي", "Allahumma innaka 'afuwwun karimun tuhibbul-'afwa fa'fu 'anni", "O Allah, You are Pardoning and Generous; You love to pardon, so pardon me.", "3513")
    )

    val duas = KAGGLE_DUAS


    var searchQuery by remember { mutableStateOf("") }
    
    val filteredDuas = if (searchQuery.isBlank()) {
        duas
    } else {
        val lowerCaseQuery = searchQuery.lowercase()
        duas.filter {
            it.title.lowercase().contains(lowerCaseQuery) ||
            it.arabic.contains(searchQuery) ||
            it.translation.lowercase().contains(lowerCaseQuery)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            placeholder = { Text("Search Duas...", color = GrayText) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = PrimaryGreen) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = GrayText)
                    }
                }
            },
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CardWhite,
                unfocusedContainerColor = CardWhite,
                focusedBorderColor = PrimaryGreen,
                unfocusedBorderColor = DividerLight
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
        )

        if (filteredDuas.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No Duas found for '$searchQuery'", color = GrayText)
            }
        } else {
            LazyColumnScope(filteredDuas)
        }
    }
}

@Composable
private fun LazyColumnScope(duas: List<DuaItem>) {
    androidx.compose.foundation.lazy.LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(duas.size) { index ->
            val item = duas[index]
            var isExpanded by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = PrimaryGreen,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = "Expand",
                            tint = GrayText
                        )
                    }

                    AnimatedVisibility(visible = isExpanded) {
                        Column {
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Arabic Text - Large, Right Aligned
                            if (item.arabic.isNotEmpty()) {
                                Text(
                                    text = item.arabic,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontFamily = ArabicFontFamily,
                                        lineHeight = 42.sp,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = DarkText,
                                    textAlign = TextAlign.Right,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                            
                            HorizontalDivider(color = DividerLight.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(12.dp))

                            // Transliteration - Italic
                            if (item.transliteration.isNotEmpty()) {
                                Text(
                                    text = item.transliteration,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontStyle = FontStyle.Italic,
                                        lineHeight = 24.sp
                                    ),
                                    color = DarkText.copy(alpha = 0.8f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            // English Translation
                            if (item.translation.isNotEmpty()) {
                                Text(
                                    text = item.translation,
                                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                                    color = GrayText
                                )
                            }

                            if (item.reference.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Ref: ${item.reference}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GrayText.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QiblaContent() {
    val context = LocalContext.current
    var azimuth by remember { mutableFloatStateOf(0f) }
    
    // Low pass filter variables to smooth out sensor jitter
    val alpha = 0.05f
    var smoothedAzimuth by remember { mutableFloatStateOf(0f) }

    // Kaaba coordinates
    val kaabaLat = Math.toRadians(21.4225)
    val kaabaLon = Math.toRadians(39.8262)

    // Dynamic user location
    var userLat by remember { mutableDoubleStateOf(Math.toRadians(17.385)) } // Default to Hyderabad
    var userLon by remember { mutableDoubleStateOf(Math.toRadians(78.4867)) }
    var locationFetched by remember { mutableStateOf(false) }

    // Request Location Permission
    val launcher = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val fusedLocationClient = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(context)
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        userLat = Math.toRadians(location.latitude)
                        userLon = Math.toRadians(location.longitude)
                        locationFetched = true
                    }
                }
            } catch (e: SecurityException) {
                // Ignore, permission was checked
            }
        }
    }

    LaunchedEffect(Unit) {
        val permissionStatus = androidx.core.content.ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permissionStatus == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            val fusedLocationClient = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(context)
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        userLat = Math.toRadians(location.latitude)
                        userLon = Math.toRadians(location.longitude)
                        locationFetched = true
                    }
                }
            } catch (e: SecurityException) { }
        } else {
            launcher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Calculate Qibla direction based on (dynamic) user location
    val qiblaBearing = remember(userLat, userLon) {
        val dLon = kaabaLon - userLon
        val y = kotlin.math.sin(dLon) * kotlin.math.cos(kaabaLat)
        val x = kotlin.math.cos(userLat) * kotlin.math.sin(kaabaLat) - kotlin.math.sin(userLat) * kotlin.math.cos(kaabaLat) * kotlin.math.cos(dLon)
        val bearing = Math.toDegrees(atan2(y, x)).toFloat()
        (bearing + 360) % 360
    }

    // Sensor-based compass
    DisposableEffect(context) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        var gravity: FloatArray? = null
        var geomagnetic: FloatArray? = null

        val sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> gravity = event.values.clone()
                    Sensor.TYPE_MAGNETIC_FIELD -> geomagnetic = event.values.clone()
                }
                val g = gravity ?: return
                val m = geomagnetic ?: return

                val r = FloatArray(9)
                val i = FloatArray(9)
                if (SensorManager.getRotationMatrix(r, i, g, m)) {
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(r, orientation)
                    val currentAzimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                    
                    // Apply low-pass filter for smooth needle movement
                    // Handle wrap-around at 360 degrees
                    var diff = currentAzimuth - smoothedAzimuth
                    if (diff < -180f) diff += 360f
                    if (diff > 180f) diff -= 360f
                        
                    smoothedAzimuth += alpha * diff
                    // Normalize to 0-360
                    if (smoothedAzimuth < 0) smoothedAzimuth += 360f
                    if (smoothedAzimuth >= 360f) smoothedAzimuth -= 360f
                    
                    azimuth = smoothedAzimuth
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        accelerometerSensor?.let {
            sensorManager.registerListener(sensorListener, it, SensorManager.SENSOR_DELAY_UI)
        }
        magnetometerSensor?.let {
            sensorManager.registerListener(sensorListener, it, SensorManager.SENSOR_DELAY_UI)
        }

        onDispose {
            sensorManager.unregisterListener(sensorListener)
        }
    }

    val needleRotation = qiblaBearing - azimuth
    val animatedRotation by animateFloatAsState(
        targetValue = needleRotation,
        animationSpec = tween(durationMillis = 300),
        label = "compass_rotation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Qibla Direction",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = DarkText
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (locationFetched) "Using Live GPS accurate direction" else "Point the arrow toward the Qibla",
            style = MaterialTheme.typography.bodyMedium,
            color = if (locationFetched) PrimaryGreen else GrayText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Compass circle - Premium styling
        Box(
            modifier = Modifier
                .size(280.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(LightGreen, CardWhite),
                        radius = 400f
                    ), 
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // Inner decorative ring
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .clip(CircleShape)
                    .background(Color.Transparent)
            ) {
                androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = PrimaryGreen.copy(alpha = 0.2f),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                    )
                }
            }
            // Outer ring markers
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .background(Color.Transparent)
            ) {
                // N marker - North indicator style
                Text("N", modifier = Modifier.align(Alignment.TopCenter).padding(top = 10.dp),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = PrimaryGreen)
                // S marker
                Text("S", modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 10.dp),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = GrayText)
                // E marker
                Text("E", modifier = Modifier.align(Alignment.CenterEnd).padding(end = 10.dp),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = GrayText)
                // W marker
                Text("W", modifier = Modifier.align(Alignment.CenterStart).padding(start = 10.dp),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = GrayText)
            }

            // Rotating compass rose and needle to point to Qibla
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .rotate(animatedRotation)
                    .align(Alignment.Center)
            ) {
                // Outer circle of the pointer
                androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = PrimaryGreen,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx())
                    )
                    drawCircle(
                        color = PrimaryGreen.copy(alpha = 0.1f)
                    )
                }
                
                // Kaaba Icon pointer
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Icon(
                        Icons.Filled.Navigation,
                        contentDescription = "Qibla Direction",
                        tint = PrimaryGreen,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Bearing info card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Qibla Bearing",
                    style = MaterialTheme.typography.labelLarge,
                    color = GrayText
                )
                Text(
                    text = "${qiblaBearing.toInt()}°",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = PrimaryGreen
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "🕋 Makkah Al-Mukarramah",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrayText
                )
            }
        }
    }
}

@Composable
fun ToolTabItem(title: String, icon: ImageVector, isActive: Boolean, onClick: () -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isActive) PrimaryGreen else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isActive) Color.White else GrayText,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            color = if (isActive) Color.White else GrayText,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
fun ControlCircleButton(icon: ImageVector, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .size(56.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = CardWhite,
        border = androidx.compose.foundation.BorderStroke(1.dp, DividerLight)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = GrayText)
        }
    }
}

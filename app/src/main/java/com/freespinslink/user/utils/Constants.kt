package com.freespinslink.user.utils

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.freespinslink.user.BuildConfig
import com.freespinslink.user.enums.EnumGames


object Constants : GamesPackage() {

    const val PLAY_STORE_BASE_URL = "https://play.google.com/store/apps/details?id="
    private const val BASE_URL_PRIVACY_POLICY = "https://sites.google.com/view/maxopgames/"
    const val PAGE_SIZE = 30
    const val PREFETCH_DISTANCE = 5
    const val SUPPORT_EMAIL = "dailyfreespinapps@gmail.com"

    const val BUILD_FLAVOUR = BuildConfig.FLAVOR // format -> Dice_Dreams

    private val ppMap = HashMap<String, String>()
    private val gamePackageMap = HashMap<String, String>()

    val getPPUrl get() = ppMap[BUILD_FLAVOUR]
    private val getGamePackage get() = gamePackageMap[BUILD_FLAVOUR]
    val getGameName
        get() = ppMap.filter { it.key == BUILD_FLAVOUR }
            .keys.elementAtOrNull(0)
            ?.replace("_", " ")


    fun setupPPUrl() {
        ppMap[EnumGames.COIN_DRAGON.value] = "${BASE_URL_PRIVACY_POLICY}cd-rewards"
        ppMap[EnumGames.COIN_MASTER.value] = "${BASE_URL_PRIVACY_POLICY}cm-rewardz"
        ppMap[EnumGames.COIN_TALES.value] = "${BASE_URL_PRIVACY_POLICY}ct-rewardz"
        ppMap[EnumGames.CRAZY_FOX.value] = "${BASE_URL_PRIVACY_POLICY}cf-rewardz"
        ppMap[EnumGames.DICE_DREAMS.value] = "${BASE_URL_PRIVACY_POLICY}dd-rewardz"
        ppMap[EnumGames.MATCH_MASTER.value] = "${BASE_URL_PRIVACY_POLICY}mm-rewardz"
        ppMap[EnumGames.PET_MASTER.value] = "${BASE_URL_PRIVACY_POLICY}pm-rewardz"
        ppMap[EnumGames.PIGGY_GO.value] = "${BASE_URL_PRIVACY_POLICY}pg-rewardz"
        ppMap[EnumGames.FAMILY_ISLAND.value] = "${BASE_URL_PRIVACY_POLICY}fi-rewards"
        ppMap[EnumGames.MAFIA_MASTER.value] = "${BASE_URL_PRIVACY_POLICY}mm-rewards"
    }

    fun getGame(): String = BUILD_FLAVOUR.replace("_", " ")

    fun setupGamePackage() {
        gamePackageMap[EnumGames.COIN_DRAGON.value] = COIN_DRAGON
        gamePackageMap[EnumGames.COIN_MASTER.value] = COIN_MASTER
        gamePackageMap[EnumGames.COIN_TALES.value] = COIN_TALES
        gamePackageMap[EnumGames.CRAZY_FOX.value] = CRAZY_FOX
        gamePackageMap[EnumGames.DICE_DREAMS.value] = DICE_DREAMS
        gamePackageMap[EnumGames.MATCH_MASTER.value] = MATCH_MASTER
        gamePackageMap[EnumGames.PET_MASTER.value] = PET_MASTER
        gamePackageMap[EnumGames.PIGGY_GO.value] = PIGGY_GO
        gamePackageMap[EnumGames.FAMILY_ISLAND.value] = FAMILY_ISLAND
        gamePackageMap[EnumGames.MAFIA_MASTER.value] = MAFIA_MASTER
    }

    fun openGame(context: Context) {
        (PLAY_STORE_BASE_URL + getGamePackage).openInBrowser(context)
    }

    fun isAppInstalled(context: Context): Boolean {
        if (isAllowed()) {
            val pm: PackageManager = context.packageManager
            try {
                pm.getPackageInfo(getGamePackage ?: "", PackageManager.GET_ACTIVITIES)
                return true
            } catch (e: PackageManager.NameNotFoundException) {
                Log.d("GAME_INSTALL", "isAppInstalled: ${e.localizedMessage}")
            }
            return false
        }
        return true
    }

    private fun isAllowed(): Boolean = when (BUILD_FLAVOUR) {
        EnumGames.DICE_DREAMS.value -> true
        EnumGames.MAFIA_MASTER.value -> true
        EnumGames.CRAZY_FOX.value -> true
        EnumGames.FAMILY_ISLAND.value -> true
        EnumGames.MATCH_MASTER.value -> true
        else -> false
    }

}
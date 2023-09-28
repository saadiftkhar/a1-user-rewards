package com.freespinslink.user.ads.unity

import com.freespinslink.user.enums.EnumGames
import com.freespinslink.user.utils.Constants

open class AdsConfig {

    private val appKeyMap = HashMap<String, String>()
    val appKey get() = appKeyMap[Constants.BUILD_FLAVOUR]
    val demoAppKey = "85460dcd"
    protected val bannerPlacement = "DefaultBanner"
    protected val intPlacement1 = "DefaultInterstitial"
    protected val intPlacement2 = "Game_Screen"
    protected val isTestMode = false

    init {
        setupAdsIds()
    }

    private fun setupAdsIds() {
        appKeyMap[EnumGames.COIN_DRAGON.value] = ""
        appKeyMap[EnumGames.COIN_MASTER.value] = ""
        appKeyMap[EnumGames.COIN_TALES.value] = ""
        appKeyMap[EnumGames.CRAZY_FOX.value] = ""
        appKeyMap[EnumGames.DICE_DREAMS.value] = ""
        appKeyMap[EnumGames.MATCH_MASTER.value] = "1bcf0757d"
        appKeyMap[EnumGames.PET_MASTER.value] = ""
        appKeyMap[EnumGames.PIGGY_GO.value] = ""
        appKeyMap[EnumGames.FAMILY_ISLAND.value] = ""
        appKeyMap[EnumGames.MAFIA_MASTER.value] = ""
    }

}
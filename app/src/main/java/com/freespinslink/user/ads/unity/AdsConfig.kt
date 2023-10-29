package com.freespinslink.user.ads.unity

import com.freespinslink.user.enums.EnumGames
import com.freespinslink.user.utils.Constants
object AdsConfig {

    private val appKeyMap = HashMap<String, String>()
    val appKey get() = appKeyMap[Constants.BUILD_FLAVOUR]
    val demoAppKey = "85460dcd"
    val bannerPlacement = "DefaultBanner"
    val intPlacement = "DefaultInterstitial"

    init {
        setupAdsIds()
    }

    private fun setupAdsIds() {
        appKeyMap[EnumGames.COIN_DRAGON.value] = ""
        appKeyMap[EnumGames.COIN_MASTER.value] = "1c2231805"
        appKeyMap[EnumGames.COIN_TALES.value] = "1c3e7ed7d"
        appKeyMap[EnumGames.CRAZY_FOX.value] = "1c1516b5d"
        appKeyMap[EnumGames.DICE_DREAMS.value] = ""
        appKeyMap[EnumGames.MATCH_MASTER.value] = "1bcf0757d"
        appKeyMap[EnumGames.PET_MASTER.value] = ""
        appKeyMap[EnumGames.PIGGY_GO.value] = "1bae2965d"
        appKeyMap[EnumGames.FAMILY_ISLAND.value] = ""
        appKeyMap[EnumGames.MAFIA_MASTER.value] = ""
    }

}
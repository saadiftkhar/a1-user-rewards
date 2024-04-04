package com.freespinslink.user.ads.applovinUnityMediation

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
        appKeyMap[EnumGames.COIN_DRAGON.value] = "1c3f5dc6d"
        appKeyMap[EnumGames.COIN_MASTER.value] = "1c3f5a2e5"
        appKeyMap[EnumGames.COIN_TALES.value] = "1c3e7ed7d"
        appKeyMap[EnumGames.CRAZY_FOX.value] = "1c3f2f085"
        appKeyMap[EnumGames.DICE_DREAMS.value] = "1c3f2b6fd"
        appKeyMap[EnumGames.MATCH_MASTER.value] = "1bcf0757d"
        appKeyMap[EnumGames.PET_MASTER.value] = "1c3f615f5"
        appKeyMap[EnumGames.PIGGY_GO.value] = "1bae2965d"
        appKeyMap[EnumGames.FAMILY_ISLAND.value] = "1c3f1d0dd"
        appKeyMap[EnumGames.MAFIA_MASTER.value] = ""
    }

}
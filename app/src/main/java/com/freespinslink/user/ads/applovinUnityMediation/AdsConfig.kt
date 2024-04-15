package com.freespinslink.user.ads.applovinUnityMediation

import com.freespinslink.user.enums.EnumGames
import com.freespinslink.user.utils.Constants

object AdsConfig {


    val isDebuggerShown = false
    private val bannerAdIdMap = HashMap<String, String>()
    private val nativeAdIdMap = HashMap<String, String>()
    private val openAppAdIdMap = HashMap<String, String>()
    private val initAdIdMap = HashMap<String, String>()

    val bannerAdId get() = bannerAdIdMap[Constants.BUILD_FLAVOUR]
    val nativeAdId get() = nativeAdIdMap[Constants.BUILD_FLAVOUR]
    val openAppAdId get() = openAppAdIdMap[Constants.BUILD_FLAVOUR]
    val initAdId get() = initAdIdMap[Constants.BUILD_FLAVOUR]

    init {
        setupBannerAdIds()
        setupNativeAdIds()
        setupOpenAppAdIds()
        setupIntAdIds()
    }

    private fun setupBannerAdIds() {
        bannerAdIdMap[EnumGames.COIN_DRAGON.value] = ""
        bannerAdIdMap[EnumGames.COIN_MASTER.value] = ""
        bannerAdIdMap[EnumGames.COIN_TALES.value] = ""
        bannerAdIdMap[EnumGames.CRAZY_FOX.value] = ""
        bannerAdIdMap[EnumGames.DICE_DREAMS.value] = ""
        bannerAdIdMap[EnumGames.MATCH_MASTER.value] = "696d51a9a9b2a1bb"
        bannerAdIdMap[EnumGames.PET_MASTER.value] = ""
        bannerAdIdMap[EnumGames.PIGGY_GO.value] = ""
        bannerAdIdMap[EnumGames.FAMILY_ISLAND.value] = ""
        bannerAdIdMap[EnumGames.MAFIA_MASTER.value] = ""
    }

    private fun setupNativeAdIds() {
        nativeAdIdMap[EnumGames.COIN_DRAGON.value] = ""
        nativeAdIdMap[EnumGames.COIN_MASTER.value] = ""
        nativeAdIdMap[EnumGames.COIN_TALES.value] = ""
        nativeAdIdMap[EnumGames.CRAZY_FOX.value] = ""
        nativeAdIdMap[EnumGames.DICE_DREAMS.value] = ""
        nativeAdIdMap[EnumGames.MATCH_MASTER.value] = "99be9d8531c6a18d"
        nativeAdIdMap[EnumGames.PET_MASTER.value] = ""
        nativeAdIdMap[EnumGames.PIGGY_GO.value] = ""
        nativeAdIdMap[EnumGames.FAMILY_ISLAND.value] = ""
        nativeAdIdMap[EnumGames.MAFIA_MASTER.value] = ""
    }

    private fun setupOpenAppAdIds() {
        openAppAdIdMap[EnumGames.COIN_DRAGON.value] = ""
        openAppAdIdMap[EnumGames.COIN_MASTER.value] = ""
        openAppAdIdMap[EnumGames.COIN_TALES.value] = ""
        openAppAdIdMap[EnumGames.CRAZY_FOX.value] = "58faaadb59f3da34"
        openAppAdIdMap[EnumGames.DICE_DREAMS.value] = ""
        openAppAdIdMap[EnumGames.MATCH_MASTER.value] = "061dedcc79122c7b"
        openAppAdIdMap[EnumGames.PET_MASTER.value] = ""
        openAppAdIdMap[EnumGames.PIGGY_GO.value] = ""
        openAppAdIdMap[EnumGames.FAMILY_ISLAND.value] = ""
        openAppAdIdMap[EnumGames.MAFIA_MASTER.value] = ""
    }

    private fun setupIntAdIds() {
        initAdIdMap[EnumGames.COIN_DRAGON.value] = ""
        initAdIdMap[EnumGames.COIN_MASTER.value] = ""
        initAdIdMap[EnumGames.COIN_TALES.value] = ""
        initAdIdMap[EnumGames.CRAZY_FOX.value] = "182d3d3e47a182f4"
        initAdIdMap[EnumGames.DICE_DREAMS.value] = ""
        initAdIdMap[EnumGames.MATCH_MASTER.value] = "22121c8d6e49d153"
        initAdIdMap[EnumGames.PET_MASTER.value] = ""
        initAdIdMap[EnumGames.PIGGY_GO.value] = ""
        initAdIdMap[EnumGames.FAMILY_ISLAND.value] = ""
        initAdIdMap[EnumGames.MAFIA_MASTER.value] = ""
    }


}
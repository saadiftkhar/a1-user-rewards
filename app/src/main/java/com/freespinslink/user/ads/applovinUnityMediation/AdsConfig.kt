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
        bannerAdIdMap[EnumGames.COIN_DRAGON.value] = "ebbe380c73373fa2"
        bannerAdIdMap[EnumGames.COIN_MASTER.value] = "3a4378ef9a15cbbc"
        bannerAdIdMap[EnumGames.COIN_TALES.value] = "d6acab5efa04d23d"
        bannerAdIdMap[EnumGames.CRAZY_FOX.value] = "b93aea8b507aa280"
        bannerAdIdMap[EnumGames.DICE_DREAMS.value] = "9801283a4820791f"
        bannerAdIdMap[EnumGames.MATCH_MASTER.value] = "696d51a9a9b2a1bb"
        bannerAdIdMap[EnumGames.PET_MASTER.value] = "ea3777dcacef163b"
        bannerAdIdMap[EnumGames.PIGGY_GO.value] = "cb829bbe6f6fed6e"
        bannerAdIdMap[EnumGames.FAMILY_ISLAND.value] = "cc302136593767a6"
        bannerAdIdMap[EnumGames.MAFIA_MASTER.value] = ""
    }

    private fun setupNativeAdIds() {
        nativeAdIdMap[EnumGames.COIN_DRAGON.value] = "dff1876a2bd42898"
        nativeAdIdMap[EnumGames.COIN_MASTER.value] = "911cd3eb179bb013"
        nativeAdIdMap[EnumGames.COIN_TALES.value] = "9ed590b2090290ff"
        nativeAdIdMap[EnumGames.CRAZY_FOX.value] = "6f08c0e2464a71f7"
        nativeAdIdMap[EnumGames.DICE_DREAMS.value] = "056d9e7f055ab3b4"
        nativeAdIdMap[EnumGames.MATCH_MASTER.value] = "99be9d8531c6a18d"
        nativeAdIdMap[EnumGames.PET_MASTER.value] = "3f2ac3a180e6e8de"
        nativeAdIdMap[EnumGames.PIGGY_GO.value] = "298f5b56e4ed6f37"
        nativeAdIdMap[EnumGames.FAMILY_ISLAND.value] = "d3498f9bb0c20881"
        nativeAdIdMap[EnumGames.MAFIA_MASTER.value] = ""
    }

    private fun setupOpenAppAdIds() {
        openAppAdIdMap[EnumGames.COIN_DRAGON.value] = "d0f307cf3ccf3000"
        openAppAdIdMap[EnumGames.COIN_MASTER.value] = "b5be46abc9134e90"
        openAppAdIdMap[EnumGames.COIN_TALES.value] = "6129a979914b6894"
        openAppAdIdMap[EnumGames.CRAZY_FOX.value] = "58faaadb59f3da34"
        openAppAdIdMap[EnumGames.DICE_DREAMS.value] = "db61dd6e99bdd2a3"
        openAppAdIdMap[EnumGames.MATCH_MASTER.value] = "061dedcc79122c7b"
        openAppAdIdMap[EnumGames.PET_MASTER.value] = "3a1f7e959b1e4090"
        openAppAdIdMap[EnumGames.PIGGY_GO.value] = "548fc588649bb08b"
        openAppAdIdMap[EnumGames.FAMILY_ISLAND.value] = "c5cdf5814cab5ae0"
        openAppAdIdMap[EnumGames.MAFIA_MASTER.value] = ""
    }

    private fun setupIntAdIds() {
        initAdIdMap[EnumGames.COIN_DRAGON.value] = "93d374e53a37d6bb"
        initAdIdMap[EnumGames.COIN_MASTER.value] = "42c404865245bc35"
        initAdIdMap[EnumGames.COIN_TALES.value] = "31aa7a5fa21b6d80"
        initAdIdMap[EnumGames.CRAZY_FOX.value] = "182d3d3e47a182f4"
        initAdIdMap[EnumGames.DICE_DREAMS.value] = "2dd2371fcd6242a7"
        initAdIdMap[EnumGames.MATCH_MASTER.value] = "22121c8d6e49d153"
        initAdIdMap[EnumGames.PET_MASTER.value] = "02c318d2954fbe55"
        initAdIdMap[EnumGames.PIGGY_GO.value] = "c51a560572f18b9c"
        initAdIdMap[EnumGames.FAMILY_ISLAND.value] = "69ecd413aaae2cce"
        initAdIdMap[EnumGames.MAFIA_MASTER.value] = ""
    }


}
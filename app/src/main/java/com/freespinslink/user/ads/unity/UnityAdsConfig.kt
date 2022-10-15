package com.freespinslink.user.ads.unity

import com.freespinslink.user.enums.EnumGames
import com.freespinslink.user.utils.Constants

open class UnityAdsConfig {


    private val gameIdsMap = HashMap<String, String>()
    protected val gameId get() = gameIdsMap[Constants.BUILD_FLAVOUR]
    protected val bannerPlacement = "Banner_Android"
    protected val interstitialPlacement = "Interstitial_Android"
    protected val isTestMode = false

    init {
        setupGameIds()
    }

    private fun setupGameIds() {
        gameIdsMap[EnumGames.COIN_DRAGON.value] = "4851029"
        gameIdsMap[EnumGames.COIN_MASTER.value] = "4860257"
        gameIdsMap[EnumGames.COIN_TALES.value] = "4842183"
        gameIdsMap[EnumGames.CRAZY_FOX.value] = "4855283"
        gameIdsMap[EnumGames.DICE_DREAMS.value] = "4865069"
        gameIdsMap[EnumGames.MATCH_MASTER.value] = "4865084"
        gameIdsMap[EnumGames.PET_MASTER.value] = "4868903"
        gameIdsMap[EnumGames.PIGGY_GO.value] = "4868889"
        gameIdsMap[EnumGames.FAMILY_ISLAND.value] = "4906261"
        gameIdsMap[EnumGames.MAFIA_MASTER.value] = "4906267"
    }
}
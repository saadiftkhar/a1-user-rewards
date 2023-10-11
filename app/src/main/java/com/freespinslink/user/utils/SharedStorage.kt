package com.freespinslink.user.utils

import com.freespinslink.user.enums.EnumRating


object SharedStorage : SharedPrefHelper(RewardsConfig.PREF_NAME) {

    private const val PRIVACY_POLICY = "privacy_policy"
    private const val NOT_SHOW_DIALOG_AGAIN = "not_show_this_dialog_again"
    private const val CURRENT_SESSIONS = "current_sessions"
    private const val RATING_STATE = "rating_state"
    private const val REWARD_DETAIL_SCREEN_OPENED = "reward_detail_screen_opened"


    @Synchronized
    fun incSessionCount() {
        saveInt(CURRENT_SESSIONS, getSessionCount().inc())
    }

    private fun getSessionCount(): Int {
        return getInt(CURRENT_SESSIONS, 0)
    }

    @Synchronized
    fun setRatingState(value: String) {
        saveString(RATING_STATE, value)
    }

    private fun getRatingState(): String {
        return getString(RATING_STATE, RewardsConfig.DEFAULT_RATING_STATE.toString())
    }

    fun isRatingApplicable(): Boolean {
        return (getRatingState() == EnumRating.NOT_RATED_YET.value
                || getRatingState() == EnumRating.RECALL_RATING.value)
                && getSessionCount() % 5 == 0
    }

    @Synchronized
    fun updatePrivacyCheck(value: Boolean) {
        saveBoolean(PRIVACY_POLICY, value)
    }

    fun isPrivacyChecked(): Boolean {
        return getBoolean(PRIVACY_POLICY, false)
    }

    @Synchronized
    fun saveNotShowAgain(value: Boolean) {
        saveBoolean(NOT_SHOW_DIALOG_AGAIN, value)
    }

    fun isDialogShowAgain(): Boolean {
        return getBoolean(NOT_SHOW_DIALOG_AGAIN, false)
    }

    @Synchronized
    fun saveRewardDetailScreenOpened(value: Boolean) {
        saveBoolean(REWARD_DETAIL_SCREEN_OPENED, value)
    }

    fun isRewardDetailScreenOpened(): Boolean {
        return getBoolean(REWARD_DETAIL_SCREEN_OPENED, false)
    }
}
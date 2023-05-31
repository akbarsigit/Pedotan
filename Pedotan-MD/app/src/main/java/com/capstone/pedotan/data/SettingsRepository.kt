package com.capstone.pedotan.data

import android.content.Context
import android.provider.Telephony.Carriers.PASSWORD
import com.senpro.ulamsae.model.Settings

class SettingsRepository(context: Context)  {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setDarkMode(value: Int) {
        val editor = preferences.edit()
        editor.putInt(STATE_DARK_MODE, value)
        editor.apply()
    }

    fun setLogin(state: Boolean, token: String, refToken: String, id: String) {
        val editor = preferences.edit()
        editor.putBoolean(STATE_LOGIN, state)
        editor.putString(TOKEN, token)
        editor.putString(REF_TOKEN, refToken)
        editor.putString(USER_ID, id)
        editor.apply()
    }

    fun getSettings(): Settings {
        val model = Settings()
        model.isDarkMode = preferences.getInt(STATE_DARK_MODE, 2)
        model.isLogin = preferences.getBoolean(STATE_LOGIN, false)
        model.token = preferences.getString(TOKEN, "token").toString()
        model.refToken = preferences.getString(REF_TOKEN, "ref_token").toString()
        model.userID = preferences.getString(USER_ID, "user_id").toString()
        return model
    }

    fun clearSession() {
        val editor = preferences.edit()
        editor.remove("login")
        editor.remove("user_id")
        editor.remove("token")
        editor.remove("ref_token")
        editor.apply()
    }

    companion object {
        private const val PREFS_NAME = "settings_pref"
        private const val STATE_DARK_MODE = "dark_mode"
        private const val STATE_LOGIN = "login"
        private const val USER_ID = "user_id"
        private const val TOKEN = "token"
        private const val REF_TOKEN = "ref_token"
    }
}
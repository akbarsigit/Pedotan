package com.capstone.pedotan.data

import android.content.Context
import com.capstone.pedotan.model.Contract
import com.capstone.pedotan.model.ContractData
import com.capstone.pedotan.model.History
import com.capstone.pedotan.model.HistoryData
import com.capstone.pedotan.model.Loan
import com.capstone.pedotan.model.LoanData
import com.capstone.pedotan.model.Market
import com.capstone.pedotan.model.MarketData
import com.senpro.ulamsae.model.Settings

class SettingsRepository(context: Context)  {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getMarkets(): List<Market> {
        return MarketData.markets
    }

    fun getContracts(): List<Contract> {
        return ContractData.contracts
    }

    fun getLoans(): List<Loan> {
        return LoanData.loans
    }

    fun getHistories(id: Int): List<History> {
        return HistoryData.histories.filter { it.fieldId == id }
    }

    fun setDarkMode(value: Int) {
        val editor = preferences.edit()
        editor.putInt(STATE_DARK_MODE, value)
        editor.apply()
    }

    fun setLogin(state: Boolean, token: String, email:String) {
        val editor = preferences.edit()
        editor.putBoolean(STATE_LOGIN, state)
        editor.putString(TOKEN, token)
//        editor.putString(REF_TOKEN, refToken)
        editor.putString(USER_EMAIL, email)
        editor.apply()
    }

    fun getSettings(): Settings {
        val model = Settings()
        model.isDarkMode = preferences.getInt(STATE_DARK_MODE, 2)
        model.isLogin = preferences.getBoolean(STATE_LOGIN, false)
        model.token = preferences.getString(TOKEN, "token").toString()
//        model.refToken = preferences.getString(REF_TOKEN, "ref_token").toString()
        model.email = preferences.getString(USER_EMAIL, "user_email").toString()
        return model
    }

    fun clearSession() {
        val editor = preferences.edit()
        editor.remove("login")
        editor.remove("token")
        editor.remove("email")
//        editor.remove("ref_token")
        editor.apply()
    }

    companion object {
        private const val PREFS_NAME = "settings_pref"
        private const val STATE_DARK_MODE = "dark_mode"
        private const val STATE_LOGIN = "login"
        private const val TOKEN = "token"
        private const val USER_EMAIL = "email"
//        private const val REF_TOKEN = "ref_token"
    }
}
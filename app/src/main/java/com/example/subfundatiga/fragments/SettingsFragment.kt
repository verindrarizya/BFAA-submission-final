package com.example.subfundatiga.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.subfundatiga.R
import com.example.subfundatiga.alarm.AlarmReceiver

class SettingsFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var KEY_ALARM: String
    private lateinit var KEY_lANGUAGE: String

    private lateinit var alarmPreference: SwitchPreferenceCompat
    private lateinit var languagePreference: Preference
    private lateinit var alarmReceiver: AlarmReceiver

    companion object {
        private const val DEFAULT_ALARM_VALUE = false
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey)
        init()
        setPreferencesFunctionality()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun init() {
        KEY_ALARM = resources.getString(R.string.key_alarm)
        KEY_lANGUAGE = resources.getString(R.string.key_language)

        alarmReceiver = AlarmReceiver()

        alarmPreference = findPreference<SwitchPreferenceCompat>(KEY_ALARM) as SwitchPreferenceCompat
        languagePreference = findPreference<Preference>(KEY_lANGUAGE) as Preference
    }

    private fun setPreferencesFunctionality() {
        val sh = preferenceManager.sharedPreferences
        alarmPreference.isChecked = sh.getBoolean(KEY_ALARM, DEFAULT_ALARM_VALUE)

        val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
        languagePreference.intent = intent
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == KEY_ALARM) {
            if (sharedPreferences != null) {
                if (alarmPreference.isChecked) {
                    context?.let { alarmReceiver.setRepeatingAlarm(it) }
                } else {
                    context?.let { alarmReceiver.cancelAlarm(it) }
                }
            }
        }
    }

}
package com.example.statuspherenew.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.statuspherenew.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(
            R.xml.settings,
            rootKey
        )
    }
}






//    The GNU General Public License does not permit incorporating this program
//    into proprietary programs.
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <https://www.gnu.org/licenses/>.

package io.github.installalogs.ui.about;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.Date;

import io.github.installalogs.R;
import io.github.installalogs.tools.Scheduler;
import io.github.installalogs.ui.About;
import io.github.installalogs.ui.NumberPref;

public class AboutFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.about, rootKey);
        findPreference("command about").setOnPreferenceClickListener(preference -> {
            new About(getActivity()).about();
            return false;
        });
        findPreference("command license").setOnPreferenceClickListener(preference -> {
            new About(getActivity()).license();
            return false;
        });
        findPreference("command version").setOnPreferenceClickListener(preference -> {
            try {
                new About(getActivity()).version();
            } catch (PackageManager.NameNotFoundException n) {
                n.printStackTrace();
            }
            return false;
        });
        long logStart = Prefs.getLong("log start time-stamp", 0);
        findPreference("log start time-stamp")
                .setSummary("" + logStart + " (" + new Date(logStart).toLocaleString() + ")");
        new NumberPref().change(findPreference("force background rescans minutes"));
        findPreference("force background rescans").setOnPreferenceChangeListener((preference, newValue) -> {
            Prefs.putBoolean("force background rescans", Boolean.parseBoolean(newValue.toString()));
            new Scheduler(getActivity()).scheduleScanService();
            return true;
        });
        findPreference("force background rescans minutes").setOnPreferenceChangeListener((preference, newValue) -> {
            Prefs.putString("force background rescans minutes", newValue.toString());
            new Scheduler(getActivity()).scheduleScanService();
            return true;
        });
    }

}
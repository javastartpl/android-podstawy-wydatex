package pl.javastart.wydatex.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import pl.javastart.wydatex.R;

public class PreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}

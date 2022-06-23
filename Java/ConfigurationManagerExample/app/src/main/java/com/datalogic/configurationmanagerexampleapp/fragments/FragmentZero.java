package com.datalogic.configurationmanagerexampleapp.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.datalogic.configurationmanagerexampleapp.databinding.FragmentZeroBinding;

/** "Help" Fragment **/
public class FragmentZero extends Fragment {
    private static final String TAG = "FragmentZero";
    private FragmentZeroBinding fragmentZeroBinding;
    private static final String HELP_TEXT =
                    "Welcome on Configuration Manager Example App!\n\n" +
                    "In this app there are some examples of common use cases using Datalogic Configuration Manager.\n\n" +
                    "All examples are good commented to provide readable and easy examples.\n\n" +
                    "To see an example, go to the navigation drawer through the hamburger button and select the use case you are interested to.\n\n";


    public FragmentZero() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        fragmentZeroBinding = FragmentZeroBinding.inflate(inflater, container, false);
        fragmentZeroBinding.f0HelpText.setText(HELP_TEXT);


        return fragmentZeroBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
package habbitatvalley.com.geebelataxigeebela.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import habbitatvalley.com.geebelataxigeebela.R;

public class CoveredAreasFragment extends Fragment {

    // Saved Instance State
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View convertView =inflater.inflate(R.layout.coveredareas,container,false);

        return convertView;
    }
}
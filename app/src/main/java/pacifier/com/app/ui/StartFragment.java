package pacifier.com.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.OnClick;
import pacifier.com.app.R;

public class StartFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @OnClick(R.id.btnStart)
    public void launchNextView() {
        ((MainActivity)getActivity()).setFragment(new SettingsFragment(), true);
    }
}

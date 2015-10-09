package pacifier.com.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import javax.inject.Inject;

import butterknife.ButterKnife;
import pacifier.com.app.PaceifierApp;
import pacifier.com.app.network.APIService;

public class BaseFragment extends Fragment {
    @Inject PaceifierApp mApp;
    @Inject MainActivity mActivity;
    @Inject APIService mAPI;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).inject(this);
    }
}

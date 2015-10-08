package pacifier.com.app.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class BaseFragment extends Fragment {
    @Inject MainActivity mActivity;
    @Inject OkHttpClient mClient;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).inject(this);

        Request request = new Request.Builder()
                .url("https://api.ipify.org/?format=text")
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            Handler mainHandler = new Handler(mActivity.getMainLooper());

            @Override
            public void onResponse(Response response) throws IOException {
                String netResponse = response.body().string();
            }

            @Override
            public void onFailure(Request request, IOException e) {
                String netResponse = e.getMessage();
            }
        });
    }
}

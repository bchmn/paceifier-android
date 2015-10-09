package pacifier.com.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTouch;
import de.greenrobot.event.EventBus;
import pacifier.com.app.R;
import pacifier.com.app.events.AccelerationChangeEvent;
import pacifier.com.app.events.SpeedChangeEvent;
import pacifier.com.app.managers.TiltSpeedManager;
import pacifier.com.app.utils.Logger;

public class DriveFragment extends BaseFragment {

    @Inject TiltSpeedManager mSpeedManager;

    @Bind(R.id.webView) WebView mWebView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drive, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWebView();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        mSpeedManager.resetBaseline();
    }

    @OnClick(R.id.btnCheckout)
    public void checkout() {
        EventBus.getDefault().unregister(this);
        executeJS("window.speed = 0; window.acc = 0");
        executeJS("android.getFinalScore(window.points)");
    }

    @JavascriptInterface
    public void getFinalScore(String finalScoreStr) {
        float finalScore = Float.parseFloat(finalScoreStr);
        if (true) {//finalScore > 1) {
            mActivity.startBraintreeActivity(finalScore);
            /*Call<TokenResponse> call = mAPI.checkout()
            call.enqueue(new Callback<TokenResponse>() {
                @Override
                public void onResponse(Response<TokenResponse> response, Retrofit retrofit) {
                    mApp.setBrainTreeToken(response.body().getToken());
                }

                @Override
                public void onFailure(Throwable t) {
                    Logger.l(t.getMessage(), Logger.ERROR);
                }
            });*/
        } else {
            mActivity.showDialog("Nice Try", "Try again after you've drived");
        }
    }

    @OnTouch(R.id.webView)
    public boolean resetMagenometerBaseline(View view, MotionEvent motionEvent) {
        Logger.l("webview clicked");
        mSpeedManager.resetBaseline();
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(SpeedChangeEvent event) {
        executeJS("window.speed = " + event.speed + "; window.speedLimit = " + event.speedLimit);
    }

    public void onEvent(AccelerationChangeEvent event) {
        executeJS("window.acc = " + event.getAcceleration());
    }

    void initWebView() {
        mWebView.clearCache(true);
        mWebView.clearHistory();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.addJavascriptInterface(this, "android");
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            executeJS("window.pointsPerKm = " + 15);//mApp.getPricePerKm());
            }
        });
        mWebView.loadUrl("file:///android_asset/web/index.html");
    }

    void executeJS(String javascript) {
        mWebView.loadUrl("javascript:" + javascript + ";");
    }
}

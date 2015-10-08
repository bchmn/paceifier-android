package pacifier.com.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.OnClick;
import pacifier.com.app.R;

/**
 * Created by bachman on 8/10/15.
 */
public class DriveFragment extends BaseFragment {

    @Bind(R.id.webView) WebView mWebView;
    @Bind(R.id.inputSpeed) EditText mInputSpeed;

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

    private void initWebView() {
        mWebView.clearCache(true);
        mWebView.clearHistory();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        mWebView.loadUrl("file:///android_asset/web/index.html");

    }

    @OnClick(R.id.btnSetSpeed)
    public void setSpeed() {
        executeJS("window.speed = " + mInputSpeed.getText());
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mInputSpeed.getWindowToken(), 0);
    }

    private void executeJS(String javascript) {
        mWebView.loadUrl("javascript:" + javascript + ";");
    }
}

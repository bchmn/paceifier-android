package pacifier.com.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.braintreepayments.api.dropin.BraintreePaymentActivity;
import com.braintreepayments.api.dropin.Customization;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.ObjectGraph;
import pacifier.com.app.PaceifierApp;
import pacifier.com.app.R;
import pacifier.com.app.managers.TiltSpeedManager;
import pacifier.com.app.modules.ActivityModule;
import pacifier.com.app.utils.Conf;

public class MainActivity extends AppCompatActivity {
    ObjectGraph mActivityGraph;

    @Inject PaceifierApp mApp;
    @Inject TiltSpeedManager mSpeedManager;

    @Bind(R.id.flContent) FrameLayout flContent;
    @Bind(R.id.progressBar) CircularProgressView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        mActivityGraph = ((PaceifierApp)getApplication()).getApplicationGraph().plus(getModules());
        mActivityGraph.inject(this);

        setFragment(new StartFragment(), false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSpeedManager.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSpeedManager.stopListening();
    }

    protected Object[] getModules() {
        return new Object[] {
                new ActivityModule(this),
        };
    }

    public void inject(Object object) {
        mActivityGraph.inject(object);
    }

    public void setFragment(Fragment fragment, boolean addCurrentFragmentToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, fragment);
        if (addCurrentFragmentToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
        if (progressBarVisible())
            toggleProgress();
    }

    public void toggleProgress() {
        if (null != flContent && null != progressBar) {
            boolean showProgressBar = !progressBarVisible();
            flContent.setVisibility(showProgressBar ? View.INVISIBLE : View.VISIBLE);
            progressBar.setVisibility(showProgressBar ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void showDialog(String title, String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    public void startBraintreeActivity(float amount) {
        Intent intent = new Intent(MainActivity.this, BraintreePaymentActivity.class);
        intent.putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, mApp.getBrainTreeToken());
        Customization customization = new Customization.CustomizationBuilder()
                .primaryDescription("You're a safe driver!")
                .amount("$" + Math.ceil(amount))
                .submitButtonText("Pay Up!")
                .build();
        intent.putExtra(BraintreePaymentActivity.EXTRA_CUSTOMIZATION, customization);
        startActivityForResult(intent, Conf.BRAINTREE_REQUEST_CODE);
    }

    boolean progressBarVisible() {
        if (null != progressBar)
            return progressBar.getVisibility() == View.VISIBLE;
        else
            return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Conf.BRAINTREE_REQUEST_CODE) {
            if (resultCode == BraintreePaymentActivity.RESULT_OK) {
                setFragment(new StartFragment(), false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        mActivityGraph = null;
        super.onDestroy();
    }
}

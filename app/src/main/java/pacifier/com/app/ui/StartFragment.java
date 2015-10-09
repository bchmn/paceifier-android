package pacifier.com.app.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import butterknife.Bind;
import butterknife.OnClick;
import pacifier.com.app.R;
import pacifier.com.app.network.TokenResponse;
import pacifier.com.app.utils.Conf;
import pacifier.com.app.utils.Logger;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class StartFragment extends BaseFragment {

    @Bind(R.id.inputParentEmail) MaterialEditText inputParentEmail;
    @Bind(R.id.inputDriverEmail) MaterialEditText inputDriverEmail;
    @Bind(R.id.seekBar) SeekBar seekBar;
    @Bind(R.id.tvPricePerKm) TextView tvPricePerKm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String deviceAccountEmail = getDeviceAccountEmail();
        if (!TextUtils.isEmpty(deviceAccountEmail)) {
            inputParentEmail.setText("guysopher@gmail.com");
            inputDriverEmail.setText(deviceAccountEmail);
        }
        RegexpValidator emailValidator = new RegexpValidator("Please enter a valid e-mail address", Conf.EMAIL_ADDRESS_PATTERN);
        inputParentEmail.validateWith(emailValidator);
        inputDriverEmail.validateWith(emailValidator);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvPricePerKm.setText(Integer.toString(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @OnClick(R.id.btnStart)
    public void startDriving() {
        if (null != mApp && null!= mActivity) {
            int pricePerKm = Integer.parseInt(tvPricePerKm.getText().toString());
            if (pricePerKm > 0) {
                String parentEmail = inputParentEmail.getText().toString();
                String driverEmail = inputDriverEmail.getText().toString();
                if (!TextUtils.isEmpty(parentEmail) && !TextUtils.isEmpty(driverEmail)) {
                    mApp.setParentEmail(parentEmail);
                    mApp.setDriverEmail(driverEmail);
                    mApp.setPricePerKm(pricePerKm);
                    //mActivity.setFragment(new SensorsFragment(), true);
                    getBrainTreeToken();
                }
                else {
                    mActivity.showDialog("Invalid Email Address", "Please make sure you have entered valid email addresses.");
                }
            }
            else {
                mActivity.showDialog("Invalid Reward", "Please adjust the reward amount to be higher than zero.");
            }
        }
    }

    String getDeviceAccountEmail() {
        String email = null;
        if (null != mActivity) {
            Account[] accounts = AccountManager.get(mActivity).getAccounts();
            for (Account account : accounts) {
                if (Patterns.EMAIL_ADDRESS.matcher(account.name).matches()) {
                    email = account.name;
                }
            }
        }
        return email;
    }



    void getBrainTreeToken() {
        if (null == mApp.getBrainTreeToken()) {
            mActivity.toggleProgress();
            Call<TokenResponse> call = mAPI.getToken();
            call.enqueue(new Callback<TokenResponse>() {
                @Override
                public void onResponse(Response<TokenResponse> response, Retrofit retrofit) {
                    mApp.setBrainTreeToken(response.body().getToken());
                    mActivity.setFragment(new DriveFragment(), false);
                }

                @Override
                public void onFailure(Throwable t) {
                    Logger.l(t.getMessage(), Logger.ERROR);
                }
            });
        }
        else
            mActivity.setFragment(new DriveFragment(), true);
    }
}

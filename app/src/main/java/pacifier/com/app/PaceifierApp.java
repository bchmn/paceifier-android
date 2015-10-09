package pacifier.com.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

import dagger.ObjectGraph;
import pacifier.com.app.modules.ApplicationModule;
import pacifier.com.app.utils.Conf;

import static android.content.Context.MODE_PRIVATE;


public class PaceifierApp extends Application {
    ObjectGraph applicationGraph;
    SharedPreferences mPrefs;
    String brainTreeToken;
    String parentEmail;
    String driverEmail;
    int pricePerKm;

    @Override
    public void onCreate() {
        super.onCreate();
        mPrefs = this.getSharedPreferences("com.paceifier.app", MODE_PRIVATE);
        this.applicationGraph = ObjectGraph.create(getModules());
        this.applicationGraph.inject(this);

        //mPrefs.edit().remove(Conf.Prefs.BRAINTREE_TOKEN);
    }

    protected Object[] getModules() {
        return new Object[] {
            new ApplicationModule(this),
        };
    }

    public ObjectGraph getApplicationGraph() {
        return this.applicationGraph;
    }

    public String getBrainTreeToken() {
        if (TextUtils.isEmpty(brainTreeToken) && mPrefs.contains(Conf.Prefs.BRAINTREE_TOKEN))
            brainTreeToken = mPrefs.getString(Conf.Prefs.BRAINTREE_TOKEN, "");
        return brainTreeToken;

    }

    public void setBrainTreeToken(String brainTreeToken) {
        mPrefs.edit().putString(Conf.Prefs.BRAINTREE_TOKEN, brainTreeToken).commit();
        this.brainTreeToken = brainTreeToken;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public int getPricePerKm() {
        return pricePerKm;
    }

    public void setPricePerKm(int pricePerKm) {
        this.pricePerKm = pricePerKm;
    }
}

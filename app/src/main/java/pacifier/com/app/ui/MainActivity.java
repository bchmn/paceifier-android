package pacifier.com.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import butterknife.ButterKnife;
import dagger.ObjectGraph;
import pacifier.com.app.PaceifierApp;
import pacifier.com.app.R;
import pacifier.com.app.modules.ActivityModule;

public class MainActivity extends AppCompatActivity {

    ObjectGraph mActivityGraph;

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
    }

    @Override
    protected void onDestroy() {
        mActivityGraph = null;
        super.onDestroy();
    }
}

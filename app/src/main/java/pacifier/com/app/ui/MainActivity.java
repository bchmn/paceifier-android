package pacifier.com.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;
import pacifier.com.app.PaceifierApp;
import pacifier.com.app.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ((PaceifierApp) getApplication()).inject(this);
        ButterKnife.bind(this);

        setFragment(new StartFragment(), false);

//        String netResponse = null;
//        try {
//            Request request = new Request.Builder()
//                    .url("https://api.ipify.org/?format=text")
//                    .build();
//
//            Response response = mClient.newCall(request).execute();
//            netResponse = response.body().string();
//            Toast.makeText(MainActivity.this, netResponse, Toast.LENGTH_SHORT).show();
//        }
//        catch (IOException e) {
//
//        }
    }

    public void setFragment(Fragment fragment, boolean addCurrentFragmentToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, fragment);
        if (addCurrentFragmentToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }
}

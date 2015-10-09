package pacifier.com.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import pacifier.com.app.R;
import pacifier.com.app.events.SensorChangeEvent;
import pacifier.com.app.managers.TiltSpeedManager;
import pacifier.com.app.network.APIService;

/**
 * Created by bachman on 8/10/15.
 */
public class SensorsFragment extends BaseFragment {
    @Inject TiltSpeedManager mSpeedManager;
    @Inject APIService mAPI;

    @Bind(R.id.tvAccelerometerX) TextView mAccelerometerX;
    @Bind(R.id.tvAccelerometerY) TextView mAccelerometerY;
    @Bind(R.id.tvAccelerometerZ) TextView mAccelerometerZ;
    @Bind(R.id.tvGeomagneticX) TextView mGeomagneticX;
    @Bind(R.id.tvGeomagneticY) TextView mGeomagneticY;
    @Bind(R.id.tvGeomagneticZ) TextView mGeomagneticZ;
    @Bind(R.id.tvOrientationX) TextView mOrientationX;
    @Bind(R.id.tvOrientationY) TextView mOrientationY;
    @Bind(R.id.tvOrientationZ) TextView mOrientationZ;

    private boolean viewsInjected = false;

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        if (null != mSpeedManager)
            mSpeedManager.startListening();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sensors, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewsInjected = true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSpeedManager.startListening();

    }

    public void onEvent(SensorChangeEvent event) {
        if (viewsInjected) {
            mAccelerometerX.setText(event.accelerometer[0]);
            mAccelerometerY.setText(event.accelerometer[1]);
            mAccelerometerZ.setText(event.accelerometer[2]);

            mGeomagneticX.setText(event.geomagnetic[0]);
            mGeomagneticY.setText(event.geomagnetic[1]);
            mGeomagneticZ.setText(event.geomagnetic[2]);

            mOrientationX.setText(event.orientation[0]);
            mOrientationY.setText(event.orientation[1]);
            mOrientationZ.setText(event.orientation[2]);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        if (null != mSpeedManager)
            mSpeedManager.stopListening();
    }
}

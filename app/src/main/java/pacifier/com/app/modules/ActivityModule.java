package pacifier.com.app.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pacifier.com.app.ui.BaseFragment;
import pacifier.com.app.ui.DriveFragment;
import pacifier.com.app.ui.MainActivity;
import pacifier.com.app.ui.SettingsFragment;
import pacifier.com.app.ui.StartFragment;

@Module(
    addsTo = ApplicationModule.class,
    injects = {
        MainActivity.class,
        BaseFragment.class,
        StartFragment.class,
        SettingsFragment.class,
        DriveFragment.class
    }
)
public class ActivityModule {
    private final MainActivity activity;

    public ActivityModule(MainActivity activity) {
        this.activity = activity;
    }

    @Provides
    @Singleton
    MainActivity provideActivity() {
        return activity;
    }
}

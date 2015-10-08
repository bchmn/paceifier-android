package pacifier.com.app.modules;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pacifier.com.app.PaceifierApp;

@Module(
    includes = {
        NetworkModule.class
    },
    injects = {
        PaceifierApp.class
    },
    library = true
)
public class ApplicationModule {
    final PaceifierApp app;

    public ApplicationModule(PaceifierApp app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application provideApplication() { return this.app; }
}

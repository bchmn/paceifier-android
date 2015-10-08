package pacifier.com.app;

import android.app.Application;

import dagger.ObjectGraph;
import pacifier.com.app.modules.ApplicationModule;


public class PaceifierApp extends Application {
    private ObjectGraph applicationGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        this.applicationGraph = ObjectGraph.create(getModules());
        this.applicationGraph.inject(this);
    }

    protected Object[] getModules() {
        return new Object[] {
            new ApplicationModule(this),
        };
    }

    public ObjectGraph getApplicationGraph() {
        return this.applicationGraph;
    }
}

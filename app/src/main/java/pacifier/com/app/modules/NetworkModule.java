package pacifier.com.app.modules;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pacifier.com.app.PaceifierApp;
import pacifier.com.app.network.APIService;
import pacifier.com.app.utils.Conf;
import pacifier.com.app.utils.Logger;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import static com.jakewharton.byteunits.DecimalByteUnit.MEGABYTES;
import static java.util.concurrent.TimeUnit.SECONDS;

@Module(
    complete = false,
    library = true
)
public class NetworkModule {
    static final int DISK_CACHE_SIZE = (int) MEGABYTES.toBytes(50);

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(PaceifierApp app) {
        return createOkHttpClient(app);
    }

    @Singleton
    @Provides
    APIService provideAPIService() {
        return createAPIService();
    }

    @Provides @Singleton Picasso providePicasso(PaceifierApp app, OkHttpClient client) {
        return new Picasso.Builder(app)
                .downloader(new OkHttpDownloader(client))
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Logger.l(exception);
                    }
                })
                .build();
    }

    static APIService createAPIService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Conf.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(APIService.class);
    }

    static OkHttpClient createOkHttpClient(PaceifierApp app) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(10, SECONDS);
        client.setReadTimeout(10, SECONDS);
        client.setWriteTimeout(10, SECONDS);

        // Install an HTTP cache in the application cache directory.
        File cacheDir = new File(app.getCacheDir(), "http");
        Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
        client.setCache(cache);

        return client;
    }
}

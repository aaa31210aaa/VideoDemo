package config;

import android.annotation.SuppressLint;
import android.app.Application;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import model.MineModel;
import viewmodel.VideoViewModel;


/**
 * Created by tanggongwen on 2019/9/18.
 */
public class MineViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile MineViewModelFactory INSTANCE;
    private final Application mApplication;
    private final MineModel mineModel;

    public static MineViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (MineViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MineViewModelFactory(application, MineInjection.provideRepository());
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private MineViewModelFactory(Application application, MineModel model) {
        this.mApplication = application;
        this.mineModel = model;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(VideoViewModel.class)) {
            return (T) new VideoViewModel(mApplication, mineModel);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}

package com.adamhurwitz.retrorecycler.DependencyInjection;

import com.adamhurwitz.retrorecycler.MainActivity;
import com.adamhurwitz.retrorecycler.MainViewModel;
import com.adamhurwitz.retrorecycler.Network.Repository;
import com.adamhurwitz.retrorecycler.RetroRecyclerApplication;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ahurwitz on 1/24/17.
 */

@Singleton
@Component(modules={DataModule.class})

public interface DataComponent {

    void inject(RetroRecyclerApplication retroRecyclerApplication);

    void inject(Repository repository);

    void inject(MainViewModel mainViewModel);
}

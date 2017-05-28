package io.github.adamshurwitz.retrorecycler.DependencyInjection;


import io.github.adamshurwitz.retrorecycler.MainViewModel;
import io.github.adamshurwitz.retrorecycler.Network.Repository;
import io.github.adamshurwitz.retrorecycler.RetroRecyclerApplication;

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

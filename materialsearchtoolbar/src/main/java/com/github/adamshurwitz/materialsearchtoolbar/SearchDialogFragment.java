package com.github.adamshurwitz.materialsearchtoolbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.Date;

import rx.Observable;
import rx.subjects.ReplaySubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ahurwitz on 4/21/17.
 */

public class SearchDialogFragment extends DialogFragment {

    public SearchDialogView searchDialogView;

    private ArrayList<String> recentSearchList = new ArrayList<>();

    private ReplaySubject<String> querySubmitedSubscriber = ReplaySubject.create();

    private CompositeSubscription compositeSubscription;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(SearchDialogFragment.STYLE_NO_FRAME, 0);
    }

    /**
     * The system calls this to get the DialogFragment's layout, regardless
     * of whether it's being displayed as a dialog or an embedded fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_term_complete, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        compositeSubscription = new CompositeSubscription();
        initOverlay();
        initSearchView(view);
        initOverlayClickListener(view);
    }

    /**
     * The system calls this only when creating the layout in a dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onPause() {
        super.onPause();
        searchDialogView.hideKeyboard();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeSubscription.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(Utils.getScreenWidth(getActivity()), ViewGroup.LayoutParams.WRAP_CONTENT);
        searchDialogView.showKeyboard();
    }


    private void initOverlay() {
        //size
        getDialog().getWindow().setGravity(Gravity.TOP);
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = Utils.getScreenWidth(getActivity());
        params.horizontalMargin = 0;
        params.verticalMargin = 0;
        params.dimAmount = 1.0f;
        getDialog().getWindow().setAttributes(params);

        //color
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.60f;
        windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(windowParams);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.dialog_overlay_color);
    }


    private void initSearchView(View view) {
        searchDialogView = (SearchDialogView) view.findViewById(R.id.search_term_view);
        searchDialogView.showSearch();
        searchDialogView.setHint(getString(R.string.hint_search_items));
        searchDialogView.setVoiceSearch(true);
        setSuggestions(view);
        onBackPressedEvent(view);
        onQueryTextChangeEvent();
        onQueryTextSubmitEvent();
    }

    private void setSuggestions(View view) {
        //todo: refactor
        searchDialogRevealAnimation(view);
        /*helper.recentSearchTerms()
                .loadRecentSearchTerms()
                .compose(RxHelpers.IOAndMainThreadSchedulers())
                .flatMap(recentSearches -> Observable.from(recentSearches))
                .doOnCompleted(() -> {
                    searchDialogView.setSuggestions(recentSearchList.toArray(new String[recentSearchList.size()]));
                    searchDialogRevealAnimation(view);
                    removeSearchTermEvent();
                }).subscribe(recentSearchTerm -> {
                    recentSearchList.add(recentSearchTerm.recentSearchTerm);
                }, throwable -> {
                    Log.e("RecentSearch", "recentSearch" + throwable);
                });*/
    }

    private void removeSearchTermEvent() {

        searchDialogView.removeSearchTermEvent()
                .subscribe(pair -> {
                    Animation searchTermFadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up_fade_100);
                    pair.first.startAnimation(searchTermFadeOut);
                    searchTermFadeOut.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            if (!recentSearchList.isEmpty()) {
                                String removeTerm = recentSearchList.get(pair.second);
                                recentSearchList.remove((int) pair.second);
                                searchDialogView.notifyDataSetChanged(recentSearchList);
                                searchTermSQLRemove(removeTerm);
                            }

                            if (recentSearchList.isEmpty()) {
                                searchDialogView.dismissSuggestions();
                            }

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                });
    }

    public Observable<String> querySubmitedEvent() {
        return querySubmitedSubscriber.asObservable();
    }

    private void onQueryTextSubmitEvent() {
        compositeSubscription.add(searchDialogView.onQueryTextSubmitEvent().subscribe(query -> {
            searchDialogView.hideKeyboard();
            saveRecentSearch(query);
            querySubmitedSubscriber.onNext(query);
        }, throwable -> {
            querySubmitedSubscriber.onError(throwable);
            Log.e(SearchDialogFragment.class.getSimpleName(), "Query Text Submitted Event: " + throwable.toString());
        }));
    }

    public void onQueryTextChangeEvent() {
        searchDialogView.onQueryTextChangeEvent().subscribe(query -> {
            //useful for SearchAutoComplete
        });
    }

    private void saveRecentSearch(String query) {

        if (recentSearchList.contains(query)) {
            recentSearchList.remove(recentSearchList.indexOf(query));
            searchTermSQLRemove(query);
        }

        //todo: make Observable to subscribe to
        /*RecentSearch recentSearch = new RecentSearch();
        recentSearch.recentSearchTerm = query;
        recentSearch.timestamp = new Date().getTime();
        recentSearch.save();*/

    }

    private void onBackPressedEvent(View view) {

        compositeSubscription.add(searchDialogView.onBackPressed()
                .subscribe(querySubmited -> {
                    if (querySubmited || !Utils.isLollipopOrAbove()) {
                        getDialog().dismiss();
                    } else {
                        searchDialogExitAnimation(view);
                    }
                    searchDialogView.hideKeyboard();
                }, throwable -> {
                    Log.e(SearchDialogFragment.class.getSimpleName(), "On Back Pressed Event: " + throwable.toString());
                }));
    }

    private void searchDialogRevealAnimation(final View view) {
        if (Utils.isLollipopOrAbove()) {
            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);

                    // get the center for the clipping circle
                    int cx = view.getRight();
                    int cy = searchDialogView.getMeasuredHeight() / 2;

                    // get the final radius for the clipping circles
                    int dx = Math.max(cx, view.getWidth() - cx);
                    int dy = Math.max(cy, view.getHeight() - cy);
                    float finalRadius = (float) Math.hypot(dx, dy);

                    Animator animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setDuration(250);
                    animator.start();

                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            searchDialogView.showSuggestions(true);
                        }
                    });
                }
            });
        } else {
            //todo: remove with Observable to know data has loaded
            searchDialogView.postDelayed(() ->
                            searchDialogView.showSuggestions(true),
                    100);
        }
    }

    private void searchDialogExitAnimation(final View view) {
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);

                // get the center for the clipping circle
                int cx = view.getRight();
                int cy = searchDialogView.getMeasuredHeight() / 2;

                // get the final radius for the clipping circles
                int dx = Math.max(cx, view.getWidth() - cx);
                int dy = Math.max(cy, view.getHeight() - cy);

                // get the initial radius for the clipping circle
                float initialRadius = (float) Math.hypot(dx, dy);

                // create the animation (the final radius is zero)
                Animator animator =
                        ViewAnimationUtils.createCircularReveal(searchDialogView, cx, cy, initialRadius, 0);

                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        getDialog().dismiss();
                    }
                });
                animator.start();
            }
        });
        view.clearFocus();
    }


    private void initOverlayClickListener(View view) {
        View overlayView = view.findViewById(R.id.overlay);
        overlayView.setOnClickListener(v -> {
            searchDialogView.closeSearch();
        });
    }

    private void searchTermSQLRemove(String removeTerm) {
        //todo: make Observable to subscribe to
        /*SQLite.delete()
                .from(RecentSearch.class)
                .where(RecentSearch_Table.recentSearchTerm.is(removeTerm))
                .query();*/
    }

}

package com.github.adamshurwitz.materialsearchtoolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.ReplaySubject;

/**
 * @author Miguel Catalan Bañuls
 */
public class SearchDialogView extends FrameLayout implements Filter.FilterListener {


    public static final int REQUEST_VOICE = 9999;

    private boolean mIsSearchOpen = false;

    private View searchLayout;
    private ListView suggestionsListView;
    private EditText searchEditText;
    private ImageButton backBtn;
    private ImageButton voiceBtn;
    private ImageButton emptyBtn;
    private CharSequence oldQueryText;
    private CharSequence userQuery;

    private ReplaySubject<Boolean> onBackPressedSubscriber = ReplaySubject.create();
    private ReplaySubject<String> onQueryTextChangeSubscriber = ReplaySubject.create();
    private ReplaySubject<String> onQueryTextSubmitSubscriber = ReplaySubject.create();

    private SearchTermsAdapter searchTermsAdapter;

    private SavedState savedState;
    private boolean submit = false;
    private boolean ellipsize = false;
    private boolean allowVoiceSearch;
    private Context context;

    public Observable<Pair<View,Integer>> removeSearchTermEvent() {
        return searchTermsAdapter.removeSearchTermEvent();
    }

    public SearchDialogView(Context context) {
        this(context, null);
    }

    public SearchDialogView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);

        this.context = context;

        initiateView();

    }

    private void initiateView() {

        LayoutInflater.from(context).inflate(R.layout.search_terms_view, this, true);
        searchLayout = findViewById(R.id.search_terms_container);
        suggestionsListView = (ListView) searchLayout.findViewById(R.id.search_terms_list);
        searchEditText = (EditText) searchLayout.findViewById(R.id.searchEditText);

        backBtn = (ImageButton) searchLayout.findViewById(R.id.up_btn);
        emptyBtn = (ImageButton) searchLayout.findViewById(R.id.action_empty_btn);

        searchEditText.setOnClickListener(mOnClickListener);
        backBtn.setOnClickListener(mOnClickListener);
        emptyBtn.setOnClickListener(mOnClickListener);
        allowVoiceSearch = false;

        initSearchView();

    }

    private void initSearchView() {

        suggestionsListView.setVisibility(GONE);

        searchEditTextListeners();

    }

    private void searchEditTextListeners() {

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            onSubmitQuery();
            return true;
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userQuery = s;
                if (s.length() == 0) {
                    startFilter(s);
                    emptyBtn.setVisibility(GONE);
                    showSuggestions(false);
                } else {
                    dismissSuggestions();
                    emptyBtn.setVisibility(VISIBLE);
                    SearchDialogView.this.onTextChanged(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void showKeyboard() {
        searchEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
    }

    private void startFilter(CharSequence s) {
        if (searchTermsAdapter != null && searchTermsAdapter instanceof Filterable) {
            ((Filterable) searchTermsAdapter).getFilter().filter(s, SearchDialogView.this);
        }
    }

    private final OnClickListener mOnClickListener = new OnClickListener() {

        public void onClick(View v) {
            if (v == backBtn) {
                closeSearch();
            } else if (v == voiceBtn) {
                onVoiceClicked();
            } else if (v == emptyBtn) {
                clearSearchAndDismissSuggestions();
            } else if (v == searchEditText) {
                showSuggestions(false);
            }
        }
    };

    private void onVoiceClicked() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);    // setting recognition model, optimized for short phrases – search queries
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);    // quantity of results we want to receive
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, REQUEST_VOICE);
        }
    }

    private void onTextChanged(CharSequence newText) {
        CharSequence text = searchEditText.getText();
        userQuery = text;

        if (!TextUtils.equals(newText, oldQueryText)) {
            onQueryTextChangeSubscriber.onNext(newText.toString());
        }
        oldQueryText = newText.toString();
    }

    public Observable<Boolean> onBackPressed() {
        return onBackPressedSubscriber.asObservable();
    }

    private void onSubmitQuery() {
        CharSequence query = searchEditText.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
                onQueryTextSubmitSubscriber.onNext(formatQuery(query));
                onBackPressedSubscriber.onNext(true);
        }
    }

    @NonNull
    public String formatQuery(CharSequence query) {
        return query.toString().substring(0, 1).trim() + query.toString().substring(1, query.length()).trim();
    }

    private boolean isVoiceAvailable() {
        if (isInEditMode()) {
            return true;
        }
        PackageManager pm = getContext().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        return activities.size() == 0;
    }

    public void setHint(CharSequence hint) {
        searchEditText.setHint(hint);
    }

    public void setVoiceIcon(Drawable drawable) {
        voiceBtn.setImageDrawable(drawable);
    }

    public void setCloseIcon(Drawable drawable) {
        emptyBtn.setImageDrawable(drawable);
    }

    public void setVoiceSearch(boolean voiceSearch) {
        allowVoiceSearch = voiceSearch;
    }

    /**
     * Set Suggest List OnItemClickListener
     *
     * @param listener
     */
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        suggestionsListView.setOnItemClickListener(listener);
    }

    /**
     * Set Adapter for suggestions list. Should implement Filterable.
     *
     * @param adapter
     */
    public void setAdapter(ListAdapter adapter) {
        suggestionsListView.setAdapter(adapter);
        startFilter(searchEditText.getText());
    }

    /**
     * Set Adapter for suggestions list with the given suggestion array
     *
     * @param suggestions array of suggestions
     */
    public void setSuggestions(String[] suggestions) {
        if (suggestions != null) {

            searchTermsAdapter = new SearchTermsAdapter(context, suggestions);
            setAdapter(searchTermsAdapter);

            setOnItemClickListener((parent, view, position, id) ->
                    setQuery((String) searchTermsAdapter.getItem(position), submit));

            ((Filterable) searchTermsAdapter).getFilter().filter("s", this);

        }
    }

    /**
     * Dismiss the suggestions list.
     */
    public void dismissSuggestions() {
        if (suggestionsListView.getVisibility() == VISIBLE) {
            suggestionsListView.setVisibility(GONE);
        }
    }


    /**
     * Calling this will set the query to search text box. if submit is true, it'll submit the query.
     *
     * @param query
     * @param submit
     */
    public void setQuery(CharSequence query, boolean submit) {
        searchEditText.setText(query);
        if (query != null) {
            searchEditText.setSelection(searchEditText.length());
            userQuery = query;
        }
        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
    }

    /**
     * Return true if search is open
     *
     * @return
     */
    public boolean isSearchOpen() {
        return mIsSearchOpen;
    }

    /**
     * Open Search View. If animate is true, Animate the showing of the view.
     */
    public void showSearch() {
        if (isSearchOpen()) {
            return;
        }

        searchLayout.setVisibility(VISIBLE);

        searchEditText.requestFocus();

        mIsSearchOpen = true;
    }

    public Observable<String> onQueryTextChangeEvent(){
        return onQueryTextChangeSubscriber.asObservable();
    }

    public Observable<String> onQueryTextSubmitEvent(){
        return onQueryTextSubmitSubscriber.asObservable();
    }

    /**
     * Ellipsize suggestions longer than one line.
     *
     * @param ellipsize
     */
    public void setEllipsize(boolean ellipsize) {
        this.ellipsize = ellipsize;
    }

    @Override
    public void onFilterComplete(int count) {
        // Useful for AutoComplete
        if (count > 0) {
        } else {
        }
    }

    @Override
    public void clearFocus() {
        super.clearFocus();
        searchEditText.clearFocus();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        savedState = new SavedState(superState);
        savedState.query = userQuery != null ? userQuery.toString() : null;
        savedState.isSearchOpen = this.mIsSearchOpen;

        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        savedState = (SavedState) state;

        if (savedState.isSearchOpen) {
            showSearch();
            setQuery(savedState.query, false);
        }

        super.onRestoreInstanceState(savedState.getSuperState());
    }

    static class SavedState extends BaseSavedState {
        String query;
        boolean isSearchOpen;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.query = in.readString();
            this.isSearchOpen = in.readInt() == 1;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(query);
            out.writeInt(isSearchOpen ? 1 : 0);
        }

        //required field that makes Parcelables from a Parcel
        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    public void clearSearchAndDismissSuggestions() {
        searchEditText.setText(null);
    }

    public void showSuggestions(boolean animate) {

        if (searchTermsAdapter != null && !searchTermsAdapter.isEmpty()) {
            suggestionsListView.setVisibility(VISIBLE);
            if (animate) {
                Animation slideInFromUp = AnimationUtils.loadAnimation(context, R.anim.slide_in_from_top_125);
                suggestionsListView.startAnimation(slideInFromUp);
            }
        }
    }

    public void closeSearch() {
        if (!Utils.isLollipopOrAbove()) {
            hideKeyboard();
        }
        if (searchTermsAdapter != null && !searchTermsAdapter.isEmpty()) {
            Animation slideOutToUp = AnimationUtils.loadAnimation(context, R.anim.slide_out_from_top_125);
            suggestionsListView.startAnimation(slideOutToUp);
            slideOutToUp.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    onBackPressedSubscriber.onNext(false);
                    suggestionsListView.setVisibility(GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            suggestionsListView.setVisibility(GONE);
            onBackPressedSubscriber.onNext(false);
        }
    }

    public void notifyDataSetChanged(ArrayList<String> suggestions) {
        searchTermsAdapter.notifyDataSetChanged(suggestions);
    }
}
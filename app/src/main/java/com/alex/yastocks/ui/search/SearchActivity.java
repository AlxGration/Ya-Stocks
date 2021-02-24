package com.alex.yastocks.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.alex.yastocks.R;
import com.alex.yastocks.models.PrefsManager;
import com.alex.yastocks.ui.search.empty.SearchEmptyFragment;

public class SearchActivity extends AppCompatActivity {

    EditText etSearch;
    ImageButton btnBack, btnClear;

    private SearchListFragment listFragment;
    private SearchEmptyFragment emptyFragment;
    private PrefsManager prefsManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        prefsManager = new PrefsManager(this);

        btnClear = findViewById(R.id.img_btn_clear);
        btnBack = findViewById(R.id.img_btn_back);
        etSearch = findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                showFragment(charSequence.length());
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String s = textView.getText().toString();
                    if ( ! s.isEmpty()) {
                        prefsManager.saveLastSearchString(s);
                    }
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        btnClear.setOnClickListener(view -> {
            etSearch.setText("");
        });


        btnBack.setOnClickListener(view -> {
            etSearch.clearFocus();
            onBackPressed();
        });


        if (savedInstanceState == null) {
            showFragment(0);
        }

        etSearch.requestFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showFragment(int len){

        Fragment fragment;

        if (len > 0){
            fragment = getListFragment();
            btnClear.setVisibility(View.VISIBLE);
        }else{
            fragment = getEmptyFragment();
            btnClear.setVisibility(View.INVISIBLE);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow();
    }

    private Fragment getEmptyFragment(){
        if (emptyFragment == null){
            emptyFragment = new SearchEmptyFragment();
        }
        return emptyFragment;
    }
    private Fragment getListFragment(){
        if (listFragment == null){
            listFragment = new SearchListFragment();
        }
        return listFragment;
    }

    public void startSearchWith(String word){
        etSearch.setText(word);
        etSearch.clearFocus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
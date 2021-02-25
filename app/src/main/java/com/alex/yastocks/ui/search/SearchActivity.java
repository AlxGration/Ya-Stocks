package com.alex.yastocks.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alex.yastocks.R;
import com.alex.yastocks.db.DBManager;
import com.alex.yastocks.db.PrefsManager;
import com.alex.yastocks.db.RealmManager;
import com.alex.yastocks.models.StocksListRecyclerAdapter;
import com.alex.yastocks.ui.stock.InfoActivity;

public class SearchActivity extends AppCompatActivity
        implements StocksListRecyclerAdapter.IonItemClickListener {

    EditText etSearch;
    ImageButton btnBack, btnClear;

    private SearchListFragment listFragment;
    private SearchEmptyFragment emptyFragment;
    private PrefsManager prefsManager;

    DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        prefsManager = new PrefsManager(this);
        db = new RealmManager();

        btnClear = findViewById(R.id.img_btn_clear);
        btnBack = findViewById(R.id.img_btn_back);
        etSearch = findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                showFragment(charSequence.length());
                showSearchResults(charSequence.toString());
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
                    hideKeyboard();
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

        showKeyboard();
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

    private void showKeyboard(){
        etSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
        etSearch.clearFocus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void startInfoActivityWith(String ticker, String companyName, boolean isSelected) {
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("isSelected", isSelected);
        intent.putExtra("ticker", ticker);
        intent.putExtra("companyName", companyName);

        startActivity(intent);
    }

    private void showSearchResults(String searchRequest){
        if (searchRequest.isEmpty()) return;
        listFragment.setData(db.searchStocks(searchRequest));
    }

    @Override
    protected void onStart() {
        super.onStart();
        String searchRequest = etSearch.getText().toString();
        if ( ! searchRequest.isEmpty()){
            showSearchResults(searchRequest);
        }
    }
}
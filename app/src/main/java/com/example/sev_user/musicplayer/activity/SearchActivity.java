package com.example.sev_user.musicplayer.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.adapter.SearchAdapter;
import com.example.sev_user.musicplayer.constant.Constant;
import com.example.sev_user.musicplayer.model.Album;
import com.example.sev_user.musicplayer.model.Artist;
import com.example.sev_user.musicplayer.model.BaseModel;
import com.example.sev_user.musicplayer.model.Song;
import com.example.sev_user.musicplayer.utils.ImageUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sev_user on 7/23/2016.
 */
public class SearchActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private SearchAdapter adapter;
    private ArrayList<BaseModel> arrayList;
    private SearchView searchView;
    private MenuItem searchItem;

    private ArrayList<Song> songArrayList;
    private ArrayList<Artist> artistArrayList;
    private ArrayList<Album> albumArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                finish();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData() {
        Intent intent = getIntent();
        songArrayList = intent.getParcelableArrayListExtra(Constant.ARRAY_SONG);
        artistArrayList = intent.getParcelableArrayListExtra(Constant.ARRAY_ARTIST);
        albumArrayList = intent.getParcelableArrayListExtra(Constant.ALBUM_ARTIST);

        arrayList = new ArrayList<>();
        adapter = new SearchAdapter(this, arrayList);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_has_search, menu);
        MenuItem searchItem = menu.findItem(R.id.miSearch);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter(songArrayList, albumArrayList, artistArrayList).filter(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter(songArrayList, albumArrayList, artistArrayList).filter(newText);
                return true;
            }
        });

        int searchImgId = android.support.v7.appcompat.R.id.search_button;
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_search);

        // Customize searchview text and hint colors
        int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
        EditText et = (EditText) searchView.findViewById(searchEditId);
        et.setTextColor(Color.WHITE);
        et.setHintTextColor(ContextCompat.getColor(this, R.color.colorGray));
        et.setPadding((int) ImageUtils.convertDpToPixel(-0.5f, this), 0, 0, 0);

        SpannableString string = new SpannableString("  Nhập gì đó...");
        Drawable d = ContextCompat.getDrawable(this, R.drawable.ic_search);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
        string.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        et.setHint(string);

        int closeButtonId = android.support.v7.appcompat.R.id.search_close_btn;
        ImageView closeButtonImage = (ImageView) searchView.findViewById(closeButtonId);
        closeButtonImage.setImageResource(R.drawable.ic_close_24dp);

        searchItem.expandActionView();

        return true;
    }
}

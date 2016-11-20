package com.example.sev_user.musicplayer.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.activity.MainActivity;
import com.example.sev_user.musicplayer.adapter.SearchAdapter;
import com.example.sev_user.musicplayer.callback.SearchAdapterCallback;
import com.example.sev_user.musicplayer.model.Album;
import com.example.sev_user.musicplayer.model.Artist;
import com.example.sev_user.musicplayer.model.BaseModel;
import com.example.sev_user.musicplayer.utils.ImageUtils;
import com.example.sev_user.musicplayer.utils.UIHelpers;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sev_user on 7/23/2016.
 */
public class SearchFragment extends Fragment implements SearchAdapterCallback {
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.layoutNotFound)
    LinearLayout layoutNotFound;

    private SearchAdapter adapter;
    private SearchView searchView;
    private MenuItem searchItem;

    private ArrayList<BaseModel> songArrayList;
    private ArrayList<Artist> artistArrayList;
    private ArrayList<Album> albumArrayList;

    public static SearchFragment newInstance(ArrayList<BaseModel> songArrayList, ArrayList<Artist> artistArrayList,
                                             ArrayList<Album> albumArrayList) {
        SearchFragment fragment = new SearchFragment();
        fragment.setSongArrayList(songArrayList);
        fragment.setArtistArrayList(artistArrayList);
        fragment.setAlbumArrayList(albumArrayList);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initData();
        initView();
    }

    private void initView() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelpers.closeSoftKeyboard(getActivity());
                ((MainActivity) getActivity()).backToMainFragment(SearchFragment.this);
            }
        });
        toolbar.inflateMenu(R.menu.menu_toolbar_has_search);

        MenuItem searchItem = toolbar.getMenu().findItem(R.id.miSearch);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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
        et.setHintTextColor(ContextCompat.getColor(getContext(), R.color.colorGray));
        et.setPadding((int) ImageUtils.convertDpToPixel(-0.5f, getContext()), 0, 0, 0);

        SpannableString string = new SpannableString("  Nhập gì đó...");
        Drawable d = ContextCompat.getDrawable(getContext(), R.drawable.ic_search);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
        string.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        et.setHint(string);

        int closeButtonId = android.support.v7.appcompat.R.id.search_close_btn;
        ImageView closeButtonImage = (ImageView) searchView.findViewById(closeButtonId);
        closeButtonImage.setImageResource(R.drawable.ic_close_24dp);

        UIHelpers.openSoftKeyboard(getContext(), searchView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initData() {
        ArrayList<BaseModel> arrayList = new ArrayList<>();
        adapter = new SearchAdapter(getActivity(), arrayList, this, ((MainActivity) getActivity()));

        recyclerView.setAdapter(adapter);
    }

    public void setSongArrayList(ArrayList<BaseModel> songArrayList) {
        this.songArrayList = songArrayList;
    }

    public void setArtistArrayList(ArrayList<Artist> artistArrayList) {
        this.artistArrayList = artistArrayList;
    }

    public void setAlbumArrayList(ArrayList<Album> albumArrayList) {
        this.albumArrayList = albumArrayList;
    }

    @Override
    public void checkResultFilter(boolean hasResult) {
        if (hasResult) {
            layoutNotFound.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            layoutNotFound.setVisibility(View.VISIBLE);
        }
    }

    public SparseIntArray getMapResult() {
        return adapter.getMapResult();
    }
}

package com.example.sev_user.musicplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.activity.ResultActivity;
import com.example.sev_user.musicplayer.callback.SearchAdapterCallback;
import com.example.sev_user.musicplayer.constant.Constant;
import com.example.sev_user.musicplayer.model.Album;
import com.example.sev_user.musicplayer.model.Artist;
import com.example.sev_user.musicplayer.model.BaseModel;
import com.example.sev_user.musicplayer.model.Header;
import com.example.sev_user.musicplayer.model.Song;
import com.example.sev_user.musicplayer.viewholder.AlbumListViewHolder;
import com.example.sev_user.musicplayer.viewholder.ArtistViewHolder;
import com.example.sev_user.musicplayer.viewholder.EmptyViewHolder;
import com.example.sev_user.musicplayer.viewholder.HeaderViewHolder;
import com.example.sev_user.musicplayer.viewholder.ShowAllViewHolder;
import com.example.sev_user.musicplayer.viewholder.SongViewHolder;

import java.util.ArrayList;

import static com.example.sev_user.musicplayer.R.string.song;

/**
 * Created by DELL on 11/19/2016.
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_VIEW_TYPE_ITEM_SONG = 0;
    private static final int ITEM_VIEW_TYPE_SHOW_ALL = 1;
    private static final int ITEM_VIEW_TYPE_HEADER = 2;
    private static final int ITEM_VIEW_TYPE_ITEM_ARTIST = 3;
    private static final int ITEM_VIEW_TYPE_ITEM_ALBUM = 4;
    private Context context;

    private ArrayList<BaseModel> arrayList;
    private ArrayList<Song> songArrayListResult;
    private ArrayList<Album> albumArrayListResult;
    private ArrayList<Artist> artistArrayListResult;
    private SearchAdapterCallback callback;
    private SparseIntArray mapSong = new SparseIntArray();
    private CharSequence query;

    public SearchAdapter(Context context, ArrayList<BaseModel> arrayList, SearchAdapterCallback searchAdapterCallback) {
        this.context = context;
        this.arrayList = arrayList;
        callback = searchAdapterCallback;
        songArrayListResult = new ArrayList<>();
        albumArrayListResult = new ArrayList<>();
        artistArrayListResult = new ArrayList<>();
    }

    public SearchAdapter(Context context, ArrayList<BaseModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;
        View view;
        switch (viewType) {
            case ITEM_VIEW_TYPE_ITEM_ALBUM: {
                view = inflater.inflate(R.layout.view_holder_song, parent, false);
                viewHolder = new AlbumListViewHolder(view);
                break;
            }
            case ITEM_VIEW_TYPE_ITEM_SONG: {
                view = inflater.inflate(R.layout.view_holder_song, parent, false);
                viewHolder = new SongViewHolder(view);
                break;
            }
            case ITEM_VIEW_TYPE_ITEM_ARTIST: {
                view = inflater.inflate(R.layout.view_holder_artist, parent, false);
                viewHolder = new ArtistViewHolder(view);
                break;
            }
            case ITEM_VIEW_TYPE_HEADER: {
                view = inflater.inflate(R.layout.view_holder_header, parent, false);
                viewHolder = new HeaderViewHolder(view);
                break;
            }
            case ITEM_VIEW_TYPE_SHOW_ALL: {
                view = inflater.inflate(R.layout.view_holder_show_all, parent, false);
                viewHolder = new ShowAllViewHolder(view);
                break;
            }
            default: {
                view = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
                viewHolder = new EmptyViewHolder(view);
                break;
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (arrayList.get(position).getTypeModel()) {
            case BaseModel.TYPE_ALBUM: {
                ((AlbumListViewHolder) holder).setupViewHolder(((Album) arrayList.get(position)), position);
                break;
            }
            case BaseModel.TYPE_ARTIST: {
                ((ArtistViewHolder) holder).setupViewHolder(((Artist) arrayList.get(position)));
                break;
            }
            case BaseModel.TYPE_SONG: {
                ((SongViewHolder) holder).setupViewHolder((Song) arrayList.get(position)
                        , mapSong.get(((Song) arrayList.get(position)).getId()));
                break;
            }
            case BaseModel.TYPE_HEADER: {
                if (!((Header) arrayList.get(position)).getHeader().contains("Hiển thị")) {
                    ((HeaderViewHolder) holder).setupViewHolder((Header) arrayList.get(position));
                } else {
                    final String header = ((Header) arrayList.get(position)).getHeader();
                    ((ShowAllViewHolder) holder).setupData(header);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, ResultActivity.class);
                            if (header.equals(context.getString(R.string.show_all_album))){
                                intent.putParcelableArrayListExtra(Constant.ARRAY, albumArrayListResult);
                            } else if (header.equals(context.getString(R.string.show_all_artist))) {
                                intent.putParcelableArrayListExtra(Constant.ARRAY, artistArrayListResult);
                            } else {
                                intent.putParcelableArrayListExtra(Constant.ARRAY, songArrayListResult);
                            }
                            intent.putExtra(Constant.QUERY, query);
                            intent.putExtra(Constant.TYPE, header.substring(16));
                            context.startActivity(intent);
                        }
                    });
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (arrayList.get(position).getTypeModel()) {
            case BaseModel.TYPE_SONG: {
                return ITEM_VIEW_TYPE_ITEM_SONG;
            }
            case BaseModel.TYPE_ALBUM: {
                return ITEM_VIEW_TYPE_ITEM_ALBUM;
            }
            case BaseModel.TYPE_ARTIST: {
                return ITEM_VIEW_TYPE_ITEM_ARTIST;
            }
            case BaseModel.TYPE_HEADER: {
                if (((Header) arrayList.get(position)).getHeader().contains("Hiển thị")) {
                    return ITEM_VIEW_TYPE_SHOW_ALL;
                }
                return ITEM_VIEW_TYPE_HEADER;
            }
            default:
                return 1;
        }
    }

    public Filter getFilter(final ArrayList<BaseModel> songArrayList, final ArrayList<Album> albumArrayList, final ArrayList<Artist> artistArrayList) {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                query = charSequence;
                FilterResults filterResults = new FilterResults();
                if (charSequence.length() == 0) {
                    filterResults.values = new ArrayList<BaseModel>();
                    filterResults.count = 0;
                } else {
                    filterResults.values = searchAll(songArrayList, albumArrayList, artistArrayList, charSequence);
                    filterResults.count = ((ArrayList) filterResults.values).size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                arrayList.clear();
                arrayList.addAll((ArrayList<BaseModel>) filterResults.values);

                callback.checkResultFilter(arrayList.size() > 0);

                notifyDataSetChanged();
            }
        };
    }

    private ArrayList<BaseModel> searchAll(ArrayList<BaseModel> songArrayList, ArrayList<Album> albumArrayList, ArrayList<Artist> artistArrayList, CharSequence charSequence) {
        songArrayListResult.clear();
        albumArrayListResult.clear();
        artistArrayListResult.clear();
        mapSong.clear();

        arrayList.clear();
        for (int i = 0, size = songArrayList.size(); i < size; i++) {
            if (songArrayList.get(i).getTypeModel() == BaseModel.TYPE_SONG
                    && ((Song) songArrayList.get(i)).getName().contains(charSequence)) {
                songArrayListResult.add((Song) songArrayList.get(i));
                mapSong.put(((Song) songArrayList.get(i)).getId(), i);
            }
        }
        for (Album album : albumArrayList) {
            if (album.getAlbumName().contains(charSequence)) {
                albumArrayListResult.add(album);
            }
        }
        for (Artist artist : artistArrayList) {
            if (artist.getName().contains(charSequence)) {
                artistArrayListResult.add(artist);
            }
        }

        ArrayList<BaseModel> arrayListResult = new ArrayList<>();
        if (songArrayListResult.size() > 0) {
            arrayListResult.add(new Header(context.getString(song)));
        }
        if (songArrayListResult.size() < 4) {
            for (int i = 0, size = songArrayListResult.size() - 1; i < size; i++) {
                songArrayListResult.get(i).setHasLine(true);
            }
            if (songArrayListResult.size() > 0) {
                albumArrayList.get(songArrayListResult.size() - 1).setHasLine(false);
                arrayListResult.addAll(songArrayListResult);
            }
        } else {
            songArrayListResult.get(0).setHasLine(true);
            arrayListResult.add(songArrayListResult.get(0));
            songArrayListResult.get(1).setHasLine(true);
            arrayListResult.add(songArrayListResult.get(1));
            songArrayListResult.get(2).setHasLine(false);
            arrayListResult.add(songArrayListResult.get(2));
            arrayListResult.add(new Header(context.getString(R.string.show_all_song)));
        }

        if (artistArrayListResult.size() > 0) {
            arrayListResult.add(new Header(context.getString(R.string.artist)));
        }
        if (artistArrayListResult.size() < 4) {
            for (int i = 0, size = artistArrayListResult.size() - 1; i < size; i++) {
                artistArrayListResult.get(i).setHasLine(true);
            }
            if (artistArrayListResult.size() > 0) {
                artistArrayList.get(artistArrayListResult.size() - 1).setHasLine(false);
                arrayListResult.addAll(artistArrayListResult);
            }
        } else {
            artistArrayListResult.get(0).setHasLine(true);
            arrayListResult.add(artistArrayListResult.get(0));
            artistArrayListResult.get(1).setHasLine(true);
            arrayListResult.add(artistArrayListResult.get(1));
            artistArrayListResult.get(2).setHasLine(false);
            arrayListResult.add(artistArrayListResult.get(2));
            arrayListResult.add(new Header(context.getString(R.string.show_all_artist)));
        }

        if (albumArrayListResult.size() > 0) {
            arrayListResult.add(new Header(context.getString(R.string.album)));
        }
        if (albumArrayListResult.size() < 4) {
            for (int i = 0, size = albumArrayListResult.size() - 1; i < size; i++) {
                albumArrayListResult.get(i).setHasLine(true);
            }
            if (albumArrayListResult.size() > 0) {
                albumArrayList.get(albumArrayListResult.size() - 1).setHasLine(false);
                arrayListResult.addAll(albumArrayListResult);
            }
        } else {
            albumArrayListResult.get(0).setHasLine(true);
            arrayListResult.add(albumArrayListResult.get(0));
            albumArrayListResult.get(0).setHasLine(true);
            arrayListResult.add(albumArrayListResult.get(1));
            albumArrayListResult.get(0).setHasLine(false);
            arrayListResult.add(albumArrayListResult.get(2));
            arrayListResult.add(new Header(context.getString(R.string.show_all_album)));
        }
        return arrayListResult;
    }
}

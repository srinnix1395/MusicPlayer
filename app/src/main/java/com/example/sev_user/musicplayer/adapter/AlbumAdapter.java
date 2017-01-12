package com.example.sev_user.musicplayer.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.custom.AlbumDiffCallback;
import com.example.sev_user.musicplayer.model.Album;
import com.example.sev_user.musicplayer.utils.ImageUtils;
import com.example.sev_user.musicplayer.viewholder.AlbumGridViewHolder;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sev_user on 7/15/2016.
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumGridViewHolder> {
	public static final int ASCENDING = 0;
	public static final int DESCENDING = 1;
	public static final int DEFAULT_SORT = ASCENDING;
	
	private ArrayList<Album> arrayList;
	private int margin;
	private int noMargin;
	
	public AlbumAdapter(Context context, ArrayList<Album> arrayList) {
		this.arrayList = arrayList;
		margin = (int) ImageUtils.convertDpToPixel(15, context);
		noMargin = (int) ImageUtils.convertDpToPixel(1, context);
	}
	
	@Override
	public AlbumGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view = inflater.inflate(R.layout.item_album, parent, false);
		return new AlbumGridViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(AlbumGridViewHolder holder, int position) {
		holder.setupViewHolder(arrayList.get(position), position, margin, noMargin);
	}
	
	@Override
	public int getItemCount() {
		return arrayList.size();
	}
	
	public void swapItems(final ArrayList<Album> newList) {
		Single.fromCallable(new Callable<DiffUtil.DiffResult>() {
			@Override
			public DiffUtil.DiffResult call() throws Exception {
				AlbumDiffCallback diffCallback = new AlbumDiffCallback(newList, arrayList);
				return DiffUtil.calculateDiff(diffCallback);
			}
		}).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new SingleSubscriber<DiffUtil.DiffResult>() {
					@Override
					public void onSuccess(DiffUtil.DiffResult value) {
						arrayList.clear();
						arrayList.addAll(newList);
						value.dispatchUpdatesTo(AlbumAdapter.this);
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								notifyDataSetChanged();
							}
						}, 300);
					}
					
					@Override
					public void onError(Throwable error) {
						
					}
				});
	}
}

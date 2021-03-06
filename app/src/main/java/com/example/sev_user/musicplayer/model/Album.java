package com.example.sev_user.musicplayer.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sev_user on 7/15/2016.
 */
public class Album extends BaseModel implements Parcelable{
    private int id;
    private String image;
    private String albumName;
    private String artistName;
    private int numberOfSong;
    private int firstYear;
	private int placeHolder;
	
	public Album(int id, String image, String albumName, String artistName, int numberOfSong, int firstYear, int placeHolder) {
		this.id = id;
		this.image = image;
		this.albumName = albumName;
		this.artistName = artistName;
		this.numberOfSong = numberOfSong;
		this.firstYear = firstYear;
		this.placeHolder = placeHolder;
	}
	
	public Album(int id, String image, String albumName, String artistName, int numberOfSong
			, int firstYear, boolean hasLine, int placeHolder) {
        this.id = id;
        this.image = image;
        this.albumName = albumName;
        this.artistName = artistName;
        this.numberOfSong = numberOfSong;
        this.firstYear = firstYear;
		this.placeHolder = placeHolder;
		this.hasLine = hasLine;
	}

    protected Album(Parcel in) {
        id = in.readInt();
        image = in.readString();
        albumName = in.readString();
        artistName = in.readString();
        numberOfSong = in.readInt();
        firstYear = in.readInt();
		placeHolder = in.readInt();
        hasLine = in.readByte() != 0;
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getNumberOfSong() {
        return numberOfSong;
    }

    public int getFirstYear() {
        return firstYear;
    }
	
	public int getPlaceHolder() {
		return placeHolder;
	}
	
	@Override
    public int getTypeModel() {
        return BaseModel.TYPE_ALBUM;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(image);
        parcel.writeString(albumName);
        parcel.writeString(artistName);
        parcel.writeInt(numberOfSong);
        parcel.writeInt(firstYear);
		parcel.writeInt(placeHolder);
        parcel.writeByte((byte) (hasLine ? 1 : 0));
    }
}

package com.example.maciu.a1stapp.object;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by maciu on 03.07.2017.
 */
public class Card implements Parcelable{
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("activity_id")
    @Expose
    private Integer activityId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("score")
    @Expose
    private Double score;
    @SerializedName("recommended")
    @Expose
    private Integer recommended;
    @SerializedName("thumb_id")
    @Expose
    private Integer thumbId;
    @SerializedName("country")
    @Expose
    private Object country;
    @SerializedName("province")
    @Expose
    private Object province;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("public")
    @Expose
    private Integer _public;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("level")
    @Expose
    private Integer level;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("add_date")
    @Expose
    private Integer addDate;
    @SerializedName("create_date")
    @Expose
    private Integer createDate;
    @SerializedName("modification_date")
    @Expose
    private Integer modificationDate;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getRecommended() {
        return recommended;
    }

    public void setRecommended(Integer recommended) {
        this.recommended = recommended;
    }

    public Integer getThumbId() {
        return thumbId;
    }

    public void setThumbId(Integer thumbId) {
        this.thumbId = thumbId;
    }

    public Object getCountry() {
        return country;
    }

    public void setCountry(Object country) {
        this.country = country;
    }

    public Object getProvince() {
        return province;
    }

    public void setProvince(Object province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getPublic() {
        return _public;
    }

    public void setPublic(Integer _public) {
        this._public = _public;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getAddDate() {
        return addDate;
    }

    public void setAddDate(Integer addDate) {
        this.addDate = addDate;
    }

    public Integer getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Integer createDate) {
        this.createDate = createDate;
    }

    public Integer getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Integer modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public class User implements Parcelable {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("login")
        @Expose
        private String login;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        protected User(Parcel in) {
            id = in.readByte() == 0x00 ? null : in.readInt();
            login = in.readByte() == 0x00 ? null : in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            if (id == null) {
                parcel.writeByte((byte) (0x00));
            } else {
                parcel.writeByte((byte) (0x01));
                parcel.writeInt(id);
            }
            parcel.writeString(login);
        }
        public final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
            @Override
            public User createFromParcel(Parcel in) {
                return new User(in);
            }

            @Override
            public User[] newArray(int size) {
                return new User[size];
            }
        };
    }

    protected Card(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        activityId = in.readByte() == 0x00 ? null : in.readInt();
        name = in.readString();
        slug = in.readString();
        rating = in.readByte() == 0x00 ? null : in.readInt();
        score = in.readByte() == 0x00 ? null : in.readDouble();
        recommended = in.readByte() == 0x00 ? null : in.readInt();
        thumbId = in.readByte() == 0x00 ? null : in.readInt();
        country = (Object) in.readValue(Object.class.getClassLoader());
        province = (Object) in.readValue(Object.class.getClassLoader());
        city = in.readString();
        _public = in.readByte() == 0x00 ? null : in.readInt();
        distance = in.readByte() == 0x00 ? null : in.readDouble();
        duration = in.readByte() == 0x00 ? null : in.readInt();
        level = in.readByte() == 0x00 ? null : in.readInt();
        user = (User) in.readValue(User.class.getClassLoader());
        addDate = in.readByte() == 0x00 ? null : in.readInt();
        createDate = in.readByte() == 0x00 ? null : in.readInt();
        modificationDate = in.readByte() == 0x00 ? null : in.readInt();
        latitude = in.readByte() == 0x00 ? null : in.readDouble();
        longitude = in.readByte() == 0x00 ? null : in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        if (activityId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(activityId);
        }
        dest.writeString(name);
        dest.writeString(slug);
        if (rating == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(rating);
        }
        if (score == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(score);
        }
        if (recommended == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(recommended);
        }
        if (thumbId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(thumbId);
        }
        dest.writeValue(country);
        dest.writeValue(province);
        dest.writeString(city);
        if (_public == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(_public);
        }
        if (distance == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(distance);
        }
        if (duration == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(duration);
        }
        if (level == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(level);
        }
        //dest.writeValue(user);
        if (addDate == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(addDate);
        }
        if (createDate == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(createDate);
        }
        if (modificationDate == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(modificationDate);
        }
        if (latitude == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(longitude);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };
}

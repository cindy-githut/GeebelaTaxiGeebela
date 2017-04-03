package habbitatvalley.com.geebelataxigeebela.models;


import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

public class ReportsListItem implements Parcelable {

    public String friendsId;
    public String FriendsPicture;
    public String friendsName;
    public String friendUrl;
    public String mutualFriends, photoAttached;

    public String getPhotoAttached() {
        return photoAttached;
    }

    public void setPhotoAttached(String photoAttached) {
        this.photoAttached = photoAttached;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String totalFriends;
    public String username, title, body, time;
    public int total;
    public Boolean superStar;
    public String status;
    public Boolean public_figure;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMultiple_photos() {
        return multiple_photos;
    }

    public void setMultiple_photos(String multiple_photos) {
        this.multiple_photos = multiple_photos;
    }

    String multiple_photos;

    public Boolean getShowMultiple() {
        return showMultiple;
    }

    public void setShowMultiple(Boolean showMultiple) {
        this.showMultiple = showMultiple;
    }

    public Boolean showMultiple;
    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String profilePicture;

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String reportType;

    public Boolean getShowUpdateStatus() {
        return showUpdateStatus;
    }

    public void setShowUpdateStatus(Boolean showUpdateStatus) {
        this.showUpdateStatus = showUpdateStatus;
    }

    public Boolean showUpdateStatus;

    public String getSpazaAddress() {
        return spazaAddress;
    }

    public void setSpazaAddress(String spazaAddress) {
        this.spazaAddress = spazaAddress;
    }

    String spazaAddress;


    public Boolean getPublic_figure() {
        return public_figure;
    }

    public void setPublic_figure(Boolean public_figure) {
        this.public_figure = public_figure;
    }

    public Boolean getSuperStar() {
        return superStar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSuperStar(Boolean superStar) {
        this.superStar = superStar;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ImageView getPublic_figure_icon() {
        return public_figure_icon;
    }

    public void setPublic_figure_icon(ImageView public_figure_icon) {
        this.public_figure_icon = public_figure_icon;
    }

    public ImageView public_figure_icon;

    public String getPublicFigure() {
        return publicFigure;
    }

    public void setPublicFigure(String publicFigure) {
        this.publicFigure = publicFigure;
    }

    public String publicFigure;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ReportsListItem() {

    }
    private ReportsListItem(Parcel parcel) {
        friendsId = parcel.readString();
        FriendsPicture = parcel.readString();
        friendsName = parcel.readString();
        friendUrl = parcel.readString();
        mutualFriends = parcel.readString();
        totalFriends = parcel.readString();
    }

    public String getTotalFriends() {
        return totalFriends;
    }

    public void setTotalFriends(String totalFriends) {
        this.totalFriends = totalFriends;
    }

    public String getMutualFriends() {
        return mutualFriends;
    }

    public void setMutualFriends(String mutualFriends) {
        this.mutualFriends = mutualFriends;
    }

    public String getFriendUrl() {

        return friendUrl;
    }

    public void setFriendUrl(String friendUrl) {
        this.friendUrl = friendUrl;
    }

    public String getFriendsName() {
        return friendsName;
    }

    public void setFriendsName(String friendsName) {
        this.friendsName = friendsName;
    }

    public String getFriendsId() {
        return friendsId;
    }

    public String getFriendsPicture() {
        return FriendsPicture;
    }

    public void setFriendsPicture(String friendsPicture) {
        FriendsPicture = friendsPicture;
    }

    public void setFriendsId(String friendsId) {
        this.friendsId = friendsId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(friendsId);
        parcel.writeString(FriendsPicture);
        parcel.writeString(friendsName);
        parcel.writeString(friendUrl);
        parcel.writeString(mutualFriends);
        parcel.writeString(totalFriends);
    }

    public static final Creator<ReportsListItem> CREATOR = new Creator<ReportsListItem>() {
        public ReportsListItem createFromParcel(Parcel parcel) {
            return new ReportsListItem(parcel);
        }

        public ReportsListItem[] newArray(int size) {
            return new ReportsListItem[size];
        }
    };
}
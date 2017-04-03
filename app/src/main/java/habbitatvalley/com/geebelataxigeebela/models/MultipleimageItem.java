package habbitatvalley.com.geebelataxigeebela.models;

import org.json.JSONArray;

/**
 * Created by cindymbonani on 15/10/05.
 */
public class MultipleimageItem {

    String photoid, photourl, jsonobject,prayersAttachments;
    String[] allPhotos;
    JSONArray photos;

    public Boolean getPrayersAttachment() {
        return prayersAttachment;
    }

    public void setPrayersAttachment(Boolean prayersAttachment) {
        this.prayersAttachment = prayersAttachment;
    }

    Boolean prayersAttachment;

    public JSONArray getPhotos() {
        return photos;
    }

    public void setPhotos(JSONArray photos) {
        this.photos = photos;
    }

    public String[] getAllPhotos() {
        return allPhotos;
    }

    public void setAllPhotos(String[] allPhotos) {
        this.allPhotos = allPhotos;
    }

    public String getJsonobject() {
        return jsonobject;
    }

    public void setJsonobject(String jsonobject) {
        this.jsonobject = jsonobject;
    }

    public String getPhotoid() {
        return photoid;
    }

    public void setPhotoid(String photoid) {
        this.photoid = photoid;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }
}
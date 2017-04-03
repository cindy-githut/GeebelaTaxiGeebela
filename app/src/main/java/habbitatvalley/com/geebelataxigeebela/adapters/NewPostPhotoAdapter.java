package habbitatvalley.com.geebelataxigeebela.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.io.File;
import java.util.ArrayList;
import habbitatvalley.com.geebelataxigeebela.R;
import me.iwf.photopicker.PhotoPagerActivity;
import me.iwf.photopicker.PhotoPreview;
import me.iwf.photopicker.fragment.ImagePagerFragment;
import me.iwf.photopicker.utils.AndroidLifecycleUtils;


public class NewPostPhotoAdapter extends RecyclerView.Adapter<NewPostPhotoAdapter.PhotoViewHolder> {

  private ArrayList<String> photoPaths = new ArrayList<String>();
  private LayoutInflater inflater;

  private Context mContext;
  private ImagePagerFragment pagerFragment;

  public NewPostPhotoAdapter(Context mContext, ArrayList<String> photoPaths) {
    this.photoPaths = photoPaths;
    this.mContext = mContext;
    inflater = LayoutInflater.from(mContext);

  }


  @Override
  public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = inflater.inflate(R.layout.selectedimages, parent, false);
    return new PhotoViewHolder(itemView);
  }


  @Override
  public void onBindViewHolder(final PhotoViewHolder holder, final int position) {

    Uri uri = Uri.fromFile(new File(photoPaths.get(position)));

    boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(holder.ivPhoto.getContext());

    if (canLoadImage) {
      Glide.with(mContext)
              .load(uri)
              .centerCrop()
              .thumbnail(0.1f)
              .placeholder(R.drawable.photo_placeholder)
              .error(me.iwf.photopicker.R.drawable.__picker_ic_broken_image_black_48dp)
              .into(holder.ivPhoto);
    }

    holder.vSelected.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        PhotoPreview.builder()
                .setPhotos(photoPaths)
                .setCurrentItem(position)
                .start((Activity)mContext);

      }
    });
  }


  @Override
  public int getItemCount() {
    return photoPaths.size();
  }


  public static class PhotoViewHolder extends RecyclerView.ViewHolder {
    private ImageView ivPhoto;
    private View vSelected, imgremove;
    public PhotoViewHolder(View itemView) {
      super(itemView);
      ivPhoto   = (ImageView) itemView.findViewById(me.iwf.photopicker.R.id.iv_photo);
      vSelected = itemView.findViewById(me.iwf.photopicker.R.id.v_selected);

    }
  }

}

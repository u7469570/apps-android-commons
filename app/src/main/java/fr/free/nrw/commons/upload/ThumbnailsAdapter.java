package fr.free.nrw.commons.upload;

import static fr.free.nrw.commons.upload.UploadPresenter.IMAGE_LIMIT;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import fr.free.nrw.commons.utils.ViewUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.free.nrw.commons.R;
import fr.free.nrw.commons.filepicker.UploadableFile;

/**
 * The adapter class for image thumbnails to be shown while uploading.
 */
class ThumbnailsAdapter extends RecyclerView.Adapter<ThumbnailsAdapter.ViewHolder> {
    public  static  Context context;
    List<UploadableFile> uploadableFiles;
    private Callback callback;

    public ThumbnailsAdapter(Callback callback) {
        this.uploadableFiles = new ArrayList<>();
        this.callback = callback;
    }

    /**
     * Sets the data, the media files
     * @param uploadableFiles
     */
    public void setUploadableFiles(
            List<UploadableFile> uploadableFiles) {
        this.uploadableFiles=uploadableFiles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_upload_thumbnail, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return uploadableFiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.rl_container)
        RelativeLayout rlContainer;
        @BindView(R.id.iv_thumbnail)
        SimpleDraweeView background;
        @BindView(R.id.iv_error)
        ImageView ivError;
        @BindView(R.id.ib_delete_image)
        ImageButton ibDeleteImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ibDeleteImage.setOnClickListener(this);
        }

        /**
         * Binds a row item to the ViewHolder
         * @param position
         */
        public void bind(int position) {
            UploadableFile uploadableFile = uploadableFiles.get(position);
            Uri uri = uploadableFile.getMediaUri();
            background.setImageURI(Uri.fromFile(new File(String.valueOf(uri))));
            if (position == callback.getCurrentSelectedFilePosition()) {
                GradientDrawable border = new GradientDrawable();
                border.setShape(GradientDrawable.RECTANGLE);
                border.setStroke(8, context.getResources().getColor(R.color.primaryColor));
                rlContainer.setEnabled(true);
                rlContainer.setClickable(true);
                rlContainer.setAlpha(1.0f);
                rlContainer.setBackground(border);
                if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                    rlContainer.setElevation(10);
                }
            } else {
                rlContainer.setEnabled(false);
                rlContainer.setClickable(false);
                rlContainer.setAlpha(0.7f);
                rlContainer.setBackground(null);
                if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                    rlContainer.setElevation(0);
                }
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ib_delete_image) {
                removeImageAt(getAbsoluteAdapterPosition());
            }
        }

        /**
         * Removes an image from the adapter at the given position
         * @param position Index to remove image from
         */
        private void removeImageAt(int position) {
            if (uploadableFiles.size() > 1) {
            notifyItemRemoved(position);
            } else {
                ViewUtil.showLongToast(context,
                    context.getResources().getString(R.string.upload_delete_last_item_warning));
            }
        }
    }

    /**
     * Callback used to get the current selected file position
     */
    interface Callback {

        int getCurrentSelectedFilePosition();
    }
}

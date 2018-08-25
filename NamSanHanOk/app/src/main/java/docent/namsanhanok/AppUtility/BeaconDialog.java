package docent.namsanhanok.AppUtility;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import docent.namsanhanok.Category.CategoryData;
import docent.namsanhanok.Category.CategoryListActivity;
import docent.namsanhanok.Docent.DocentData;
import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.R;


public class BeaconDialog extends Dialog {

    ImageView newItemImage;
    TextView newItemTitle;
    TextView positiveBtn;
    ImageButton negativeBtn;

    public BeaconDialog(final Context context, final CategoryData categoryData) {
        super(context);
        setContentView(R.layout.beacon_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        positiveBtn = (TextView) findViewById(R.id.positiveBtn);
        negativeBtn = (ImageButton) findViewById(R.id.negativeBtn);
        newItemImage = (ImageView) findViewById(R.id.newItemImage);
        newItemTitle = (TextView) findViewById(R.id.newItemTitle);

        Glide.with(context)
                .load(Environment.getExternalStorageDirectory() + categoryData.category_image_url)
                .apply(new RequestOptions().centerCrop())
                .into(newItemImage);

        newItemTitle.setText(categoryData.category_title);

        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof CategoryListActivity) {
                    ((CategoryListActivity) context).setCategoryContent(categoryData);
                    ((CategoryListActivity) context).setDocentList(categoryData);
                    ((CategoryListActivity) context).stopScan();
                } else {
                    ((HomeActivity) context).moveToCategoryActivity(categoryData);
                }

                dismiss();
            }
        });

        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public BeaconDialog(final Context context, final DocentData docentData) {
        super(context);
        setContentView(R.layout.beacon_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        positiveBtn = (TextView) findViewById(R.id.positiveBtn);
        negativeBtn = (ImageButton) findViewById(R.id.negativeBtn);
        newItemImage = (ImageView) findViewById(R.id.newItemImage);
        newItemTitle = (TextView) findViewById(R.id.newItemTitle);

        Glide.with(context)
                .load(Environment.getExternalStorageDirectory() + docentData.docent_image_url)
                .apply(new RequestOptions().centerCrop())
                .into(newItemImage);

        newItemTitle.setText(docentData.docent_title);

        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof CategoryListActivity) {
                    ((CategoryListActivity) context).moveToDocentActivity(docentData);
                    ((CategoryListActivity) context).stopScan();
                } else {
                    ((HomeActivity) context).moveToDocentActivity(docentData);
                }
                dismiss();
            }
        });

        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

}

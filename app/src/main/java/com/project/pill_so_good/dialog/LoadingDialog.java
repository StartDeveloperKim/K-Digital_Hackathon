package com.project.pill_so_good.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ProgressBar;

public class LoadingDialog {

    private Dialog dialog;

    public LoadingDialog(Activity activity) {
        this.dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(new ProgressBar(activity));
        dialog.setCanceledOnTouchOutside(false);
    }

    public void showLoadingDialog() {
        this.dialog.show();
    }

    public void dismissLoadingDialog() {
        this.dialog.dismiss();
    }
}

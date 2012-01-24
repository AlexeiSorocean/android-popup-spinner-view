package org.mechaevil.android.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/**
 * @author st0le
 */
public class PopupSpinnerDemoActivity extends Activity implements
        OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        LinearLayout ll = (LinearLayout)findViewById(R.id.linearLayout1);
        PopupSpinnerView popupSpinnerView = new PopupSpinnerView(this);
        popupSpinnerView.setTextSize(20);

        final int N = 50;
        int[] ico = new int[N];
        String[] arr = new String[N];
        for(int i = 0; i < arr.length; i++) {
            arr[i] = String.format("Item No. %02d", i);
            ico[i] = R.drawable.icon;
        }
        popupSpinnerView.setItems(arr, ico); //leave out the second param, if you don't need icons
        ll.addView(popupSpinnerView);
    }

    @Override
    public void onClick(View v) {

    }
}
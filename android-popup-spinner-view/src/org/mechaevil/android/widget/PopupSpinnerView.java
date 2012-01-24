package org.mechaevil.android.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

/**
 * @author st0le
 */
public class PopupSpinnerView extends TextView implements OnClickListener,
        OnItemClickListener, OnDismissListener {

    private PopupWindow pw;
    private ListView lv;
    private PopupListItemAdapter adapter;
    private List<PopupListItem> list;
    private int selectedPosition;

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        refreshView();
    }

    private OnClickListener onClickListener = null;
    private int popupHeight, popupWidth;
    private final float[] r = new float[] {10, 10, 10, 10, 10, 10, 10, 10};
    private Drawable myBackgroundDrawable;
    private Drawable listDrawable;
    private float textSize = 20;

    public PopupSpinnerView(Context context) {
        super(context);
        init();
    }

    public PopupSpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PopupSpinnerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        listDrawable = getRoundDrawable();
        myBackgroundDrawable = getRoundDrawable();
        setPadding(8, 8, 8, 8);
        setGravity(Gravity.CENTER_VERTICAL);
        setTextSize(textSize);
        setTextColor(Color.BLACK);
        setOnClickListener(this);
        setBackgroundDrawable(myBackgroundDrawable);
        popupHeight = 200;
        popupWidth = 50;
    }

    private Drawable getRoundDrawable() {
        ShapeDrawable drawable = new ShapeDrawable();
        drawable.getPaint().setColor(Color.WHITE);
        drawable.setShape(new RoundRectShape(r, null, null));
        return drawable;
    }

    private void setAdapter(PopupListItemAdapter adapter) {
        if(adapter != null) {
            this.adapter = adapter;
            if(lv == null)
                lv = new ListView(getContext());
            lv.setCacheColorHint(0);
            lv.setBackgroundDrawable(listDrawable);
            lv.setAdapter(adapter);
            lv.setDividerHeight(1);
            lv.setOnItemClickListener(this);
            lv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                                                LayoutParams.WRAP_CONTENT));
            selectedPosition = 0;
        }
        else {
            selectedPosition = -1;
        }
        refreshView();
    }

    private void refreshView() {
        PopupListItem popupListItem = adapter.getItem(selectedPosition);
        setText(popupListItem.toString());
        if(popupListItem.getResId() != -1)
            setCompoundDrawablesWithIntrinsicBounds(popupListItem.getResId(),
                                                    0,
                                                    0,
                                                    0);
    }

    @Override
    public void onClick(View v) {
        if(pw == null || !pw.isShowing()) {
            popupWidth = getMeasuredWidth();
            popupHeight = Math.min(3 * getMeasuredHeight(), popupHeight);
            pw = new PopupWindow(v);
            pw.setContentView(lv);
            pw.setWidth(popupWidth);
            pw.setHeight(popupHeight);
            pw.setBackgroundDrawable(new BitmapDrawable());
            pw.setOutsideTouchable(false);
            pw.setFocusable(true);
            pw.setClippingEnabled(true);
            pw.showAsDropDown(v, v.getLeft(), v.getTop() + 3);
            pw.setOnDismissListener(this);
        }
        if(onClickListener != null)
            onClickListener.onClick(v);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if(pw != null)
            pw.dismiss();
        selectedPosition = arg2;
        refreshView();
    }

    private class PopupListItem {
        private String text;
        private int resId;

        public PopupListItem(String text) {
            this.text = text;
            this.resId = -1;
        }

        public PopupListItem(String text, int resId) {
            this.text = text;
            this.resId = resId;
        }

        public String getText() {
            return text;
        }

        public int getResId() {
            return resId;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    private class PopupListItemAdapter extends ArrayAdapter<PopupListItem> {

        public PopupListItemAdapter(Context context, List<PopupListItem> objects) {
            super(context, android.R.layout.activity_list_item, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null) {
                convertView = newView();
                viewHolder = new ViewHolder();
                viewHolder.tvText = (TextView)convertView.findViewById(android.R.id.text1);
                viewHolder.ivIcon = (ImageView)convertView.findViewById(android.R.id.icon);
                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            viewHolder.tvText.setText(getItem(position).getText());
            int resId = getItem(position).getResId();
            if(resId != -1)
                viewHolder.ivIcon.setImageResource(resId);
            return convertView;
        }

        private class ViewHolder {
            TextView tvText;
            ImageView ivIcon;
        }

        private View newView() {
            LinearLayout ll_parent = new LinearLayout(getContext());
            ll_parent.setGravity(Gravity.CENTER_VERTICAL);
            ll_parent.setOrientation(LinearLayout.HORIZONTAL);

            ImageView ivIcon = new ImageView(getContext());
            ivIcon.setId(android.R.id.icon);
            ll_parent.addView(ivIcon);

            TextView tvText = new TextView(getContext());
            tvText.setTextColor(Color.BLACK);
            tvText.setTextSize(textSize);
            tvText.setMaxLines(1);
            tvText.setEllipsize(TruncateAt.MARQUEE);
            tvText.setMarqueeRepeatLimit(-1);
            tvText.setId(android.R.id.text1);
            ll_parent.addView(tvText);
            return ll_parent;
        }
    }

    public void setItems(String[] arr) {
        if(arr == null)
            throw new NullPointerException("Items Array is null.");
        if(list == null)
            list = new ArrayList<PopupSpinnerView.PopupListItem>();
        list.clear();
        for(String text: arr)
            list.add(new PopupListItem(text));
        adapter = new PopupListItemAdapter(getContext(), list);
        setAdapter(adapter);
        refreshView();
    }

    public void setItems(String[] arr, int[] ico) {
        if(ico == null) {
            setItems(arr);
        }
        else {
            if(list == null)
                list = new ArrayList<PopupSpinnerView.PopupListItem>();
            list.clear();
            for(int i = 0; i < arr.length; i++) {
                if(i < ico.length)
                    list.add(new PopupListItem(arr[i], ico[i]));
                else
                    list.add(new PopupListItem(arr[i]));
            }
            adapter = new PopupListItemAdapter(getContext(), list);
            setAdapter(adapter);
        }
        refreshView();
    }
    
    @Override
    public void setTextSize(float size) {
        super.setTextSize(size);
        textSize = size;
    }

    @Override
    public void onDismiss() {
        pw = null;
    }

}

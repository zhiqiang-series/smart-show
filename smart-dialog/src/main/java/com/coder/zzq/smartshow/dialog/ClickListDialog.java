package com.coder.zzq.smartshow.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatDialog;

import com.coder.zzq.toolkit.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClickListDialog extends TitleBranchDialog<ClickListDialog> {
    private List mItems = new ArrayList();
    private boolean mItemCenter = true;

    private ListView mListView;
    private ClickListAdapter mClickListAdapter;
    private OnItemClickListener mOnItemClickListener;

    public ClickListDialog items(List items) {
        if (!mItems.equals(items)) {
            mItems.clear();
            mItems.addAll(items);
            applyItems(mNestedDialog);
        }
        return this;
    }

    public ClickListDialog items(Object[] items) {
        items(Arrays.asList(items));
        return this;
    }

    protected void applyItems(AppCompatDialog dialog) {
        if (dialog == null) {
            return;
        }
        adjustHeight();
        mClickListAdapter.setItems(mItems);
    }

    private void adjustHeight() {
        ViewGroup.LayoutParams lp = mListView.getLayoutParams();
        if (mItems.size() <= (Utils.isScreenPortrait() ? 5 : 4)) {
            lp.height = ListView.LayoutParams.WRAP_CONTENT;
        } else {
            lp.height = Utils.dpToPx(50) * (Utils.isScreenPortrait() ? 5 : 4);
        }
        mListView.setLayoutParams(lp);
    }

    public ClickListDialog itemCenter(boolean itemCenter) {
        mItemCenter = itemCenter;
        applyItemCenter(mNestedDialog);
        return this;
    }

    protected void applyItemCenter(AppCompatDialog dialog) {
        if (dialog != null) {
            mClickListAdapter.setItemCenter(mItemCenter);
        }
    }

    public ClickListDialog itemClickListener(OnItemClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
        return this;
    }

    @Override
    public void onScreenOrientationChanged() {
        adjustHeight();
    }

    @Override
    protected int provideBodyLayout() {
        return R.layout.smart_show_click_list_dialog;
    }

    @Override
    protected void initBody(AppCompatDialog dialog, FrameLayout bodyViewWrapper) {
        super.initBody(dialog, bodyViewWrapper);
        mListView = bodyViewWrapper.findViewById(R.id.smart_show_list_view);
        mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mListView.setDivider(new ColorDrawable(Color.parseColor("#cccccc")));
        mListView.setDividerHeight(Utils.dpToPx(0.5f));
        mClickListAdapter = new ClickListAdapter();
        mListView.setAdapter(mClickListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(ClickListDialog.this, position, mItems.get(position));
                } else {
                    ClickListDialog.this.dismiss();
                }
            }
        });
    }

    @Override
    protected void applyBody(AppCompatDialog dialog) {
        super.applyBody(dialog);
        applyItems(dialog);
        applyItemCenter(dialog);
    }

    @Override
    protected int provideFooterLayout() {
        return 0;
    }

    public interface OnItemClickListener {
        void onItemClick(ClickListDialog dialog, int position, Object data);
    }
}

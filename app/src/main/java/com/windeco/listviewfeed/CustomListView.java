package com.windeco.listviewfeed;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;


public class CustomListView extends ListView {

    private static final int MAX_LISTVIEW_ROW_HEIGHT = 550;

    private Rect rect = new Rect();

    public CustomListView(Context context) {
        super(context);
    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);

        resizeParentLayoutHeight(adapter);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

		//checkIsLocatedAtFooter(); // ����Ʈ�� ���� ���� �Դ��� üũ
	}

    
	private void checkIsLocatedAtFooter() {

		int oldBottom = rect.bottom;
		getLocalVisibleRect(rect);
		int height = getMeasuredHeight();

		if (oldBottom > 0 && height > 0) {
			if (oldBottom != rect.bottom && rect.bottom == height) {
				// ���� ���� ���� ó��
				Log.d("ghlab", "���� ���� ���� ó��");
			}
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		if (h > oldh) {
			resizeParentLayoutHeight(h);
		}
	}


    private void resizeParentLayoutHeight(Adapter adapter) {

        if (adapter != null) {
            int listCount = adapter.getCount();

            /*for (int i = 0; i < listCount; i++) {
                View view = adapter.getView(i, null, )
            }*/

            if (listCount > 0) {

                int height = MAX_LISTVIEW_ROW_HEIGHT * listCount;

                resizeParentLayoutHeight(height);
            }
        }
    }

    private void resizeParentLayoutHeight(int height) {

        final LinearLayout layout = (LinearLayout) getRootView().findViewById(R.id.listview_layout);

        if (layout != null) {
            final ViewGroup.LayoutParams params = layout.getLayoutParams();

            if (params != null) {


				/*// ���� ���� margin �� ����Ѵ�.
				MarginLayoutParams margin = (MarginLayoutParams)getLayoutParams();
				if (margin != null) {
					height += (margin.topMargin + margin.bottomMargin);
				}*/

				params.height = height;

                Log.d("로그", " resizeParentLayoutHeight : "+ height);

                post(new Runnable() {

                    @Override
                    public void run() {
                        layout.setLayoutParams(params);
                    }
                });

            }
        }
    }
}

package utils;

import android.view.View;
import android.widget.TextView;

import tn.android.etransport.etransport.R;

/**
 * Created by mohamed salah on 06/11/2016.
 */

public  class ActionBar {
    public static void changetextview(View view,String string)
    {
        TextView textView;
        textView =(TextView) view.findViewById(R.id.title_text_action_bar);
        textView.setText(string);
    }
}

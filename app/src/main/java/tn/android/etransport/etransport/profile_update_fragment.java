package tn.android.etransport.etransport;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.rey.material.widget.Button;

import utils.KeyboardUtil;
import utils.Links;
import utils.UserInfos;

public class profile_update_fragment extends Fragment implements View.OnClickListener {
    private EditText edit_f_name;
    private EditText edit_l_name;
    private EditText edit_phone;
    private EditText edit_address;
    private Button update_profile_BTN;
    private Context activitycontext;


    public Context getActivitycontext() {
        return activitycontext;
    }

    public void setActivitycontext(Context activitycontext) {
        this.activitycontext = activitycontext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_layout,container,false);
        finding_edit_text(v);
        affecting_edit_text();
        update_profile_BTN =(Button) v.findViewById(R.id.update_profile_BTN);
        update_profile_BTN.setOnClickListener(this);
        return v;
    }

    private void finding_edit_text(View v)
    {
        edit_f_name =(EditText) v.findViewById(R.id.edit_user_f_name);
        edit_l_name=(EditText)  v.findViewById(R.id.edit_user_l_name);
        edit_phone =(EditText) v.findViewById(R.id.edit_telephone);
        edit_address =(EditText) v.findViewById(R.id.edit_address);
    }

    private void affecting_edit_text()
    {

        //affecting values
        edit_f_name.setText(UserInfos.getConnecteduser().getF_name());
        edit_l_name.setText(UserInfos.getConnecteduser().getL_name());
        edit_phone.setText(UserInfos.getConnecteduser().getPhone());
        edit_address.setText(UserInfos.getConnecteduser().getAddress());
    }



    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.update_profile_BTN)
        {
            UpdateUserTask updatetask = new UpdateUserTask(getActivity(),getActivitycontext());
            updatetask.execute(Links.getRootFolder()+"update_user.php"
            ,String.valueOf(UserInfos.getConnecteduser().getId()),edit_f_name.getText().toString()
                    ,edit_l_name.getText().toString(),edit_phone.getText().toString()
                    ,edit_address.getText().toString());
            KeyboardUtil.hideKeyboard(getActivity());
        }
    }

    private void clearall()
    {
        edit_f_name.setText("");
        edit_l_name.setText("");
        edit_phone.setText("");
        edit_address.setText("");
    }

}


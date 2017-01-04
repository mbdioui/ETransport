package tn.android.etransport.etransport;
/**
 * Created by mohamed salah
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alertdialogpro.AlertDialogPro;

import java.util.logging.Logger;

import utils.KeyboardUtil;
import utils.Links;
import utils.UserInfos;

public class Forgetpass_activity extends Activity implements View.OnClickListener {

    private static final Logger logger = Logger.getLogger(LoginActivity.class.getName());
    private EditText mail;
    private EditText Mdp;
    private EditText Confirm_Mdp;
    private com.rey.material.widget.Button cancel_BTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass_activity);
        mail = (EditText) findViewById(R.id.Reset_mail);
        Mdp = (EditText) findViewById(R.id.Reset_password);
        Confirm_Mdp = (EditText) findViewById(R.id.Reset_confirmation_password);
        Button reset_button = (com.rey.material.widget.Button) findViewById(R.id.Reset_button);
        reset_button.setOnClickListener(this);

        cancel_BTN =(com.rey.material.widget.Button) findViewById(R.id.cancel_reset_password_BTN);
        cancel_BTN.setOnClickListener(this);
     }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Reset_button)
        {
            AlertDialogPro.Builder alertDialogBuilder = new AlertDialogPro.Builder(this);
            alertDialogBuilder.setMessage("veuillez remplir tous les champs");
            alertDialogBuilder.setPositiveButton("Continuez", null);
//            alertDialogBuilder.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    finish();
//                }
//            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            if(!mail.getText().toString().equals("") && !Mdp.getText().toString().equals("") && !Confirm_Mdp.getText().toString().equals("")) {
                if (Mdp.getText().toString().equals(Confirm_Mdp.getText().toString())) {
                    ResetPasswordUserTask reset_User = new ResetPasswordUserTask(Forgetpass_activity.this);
                    reset_User.setCntx(getApplicationContext());
                    reset_User.execute(Links.getRootFolder()+"ResetPassword.php", mail.getText().toString()
                            , Mdp.getText().toString());
                    clearall();
                    KeyboardUtil.hideKeyboard(Forgetpass_activity.this);
                }
                else
                {
                    Mdp.setText("");
                    Confirm_Mdp.setText("");
                    Confirm_Mdp.setError("mot de passe et confirmation non conforme",getResources().getDrawable(R.drawable.ic_info_black_24dp));
                }

            }
            else
            {
                alertDialog.show();
            }
        }
        else if(v.getId() == R.id.cancel_reset_password_BTN)
        {
            if(UserInfos.getConnecteduser()!=null)
            {Intent intentparent = new Intent(this,Home_affreteur_activity.class);
            startActivity(intentparent);
            this.finish();}
            else if(UserInfos.getConnecteduser()==null)
            {Intent cnxintent=new Intent(this,LoginActivity.class);
            startActivity(cnxintent);
            this.finish();}
        }
    }

    private void clearall() {
        mail.setText("");
        Mdp.setText("");
        Confirm_Mdp.setText("");
    }


}

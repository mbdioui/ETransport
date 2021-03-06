package tn.android.etransport.etransport;

/**
 * Created by mohamed salah on
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.logging.Logger;

import tasks.LoginUserTask;
import utils.AlertDialogCustom;
import utils.Connectivity;
import utils.KeyboardUtil;
import utils.Links;


public class LoginActivity extends Activity implements OnClickListener {

    AutoCompleteTextView user_mail;
    EditText user_password;
    private static final Logger logger = Logger.getLogger(LoginActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button subButton = (Button) findViewById(R.id.email_sign_in_button);
        user_mail = (AutoCompleteTextView) findViewById(R.id.email);
        user_password = (EditText) findViewById(R.id.password);
        subButton.setOnClickListener(this);
        Button already_member = (Button) findViewById(R.id.BTN_forgetpassword);
        already_member.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgetIntent = new Intent(LoginActivity.this,Forgetpass_activity.class);
                startActivity(forgetIntent);
                LoginActivity.this.finish();
            }
        });
        Button subscribe = (Button) findViewById(R.id.BTN_subscription);
        subscribe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent InsertUser = new Intent(LoginActivity.this, InsertUserActivity.class);
                startActivity(InsertUser);
                LoginActivity.this.finish();
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        if(Connectivity.Checkinternet(this)) {
            if (!user_mail.getText().toString().equals("") && !user_password.getText().toString().equals("")) {
                KeyboardUtil.hideKeyboard(this);
                LoginUserTask log_User = new LoginUserTask(LoginActivity.this);
                log_User.setCntx(LoginActivity.this);
                log_User.execute(Links.getRootFolder() + "userconnexion.php", user_mail.getText().toString()
                        , user_password.getText().toString());
//            while (log_User.Auth==null)
//            {}
//            if (log_User.Auth.equals("ok"))
//            {
//                Intent intent = new Intent(LoginActivity.this,MainClient.class);
//                startActivity(intent);
//                LoginActivity.this.finish();
//            }

            } else {
                AlertDialogCustom.show(this,"veuillez remplir tous les champs");
            }
        }
        //no connectivity to the internet
        else
        {
            AlertDialogCustom.show(this,"vous devez etre lié à internet");
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }

}

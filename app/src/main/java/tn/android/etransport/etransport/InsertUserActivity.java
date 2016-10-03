package tn.android.etransport.etransport;
/**
 * Created by mohamed salah
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.Connectivity;
import utils.KeyboardUtil;
import utils.Links;

public class InsertUserActivity extends Activity implements OnClickListener {
	String sprenom;
	String sname;
	String smail;
	String smdp;
	String phone;
	String status;
	private static final Logger logger = Logger.getAnonymousLogger();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insert_user);
		Button subButton =(Button) findViewById(R.id.User_subs);
		Button already_member = (Button) findViewById(R.id.BTN_alreadymember);
		already_member.setOnClickListener(this);
		subButton.setOnClickListener(this);
	}



			@Override
			public void onClick(View v) {
				if (!Connectivity.Checkinternet(getApplicationContext()))
				{
					AlertDialog.Builder alertbuilder= new AlertDialog.Builder(this);
					alertbuilder.setMessage("Vérifier votre connection");
					alertbuilder.setIcon(R.drawable.ic_info_black_24dp);
					alertbuilder.setPositiveButton("Ok",null);
					AlertDialog dialog= alertbuilder.create();
					dialog.show();
				}
				else
				if (v.getId()==R.id.User_subs)
				{
		        	 Drawable icon = getResources().getDrawable(R.drawable.ic_info_black_24dp);
					 EditText User_f_name_tv =  (EditText)findViewById(R.id.User_f_name);
					 EditText User_l_name_tv =  (EditText)findViewById(R.id.User_l_name);
					 EditText User_mail_tv =  (EditText)findViewById(R.id.User_mail);
					 EditText User_pass =  (EditText)findViewById(R.id.User_password);
					 EditText User_pass_confirm = (EditText)findViewById(R.id.User_password_confirm);
					 ToggleButton User_status =  (ToggleButton)findViewById(R.id.User_status);
					 EditText User_phone = (EditText)findViewById(R.id.User_phone);
					 //check status of user
					 if(User_status.isChecked())
						 status="1";
					 else
						 status="2";
					// matching pass & confirmation pass
					 String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
					 Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
					 Matcher m = p.matcher(User_mail_tv.getText().toString());

					 sprenom=User_f_name_tv.getText().toString();
					 sname=User_l_name_tv.getText().toString() ;
					 smail=User_mail_tv.getText().toString() ;
					 smdp=User_pass.getText().toString() ;
					 phone=User_phone.getText().toString();
					 //fields check
						 if(sprenom.isEmpty()||sname.isEmpty()||smail.isEmpty()||smdp.isEmpty()||phone.isEmpty())
						 {
							 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
							 alertDialogBuilder.setMessage("vérifier les champs manquants");
							 alertDialogBuilder.setTitle("informations");
							 alertDialogBuilder.setIcon(R.drawable.ic_info_black_24dp);
							 alertDialogBuilder.setPositiveButton("ok",null);
							 alertDialogBuilder.create();
							 alertDialogBuilder.show();
						 }
						 else
						 {  //pass verification
							 if (User_pass.getText().toString().equals(User_pass_confirm.getText().toString())) {
								 //mail verification
								 if (m.matches())
								 {
									 //Asyn task for inserting user
									 InsertUserTask user_insert_task = new InsertUserTask(InsertUserActivity.this);
									 //pass context in parameter
									 user_insert_task.setCntx(getApplicationContext());
									 user_insert_task.execute(Links.getRootFolder()+"insertusers.php", sprenom, sname, smail
											 , smdp, phone, status);
									 clearall();
									 KeyboardUtil.hideKeyboard(InsertUserActivity.this);

								 }
								 else
								 {
									 User_mail_tv.setText("");
									 User_mail_tv.setError("mail incorrecte",icon);
									 User_mail_tv.requestFocus();
								 }

							 }
							 else
							 {
								 User_pass.setText("");
								 User_pass_confirm.setText("");
								 User_pass_confirm.setError("Retapez votre mot de passe",icon);
							 }
						 }
			}
				else
					if (v.getId()==R.id.BTN_alreadymember)
					{
						Intent cnx_activity = new Intent(InsertUserActivity.this,LoginActivity.class);
						startActivity(cnx_activity);
						InsertUserActivity.this.finish();
					}
			}

	private void clearall() {
		EditText User_f_name_tv =  (EditText)findViewById(R.id.User_f_name);
		EditText User_l_name_tv =  (EditText)findViewById(R.id.User_l_name);
		EditText User_mail_tv =  (EditText)findViewById(R.id.User_mail);
		EditText User_pass =  (EditText)findViewById(R.id.User_password);
		EditText User_pass_confirm = (EditText)findViewById(R.id.User_password_confirm);
		ToggleButton User_status =  (ToggleButton)findViewById(R.id.User_status);
		EditText User_phone = (EditText)findViewById(R.id.User_phone);

		User_f_name_tv.setText("");
		User_l_name_tv.setText("");
		User_mail_tv.setText("");
		User_pass.setText("");
		User_pass_confirm.setText("");
		User_status.setChecked(false);
		User_phone.setText("");
		User_phone.clearFocus();
	}

}

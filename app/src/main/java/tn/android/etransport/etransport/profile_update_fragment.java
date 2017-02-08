package tn.android.etransport.etransport;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.alertdialogpro.AlertDialogPro;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import Beans.User;
import cz.msebera.android.httpclient.Header;
import tasks.Deleteprofilepicture;
import tasks.UpdateUserTask;
import utils.AlertDialogCustom;
import utils.Connectivity;
import utils.KeyboardUtil;
import utils.Links;
import utils.PermissionUtils;
import utils.UserInfos;

import static android.app.Activity.RESULT_OK;

public class profile_update_fragment extends Fragment implements View.OnClickListener {
    private EditText edit_f_name;
    private EditText edit_l_name;
    private EditText edit_phone;
    private EditText edit_address;
    private Button update_profile_BTN;
    private Context activitycontext;
    private BootstrapCircleThumbnail thumbnail;
    private final int Galeriepermission=111;
    private final int TakeaPhoto=222;

    private Bitmap bitmap;
    private String imgPath, fileName,encodedString;
    private LoadToast lt;
    private RequestParams params = new RequestParams();
    private Bitmap photo;
    private boolean pictureset;

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
        thumbnail =(BootstrapCircleThumbnail) v.findViewById(R.id.thumbnail);
        thumbnail.setOnClickListener(this);
        if (!UserInfos.getConnecteduser().getUser_picture().equals("null"))
        {
            Picasso.with(getActivity()).load(Links.getProfilePictures()+UserInfos.getConnecteduser().getUser_picture())
                    .into(thumbnail);
            pictureset=true;
        }
        else
            pictureset=false;
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
            if (Connectivity.Checkinternet(getActivitycontext()))
            {
            UpdateUserTask updatetask = new UpdateUserTask(getActivity(),getActivitycontext());
            updatetask.execute(Links.getRootFolder()+"update_user.php"
            ,String.valueOf(UserInfos.getConnecteduser().getId()),edit_f_name.getText().toString()
                    ,edit_l_name.getText().toString(),edit_phone.getText().toString()
                    ,edit_address.getText().toString());
            KeyboardUtil.hideKeyboard(getActivity());
            }
            else
                AlertDialogCustom.show(getActivity(),"vous devez être lié à internet");
        }
        else if (v.getId()==R.id.thumbnail)
        {
            uploadProfileImage();
        }
    }

    private void clearall()
    {
        edit_f_name.setText("");
        edit_l_name.setText("");
        edit_phone.setText("");
        edit_address.setText("");
    }



    private void uploadProfileImage()
    {
        final CharSequence[] items = {"Galerie", "Capture Instantanée"};
        AlertDialogPro.Builder builder = new AlertDialogPro.Builder(getActivity());
        builder.setTitle("Modification de l'image de profile");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if(items[item].equals("Galerie"))
                {
                        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.M)
                        { accessgalerie();}
                        else
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                                    , Galeriepermission);
                        else
                            accessgalerie();

                }
                else
                {

                        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.M)
                        { takephoto();}
                        else
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                            requestPermissions(new String[]{Manifest.permission.CAMERA}
                                    , TakeaPhoto);
                        else
                            takephoto();

                }
            }
        });
        AlertDialogPro alert = builder.create();
        alert.show();
    }
    private void takephoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, TakeaPhoto);
    }
    private void accessgalerie() {
        Intent intent = new Intent(getActivity(), AlbumSelectActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 1);
        startActivityForResult(intent, Galeriepermission);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Galeriepermission && resultCode == RESULT_OK && data != null)
        {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            Image img=  images.get(0);
            fileName = img.name;
            imgPath = img.path;
            // Put file name in Async Http Post Param which will used in Php web app
            params.put("filename", fileName);
            // Set the Image in ImageView
            Bitmap bitmap= Bitmap.createBitmap(BitmapFactory.decodeFile(imgPath));
            thumbnail.setImageBitmap(bitmap);
        }
        else if (requestCode == TakeaPhoto && resultCode == RESULT_OK && data != null)
        {
            Calendar c = Calendar.getInstance();
            int seconds = c.get(Calendar.SECOND);
            photo = (Bitmap) data.getExtras().get("data");
            fileName = "instant_capture_ID="+UserInfos.getConnecteduser().getId()+seconds+c.get(Calendar.MINUTE)+".jpeg";
            params.put("filename", fileName);
            thumbnail.setImageBitmap(photo);
        }
        if (data!= null && resultCode==RESULT_OK)
            encodeImagetoString();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M ) {
            if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.READ_EXTERNAL_STORAGE)
                    && requestCode == Galeriepermission) {
                accessgalerie();
            }
            else if(PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.CAMERA)
                    && requestCode == TakeaPhoto)
                takephoto();
        }
    }


    //encode image
    // AsyncTask - To convert Image to String
    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {
                lt= new LoadToast(getActivity());
                lt.setText("Enregistrement en cours...");
                lt.show();
                params.put("id",String.valueOf(UserInfos.getConnecteduser().getId()));
            };

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                if (imgPath!=null )
                    if (!imgPath.equals(""))
                        bitmap = BitmapFactory.decodeFile(imgPath,
                                options);
                    else
                        bitmap=photo;
                else
                    bitmap=photo;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                // Put converted Image string into Async Http Post param
                params.put("image", encodedString);
                // Trigger Image upload
                triggerImageUpload();
            }
        }.execute(null, null, null);
    }

    public void triggerImageUpload() {
        makeHTTPCall();
    }

    // Make Http call to upload Image to Php server
    public void makeHTTPCall() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(5000);
        client.post(Links.getRootFolder()+"uploadprofileimage.php",
                params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        lt.success();
                        User connecteduser=UserInfos.getConnecteduser();
                        if(pictureset)
                        {Deleteprofilepicture deleteprofilepicture= new Deleteprofilepicture(getActivity());
                        deleteprofilepicture.execute(Links.getRootFolder()+"/deleteprofilepicture.php",connecteduser.getUser_picture());}
                        params.remove("image");
                        fileName="";
                        if (imgPath!=null)
                            imgPath="";
                        getActivity().finish();
                        String newimagename=" ";
                        try {
                        JSONObject jsonObj = new JSONObject(new String(responseBody));
                        newimagename=jsonObj.getString("image");
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        connecteduser.setUser_picture(newimagename);
                        UserInfos.setConnecteduser(connecteduser);
                        Intent refresh = new Intent(getActivitycontext(), Home_activity.class);
                        startActivity(refresh);
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        thumbnail.setImageResource(android.R.drawable.ic_menu_camera);
                        lt.error();
                        params.remove("image");
                        fileName="";
                        imgPath="";
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getActivity(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getActivity(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(
                                    getActivity(),
                                    "Error Occured n Most Common Error: n1. Device not connected to Internetn2. Web App is not deployed in App servern3. App server is not runningn HTTP Status code : "
                                            + statusCode, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });


    }
}


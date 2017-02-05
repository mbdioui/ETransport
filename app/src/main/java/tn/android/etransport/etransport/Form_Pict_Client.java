package tn.android.etransport.etransport;

import android.Manifest;
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
import android.widget.Toast;

import com.alertdialogpro.AlertDialogPro;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rey.material.widget.ImageView;
import com.rey.material.widget.ProgressView;

import net.steamcrafted.loadtoast.LoadToast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import utils.Links;
import utils.PermissionUtils;
import utils.UserInfos;

import static android.app.Activity.RESULT_OK;


public class Form_Pict_Client extends android.support.v4.app.Fragment implements View.OnClickListener {


    private BootstrapButton galeriebtn;
    private final int numberOfImagesToSelect = 1;
    private final int Galeriepermission= 5656;
    private final int TakeaPhoto = 9999;
    private LoadToast lt;

    public HashMap<String, Boolean> getSavedImages() {
        return savedImages;
    }

    private HashMap<String,Boolean> savedImages;
    private Bitmap bitmap;
    private String imgPath, fileName,encodedString;
    private RequestParams params = new RequestParams();
    private ProgressView progressview;
    private Bitmap photo;
    private ImageView image1,image2,image3;
    private int selectedimage=0;

    //after capturing image from(camera or galery)
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
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                    bitmap, 400,220, true);
            ImageView view= (ImageView) getView().findViewById(selectedimage);
            view.setImageBitmap(resizedBitmap);

        }
        else if (requestCode == TakeaPhoto && resultCode == RESULT_OK && data != null)
         {
             Calendar c = Calendar.getInstance();
             int seconds = c.get(Calendar.SECOND);
             photo = (Bitmap) data.getExtras().get("data");
             Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                     photo, 400,220, true);
             fileName = "instant_capture_ID="+UserInfos.getConnecteduser().getId()+seconds+c.get(Calendar.MINUTE)+".jpeg";
             params.put("filename", fileName);
             ImageView view= (ImageView) getView().findViewById(selectedimage);
             view.setImageBitmap(resizedBitmap);
         }
        if (data!= null && resultCode==RESULT_OK)
            encodeImagetoString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_form_pict_client,container,false);
        confilay(v);
        return v;
    }

    private void confilay(View v) {
        savedImages = new HashMap<>();
        progressview =(ProgressView) v.findViewById(R.id.progress_view_picture);
        progressview.setVisibility(View.GONE);
        image1 = (ImageView) v.findViewById(R.id.Image1);
        image2 = (ImageView) v.findViewById(R.id.Image2);
        image3 = (ImageView) v.findViewById(R.id.Image3);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);

        image1.setImageResource(R.drawable.upload_photo);
        image2.setImageResource(R.drawable.upload_photo);
        image3.setImageResource(R.drawable.upload_photo);

        progressview.stop();
    }

    //clicking on the Imageview
    @Override
    public void onClick(View v) {
            selectedimage =v.getId();
            final CharSequence[] items = {"Galerie", "Capture Instantanée"};
            AlertDialogPro.Builder builder = new AlertDialogPro.Builder(getContext());
            builder.setTitle("Séléctionnez un choix");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    if(items[item].equals("Galerie"))
                    {
                        if(nbsuccessupload()<3) {
                            if(Build.VERSION.SDK_INT< Build.VERSION_CODES.M)
                            { accessgalerie();}
                            else
                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                                        , Galeriepermission);
                            else
                                accessgalerie();
                        }
                        else
                            Toast.makeText(getContext(),"Nombre d'images maximum est atteint",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(nbsuccessupload()<3) {
                            if(Build.VERSION.SDK_INT< Build.VERSION_CODES.M)
                            { takephoto();}
                            else
                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                                requestPermissions(new String[]{Manifest.permission.CAMERA}
                                        , TakeaPhoto);
                            else
                                takephoto();
                        }
                        else
                            Toast.makeText(getContext(),"Nombre d'images maximum est atteint",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            AlertDialogPro alert = builder.create();
            alert.show();

    }

    private void accessgalerie() {
        Intent intent = new Intent(getActivity(), AlbumSelectActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, numberOfImagesToSelect);
        startActivityForResult(intent, Galeriepermission);
    }

    private int nbsuccessupload()
    {
        int i=0;
        for (String key : savedImages.keySet())
            if (savedImages.get(key)==true)
                i++;
        return i;
    }


    // AsyncTask - To convert Image to String
    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {
            progressview.start();
            progressview.setVisibility(View.VISIBLE);
                lt= new LoadToast(getContext());
                lt.setText("Enregistrement en cours...");
                lt.show();
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
        client.post(Links.getRootFolder()+"uploadimage.php",
                params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        hideprogressview();
                        lt.success();
                        savedImages.put(fileName,true);
                        params.remove("image");
                        fileName="";
                        if (imgPath!=null)
                            imgPath="";
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        hideprogressview();
                        ImageView view= (ImageView) getView().findViewById(selectedimage);
                        view.setImageResource(R.drawable.camera_icon);
                        lt.error();
                        savedImages.put(fileName,false);
                        params.remove("image");
                        fileName="";
                        imgPath="";
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getContext(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getContext(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(
                                    getContext(),
                                    "Error Occured n Most Common Error: n1. Device not connected to Internetn2. Web App is not deployed in App servern3. App server is not runningn HTTP Status code : "
                                            + statusCode, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });


    }
    private void hideprogressview()
    {
        progressview.stop();
        progressview.setVisibility(View.INVISIBLE);
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

    private void takephoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, TakeaPhoto);
    }

}

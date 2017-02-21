package tn.android.etransport.etransport;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.squareup.picasso.Picasso;

import fr.tvbarthel.lib.blurdialogfragment.BlurDialogFragment;
import utils.Links;

/**
 * Created by mohamed salah on 21/02/2017.
 */
public class SampleDialogFragment extends BlurDialogFragment {

    /**
     * Bundle key used to start the blur dialog with a given scale factor (float).
     */
    private static final String BUNDLE_KEY_DOWN_SCALE_FACTOR = "bundle_key_down_scale_factor";

    /**
     * Bundle key used to start the blur dialog with a given blur radius (int).
     */
    private static final String BUNDLE_KEY_BLUR_RADIUS = "bundle_key_blur_radius";

    /**
     * Bundle key used to start the blur dialog with a given dimming effect policy.
     */
    private static final String BUNDLE_KEY_DIMMING = "bundle_key_dimming_effect";

    /**
     * Bundle key used to start the blur dialog with a given debug policy.
     */
    private static final String BUNDLE_KEY_DEBUG = "bundle_key_debug_effect";

    private int mRadius;
    private float mDownScaleFactor;
    private boolean mDimming;
    private boolean mDebug;
    private TextView mail_offer;
    private TextView flname;
    private TextView phone;
    private Bundle bundle;
    private BootstrapCircleThumbnail thunbail_user;

    /**
     * Retrieve a new instance of the sample fragment.
     *
     * @param radius          blur radius.
     * @param downScaleFactor down scale factor.
     * @param dimming         dimming effect.
     * @param debug           debug policy.
     * @return well instantiated fragment.
     */
    public static SampleDialogFragment newInstance(int radius,
                                                   float downScaleFactor,
                                                   boolean dimming,
                                                   boolean debug) {
        SampleDialogFragment fragment = new SampleDialogFragment();
        Bundle args = new Bundle();
        args.putInt(
                BUNDLE_KEY_BLUR_RADIUS,
                radius
        );
        args.putFloat(
                BUNDLE_KEY_DOWN_SCALE_FACTOR,
                downScaleFactor
        );
        args.putBoolean(
                BUNDLE_KEY_DIMMING,
                dimming
        );
        args.putBoolean(
                BUNDLE_KEY_DEBUG,
                debug
        );

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Bundle args = getArguments();
        mRadius = args.getInt(BUNDLE_KEY_BLUR_RADIUS);
        mDownScaleFactor = args.getFloat(BUNDLE_KEY_DOWN_SCALE_FACTOR);
        mDimming = args.getBoolean(BUNDLE_KEY_DIMMING);
        mDebug = args.getBoolean(BUNDLE_KEY_DEBUG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment, null);
        mail_offer = ((TextView) view.findViewById(R.id.user_mail_offer));
        thunbail_user = (BootstrapCircleThumbnail) view.findViewById(R.id.thumbnail_offer_user);
        flname = (TextView) view.findViewById(R.id.user_flname_offer);
        phone =(TextView) view.findViewById(R.id.user_phone_offer);
//        label.setMovementMethod(LinkMovementMethod.getInstance());
//        Linkify.addLinks(label, Linkify.WEB_URLS);
        builder.setView(view);
        phone.setText(bundle.getString("phone"));
        flname.setText(bundle.getString("fname")+bundle.getString("lname"));
        mail_offer.setText(bundle.getString("mail"));
        if (!bundle.getString("picture").equals("null"))
        {
            Picasso.with(getActivity()).load(Links.getProfilePictures()+ bundle.getString("picture"))
                    .into(thunbail_user);
        }
        return builder.create();
    }

    @Override
    protected boolean isDebugEnable() {
        return mDebug;
    }

    @Override
    protected boolean isDimmingEnable() {
        return mDimming;
    }

    @Override
    protected boolean isActionBarBlurred() {
        return true;
    }

    @Override
    protected float getDownScaleFactor() {
        return mDownScaleFactor;
    }

    @Override
    protected int getBlurRadius() {
        return mRadius;
    }

    public void setbundle(Bundle b)
    {
        this.bundle=b;
    }
}
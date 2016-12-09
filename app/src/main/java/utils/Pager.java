package utils;

/**
 * Created by mohamed salah on 09/11/2016.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.HashMap;

import tn.android.etransport.etransport.Form_Infos_Client;
import tn.android.etransport.etransport.Form_Paths_Client;
import tn.android.etransport.etransport.Form_Pict_Client;
import tn.android.etransport.etransport.Form_Time_Client;

//Extending FragmentStatePagerAdapter
public class Pager extends FragmentStatePagerAdapter{

    //integer to count number of tabs
    int tabCount;
    HashMap<Integer,Fragment> mPageReferenceMap = new HashMap<>();

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }


//    @Override
//    public CharSequence getPageTitle(int position) {
//        String title=" ";
//        switch (position){
//            case 0:
//                title="Horaire";
//                break;
//            case 1:
//                title="Emplacement";
//                break;
//            case 2 :
//                title="Infos";
//                break;
//            case 3 :
//                title="Photos";
//                break;
//            default:
//                title="";
//                break;
//        }
//
//        return title;
//    }
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                Form_Time_Client tab1 = new Form_Time_Client();
                mPageReferenceMap.put(position, tab1);
                return tab1;
            case 1:
                 Form_Paths_Client tab2= new Form_Paths_Client();
                mPageReferenceMap.put(position, tab2);
                return tab2;
            case 2 :
                Form_Infos_Client tab3 = new Form_Infos_Client();
                mPageReferenceMap.put(position, tab3);
                return tab3;
            case 3 :
                Form_Pict_Client tab4 = new Form_Pict_Client();
                mPageReferenceMap.put(position, tab4);
                return tab4;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }

    public Fragment getFragment(int key) {
        return mPageReferenceMap.get(key);
    }
}
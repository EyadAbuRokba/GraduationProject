package com.example.eyad.ministryofinteriorproject.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.eyad.ministryofinteriorproject.Model.Fisher;
import com.example.eyad.ministryofinteriorproject.Model.User;

import java.util.ArrayList;

public class Common {

    public static User currentUser;
    public static ArrayList<Fisher>fisherArrayList = new ArrayList<>();
    public static ArrayList<Fisher>fisherArrayListForDelete = new ArrayList<>();



    public static boolean isConnectedToInternet(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null){
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null){
                for (int i=0;i<info.length;i++){
                    if (info[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

package com.gac2013.tdylf.pocketparamedic;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragmentContainer, new StartFragment(), "instructions");
        //ft.addToBackStack(null);
        ft.commit();

    }

    public void setupInstructionFragment() {
        FragmentManager fm = getFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); i++)
            Log.e(getClass().getName(), i + " -> " + fm.getBackStackEntryAt(i).getName());

        //if (fm.getBackStackEntryCount() == 2)
        //     fm.popBackStack();

        FragmentTransaction ft = fm.beginTransaction();
        //ft.remove(StartFragment.this);
        ft.replace(R.id.fragmentContainer, new NextFragment(), "instructions");
        //ft.addToBackStack(null);
        //ft.remove();
        ft.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
    }

}

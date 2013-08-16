package com.gac2013.tdylf.pocketparamedic;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.PowerManager;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {

    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.main);

        if (bundle == null)
            setupInstructionScreen();

        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass().getName());


        ActionBar actionBar = getActionBar();

        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(
                getActionBar().getThemedContext(),
                R.array.action_list,
                android.R.layout.simple_spinner_dropdown_item);

        ActionBar.OnNavigationListener onNavigationListener = new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int position, long itemId) {
                return true;
            }
        };

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(spinnerAdapter, onNavigationListener);
    }


    @Override
    public void onResume() {
        super.onResume();
        mWakeLock.acquire();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWakeLock.release();
    }

    private void setupStartScreen() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragmentContainer, new StartFragment(), "instructions");
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        bundle.putInt("STATE", StateMachine.getCurrentState().getId());
    }

    public void setupInstructionScreen() {
        FragmentManager fm = getFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); i++)
            Log.e(getClass().getName(), i + " -> " + fm.getBackStackEntryAt(i).getName());

        //if (fm.getBackStackEntryCount() == 2)
        //     fm.popBackStack();

        FragmentTransaction ft = fm.beginTransaction();
        //ft.remove(StartFragment.this);
        ft.replace(R.id.fragmentContainer, new NextFragment(), "instructions");
        ft.addToBackStack(null);
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

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        finish();
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to exit Pocket Paramedic?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }

}

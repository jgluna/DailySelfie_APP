package com.github.jgluna.dailyselfie.listeners;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.view.MenuItem;

import com.github.jgluna.dailyselfie.LoginActivity;
import com.github.jgluna.dailyselfie.MainActivity;
import com.github.jgluna.dailyselfie.R;
import com.github.jgluna.dailyselfie.SelfieHelper;
import com.github.jgluna.dailyselfie.TimePickerFragment;
import com.github.jgluna.dailyselfie.model.Selfie;
import com.github.jgluna.dailyselfie.model.SelfiesOrder;

import java.util.List;

public class NavItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {

    private final MainActivity activity;

    public NavItemSelectedListener(MainActivity activity) {
        this.activity = activity;
    }

    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        List<Selfie> selfies;
        switch (menuItem.getItemId()) {
            case R.id.drawer_set_alarm_menu:
                //Start fragment to set a daily alarm
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(activity.getSupportFragmentManager(), "timePicker");
                return true;
            case R.id.drawer_logout_menu:
                activity.logout();
                Intent intent = new Intent(this.activity.getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                this.activity.startActivity(intent);
                return true;
            case R.id.drawer_sort_by_date_asc:
                selfies = SelfieHelper.loadSelfiesFromProvider(SelfiesOrder.DATE_ASC, false, activity);
                this.activity.updateAdapter(selfies);
                return true;
            case R.id.drawer_sort_by_date_desc:
                selfies = SelfieHelper.loadSelfiesFromProvider(SelfiesOrder.DATE_DESC, false, activity);
                this.activity.updateAdapter(selfies);
                return true;
            case R.id.drawer_sort_by_last_modified_asc:
                selfies = SelfieHelper.loadSelfiesFromProvider(SelfiesOrder.MODIFIED_ASC, false, activity);
                this.activity.updateAdapter(selfies);
                return true;
            case R.id.drawer_sort_by_last_modified_desc:
                selfies = SelfieHelper.loadSelfiesFromProvider(SelfiesOrder.MODIFIED_DESC, false, activity);
                this.activity.updateAdapter(selfies);
                return true;
            default:
                return true;
        }
    }
}

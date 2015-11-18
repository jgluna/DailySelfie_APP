package com.github.jgluna.dailyselfie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.jgluna.dailyselfie.comm.BackgroundTask;
import com.github.jgluna.dailyselfie.listeners.NavItemSelectedListener;
import com.github.jgluna.dailyselfie.model.EffectsRequestWrapper;
import com.github.jgluna.dailyselfie.model.Selfie;
import com.github.jgluna.dailyselfie.model.SelfieListAdapter;
import com.github.jgluna.dailyselfie.model.SelfiesOrder;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 1;
    private final SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SelfieListAdapter adapter;
    private List<Selfie> selfies;
    private LinearLayout effectList;
    private Set<String> selectedEffects = new HashSet<>();
    private MenuItem applyEffectsMenuItem;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getBoolean("fromNotification")) {
                //Open Camera to take selfie when starting from notification
                Intent takePictureIntent = SelfieHelper.addOneSelfie(this.getApplicationContext());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
        setContentView(R.layout.activity_nav_drawer);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("DailySelfiePrefs", 0);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_drawer);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
            mNavigationView.setNavigationItemSelectedListener(new NavItemSelectedListener(this));
            FrameLayout header = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.drawer_header, mNavigationView);
            String user = pref.getString("user_name", "Empty User");
            ((TextView) header.findViewById(R.id.drawer_header_text)).setText(user);
        }

        createEffectsList();
        SelfiesOrder order = SelfiesOrder.getByString(pref.getString("user_selfie_order", SelfiesOrder.DATE_DESC.getDescription()));
        //Load selfies from provider to show them in the list view
        selfies = SelfieHelper.loadSelfiesFromProvider(order, false, this);
        adapter = new SelfieListAdapter(this, selfies);
        final ListView listView = (ListView) findViewById(R.id.nav_list_selfies);
        listView.setAdapter(adapter);
        listView.setSelector(R.drawable.selector);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        //set multi choice for selfies list
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int checkedCount = listView.getCheckedItemCount();
                mode.setTitle(checkedCount + " Selected");
                adapter.toggleSelection(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_multiple, menu);
                if (effectList != null) {
                    effectList.setVisibility(View.VISIBLE);
                }
                applyEffectsMenuItem = menu.findItem(R.id.action_apply_effects);
                applyEffectsMenuItem.setEnabled(false);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                SparseBooleanArray selected = adapter
                        .getSelectedIds();
                switch (item.getItemId()) {
                    case R.id.action_apply_effects:
                        EffectsRequestWrapper wrapper;
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                wrapper = new EffectsRequestWrapper();
                                Selfie selectedItem = adapter
                                        .getItem(selected.keyAt(i));
                                wrapper.setSelfie(selectedItem);
                                wrapper.setEffects(selectedEffects);
                                //Start a new AsyncTask to manage the communication to the server in order to apply the effects
                                new BackgroundTask(MainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, wrapper);
                            }
                        }
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.removeSelection();
                if (effectList != null) {
                    effectList.setVisibility(View.GONE);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, FullSizeActivity.class);
                intent.putExtra("data", selfies.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Selfie s = SelfieHelper.storeSelfie(this.getApplicationContext());
            adapter.add(s);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                //Start camera app in order to take the selfie
                Intent takePictureIntent = SelfieHelper.addOneSelfie(this.getApplicationContext());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createEffectsList() {
        effectList = (LinearLayout) findViewById(R.id.nav_list_effects);
        String[] effects = getResources().getStringArray(R.array.effects);
        for (String effect : Arrays.asList(effects)) {
            ToggleButton effectButton = new ToggleButton(this);
            effectButton.setText(effect);
            effectButton.setTextOff(effect);
            effectButton.setTextOn(effect);
            effectButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            effectButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedEffects.add(buttonView.getText().toString());
                    } else {
                        selectedEffects.remove(buttonView.getText().toString());
                    }
                    if (selectedEffects.isEmpty()) {
                        applyEffectsMenuItem.setEnabled(false);
                    } else {
                        applyEffectsMenuItem.setEnabled(true);
                    }
                }
            });
            effectList.addView(effectButton);
        }
    }

    public void logout() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("DailySelfiePrefs", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

    public void updateAdapter(List<Selfie> selfiesList) {
        if (selfiesList != null) {
            selfies.clear();
            selfies.addAll(selfiesList);
            adapter.notifyDataSetChanged();
        }
    }
}

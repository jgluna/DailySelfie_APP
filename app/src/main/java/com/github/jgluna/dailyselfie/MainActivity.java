package com.github.jgluna.dailyselfie;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.github.jgluna.dailyselfie.comm.BackgroundTask;
import com.github.jgluna.dailyselfie.model.EffectsRequestWrapper;
import com.github.jgluna.dailyselfie.model.Selfie;
import com.github.jgluna.dailyselfie.model.SelfieListAdapter;
import com.github.jgluna.dailyselfie.notification.AlarmManagerHelper;
import com.github.jgluna.dailyselfie.provider.DBHelper;
import com.github.jgluna.dailyselfie.provider.SelfiesProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getBoolean("fromNotification")) {
                Intent takePictureIntent = SelfieHelper.addOneSelfie(this.getApplicationContext());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
//        setAlarm();
        setContentView(R.layout.activity_main);
        createEffectsList();
        selfies = loadSelfiesFromProvider();
        adapter = new SelfieListAdapter(this, selfies);
        final ListView listView = (ListView) findViewById(R.id.list_selfies);
        listView.setAdapter(adapter);
        listView.setSelector(R.drawable.selector);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
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
                    case R.id.action_settings:
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                Selfie selectedItem = adapter
                                        .getItem(selected.keyAt(i));
                                adapter.remove(selectedItem);
                            }
                        }
                        mode.finish();
                        return true;
                    case R.id.action_apply_effects:
                        EffectsRequestWrapper wrapper;
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                wrapper = new EffectsRequestWrapper();
                                Selfie selectedItem = adapter
                                        .getItem(selected.keyAt(i));
                                wrapper.setSelfie(selectedItem);
                                wrapper.setEffects(selectedEffects);
                                new BackgroundTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,wrapper);
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

    private List<Selfie> loadSelfiesFromProvider() {
        //TODO mover a una fachada o algo asi...
        Uri friends = SelfiesProvider.CONTENT_URI;
        Cursor c = getContentResolver().query(friends, null, null, null, null);
        List<Selfie> selfies = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                Selfie s;
                try {
                    s = new Selfie();
                    s.setSelfieDate(iso8601Format.parse(c.getString(c.getColumnIndex(DBHelper.CREATION_DATE_COLUMN))));
                    s.setImagePath(c.getString(c.getColumnIndex(DBHelper.ORIGINAL_IMAGE_PATH_COLUMN)));
                } catch (ParseException e) {
                    s = null;
                    e.printStackTrace();
                }
                if (s != null) {
                    selfies.add(s);
                }
            } while (c.moveToNext());
        }
        c.close();
        return selfies;
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
                Intent takePictureIntent = SelfieHelper.addOneSelfie(this.getApplicationContext());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createEffectsList() {
        effectList = (LinearLayout) findViewById(R.id.list_effects);
        String[] effects = getResources().getStringArray(R.array.effects);
        for (String effect : Arrays.asList(effects)) {
            ToggleButton effectButton = new ToggleButton(this);
            effectButton.setText(effect);
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

    private void setAlarm() {
        AlarmManagerHelper helper = new AlarmManagerHelper(getApplicationContext());
        helper.setAlarm(21);
    }
}

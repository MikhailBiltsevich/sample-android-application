package com.mikhailbiltsevich.app7;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CursorFlags {
    ListView drawerList;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;

    String[] menu;
    private String currentTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(android.R.drawable.ic_dialog_info);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        setSupportActionBar(toolbar);

        if(savedInstanceState != null) {
            currentTitle = savedInstanceState.getString("currentTitle");
            setActionBarTitle(currentTitle);
        }

        menu = getResources().getStringArray(R.array.menu);
        drawerList = (ListView)findViewById(R.id.drawer);
        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, menu));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null)
            selectMenuItem(3);

        getFragmentManager().addOnBackStackChangedListener(mBackStackChangedListener);
    }

    private FragmentManager.OnBackStackChangedListener mBackStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag("visible_fragment");

            String title = ((FragmentTitle)fragment).getTitle();
            setActionBarTitle(title);
        }
    };

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            selectMenuItem(i);
        }
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
        currentTitle = title;
    }

    private void selectMenuItem(int id) {
        int containerId = R.id.fragment_container;
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;

        boolean isMenuItemSelected = true;

        String title = null;

        switch (id) {
            case 0:
                fragment = new EditPhoneFragment();
                title = "Добавить контакт";
                break;
            case 1:
                fragment = new FilterPhonesListFragment();
                ((FilterPhonesListFragment)fragment).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                title = "Ручной фильтр контактов по городским звонкам";
                break;
            case 2:
                fragment = new PhonesListFragment();
                ((PhonesListFragment)fragment).setCursorFlag(CursorFlags.PHONES_WHERE_TIME_LONG_DISTANCE_CALLS_ABOVE_ZERO);
                title = "Отфильтрованные контакты";
                break;
            case 3:
                fragment = new PhonesListFragment();
                ((PhonesListFragment)fragment).setCursorFlag(ALL_PHONES);
                title = "Список контактов";
                break;
            case 4:
                fragment = new PhonesListFragment();
                ((PhonesListFragment)fragment).setCursorFlag(CursorFlags.PHONES_WITH_ARREARS_OF_PAYMENT);
                title = "Контакты с задолженностью";
                break;
            case 5:
                fragment = new FilterPhonesListFragment();
                ((FilterPhonesListFragment)fragment).setInputType(InputType.TYPE_CLASS_TEXT);
                title = "Ручной фильтр контактов по адресу";
                break;
            case 6:
                fragment = new NearestLocationsFragment();
                title = "Ближайшие места";
                break;
            default:
                isMenuItemSelected = false;
                break;
        }

        if(isMenuItemSelected) {
            ((FragmentTitle)fragment).setTitle(title);
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(containerId, fragment, "visible_fragment");
            fragmentTransaction.commit();

            setActionBarTitle(title);
        }

        drawerLayout.closeDrawer(drawerList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentTitle", currentTitle);
    }
}
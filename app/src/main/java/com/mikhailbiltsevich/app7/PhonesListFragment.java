package com.mikhailbiltsevich.app7;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

public class PhonesListFragment extends ListFragment implements FragmentTitle, CursorFlags {

    CursorAdapter cursorAdapter = null;
    Cursor cursor = null;
    SQLiteDatabase db = null;

    public void setCursorFlag(int mCursorFlag) {
        this.mCursorFlag = mCursorFlag;
    }

    public void setParametr(String mParametr) {
        this.mParametr = mParametr;
    }

    private int mCursorFlag;
    private String mParametr;

    private Phone mPhone;

    private String mTitle;

    private ActionMode mActionMode;
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_edit:
                    EditPhoneFragment fragment = new EditPhoneFragment();
                    fragment.setPhone(mPhone);
                    fragment.setTitle("Изменить контакт");

                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment, "visible_fragment");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    mode.finish();
                    return true;
                case R.id.menu_delete:
                    db.delete("PHONE", "_id = ?", new String[] {String.valueOf(mPhone.getId())});
                    updateList();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

    public PhonesListFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setCursor(mCursorFlag);
        cursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.custom_list_item, cursor, new String[]{
                "_id",
                "surname",
                "firstname",
                "patronymic",
                "address",
                "credit_card",
                "time_long_distance_calls",
                "time_city_calls",
                "debit",
        }, new int[]{R.id.text1, R.id.text2, R.id.text3, R.id.text4, R.id.text5, R.id.text6, R.id.text7, R.id.text8, R.id.text9}, 0);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setCursor(int flag) {
        SQLiteOpenHelper helper = new PhonesDatabaseHelper(getActivity());
        db = helper.getReadableDatabase();
        switch (flag) {
            case ALL_PHONES: {
                cursor = PhoneRepository.getPhonesCursor(db);
                break;
            }
            case PHONES_WHERE_TIME_LONG_DISTANCE_CALLS_ABOVE_ZERO: {
                cursor = PhoneRepository.getPhonesCursorByTimeLongDistanceCalls(db, 0);
                break;
            }
            case PHONES_WHERE_TIME_CITY_CALLS_ABOVE_SPECIFIC_VALUE: {
                cursor = PhoneRepository.getPhonesCursorByTimeCityCalls(db, Double.valueOf(mParametr));
                break;
            }
            case PHONES_WITH_ARREARS_OF_PAYMENT: {
                cursor = PhoneRepository.getPhonesCursorByDebit(db, 0);
                break;
            }
            case PHONES_FROM_SPECIFIC_ADDRESS: {
                cursor = PhoneRepository.getPhonesCursorByAddress(db, mParametr);
                break;
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(mActionMode != null)
                    return false;

                mPhone = PhoneRepository.getPhoneById(db, l);
                mActionMode = getActivity().startActionMode(mActionModeCallback);
                return true;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        updateList();
    }

    private void updateList(){
        setListAdapter(cursorAdapter);
        cursorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    @Override
    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }
}
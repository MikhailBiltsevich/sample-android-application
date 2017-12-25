package com.mikhailbiltsevich.app7;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class FilterPhonesListFragment extends Fragment implements FragmentTitle, CursorFlags {

    private String mTitle;

    private int mInputType;

    public void setInputType(int mInputType) {
        this.mInputType = mInputType;
    }

    public FilterPhonesListFragment() {
        this.setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_filter_phones_list, container, false);

        EditText editText = view.findViewById(R.id.parametr_edit_text);
        editText.setInputType(mInputType);

        Button executeButton = (Button)view.findViewById(R.id.execute_button);
        executeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

        return view;
    }

    private void getData() {
        EditText editText = getView().findViewById(R.id.parametr_edit_text);
        String value = editText.getText().toString();

        if(!value.isEmpty()) {
            if(mInputType == (InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL))
                setSelectionParametrs(CursorFlags.PHONES_WHERE_TIME_CITY_CALLS_ABOVE_SPECIFIC_VALUE, value);
            else if(mInputType == InputType.TYPE_CLASS_TEXT)
                setSelectionParametrs(CursorFlags.PHONES_FROM_SPECIFIC_ADDRESS, value);
        }
    }

    private void setSelectionParametrs(int cursorFlag, String parametr) {
        PhonesListFragment phonesListFragment = new PhonesListFragment();
        phonesListFragment.setCursorFlag(cursorFlag);
        phonesListFragment.setParametr(parametr);

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.phones_list_container, phonesListFragment);
        fragmentTransaction.commit();
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
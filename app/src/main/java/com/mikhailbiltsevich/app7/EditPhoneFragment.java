package com.mikhailbiltsevich.app7;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mikhailbiltsevich.customview.ClearableAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;


public class EditPhoneFragment extends Fragment implements FragmentTitle {

    Button executeButton;
    Toast phoneAdded;
    Toast phoneUpdated;
    Toast expectedValue;

    EditText idEditText;
    AutoCompleteTextView firstnameAutoCompleteTextView;
    EditText surnameEditText;
    EditText patronymicEditText;
    EditText addressEditText;
    EditText creditCardEditText;
    EditText debitEditText;
    EditText timeLongDistanceCallsEditText;
    EditText timeCityCallsEditText;

    View view;
    Phone phone;
    boolean isNewPhone;

    private String mTitle;
    private List<String> names;
    private ArrayAdapter<String> adapter;

    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;

    public EditPhoneFragment() {
        setRetainInstance(true);
    }

    private View getView(int id) {
        return view.findViewById(id);
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_phone, container, false);

        dbHelper = new PhonesDatabaseHelper(getActivity());
        db = dbHelper.getWritableDatabase();

        idEditText = (EditText)getView(R.id.id_edit_text);
        firstnameAutoCompleteTextView = ((ClearableAutoCompleteTextView)getView(R.id.firstname_auto_complete_text_view)).getAutoCompleteTextViewField();
        surnameEditText = (EditText)getView(R.id.surname_edit_text);
        patronymicEditText = (EditText)getView(R.id.patronymic_edit_text);
        addressEditText = (EditText)getView(R.id.address_edit_text);
        creditCardEditText = (EditText)getView(R.id.credit_card_edit_text);
        debitEditText = (EditText)getView(R.id.debit_edit_text);
        timeLongDistanceCallsEditText = (EditText)getView(R.id.time_long_distance_calls_edit_text);
        timeCityCallsEditText = (EditText)getView(R.id.time_city_calls_edit_text);
        Button toToastButton = (Button)getView(R.id.to_toast_button);

        toToastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), firstnameAutoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        final Context activityClass = getActivity();
        phoneAdded = Toast.makeText(activityClass, "Телефон добавлен успешно!", Toast.LENGTH_SHORT);
        phoneUpdated = Toast.makeText(activityClass, "Телефон обновлен успешно!", Toast.LENGTH_SHORT);
        expectedValue = Toast.makeText(activityClass, "Заполнены не все поля!", Toast.LENGTH_LONG);

        names = new ArrayList<String>();

        if(phone == null) {
            isNewPhone = true;
            phone = new Phone();
        }
        else {
            isNewPhone = false;

            idEditText.setText(String.valueOf(phone.getId()));
            firstnameAutoCompleteTextView.setText(phone.getFirstname());
            surnameEditText.setText(phone.getSurname());
            patronymicEditText.setText(phone.getPatronymic());
            addressEditText.setText(phone.getAddress());
            creditCardEditText.setText(phone.getCreditCard());
            debitEditText.setText(String.valueOf(phone.getDebit()));
            timeLongDistanceCallsEditText.setText(String.valueOf(phone.getTimeLongDistanceCalls()));
            timeCityCallsEditText.setText(String.valueOf(phone.getTimeCityCalls()));
        }

        executeButton = (Button)getView(R.id.execute_button);
        executeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isAllEditTextElementsFilled(firstnameAutoCompleteTextView, surnameEditText, patronymicEditText, addressEditText, creditCardEditText, debitEditText, timeCityCallsEditText, timeLongDistanceCallsEditText)) {
                    expectedValue.show();
                    return;
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Изменение данных");
                    if(isNewPhone)
                        builder.setMessage("Добавить новый телефон?");
                    else
                        builder.setMessage("Изменить существующий телефон?");
                    builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            phone.setFirstname(firstnameAutoCompleteTextView.getText().toString());
                            phone.setSurname(surnameEditText.getText().toString());
                            phone.setPatronymic(patronymicEditText.getText().toString());
                            phone.setAddress(addressEditText.getText().toString());
                            phone.setCreditCard(creditCardEditText.getText().toString());
                            phone.setDebit(Double.valueOf(debitEditText.getText().toString()));
                            phone.setTimeCityCalls(Double.valueOf(timeCityCallsEditText.getText().toString()));
                            phone.setTimeLongDistanceCalls(Double.valueOf(timeLongDistanceCallsEditText.getText().toString()));

                            if (isNewPhone) {
                                db.insert("PHONE", null, PhonesDatabaseHelper.convertPhoneToContentValues(phone));
                                phoneAdded.show();
                            } else {
                                db.update("PHONE", PhonesDatabaseHelper.convertPhoneToContentValues(phone), "_id = ?", new String[] {String.valueOf(phone.getId())});
                                phoneUpdated.show();
                            }

                            updateAdapter();
                        }
                    });
                    builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        return view;
    }

    private boolean isAllEditTextElementsFilled(EditText... elements) {
        String text;
        for(EditText element : elements) {
            text = element.getText().toString();
            if (text.equals(""))
                return false;
        }

        return true;
    }

    @Override
    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateAdapter();
    }

    private void updateAdapter(){
        PhoneRepository.getPhoneNames(db, names);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, names);
        firstnameAutoCompleteTextView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
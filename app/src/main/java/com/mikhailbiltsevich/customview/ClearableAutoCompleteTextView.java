package com.mikhailbiltsevich.customview;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.mikhailbiltsevich.app7.R;

public class ClearableAutoCompleteTextView extends FrameLayout {
    private LayoutInflater inflater = null;
    private AutoCompleteTextView autoCompleteTextView;
    private Button buttonClear;

    public ClearableAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    public ClearableAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public ClearableAutoCompleteTextView(Context context) {
        super(context);
        initViews();
    }

    void initViews() {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.clearable_autocompletetextview, this,true);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.clearable_autocompletetextview);
        buttonClear = (Button)findViewById(R.id.button_clear);
        buttonClear.setVisibility(RelativeLayout.INVISIBLE);
        clearText();
        showHideClearButton();
    }

    void clearText() {
        buttonClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.setText("");
            }
        });
    }

    void showHideClearButton() {
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0)
                    buttonClear.setVisibility(RelativeLayout.VISIBLE);
                else buttonClear.setVisibility(RelativeLayout.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public AutoCompleteTextView getAutoCompleteTextViewField() {
        return autoCompleteTextView;
    }
}

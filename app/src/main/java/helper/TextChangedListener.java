package helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class TextChangedListener implements TextWatcher {

    ArrayList <EditText> elements  = new ArrayList<EditText>();
    Button button;
    public TextChangedListener(EditText ... el){

        for(EditText ex: el){
            elements.add(ex);
        }

    }

    public void setButtonLock(Button b){
        button = b;
    }

    private boolean isAnyEmpties(){

        boolean empty = false;

        for(EditText ex: elements){
            if(ex.getText().toString().length() == 0){
                empty = true;
                break;
            }
        }

        return empty;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
       if(isAnyEmpties()){
           button.setEnabled(false);
       }else{
           button.setEnabled(true);
       }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}

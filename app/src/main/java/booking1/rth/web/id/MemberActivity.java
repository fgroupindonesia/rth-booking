package booking1.rth.web.id;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import helper.ShowDialog;
import object.FamilyMember;
import object.Keys;
import shared.UserData;

public class MemberActivity extends AppCompatActivity {

    ImageView imageViewDeleteMember, imageViewAddMember,
            imageViewGenderMember1, imageViewGenderMember2,
            imageViewGenderMember3, imageViewGenderMember4,
            imageViewGenderMember5;

    TextView textViewNameMember1, textViewNameMember2,
            textViewNameMember3, textViewNameMember4,
            textViewNameMember5, textViewKosongMember;

    EditText editTextNamaMemberBaru;
    Spinner spinnerKelaminMemberBaru, spinnerHubunganMemberBaru;

    int limitMember = 5; // ini batas dari kami ya!

    int navigasiFamilyMember;
    int terisiMember;

    final int ALL_MEMBER_LIST = 0, NEW_MEMBER_FORM = 1;

    TableRow tableRow5, tableRow4, tableRow3, tableRow2, tableRow1;

    CheckBox chb1, chb2, chb3, chb4, chb5;

    LinearLayout linearLayoutMemberList,
            linearLayoutMemberForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        // first time
        navigasiFamilyMember = ALL_MEMBER_LIST;

        // for preference data saving
        UserData.setPreference(this);

        textViewNameMember1 = (TextView) findViewById(R.id.textViewNameMember1);
        textViewNameMember2 = (TextView) findViewById(R.id.textViewNameMember2);
        textViewNameMember3 = (TextView) findViewById(R.id.textViewNameMember3);
        textViewNameMember4 = (TextView) findViewById(R.id.textViewNameMember4);
        textViewNameMember5 = (TextView) findViewById(R.id.textViewNameMember5);
        textViewKosongMember = (TextView) findViewById(R.id.textViewKosongMember);

        imageViewGenderMember1 = (ImageView) findViewById(R.id.imageViewGenderMember1);
        imageViewGenderMember2 = (ImageView) findViewById(R.id.imageViewGenderMember2);
        imageViewGenderMember3 = (ImageView) findViewById(R.id.imageViewGenderMember3);
        imageViewGenderMember4 = (ImageView) findViewById(R.id.imageViewGenderMember4);
        imageViewGenderMember5 = (ImageView) findViewById(R.id.imageViewGenderMember5);

        imageViewDeleteMember = (ImageView) findViewById(R.id.imageViewDeleteMember);
        imageViewAddMember = (ImageView) findViewById(R.id.imageViewAddMember);

        linearLayoutMemberList = (LinearLayout) findViewById(R.id.linearLayoutMemberList);
        linearLayoutMemberForm = (LinearLayout) findViewById(R.id.linearLayoutMemberForm);

        editTextNamaMemberBaru = (EditText) findViewById(R.id.editTextNamaMemberBaru);
        spinnerKelaminMemberBaru = (Spinner) findViewById(R.id.spinnerKelaminMemberBaru);
        spinnerHubunganMemberBaru = (Spinner) findViewById(R.id.spinnerHubunganMemberBaru);

        buttonBookingBersama = (Button) findViewById(R.id.buttonBookingBersama);
        buttonBookingMerekaSaja = (Button) findViewById(R.id.buttonBookingMerekaSaja);

        tableRow5 = (TableRow) findViewById(R.id.tableRow5);
        tableRow4 = (TableRow) findViewById(R.id.tableRow4);
        tableRow3 = (TableRow) findViewById(R.id.tableRow3);
        tableRow2 = (TableRow) findViewById(R.id.tableRow2);
        tableRow1 = (TableRow) findViewById(R.id.tableRow1);

        chb1 = (CheckBox) findViewById(R.id.checkBoxMember1);
        chb2 = (CheckBox) findViewById(R.id.checkBoxMember2);
        chb3 = (CheckBox) findViewById(R.id.checkBoxMember3);
        chb4 = (CheckBox) findViewById(R.id.checkBoxMember4);
        chb5 = (CheckBox) findViewById(R.id.checkBoxMember5);

        // render the data to UI if any
        renderData();

    }

    private void showNewMemberForm(boolean b){

        // clear dulu form nya
        editTextNamaMemberBaru.setText("");
        spinnerHubunganMemberBaru.setSelection(0);
        spinnerKelaminMemberBaru.setSelection(0);

        if(b){
            linearLayoutMemberForm.setVisibility(View.VISIBLE);
        }else{
            linearLayoutMemberForm.setVisibility(View.GONE);
        }

    }

    private void showMemberlist(boolean b){
        if(b) {
            linearLayoutMemberList.setVisibility(View.VISIBLE);
        } else {
            linearLayoutMemberList.setVisibility(View.GONE);
        }
    }

    public void saveMemberForm(View v){
        // grab all data
        // store into the UserData Preference with Key
        terisiMember++;

        FamilyMember fmb = new FamilyMember();

        fmb.setNama(editTextNamaMemberBaru.getText().toString());
        fmb.setHubungan(spinnerHubunganMemberBaru.getSelectedItem().toString());
        fmb.setKelamin(spinnerKelaminMemberBaru.getSelectedItem().toString());

        if(terisiMember==1) {
            UserData.savePreference(Keys.DATA_MEMBER_1, fmb.toString());
        } else if(terisiMember==2) {
            UserData.savePreference(Keys.DATA_MEMBER_2, fmb.toString());
        } else if(terisiMember==3) {
            UserData.savePreference(Keys.DATA_MEMBER_3, fmb.toString());
        } else if(terisiMember==4) {
            UserData.savePreference(Keys.DATA_MEMBER_4, fmb.toString());
        }  else if(terisiMember==5) {
            UserData.savePreference(Keys.DATA_MEMBER_5, fmb.toString());
        }

        navigasiFamilyMember = ALL_MEMBER_LIST;
        renderPage();
        renderData();
    }

    private void renderPage(){
        if(navigasiFamilyMember==NEW_MEMBER_FORM) {
            showMemberlist(false);
            showNewMemberForm(true);
        }else if (navigasiFamilyMember == ALL_MEMBER_LIST){
            showMemberlist(true);
            showNewMemberForm(false);
        }
    }

    public void addMember(View v){
        if(terisiMember<limitMember){
            // open next page untuk masukkin data member baru

            navigasiFamilyMember = NEW_MEMBER_FORM;
            renderPage();
        }else{
            ShowDialog.message(this, "Oops... Limit cuma 5 member saja!");
        }
    }

    private FamilyMember fromJSONToObject(String dataJSON){

        Gson ghelper = new Gson();
       FamilyMember obj = null;

       if(dataJSON!=null) {
        obj = ghelper.fromJson(dataJSON, FamilyMember.class);
       }

        return obj;
    }

    private void renderData(){

        // from stored data into the UI frame edittext, etc
        // the data stored is actually using json format
        String isian1 = UserData.getPreferenceString(Keys.DATA_MEMBER_1);
        String isian2 = UserData.getPreferenceString(Keys.DATA_MEMBER_2);
        String isian3 = UserData.getPreferenceString(Keys.DATA_MEMBER_3);
        String isian4 = UserData.getPreferenceString(Keys.DATA_MEMBER_4);
        String isian5 = UserData.getPreferenceString(Keys.DATA_MEMBER_5);


        //FamilyMember fmb1 = new FamilyMember(isian1);
        FamilyMember fmb1 = fromJSONToObject(isian1);
        FamilyMember fmb2 = fromJSONToObject(isian2);
        FamilyMember fmb3 =  fromJSONToObject(isian3);
        FamilyMember fmb4 =  fromJSONToObject(isian4);
        FamilyMember fmb5 =  fromJSONToObject(isian5);

        // default na table row ngga ada isina
        tableRow1.setVisibility(View.GONE);
        tableRow2.setVisibility(View.GONE);
        tableRow3.setVisibility(View.GONE);
        tableRow4.setVisibility(View.GONE);
        tableRow5.setVisibility(View.GONE);

        // default dulu
        terisiMember = 0;

        fillData(fmb1, tableRow1, textViewNameMember1, imageViewGenderMember1);
        fillData(fmb2, tableRow2, textViewNameMember2, imageViewGenderMember2);
        fillData(fmb3, tableRow3, textViewNameMember3, imageViewGenderMember3);
        fillData(fmb4, tableRow4, textViewNameMember4, imageViewGenderMember4);
        fillData(fmb5, tableRow5, textViewNameMember5, imageViewGenderMember5);

        // jika sudah full maka add button hilang
        // dan jika masih kosong maka delete button hilang
        if(terisiMember==0){
            imageViewDeleteMember.setVisibility(View.GONE);
            imageViewAddMember.setVisibility(View.VISIBLE);
        } else if(terisiMember == limitMember){
            imageViewDeleteMember.setVisibility(View.VISIBLE);
            imageViewAddMember.setVisibility(View.GONE);
        } else if(terisiMember>0){
            imageViewDeleteMember.setVisibility(View.VISIBLE);
            imageViewAddMember.setVisibility(View.VISIBLE);
            textViewKosongMember.setVisibility(View.GONE);
        }

        ShowDialog.message(this, "terisinya " + terisiMember);
        toggleButtonBookingFamily();

    }

    private void fillData(FamilyMember fmb, TableRow tbr, TextView txv, ImageView img){
        if(fmb != null){
            tbr.setVisibility(View.VISIBLE);
            txv.setText(fmb.getNama());

            if(fmb.getKelamin().equalsIgnoreCase("wanita")) {
                img.setImageResource(R.drawable.akhwat_logo32);
            } else {
                img.setImageResource(R.drawable.ikhwan_logo32);
            }

        terisiMember++;

        }
    }

    private void clearDataMember(int jumlahTerisi, int jumlahCheckbox, TableRow tbrow){
        if(jumlahTerisi>0) {
            jumlahTerisi--;
            terisiMember = jumlahTerisi;
        }

        if(jumlahCheckbox>0) {
            jumlahCheckbox--;
            jumlahCheckBoxAktif = jumlahCheckbox;
        }

        tbrow.setVisibility(View.GONE);
    }

    public void deleteMember(View v){
        // delete yg terchecklist saja
        if(jumlahCheckBoxAktif>0){
            // oke kita iterasi elemennya

            if(chb1.isChecked()){
                clearDataMember(terisiMember, jumlahCheckBoxAktif, tableRow1);
                UserData.savePreference(Keys.DATA_MEMBER_1, null);
            }

            if(chb2.isChecked()){
                clearDataMember(terisiMember, jumlahCheckBoxAktif, tableRow2);
                UserData.savePreference(Keys.DATA_MEMBER_2, null);
            }

            if(chb3.isChecked()){
                clearDataMember(terisiMember, jumlahCheckBoxAktif, tableRow3);
                UserData.savePreference(Keys.DATA_MEMBER_3, null);
            }

            if(chb4.isChecked()){
                clearDataMember(terisiMember, jumlahCheckBoxAktif, tableRow4);
                UserData.savePreference(Keys.DATA_MEMBER_4, null);
            }

            if(chb5.isChecked()){
                clearDataMember(terisiMember, jumlahCheckBoxAktif, tableRow5);
                UserData.savePreference(Keys.DATA_MEMBER_5, null);
            }


            renderData();

        }
    }

    public void chooseProfile(View v){

        Intent i = new Intent(this, ClientProfileActivity.class);
        startActivity(i);

        finish();

    }

    public void backToMemberList(View v){

        navigasiFamilyMember = ALL_MEMBER_LIST;

        renderPage();
    }

    FamilyMember familyMemberList []=null;
    private void storeFamilyMember(String dataJSON){

        int jumlahData = familyMemberList.length;
        jumlahData++;

        FamilyMember familyMemberListBaru [] = new FamilyMember[jumlahData];

        int indexUrut = 0;
        for(FamilyMember fmb: familyMemberList){
            familyMemberListBaru[indexUrut] = fmb;
            indexUrut++;
        }

        FamilyMember fmbBaru = fromJSONToObject(dataJSON);
        familyMemberListBaru[indexUrut] = fmbBaru;

        familyMemberList = familyMemberListBaru;

    }

    public String grabFamilyMemberData(){

        String data1, data2, data3, data4, data5;

        if(chb1.isChecked()){
           data1 =  UserData.getPreferenceString(Keys.DATA_MEMBER_1);
           storeFamilyMember(data1);
        }

        if(chb2.isChecked()){
            data2 =  UserData.getPreferenceString(Keys.DATA_MEMBER_2);
             storeFamilyMember(data2);
        }

        if(chb3.isChecked()){
            data3 =  UserData.getPreferenceString(Keys.DATA_MEMBER_3);
             storeFamilyMember(data3);
        }

        if(chb4.isChecked()){
            data4 =  UserData.getPreferenceString(Keys.DATA_MEMBER_4);
             storeFamilyMember(data4);

        }

        if(chb5.isChecked()){
            data5 =  UserData.getPreferenceString(Keys.DATA_MEMBER_5);
             storeFamilyMember(data5);

        }

        return familyMemberList;

    }

    public void openNextMerekaSaja(View v){

        // mereka tanpa saya
        lanjutOpenNext(false);


    }

    private void lanjutOpenNext(boolean bersamaMereka){
        if(jumlahCheckBoxAktif>0){
            // ketika checkbox aktif
            // maka simpen semua data member yg memesan booking saja
            UserData.savePreference(Keys.DATA_MEMBER_BOOKING, grabFamilyMemberData());
            openNextActivity(bersamaMereka);

        }else{
            ShowDialog.message(this, "Pilih dulu anggota checklist member untuk booking!");
        }
    }

    public void openNextBersamaMereka(View v){

        // mereka dengan saya
        lanjutOpenNext(true);

    }

    private void openNextActivity(boolean bersamaMereka){

        Intent i = new Intent(this, BookingScheduleActivity.class);

        if(grabFamilyMemberData()!=null) {
            i.putExtra(Keys.BOOKING_MODE, Keys.MODE_BOOKING_MULTI_ORDER);
        }else{
            i.putExtra(Keys.BOOKING_MODE, Keys.MODE_BOOKING_SINGLE_ORDER);
        }

        if(bersamaMereka){
            i.putExtra(Keys.BOOKING_MODE_BERSAMA, Keys.BOOKING_BERSAMA);
        }else{
            i.putExtra(Keys.BOOKING_MODE_BERSAMA, Keys.BOOKING_MEREKA_SAJA);
        }

        startActivity(i);
    }

    int jumlahCheckBoxAktif;

    public void activateButton(View v){

        CheckBox chb = (CheckBox) v;
        checkSelected(chb);

        toggleButtonBookingFamily();

        ShowDialog.message(this, "terdapat " + jumlahCheckBoxAktif + " dengan limit " + limitMember);

    }

    private void checkSelected(CheckBox cb){
        if(cb.isChecked() && jumlahCheckBoxAktif<limitMember){

                jumlahCheckBoxAktif++;

        }else if(!cb.isChecked() && jumlahCheckBoxAktif>0){
                jumlahCheckBoxAktif--;
        }
    }

    Button buttonBookingBersama, buttonBookingMerekaSaja;
    private void toggleButtonBookingFamily(){

        if(jumlahCheckBoxAktif == 0){
            buttonBookingBersama.setEnabled(false);
            buttonBookingMerekaSaja.setEnabled(false);
        }else {
            // ketika checkbox ada yg aktif minimal 1
            buttonBookingBersama.setEnabled(true);
            buttonBookingMerekaSaja.setEnabled(true);
        }

    }

}
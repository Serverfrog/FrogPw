package de.serverfrog.pw.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import de.serverfrog.pw.SHA3Util;
import de.serverfrog.pw.Website;
import de.serverfrog.pw.WebsiteBuilder;
import de.serverfrog.pw.WebsiteType;
import java.util.ArrayList;
import java.util.List;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.api.BackgroundExecutor;

@EActivity
public class MainActivity extends Activity {

    @ViewById(R.id.websiteType)
    Spinner websiteTypeSpinner;

    @ViewById(R.id.websiteName)
    EditText websiteName;

    @ViewById(R.id.passwordField)
    EditText passwordField;

    @ViewById(R.id.shouldSmall)
    CheckBox shouldSmall;

    @ViewById(R.id.shouldBig)
    CheckBox shouldBig;

    @ViewById(R.id.shouldNumbers)
    CheckBox shouldNumbers;

    @ViewById(R.id.shouldSpecial)
    CheckBox shouldSpecial;

    @ViewById(R.id.specialChars)
    EditText specialCharsField;

    @ViewById(R.id.strength)
    EditText strengthField;
    @ViewById(R.id.outputPassword)
    EditText outputPassword;

    @AfterViews
    void afterViews() {
        List<String> list = new ArrayList<String>();
        for (WebsiteType websiteType : WebsiteType.values()) {
            list.add(websiteType.getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        websiteTypeSpinner.setAdapter(dataAdapter);

    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    }

    @Click(R.id.generate)
    public void onGenerateButtonPressed() {

        if (strengthField.getEditableText().toString().isEmpty()) {
            return;
        }
        int strenght = Integer.valueOf(strengthField.getEditableText().toString());
        if (strenght <= 0) {
            return;
        }
        WebsiteType websiteType = WebsiteType.getByName(
                (String) websiteTypeSpinner.getSelectedItem());
        WebsiteBuilder wb = new WebsiteBuilder();
        Website createWebsite = wb.setAddress(websiteName.getEditableText().toString())
                .setType(websiteType).createWebsite();
        String password = passwordField.getEditableText().toString();
        String specialChars = "";
        if (shouldSpecial.isChecked()) {

            specialChars = specialCharsField.getEditableText().toString();
        }
        if (shouldSmall.isChecked() && shouldBig.isChecked() && shouldNumbers.isChecked()
                && shouldSpecial.isChecked() && specialChars.trim().isEmpty()) {
            return;
        }
        BackgroundExecutor.cancelAll("generator", true);
        generatePassword(createWebsite, password, strenght, (shouldSmall.isChecked()) ? 1 : 0,
                (shouldBig.isChecked()) ? 1 : 0, (shouldNumbers.isChecked()) ? 2 : 0,
                (shouldSpecial.isChecked()) ? 3 : 0, specialChars,
                shouldSpecial.isChecked());
    }

    @Background(id = "generator", serial = "generator")
    void generatePassword(Website createWebsite, String password,
            int strenght, int smallCharCount, int bigCharCount, int numbersCharCount,
            int specialCharCount, String specialChars, boolean withSpecial) {
        String generatePassword;
        setPasswordField("Generating...");
        Log.d("FrogPw", "GeneratePAssword");
        if (withSpecial) {
            generatePassword = SHA3Util.generatePassword(createWebsite,
                    password.getBytes(), strenght, smallCharCount, bigCharCount,
                    numbersCharCount, specialCharCount, specialChars.toCharArray());
        } else {
            generatePassword = SHA3Util.generatePassword(createWebsite,
                    password.getBytes(), strenght, smallCharCount, bigCharCount,
                    numbersCharCount);
        }
        setPasswordField(generatePassword);
        Log.d("FrogPw", generatePassword);
    }

    @UiThread
    void setPasswordField(String text) {
        outputPassword.setText(text);
    }
}

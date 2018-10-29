package telvoterminal.telvo.com.terminal.account;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.crashlytics.android.Crashlytics;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
import telvoterminal.telvo.com.terminal.baseactivity.BaseActivity;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularButton;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularEditText;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.model.kyc.PersonalInfo;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.BaseUrl;
import telvoterminal.telvo.com.terminal.utility.Constant;
import telvoterminal.telvo.com.terminal.utility.DatePicker;
import telvoterminal.telvo.com.terminal.utility.PermissionManager;
import telvoterminal.telvo.com.terminal.utility.Validation;

public class PersonalInformationActivity extends BaseActivity implements View.OnClickListener, IServiceResultListener {


    private static int REQUEST_IMAGE = 1;
    private static int REQUEST_CAMERA = 0;

    private int IMAGE_TYPE = -1;

    private CircularImageView userPhotoImageView;
    private ImageView uploadPhotoImageView;
    private RobotoRegularEditText fatherNameEditText;
    private RobotoRegularEditText motherNameEditText;
    private RobotoRegularEditText spouseNameEditText;
    private Spinner nationalitySpinner;
    private DatePicker dateOfBirthEditText;
    private ImageView calendarImageView;
    private RadioGroup genderRadioGroup;
    private RadioGroup authenticationTypeRadioGroup;
    private RobotoRegularEditText authenticationNumberEditText;
    private ImageView signatureImageView;
    private RobotoRegularButton submitButton;


    private String selectedItem = null;

    private String signPicturePath;
    private String userPicturePath;
    private String fatherName;
    private String motherName;
    private String spouseName = "";
    private String dateOfBirth;
    private String gender = "male";
    private String nidOrPassport = "0";
    private String nidOrPassportNumber;

    private PersonalInfo personalInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        setToolbar(R.id.toolbar_personal_information, getString(R.string.personal_information));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        initializeViews();


        bundle = getIntent().getExtras();
        if (bundle != null) {
            personalInfo = (PersonalInfo) bundle.getSerializable("PERSONAL_INFORMATION");
            if (!personalInfo.getFatherName().equals(Constant.VALUE_CHECK)) {
                fatherNameEditText.setText(personalInfo.getFatherName());
            }
            if (!personalInfo.getMotherName().equals(Constant.VALUE_CHECK)) {
                motherNameEditText.setText(personalInfo.getMotherName());
            }

            if (!personalInfo.getSpouseName().equals(Constant.VALUE_CHECK)) {
                spouseNameEditText.setText(personalInfo.getSpouseName());
            }

            if (!personalInfo.getNationality().equals(Constant.VALUE_CHECK)) {
                setupNationalitySpinner(personalInfo.getNationality());
            }else{
                setupNationalitySpinner(null);
            }

            if (!personalInfo.getDob().equals(Constant.VALUE_CHECK)) {
                dateOfBirthEditText.setText(personalInfo.getDob());
            }
            if (!personalInfo.getGender().equals(Constant.VALUE_CHECK)) {
                if (personalInfo.getGender().equals("male")) {
                    genderRadioGroup.check(R.id.maleRadioButton);
                } else {
                    genderRadioGroup.check(R.id.femaleRadioButton);
                }
            }
            if (!personalInfo.getNidType().equals(Constant.VALUE_CHECK)) {
                if (personalInfo.getNidType().equals("0")) {
                    authenticationTypeRadioGroup.check(R.id.nationalIdRadioButton);
                } else if (personalInfo.getNidType().equals("1")) {
                    authenticationTypeRadioGroup.check(R.id.passportNoRadioButton);
                }
            }
            if (!personalInfo.getNid().equals(Constant.VALUE_CHECK)) {
                authenticationNumberEditText.setText(personalInfo.getNid());
            }
            if (!personalInfo.getImage().equals("default.png")) {
                Picasso.with(context).load(personalInfo.getImage().equals("") ? "NO images" : BaseUrl.IMAGE_BASE + personalInfo.getImage()).placeholder(R.drawable.ic_home_user_photo).error(R.drawable.ic_home_user_photo).into(userPhotoImageView);
            }

            if (!personalInfo.getSignImage().equals("default.png")) {
                Picasso.with(context).load(personalInfo.getSignImage().equals("") ? "NO images" : BaseUrl.IMAGE_BASE + personalInfo.getSignImage()).placeholder(R.drawable.signature).error(R.drawable.signature).into(signatureImageView);
            }
        }
    }

    private void setupNationalitySpinner(String countryName) {
        int selected =0;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.nationality_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        String[] country = getResources().getStringArray(R.array.nationality_array);
        for(int i=0;i<country.length;i++){
            String anString = country[i];
            if(anString.equals(countryName)){
                selected =i;
            }
        }
        nationalitySpinner.setAdapter(adapter);
        nationalitySpinner.setSelection(selected);
    }

    private void initializeViews() {
        userPhotoImageView = (CircularImageView) findViewById(R.id.circularImageView);
        uploadPhotoImageView = getImageView(R.id.uploadPhotoImageView);
        uploadPhotoImageView.setOnClickListener(this);
        fatherNameEditText = getRobotoRegularEditText(R.id.fatherNameEditText);
        motherNameEditText = getRobotoRegularEditText(R.id.motherNameEditText);
        spouseNameEditText = getRobotoRegularEditText(R.id.spouseNameEditText);
        nationalitySpinner = getSpinner(R.id.nationalitySpinner);
        dateOfBirthEditText = (DatePicker) findViewById(R.id.dateOfBirthEditText);
        calendarImageView = getImageView(R.id.calendarImageView);
        calendarImageView.setOnClickListener(this);
        genderRadioGroup = getRadioGroup(R.id.genderRadioGroup);
        authenticationTypeRadioGroup = getRadioGroup(R.id.authenticationNumberRadioGroup);
        authenticationNumberEditText = getRobotoRegularEditText(R.id.authenticationNumberEditText);
        signatureImageView = getImageView(R.id.signatureImageView);
        signatureImageView.setOnClickListener(this);
        submitButton = getRobotoRegularButton(R.id.submitButton);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.uploadPhotoImageView:
                IMAGE_TYPE = 0;
                selectImage();
                break;
            case R.id.calendarImageView:
                dateOfBirthEditText.requestFocus();
                break;
            case R.id.signatureImageView:
                customAlertDialog.showDialogWithYes(getString(R.string.signature_upload_message), new ButtonClick() {
                    @Override
                    public void Do() {
                        IMAGE_TYPE = 1;
                        selectImage();
                    }
                });
                break;
            case R.id.submitButton:
                boolean done = true;
                fatherName = fatherNameEditText.getText().toString().trim();
                motherName = motherNameEditText.getText().toString().trim();
                spouseName = spouseNameEditText.getText().toString().trim();
                dateOfBirth = dateOfBirthEditText.getText().toString().trim();
                nidOrPassportNumber = authenticationNumberEditText.getText().toString().trim();
                if(genderRadioGroup.getCheckedRadioButtonId() == R.id.maleRadioButton){
                    gender = "male";
                }else if(genderRadioGroup.getCheckedRadioButtonId() == R.id.femaleRadioButton){
                    gender = "female";
                }

                if (personalInfo.getImage().equals(Constant.VALUE_CHECK)) {
                    if (Validation.isTextEmpty(userPicturePath)) {
                        done = false;
                        customAlertDialog.showDialog(getStringResources(R.string.picture_not_selected));
                    }
                } else if (personalInfo.getSignImage().equals(Constant.VALUE_CHECK)) {
                    if (Validation.isTextEmpty(signPicturePath)) {
                        done = false;
                        customAlertDialog.showDialog(getStringResources(R.string.sign_not_selected));
                    }
                } else if (Validation.isTextEmpty(fatherName)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.father_name_empty));
                } else if (Validation.isTextEmpty(motherName)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.mother_name_empty));
                } else if (nationalitySpinner.getSelectedItemPosition() < 1) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.nationality_not_selected));
                } else if (Validation.isTextEmpty(dateOfBirth)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.date_of_birth_empty));
                } else if (Validation.isTextEmpty(gender)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.gender_not_selected));
                } else if (Validation.isTextEmpty(nidOrPassportNumber)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.national_id_or_passport_number_empty));
                }

                if (done) {
                    if (AppManager.hasInternetConnection(context)) {
                        service.setPersonalInfo(userPicturePath, signPicturePath, fatherName, motherName, spouseName, nationalitySpinner.getSelectedItem().toString(), dateOfBirth, gender, nidOrPassport, nidOrPassportNumber);
                    } else {
                        customAlertDialog.showDialog(getStringResources(R.string.no_internet_connection));
                    }
                }
            default:
                return;
        }
    }


    private void ImagePickerIntent() {
        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void cameraIntent() {
        CropImage.activity(null).start(this);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.maleRadioButton:
                if (checked) {
                    gender = "male";
                }
                break;
            case R.id.femaleRadioButton:
                if (checked) {
                    gender = "female";
                }
                break;
            case R.id.nationalIdRadioButton:
                if (checked) {
                    nidOrPassport = "0";
                    authenticationNumberEditText.setHint("");
                    authenticationNumberEditText.setHint(getStringResources(R.string.national_id));
                    authenticationNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                break;
            case R.id.passportNoRadioButton:
                if (checked) {
                    nidOrPassport = "1";
                    authenticationNumberEditText.setHint("");
                    authenticationNumberEditText.setHint(getStringResources(R.string.passport_number));
                    authenticationNumberEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                break;
            default:
                return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            CropImage.activity(selectedImage)
                    .start(this);

        } else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            CropImage.activity(selectedImage)
                    .start(this);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try{
                    Uri resultUri = result.getUri();
                    if (IMAGE_TYPE == 0) {
                        userPhotoImageView.setImageURI(resultUri);
                        File path = new File(getRealPathFromURI(resultUri));
                        path = new Compressor(context).setQuality(80).compressToFile(path);
                        userPicturePath = path.getAbsolutePath();
                        Log.i("USER", path.getAbsolutePath());
                    } else if (IMAGE_TYPE == 1) {
                        signatureImageView.setImageURI(resultUri);
                        File path = new File(getRealPathFromURI(resultUri));
                        path = new Compressor(context).setQuality(80).compressToFile(path);
                        signPicturePath = path.getAbsolutePath();
                        Log.i("USER", path.getAbsolutePath());
                    }
                }catch (IOException ex){
                    ex.printStackTrace();
                }

            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionManager.REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (selectedItem.equals(getString(R.string.take_photo))) {
                        if (PermissionManager.checkPermission(context)) {
                            cameraIntent();
                        }
                    } else if (selectedItem.equals(getString(R.string.choose_from_gallery))) {
                        if (PermissionManager.checkPermission(context)) {
                            ImagePickerIntent();
                        }
                    }
                }
                return;
            }
        }
    }

    private String convertImageToBase64(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] byteArrayImage = baos.toByteArray();
        return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
    }

    @Override
    public void OnServiceResult(String method, DTOBase dtoBase, boolean success) {
        if (success) {
            if (method.equals("PERSONAL_INFORMATION")) {
                personalInfo = (PersonalInfo) dtoBase;
                if (personalInfo.getStatus().equals(Constant.success)) {
                    customAlertDialog.showDialogWithYes(personalInfo.getMessage(), new ButtonClick() {
                        @Override
                        public void Do() {
                            if (!preferences.getValue("USER").equals("")) {
                                user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
                                user.getKyc().setAllVerified(false);
                                user.getKyc().getPersonalInfo().setVerified(false);
                                user.getKyc().getPersonalInfo().setDeclined(false);
                                user.getKyc().getPersonalInfo().setImage(personalInfo.getImage());
                                user.getKyc().getPersonalInfo().setSignImage(personalInfo.getSignImage());
                                user.getKyc().getPersonalInfo().setFatherName(fatherName);
                                user.getKyc().getPersonalInfo().setMotherName(motherName);
                                user.getKyc().getPersonalInfo().setSpouseName(spouseName);
                                user.getKyc().getPersonalInfo().setNationality(nationalitySpinner.getSelectedItem().toString());
                                user.getKyc().getPersonalInfo().setDob(dateOfBirth);
                                user.getKyc().getPersonalInfo().setGender(gender);
                                user.getKyc().getPersonalInfo().setNidType(nidOrPassport);
                                user.getKyc().getPersonalInfo().setNid(nidOrPassportNumber);
                                user.getKyc().getPersonalInfo().setInserted(true);
                                preferences.setValue("USER", AppManager.getClassString(user));
                                finish();
                            } else {
                                finish();
                            }
                        }
                    });
                } else if (personalInfo.getStatus().equals(Constant.error)) {
                    customAlertDialog.showDialog(personalInfo.getMessage());
                }
            }
        }
    }


    private void selectImage() {
        final CharSequence[] items = {getString(R.string.take_photo), getString(R.string.choose_from_gallery),
                getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.add_photo);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                selectedItem = items[item].toString();
                if (selectedItem.equals(getString(R.string.take_photo))) {
                    if (PermissionManager.checkPermission(context)) {
                        cameraIntent();
                    }
                } else if (selectedItem.equals(getString(R.string.choose_from_gallery))) {
                    if (PermissionManager.checkPermission(context)) {
                        ImagePickerIntent();
                    }
                } else if (selectedItem.equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}

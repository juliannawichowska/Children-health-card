package com.example.children_health_card;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.service.autofill.TextValueSanitizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class EditDataFragment extends Fragment {

    TextView nameSurname, pesel;
    Button addPhoto, addMeasure;
    ImageView avatar;
    String mName, mSurname, mPesel;
    String wee,hee;

    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //dres url zdjęcia profilowego
    Uri image_uri;

    //progress dialog
    ProgressDialog pd;

    //zmienne dla pozwoleń
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    //tablice pozwoleń
    String cameraPermissions[];
    String storagePermissions[];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_data, container, false);
        // Inflate the layout for this fragment
        nameSurname = (TextView) v.findViewById(R.id.namSurname);
        pesel = (TextView) v.findViewById(R.id.peselEd);
        addPhoto = (Button) v.findViewById(R.id.addPhoto);
        addMeasure = (Button) v.findViewById(R.id.addHeightWeight);
        avatar = (ImageView) v.findViewById(R.id.photo);

        mName = getActivity().getIntent().getStringExtra("childName");
        mSurname = getActivity().getIntent().getStringExtra("childSurname");
        mPesel = getActivity().getIntent().getStringExtra("childPesel");

        //inicjacja progress dialog
        pd = new ProgressDialog(getActivity());

        //inicjacja tabeli pozwoleń
        cameraPermissions =  new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        nameSurname.setText(mName + " " + mSurname);
        pesel.setText("Pesel : "+ mPesel);

        //inicjacja instancji FirebaseAuth i FirebaseDatabase
        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //pobranie aktualnie zalogowanego użytkownika
        user = firebaseAuth.getCurrentUser();


        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });
        addMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMeasureDialog();
            }
        });

        //odwołanie się referencją do zmiennej userType w bazie
        DatabaseReference refe = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = refe.child("Children").child(user.getUid()).child(mName);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String img = dataSnapshot.child("imageURL").getValue(String.class);

                //załadowanie zdjęcia profilowego z bazy danych
                try {
                    Picasso.get().load(img).into(avatar);
                } catch (Exception e){
                    Picasso.get().load(R.drawable.child).into(avatar);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        return v;

    }

    private void showMeasureDialog() {

        // pobranie widoku
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.alert_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        alertDialogBuilder.setView(promptsView);

        final EditText weightT = (EditText) promptsView.findViewById(R.id.weight);

        // budowanie dialogu
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Zatwierdź", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // pobranie widoku
                        LayoutInflater li = LayoutInflater.from(getActivity());
                        View promptsView = li.inflate(R.layout.alert_dialog2, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getActivity());

                        alertDialogBuilder.setView(promptsView);

                        final EditText heightT = (EditText) promptsView.findViewById(R.id.height);

                        // ustawianie drugiego dialogu
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("Zatwierdź", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        long time = System.currentTimeMillis();
                                        Log.v("tak", String.valueOf(time));

                                        wee = weightT.getText().toString();
                                        hee = heightT.getText().toString();

                                        //utworzenie referencji do bazy
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                                        //utworzenie HashMap z adresem uri
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("weight", wee);
                                        hashMap.put("height", hee);

                                        //zaktualizowanie bazy danych o wagę i wzrost
                                        databaseReference.child("Children/" + user.getUid() + "/" + mName + "/" + time).updateChildren(hashMap)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        //zaktualizowano pomyślnie
                                                        pd.dismiss();
                                                        Toast.makeText(getActivity(), "Dodano pomiary dziecka o imieniu " + mName, Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        //aktualizacja nie powiodła się
                                                        pd.dismiss();
                                                        Toast.makeText(getActivity(), "Nie udało się dodać pomiarów", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
}})
                                .setNegativeButton("Anuluj",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        // utworz dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // wyswietl dialog
                        alertDialog.show();

                    }})

                        .setNegativeButton("Anuluj",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // utworz dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // wyświetlenie dialogu
        alertDialog.show();
                    }



    public void onResume(){
        super.onResume();
        // Set title bar
        ((HomeActivity) getActivity())
                .setActionBarTitle("Edycja danych dziecka");

    }

    public void showImagePickDialog(){
        //funkcja ta odpowiada za wyświetlenie opcji wyboru skąd ma zostać pobrane zdjęcie profilowe

        //opcje do wyboru
        String[] options = {"Aparat","Galeria"};

        //budowanie dialogu
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Wybierz zdjęcie z");
        //ustawianie opcji
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    //kliknięto opcje aparatu
                    //sprawdzanie czy zaakceptowano zgodę na dostęp do aparatu
                    if(!checkCameraPermissions()){
                        //przekierowanie do funkcji proszącej o zaakceptowanie zgody na dostęp do aparatu
                        requestCameraPermission();
                    } else {
                        //przekierowanie do funkcji wyboru zdjęcia z aparatu
                        pickFromCamera();
                    }
                } if (which==1){
                    //kliknięto opcję galerii
                    //sprawdzanie czy zaakceptowano zgodę na dostęp do galerii
                    if(!checkStoragePermissions()){
                        //przekierowanie do funkcji proszącej o zaakceptowanie zgody na dostęp do galerii
                        requestStoragePermission();
                    } else {
                        //przekierowanie do funkcji wyboru zdjęcia z galerii
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    private void pickFromGallery(){
        //funkcja ta odpowiada za wybór zdjęcia z galerii
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera(){
        //funkcja ta odpowiada za wybór zdjęcia z aparatu
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION,"Temp Descr");
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent intent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermissions(){
        //sprawdzanie czy zaakceptowana została zgoda na dostęp do galerii
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        //funkcja ta odpowiada za wyświetlenie prośby o dostęp do galerii
        ActivityCompat.requestPermissions(getActivity(), storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermissions(){
        //sprawdzanie czy zaakceptowana została zgoda na dostęp do aparatu
        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission(){
        //funkcja ta odpowiada za wyświetlenie prośby o dostęp do aparatu
        ActivityCompat.requestPermissions(getActivity(), cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //funkcja która następuje po zaakceptowaniu zgód o dostęp

        //sprawdzanie która ze zgód została przed chwilą zaakceptowana
        switch(requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length>0) {
                    //zapisanie zgody
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        pickFromCamera();
                    } else {
                        Toast.makeText(getActivity(), "Nie uzyskano zgody",Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0) {
                    //zapisanie zgody
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        pickFromGallery();
                    } else {
                        Toast.makeText(getActivity(), "Nie uzyskano zgody",Toast.LENGTH_LONG).show();
                    }
                }else {
                }
            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //ustawienie zdjęcia profilowego
        if(resultCode==RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //zdjęcie jest z galerii
                image_uri = data.getData();
                //ustawianie zdjęcia
                try {
                    uploadProfilePhoto(image_uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //zdjęcie jest z aparatu
                //ustawianie zdjęcia
                try {
                    uploadProfilePhoto(image_uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfilePhoto(Uri image_uri) throws IOException {
        //funkcja ta odpowiada za zaktualizowanie zdjęcia profilowego
        pd.show();
        //ścieżka do miejsca gdzie składowane zostaną zdjęcia
        String fileNameAndPath = "ProfilePictures/"+user.getUid()+"/"+mName;

        //pobranie bitmapy z uri
        final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image_uri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] data = baos.toByteArray();
        //zapis referencji do zdjęcia
        StorageReference reference = FirebaseStorage.getInstance().getReference().child(fileNameAndPath);
        reference.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //pobranie adresu uri zdjęcia
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String downloadUri = uriTask.getResult().toString();

                        if(uriTask.isSuccessful()){
                            //utworzenie referencji do bazy
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                            //utworzenie HashMap z adresem uri
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("imageURL", downloadUri);

                            //zaktualizowanie bazy danych o adres uri
                            databaseReference.child("Children/"+user.getUid()+"/"+mName).updateChildren(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //zaktualizowano pomyślnie
                                            pd.dismiss();
                                            Toast.makeText(getActivity(), "Zdjęcie zostało zaktualizowane", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //aktualizacja nie powiodła się
                                            pd.dismiss();
                                            Toast.makeText(getActivity(),"Nie udało się zaktualizować zdjęcia", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            //Akttualizacja nie powiodła się
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Operacja nie powiodła się", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Akttualizacja nie powiodła się
                        pd.dismiss();
                        Toast.makeText(getActivity(),"Nastąpił błąd", Toast.LENGTH_SHORT).show();
                    }
                });



    }



}
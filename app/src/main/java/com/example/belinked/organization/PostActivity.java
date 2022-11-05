package com.example.belinked.organization;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.app.AlertDialog.Builder;
import android.widget.ProgressBar;
import android.widget.Toast;
//import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.belinked.R;
import com.example.belinked.StartActivity;
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

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    // Variables -----------------------------------

    // Views
    EditText editTextTitle;
    EditText editTextDescription;
    ImageView imageViewPost;
    Button buttonUpload;

    // Progress bar
    ProgressBar pb;

    // Firebase
    FirebaseAuth mAuth;
    DatabaseReference userDbRef;

    // User Info
    String fName, lName, uid, email, image, accountType;

    // Constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;

    // image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;

    // image picked
    Uri image_rui = null;

    // permissions
    String[] cameraPermissions;
    String[] storagePermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        connectViews();

        // ActionBar here ...

        // init permissions
        cameraPermissions = new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE};

        mAuth = FirebaseAuth.getInstance();
        checkUserStatus();

        userDbRef = FirebaseDatabase.getInstance().getReference("users");
        Query query = userDbRef.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot db: snapshot.getChildren()) {
                    fName = "" + db.child("firstName").getValue();
                    lName = "" + db.child("lastName").getValue();
                    email = "" + db.child("email").getValue();
                    image = "" + db.child("image").getValue();
                    accountType = "" + db.child("accountType").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // get image from camera/gallery on click
        imageViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });

        // Upload button click listener
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = editTextTitle.getText().toString();
                String description = editTextDescription.getText().toString();

                if(TextUtils.isEmpty(title)) {
                    Toast.makeText(PostActivity.this, "Enter title", Toast.LENGTH_SHORT).show();
                    editTextTitle.setError("Title required");
                }
                else if(TextUtils.isEmpty(description)) {
                    Toast.makeText(PostActivity.this, "Enter Description", Toast.LENGTH_SHORT).show();
                    editTextDescription.setError("Title required");
                }

                if(image_rui == null) {
                    // post without image
                    uploadPost(title, description, "noImage");
                }
                else {
                    // post with image
                    uploadPost(title, description, String.valueOf(image_rui));
                }


            }
        });
    }

    // Functions -------------------------------------------------------------

    private void uploadPost(String title, String description, String uri) {

        String timeStamp = String.valueOf(System.currentTimeMillis());

        String filePathAndName = "Post/" + "Post_" + timeStamp;

        if(!uri.equals("noImage")) {
            // post with image
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            storageRef.putFile(Uri.parse(uri))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isSuccessful());

                            String downloadUri = uriTask.getResult().toString();

                            if(uriTask.isSuccessful()) {
                                HashMap<Object, String> hashMap = new HashMap<>();
                                // pot post info

                                hashMap.put("accountType", accountType);
                                hashMap.put("uid",uid);
                                hashMap.put("fName",fName);
                                hashMap.put("lName",lName);
                                hashMap.put("email",email);
                                hashMap.put("pId",timeStamp);
                                hashMap.put("pTitle",title);
                                hashMap.put("pDescription",description);
                                hashMap.put("pImage",downloadUri);
                                hashMap.put("pTime",timeStamp);

                                // path to store post data
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("posts");
                                ref.child(timeStamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                // success message
                                                Toast.makeText(
                                                        PostActivity.this,
                                                        "Post published",
                                                        Toast.LENGTH_SHORT
                                                ).show();

                                                // reset Views
                                                editTextTitle.setText("");
                                                editTextDescription.setText("");
                                                imageViewPost.setImageURI(null);
                                                image_rui = null;
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                // failed message
                                                Toast.makeText(
                                                        PostActivity.this,
                                                        "Post failed publishing due to " + e.getMessage(),
                                                        Toast.LENGTH_SHORT
                                                ).show();
                                            }
                                        });

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(
                                    PostActivity.this,
                                    "Access failed due to " + e.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
        }
        else {
            // post without image
            HashMap<Object, String> hashMap = new HashMap<>();
            // pot post info

            hashMap.put("accountType", accountType);
            hashMap.put("uid",uid);
            hashMap.put("fName",fName);
            hashMap.put("lName",lName);
            hashMap.put("email",email);
            hashMap.put("pId",timeStamp);
            hashMap.put("pTitle",title);
            hashMap.put("pDescription",description);
            hashMap.put("pImage","noImage");
            hashMap.put("pTime",timeStamp);

            // path to store post data
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("posts");
            ref.child(timeStamp).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            // success message
                            Toast.makeText(
                                    PostActivity.this,
                                    "Post published",
                                    Toast.LENGTH_SHORT
                            ).show();

                            // reset Views
                            editTextTitle.setText("");
                            editTextDescription.setText("");
                            imageViewPost.setImageURI(null);
                            image_rui = null;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // failed message
                            Toast.makeText(
                                    PostActivity.this,
                                    "Post failed publishing due to " + e.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
        }

    }

    private void connectViews() {
        editTextTitle = findViewById(R.id.et_title_activity_post);
        editTextDescription = findViewById(R.id.et_description_activity_post);
        imageViewPost = findViewById(R.id.iv_post_activity_post);
        buttonUpload = findViewById(R.id.btn_upload_post_activity);
    }

    private void showImagePickDialog() {
        // options(camera, gallery) to show in dialog
        String options[] = {"Camera", "Gallery"};

        // dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image from");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // item click handle
                if(which == 0) {
                    // camera clicked
                    if(!checkCameraPermission()) {
                        requestCameraPermission();
                    }
                    else {
                        pickFromCamera();
                    }

                }
                if(which == 1) {
                    // gallery clicked
                    if(!checkStoragePermission()) {
                        requestStoragePermission();
                    }
                    else {
                        pickFromStorage();
                    }

                }
            }
        });
        builder.create().show();

    }

    private void pickFromStorage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        // Intent to pick image from camera
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Temp Descr");
        image_rui = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_rui);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==  (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) ==  (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==  (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private void checkUserStatus() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            email = user.getEmail();
            uid = user.getUid();
        }
        else {
            startActivity(new Intent(this, StartActivity.class));
            finish();
        }
    }

    // handle permission
    @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if(grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] ==  PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] ==  PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted) {
                        // both permission granted
                        pickFromCamera();
                    }
                    else {
                        Toast.makeText(this, "Camera & Storage both permission are necessary...", Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if(grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted) {
                        // storage permission granted
                        pickFromStorage();
                    }
                    else {
                        Toast.makeText(this, "Storage permission are necessary...", Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                }
            }
            break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK) {
            if(requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_rui = data.getData();
                imageViewPost.setImageURI(image_rui);
            }
            else if(requestCode == IMAGE_PICK_CAMERA_CODE) {
                imageViewPost.setImageURI(image_rui);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    
}


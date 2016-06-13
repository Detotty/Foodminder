package com.hpp.foodminder;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hpp.foodminder.db.DatabaseHelper;
import com.hpp.foodminder.dialog.UserInfoDialog;
import com.hpp.foodminder.models.ItemsModel;
import com.hpp.foodminder.models.RestaurantModel;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddItem extends SlidingDrawerActivity {

    EditText Notes, ItemName;
    TextView restName;
    private RatingBar ratingBar;
    ImageView profileImageView, Cam;
    private DatabaseHelper databaseHelper = null;
    Dao<ItemsModel, Integer> itemsDao;
    RestaurantModel restaurantModel;
    ItemsModel itemsModel = new ItemsModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        try {
            // This is how, a reference of DAO object can be done
            restaurantModel = (RestaurantModel) getIntent().getSerializableExtra("Rest");
            itemsDao = getHelper().getItemsDao();
            itemsModel = (ItemsModel) getIntent().getSerializableExtra("Item");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        ItemName = (EditText) findViewById(R.id.et_name);
        Notes = (EditText) findViewById(R.id.et_notes);
        restName = (TextView) findViewById(R.id.tv_name);
        Cam = (ImageView) findViewById(R.id.iv_cam);
        profileImageView = (ImageView) findViewById(R.id.iv_pic);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        if (itemsModel != null) {
            ItemName.setText(itemsModel.getName());
            Notes.setText(itemsModel.getNotes());
            restName.setText(restaurantModel.getName());
            ratingBar.setRating(Float.parseFloat(itemsModel.getRating()));
            if (itemsModel.getPic() != null) {
                byte[] imageAsBytes = Base64.decode(itemsModel.getPic().getBytes(), Base64.DEFAULT);
                profileImageView.setImageBitmap(
                        BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
                );
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        ImageView mHome = (ImageView) toolbar.findViewById(R.id.home);
        mHome.setImageDrawable(getResources().getDrawable(R.drawable.check_mark));
        TextView mText = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Typeface tf = Typeface.createFromAsset(getAssets(), "Pacifico.ttf");
        mText.setTypeface(tf);
        restName.setTypeface(tf);
        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((!ItemName.getText().toString().isEmpty()) && (!Notes.getText().toString().isEmpty())) {

                    if (itemsModel == null) {
                        itemsModel = new ItemsModel();
                    }
                    itemsModel.setName(ItemName.getText().toString());
                    itemsModel.setNotes(Notes.getText().toString());
                    itemsModel.setRating(String.valueOf(ratingBar.getRating()));
                    itemsModel.setRestaurantModel(restaurantModel);
//                    profile_image_bitmap = profileImageView.getDrawingCache();
                    if (profile_image_bitmap != null)
                        itemsModel.setPic(encodeTobase64(profile_image_bitmap));

                    updateItems(itemsModel);
                    finish();
                } else {
                    Toast.makeText(AddItem.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfileImage(getString(R.string.add_photo_title));
            }
        });
    }


    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public void updateItems(ItemsModel itemsModel) {
        try {
            itemsDao.createOrUpdate(itemsModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //region Image Selection
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_TAKE_PHOTO_IMAGEVIEW) {
            if (resultCode == this.RESULT_OK) {

                try {

                    rotatePicture(data.getData());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else if (requestCode == ACTION_TAKE_PHOTO_FROM_GALLERY) {

            if (resultCode == this.RESULT_OK) {

                try {

                    Uri selectedImageUri = data.getData();
                    rotatePicture(selectedImageUri);


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }

    }

    private void selectProfileImage(String dialogTitle) {
        final CharSequence[] options = {getString(R.string.take_photo_title), getString(R.string
                .choose_from_gallery), getString(R.string.cancel_title)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(dialogTitle);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals(getString(R.string.take_photo_title))) {
                    requestForCameraPermission(getString(R.string.take_photo_title));
                } else if (options[item].equals(getString(R.string.choose_from_gallery))) {
                    requestForCameraPermission(getString(R.string.choose_from_gallery));

                } else if (options[item].equals(getString(R.string.cancel_title))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private int REQUEST_CAMERA_PERMISSION = 100;
    final private int REQUEST_EXTERNAL_PERMISSIONS = 124;

    public void requestForCameraPermission(String request) {

        int rCode = REQUEST_EXTERNAL_PERMISSIONS;

        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();

        if (request.equals(getString(R.string.take_photo_title))) {
            rCode = REQUEST_CAMERA_PERMISSION;
            if (!addPermission(permissionsList, android.Manifest.permission.CAMERA))
                permissionsNeeded.add("Camera");
        } else
            rCode = REQUEST_EXTERNAL_PERMISSIONS;

        if (!addPermission(permissionsList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write External Storage");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read External Storage");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);

                final int finalRCode = rCode;
                UserInfoDialog.createDialog(this, message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(AddItem.this,
                                permissionsList.toArray(new String[permissionsList.size()]),
                                finalRCode);
                    }
                });

            }
            ActivityCompat.requestPermissions(this,
                    permissionsList.toArray(new String[permissionsList.size()]),
                    rCode);
        } else {

            if (rCode == REQUEST_CAMERA_PERMISSION)
                dispatchTakePictureIntent();
            else
                openGallery();
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                // Show permission rationale
                return false;
            }
        }
        return true;
    }

    public static final int ACTION_TAKE_PHOTO_IMAGEVIEW = 21;
    public static final int ACTION_TAKE_PHOTO_FROM_GALLERY = 22;

    // New function for captureImageIntent
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, ACTION_TAKE_PHOTO_IMAGEVIEW);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, ACTION_TAKE_PHOTO_FROM_GALLERY);
    }

    Bitmap profile_image_bitmap = null;
    String imageBase64String = "";

    String profile_image_path = null;

    private void rotatePicture(Uri uri) {
        try {

            String picturePath = getPath(uri);
            Log.e("", "image from gallery path " + picturePath);
            //SharedPreferenceManager.saveString(RegistrationProfileActivity.this, GlobelConstants.profile_image, profile_image_array);
            Bitmap bitmapSelectedImage = BitmapFactory.decodeFile(picturePath);

            if (bitmapSelectedImage == null) {
                selectProfileImage(getString(R.string.reselect_picture_title));
                return;
            }

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            int targetW = profileImageView.getWidth();
            int targetH = profileImageView.getHeight();

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, targetW, targetH);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);

            // Determine Orientation
            ExifInterface exif = new ExifInterface(picturePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            // Determine Rotation
            int rotation = 0;

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                rotation = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                rotation = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                rotation = 270;
            }

            Bitmap rotated;
            android.util.Log.d("response", "rotation:" + rotation);

            //int pixel = MyTeamController.dpToPx(1);

            // Rotate Image if Necessary
            if (rotation != 0) {
                // Create Matrix
                Matrix matrix = new Matrix();
                matrix.postRotate(rotation);
                rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                bitmap.recycle();
                bitmap = Bitmap.createScaledBitmap(rotated, targetW, targetH, true);
                rotated.recycle();

            } else {
                rotated = Bitmap.createScaledBitmap(bitmap, targetW, targetH, true);
                bitmap.recycle();
                bitmap = rotated;
                rotated = null;
            }

            profile_image_bitmap = bitmap;
            //bitmapSelectedImage = Image_Scaling.getImageOrintation(bitmapSelectedImage, picturePath);
            profileImageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String index = cursor.getString(column_index);
        cursor.close();
        return index;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        android.util.Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }
    //endregion

}

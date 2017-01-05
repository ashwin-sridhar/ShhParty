package de.tudarmstadt.informatik.tk.shhparty.user;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.IOException;

import de.tudarmstadt.informatik.tk.shhparty.PartyInfoActivity;
import de.tudarmstadt.informatik.tk.shhparty.R;

public class CreateProfile extends Activity implements View.OnClickListener {

  private static int RESULT_LOAD_IMAGE = 1;

  String name ;
  Button buttonEnter,buttonLoadPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        buttonEnter = (Button) findViewById(R.id.buttonEnter);

        buttonEnter.setOnClickListener(this);

        buttonLoadPicture = (Button) findViewById(R.id.buttonLoadPicture);

        buttonLoadPicture.setOnClickListener(this);
    }

  public void onClick(View v) {

    EditText et1 = (EditText) findViewById(R.id.editText);

    switch (v.getId()) {
      case R.id.buttonEnter:

        Intent intentToPartyInfo = new Intent(this, PartyInfoActivity.class);
        intentToPartyInfo.putExtra("editText", et1.getText().toString());
        startActivity(intentToPartyInfo);
        break;
      case R.id.buttonLoadPicture:
        Intent intent = new Intent(
          Intent.ACTION_PICK,
          android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, RESULT_LOAD_IMAGE);
        break;
    }

  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
      Uri selectedImage = data.getData();
      String[] filePathColumn = { MediaStore.Images.Media.DATA };

      Cursor cursor = getContentResolver().query(selectedImage,
        filePathColumn, null, null, null);
      cursor.moveToFirst();

      int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
      String picturePath = cursor.getString(columnIndex);
      cursor.close();

      ImageView imageView = (ImageView) findViewById(R.id.imgView);

      Bitmap bmp = null;
      try {
        bmp = getBitmapFromUri(selectedImage);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      imageView.setImageBitmap(bmp);

    }


  }



  private Bitmap getBitmapFromUri(Uri uri) throws IOException {
    ParcelFileDescriptor parcelFileDescriptor =
      getContentResolver().openFileDescriptor(uri, "r");
    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
    Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

    parcelFileDescriptor.close();
    return image;
  }



}

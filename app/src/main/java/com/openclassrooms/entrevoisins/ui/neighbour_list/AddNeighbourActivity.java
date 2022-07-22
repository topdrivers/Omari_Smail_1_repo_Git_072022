package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.annotation.SuppressLint;
import android.content.Intent;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNeighbourActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avatar)
    ImageView avatar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.nameLyt)
    TextInputLayout nameInput;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.phoneNumberLyt)
    TextInputLayout phoneInput;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.addressLyt)
    TextInputLayout addressInput;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.aboutMeLyt)
    TextInputLayout aboutMeInput;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.create)
    MaterialButton addButton;

    private NeighbourApiService mApiService;
    private String mNeighbourImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_neighbour);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mApiService = DI.getNeighbourApiService();
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        mNeighbourImage = randomImage();
        Glide.with(this).load(mNeighbourImage).placeholder(R.drawable.ic_account)
                .apply(RequestOptions.circleCropTransform()).into(avatar);
        Objects.requireNonNull(nameInput.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                addButton.setEnabled(s.length() > 0);
            }
        });

    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.create)
    void addNeighbour() {
        Neighbour neighbour = new Neighbour(
                System.currentTimeMillis(),
                Objects.requireNonNull(nameInput.getEditText()).getText().toString(),
                mNeighbourImage,
                Objects.requireNonNull(addressInput.getEditText()).getText().toString(),
                Objects.requireNonNull(phoneInput.getEditText()).getText().toString(),
                Objects.requireNonNull(aboutMeInput.getEditText()).getText().toString()
        );
        mApiService.createNeighbour(neighbour);
        finish();
    }

    /**
     * Generate a random image. Useful to mock image picker
     * @return String
     */
    String randomImage() {
        return "https://i.pravatar.cc/150?u="+ System.currentTimeMillis();
    }

    /**
     * Used to navigate to this activity
     * param activity
     */
    public static void navigate(FragmentActivity activity) {
        Intent intent = new Intent(activity, AddNeighbourActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }
}

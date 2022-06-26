package com.velasco.recipeapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.velasco.recipeapp.Pojo.User;
import com.velasco.recipeapp.LoginAndRegister.SharedPrefManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View view;
    private TextView nameTv, emailTv;
    private Button logoutBtn;

    private ImageView contactPictureIv;
    private Button editPhotoBtn;
    private User user;
    private TextView welcomeTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);


        // Σύνδεση με τη xml
        nameTv = view.findViewById(R.id.tv_name);
        emailTv = view.findViewById(R.id.tv_email);
        logoutBtn = view.findViewById(R.id.btn_logout);

        contactPictureIv = view.findViewById(R.id.iv_contact_picture);
        editPhotoBtn = view.findViewById(R.id.btn_editPhoto);
        welcomeTv = view.findViewById(R.id.tv_welcome);

        //getting the current user
        user = SharedPrefManager.getInstance(getContext()).getUser();

        // set text
        nameTv.setText(user.getName());
        emailTv.setText(user.getEmail());
        Glide.with(this)
                .load(user.getPhoto())
                .placeholder(R.drawable.img_contact_logo)
                .into(contactPictureIv);


        editPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
                //getting the current user
                user = SharedPrefManager.getInstance(getContext()).getUser();


            }
        });


        //when the user presses logout button
        //calling the logout method
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(getContext()).logout();
                getActivity().finish();

            }
        });


        return view;
    }


    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Browse picture"), 1);
    }


}
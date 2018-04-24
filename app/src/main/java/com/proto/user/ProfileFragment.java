package com.proto.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.proto.MainActivity;
import com.proto.R;

import org.w3c.dom.Text;

import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {

   // public User user;

    public ProfileFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        User user = (User) getArguments().getSerializable("USER");
        if (user != null) {
            ((TextView) view.findViewById(R.id.txvCallingName)).setText(user.getCallingName());
            ((TextView) view.findViewById(R.id.txvName)).setText(user.getName());
            ((TextView) view.findViewById(R.id.txvBirthday)).setText(user.getBirthdate());
            ((TextView) view.findViewById(R.id.txvEmail)).setText(user.getEmail());
            ((TextView) view.findViewById(R.id.txvDiet)).setText(user.getDiet());

            user.getDiet();
            if (user.getPhoneVisible() == 0) {
                ((TextView) view.findViewById(R.id.txvPhone)).setText(user.getPhone());

            } else {
                ((TextView) view.findViewById(R.id.txvPhone)).setText("No phone avaliable");
            }
        }


        return view;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Menu 1");
    }
}
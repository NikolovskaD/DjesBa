package mk.ukim.finki.djesba.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import mk.ukim.finki.djesba.MainPageActivity;
import mk.ukim.finki.djesba.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment3 extends Fragment {

    ViewPager viewPager;
    TextView done;

    public Fragment3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fragment3, container, false);

        viewPager = getActivity().findViewById(R.id.introPager);

        done = view.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prodolzi so MainPageActivity
                //getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                startActivity(new Intent(getContext(), MainPageActivity.class));
            }
        });

        return view;
    }

}

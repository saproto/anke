package com.proto.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proto.MainActivity;
import com.proto.R;
import com.tyczj.extendedcalendarview.Day;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class CalendarFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this, root);

        setHasOptionsMenu(true);

        return root;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar;
        TabLayout tabLayout;
        ViewPager viewPager;

        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Calendar");

        MainActivity mainActivity = (MainActivity)getActivity();
        toolbar = (Toolbar)mainActivity.findViewById(R.id.toolbar);
        mainActivity.setSupportActionBar(toolbar);
        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager)view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) mainActivity.findViewById(R.id.nav_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new RecentEventsFragment(), getString(R.string.recent_events));
        adapter.addFragment(new SubscribedEventsFragment(), getString(R.string.subscribed_events));
        adapter.addFragment(new CalendarEventsFragment(), getString(R.string.calendar));
        adapter.addFragment(new AllEventsFragment(), getString(R.string.all_events));
        viewPager.setAdapter(adapter);
    }

//    @Overr
// ide
//    public void onDaySelected(Day day) {
//
//    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }
}
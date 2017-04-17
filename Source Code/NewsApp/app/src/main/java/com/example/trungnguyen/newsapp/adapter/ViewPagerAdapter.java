package com.example.trungnguyen.newsapp.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.trungnguyen.newsapp.fragment.FragmentCongNghe;
import com.example.trungnguyen.newsapp.fragment.FragmentKinhTe;
import com.example.trungnguyen.newsapp.fragment.FragmentGiaiTri;
import com.example.trungnguyen.newsapp.fragment.FragmentXeCo;
import com.example.trungnguyen.newsapp.fragment.FragmentPhapLuat;
import com.example.trungnguyen.newsapp.fragment.FragmentTheGioi;
import com.example.trungnguyen.newsapp.fragment.FragmentTheThao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung Nguyen on 2/21/2017.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments = new ArrayList<Fragment>();
    List<String> titleFragment = new ArrayList<String>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);

        fragments.add(new FragmentTheGioi());
        fragments.add(new FragmentTheThao());
        fragments.add(new FragmentCongNghe());
        fragments.add(new FragmentGiaiTri());
        fragments.add(new FragmentXeCo());
        fragments.add(new FragmentPhapLuat());
        fragments.add(new FragmentKinhTe());


        titleFragment.add("Thế giới");
        titleFragment.add("Thể thao");
        titleFragment.add("Công nghệ");
        titleFragment.add("Giải trí");
        titleFragment.add("Xe Cộ");
        titleFragment.add("Pháp luật");
        titleFragment.add("Kinh tế");
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleFragment.get(position);
    }

}

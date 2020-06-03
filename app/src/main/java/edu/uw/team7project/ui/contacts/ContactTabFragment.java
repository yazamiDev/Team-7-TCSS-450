package edu.uw.team7project.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import edu.uw.team7project.R;

/**
 * A contact tab fragment for displaying multiple tabs.
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 *
 * @author Trevor Nichols
 */
public class ContactTabFragment extends Fragment {
    ContactsCollectionAdapter mCollectionAdapter;
    ViewPager2 viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mCollectionAdapter = new ContactsCollectionAdapter(this);
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(mCollectionAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(mCollectionAdapter.getLable())
        ).attach();
    }
}

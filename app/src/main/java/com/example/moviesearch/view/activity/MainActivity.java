package com.example.moviesearch.view.activity;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;

import com.example.moviesearch.R;
import com.example.moviesearch.databinding.ActivityMainBinding;
import com.example.moviesearch.view.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initView();
    }

    void initView() {
        replaceFragment(R.id.activity_main_flContainer, new MainFragment(), false);
    }


    /**
     * Adds the Fragment into layout container
     *
     * @param fragmentContainerResourceId Resource id of the layout in which Fragment will be added
     * @param currentFragment             Current loaded Fragment to be hide
     * @param nextFragment                New Fragment to be loaded into fragmentContainerResourceId
     * @param commitAllowingStateLoss     true if commitAllowingStateLoss is needed
     * @return true if new Fragment added successfully into container, false otherwise
     * @throws IllegalStateException Exception if Fragment transaction is invalid
     */
    public boolean addFragment(final int fragmentContainerResourceId, final Fragment currentFragment, final Fragment nextFragment, final boolean commitAllowingStateLoss) throws IllegalStateException {
        if (currentFragment == null || nextFragment == null) {
            return false;
        }
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragmentContainerResourceId, nextFragment, nextFragment.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(nextFragment.getClass().getSimpleName());

        final Fragment parentFragment = currentFragment.getParentFragment();
        fragmentTransaction.hide(parentFragment == null ? currentFragment : parentFragment);

        if (!commitAllowingStateLoss) {
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.commitAllowingStateLoss();
        }

        return true;
    }

    /**
     * Replaces the Fragment into layout container
     *
     * @param fragmentContainerResourceId Resource id of the layout in which Fragment will be added
     * @param nextFragment                New Fragment to be loaded into fragmentContainerResourceId
     * @param commitAllowingStateLoss     true if commitAllowingStateLoss is needed
     * @return true if new Fragment added successfully into container, false otherwise
     * @throws IllegalStateException Exception if Fragment transaction is invalid
     */
    public boolean replaceFragment(final int fragmentContainerResourceId, final Fragment nextFragment, final boolean commitAllowingStateLoss) throws IllegalStateException {
        if (nextFragment == null) {
            return false;
        }
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(fragmentContainerResourceId, nextFragment, nextFragment.getClass().getSimpleName());

        if (!commitAllowingStateLoss) {
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.commitAllowingStateLoss();
        }

        return true;
    }
}

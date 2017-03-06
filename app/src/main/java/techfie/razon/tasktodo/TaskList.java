package techfie.razon.tasktodo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.gigamole.navigationtabstrip.NavigationTabStrip;

public class TaskList extends AppCompatActivity {
    static private ViewPager mViewPager;
    static FragmentPagerAdapter adapterViewPager;

    static String[] tabName = {"Remaining", "Completed"};
    static private NavigationTabStrip mCenterNavigationTabStrip;

   // Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
//        toolbar = (Toolbar) findViewById(R.id.activitytoolbar);
//        setSupportActionBar(toolbar);
        initUI();

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapterViewPager);
        mCenterNavigationTabStrip.setViewPager(mViewPager);

    }

    private void initUI() {
        mViewPager = (ViewPager) findViewById(R.id.vp);
        mCenterNavigationTabStrip = (NavigationTabStrip) findViewById(R.id.nts_center);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return new FragmentRemain();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return new FragmentDone();
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return tabName[position];
        }

    }


}
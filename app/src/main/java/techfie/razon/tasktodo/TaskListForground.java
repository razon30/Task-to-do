package techfie.razon.tasktodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

public class TaskListForground extends AppCompatActivity {
    static private ViewPager mViewPager;
    static FragmentPagerAdapter adapterViewPager;

    static String[] tabName = {"ToDo", "Remaining", "Completed"};
    static private NavigationTabStrip mCenterNavigationTabStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list_forground);

        initUI();

        SpaceNavigationView spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("Home", R.drawable.ic_menu));
        spaceNavigationView.addSpaceItem(new SpaceItem("About Techfie", R.drawable.ic_profile));

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                // Toast.makeText(TaskActivity.this,"onCentreButtonClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                if (itemIndex==0) {
                    startActivity(new Intent(TaskListForground.this, TaskActivity.class));

                }
                else if (itemIndex==1) {
                    startActivity(new Intent(TaskListForground.this, AboutTechfie.class));

                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                if (itemIndex==0) {
                  //  Toast.makeText(TaskActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TaskListForground.this, TaskActivity.class));

                }  else if (itemIndex==1) {
                 //   Toast.makeText(TaskActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TaskListForground.this, AboutTechfie.class));

                }
            }
        });

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapterViewPager);
        mCenterNavigationTabStrip.setViewPager(mViewPager);

    }

    private void initUI() {
        mViewPager = (ViewPager) findViewById(R.id.vp);
        mCenterNavigationTabStrip = (NavigationTabStrip) findViewById(R.id.nts_center);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

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
                case 0:
                    return new FragmentToDoList();
                case 1: // Fragment # 0 - This will show FirstFragment
                    return new FragmentRemain();
                case 2: // Fragment # 0 - This will show FirstFragment different title
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

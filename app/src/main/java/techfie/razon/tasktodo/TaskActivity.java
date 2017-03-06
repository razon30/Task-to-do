package techfie.razon.tasktodo;

import android.app.AlarmManager;
import android.app.PendingIntent;
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

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TaskActivity extends AppCompatActivity {

    static private ViewPager mViewPager;
    static FragmentPagerAdapter adapterViewPager;

    static String[] tabName = {"Add ToDo", "Set Task"};
    static private NavigationTabStrip mCenterNavigationTabStrip;

    // Toolbar toolbar;

    private static final long RIPPLE_DURATION = 250;
    Unbinder unbinder;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.root)
//    FrameLayout root;
//    @BindView(R.id.content_hamburger)
//    View contentHamburger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        //        toolbar = (Toolbar) findViewById(R.id.activitytoolbar);
//        setSupportActionBar(toolbar);
        unbinder = ButterKnife.bind(this);
        worksOnReceiver();
        initUI();

//        if (toolbar != null) {
//            setSupportActionBar(toolbar);
//            getSupportActionBar().setTitle(null);
//        }
//
//        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
//        root.addView(guillotineMenu);
//
//        new GuillotineAnimation.GuillotineBuilder(guillotineMenu,
//                guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
//                .setStartDelay(RIPPLE_DURATION)
//                .setActionBarViewForAnimation(toolbar)
//                .setClosedOnStart(true)
//                .build();


        SpaceNavigationView spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("Task List", R.drawable.ic_menu));
        spaceNavigationView.addSpaceItem(new SpaceItem("About Techfie", R.drawable.ic_profile));

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
               // Toast.makeText(TaskActivity.this,"onCentreButtonClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                if (itemIndex==0) {
                 //   Toast.makeText(TaskActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TaskActivity.this, TaskList.class));

                }
                else if (itemIndex==1) {
                 //   Toast.makeText(TaskActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TaskActivity.this, AboutTechfie.class));

                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
              //  Toast.makeText(TaskActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
                if (itemIndex==0) {
                //    Toast.makeText(TaskActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TaskActivity.this, TaskList.class));

                }  else if (itemIndex==1) {
                 //   Toast.makeText(TaskActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TaskActivity.this, AboutTechfie.class));

                }
            }
        });


        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapterViewPager);
        mCenterNavigationTabStrip.setViewPager(mViewPager);

    }

    private void worksOnReceiver() {

        Intent myIntent = new Intent(TaskActivity.this, PopUpReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(TaskActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC,0, pendingIntent);


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
                    return new FragmentSetTask();
                case 1: // Fragment # 0 - This will show FirstFragment
                    return new FragmentToDoList();
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

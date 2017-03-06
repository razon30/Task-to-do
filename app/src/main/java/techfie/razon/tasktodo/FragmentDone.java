package techfie.razon.tasktodo;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cleveroad.pulltorefresh.firework.FireworkyPullToRefreshLayout;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import me.drakeet.materialdialog.MaterialDialog;


public class FragmentDone extends Fragment {


    RecyclerView recyclerView;
    AdapterRecycler adapter;
    LinearLayoutManager manager;
    RealmResults<ClassTask> list;
    ArrayList<ClassTask> taskList;
    TextView textView;
    Realm realm;


    FireworkyPullToRefreshLayout mPullToRefresh;
    private boolean mIsRefreshing;
    private static final int REFRESH_DELAY = 2000;

    private View view1;
    private boolean add = false;
    private Paint p = new Paint();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_done, container, false);
        Realm.init(getActivity());
        realm = Realm.getDefaultInstance();
        mPullToRefresh = (FireworkyPullToRefreshLayout) view.findViewById(R.id.pullToRefresh);

        taskList = new ArrayList<ClassTask>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        textView = (TextView) view.findViewById(R.id.error);
        manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);

        list = realm.where(ClassTask.class).equalTo("done","1").findAll();

        if (list.size()>0){
          //  Collections.reverse(list);
            for (int i=list.size()-1;i>=0;i--){
                taskList.add(list.get(i));
            }
            adapter = new AdapterRecycler(taskList,getActivity(),"2");
            recyclerView.setAdapter(adapter);
        }else if (list.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
        initRefreshView();

        mPullToRefresh.post(new Runnable() {
            @Override
            public void run() {
                mPullToRefresh.setRefreshing(mIsRefreshing);
            }
        });

        initSwipe(view);



        recyclerView.addOnItemTouchListener(new RecyclerTOuchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onCLick(View v, final int position) {

            }

            @Override
            public void onLongClick(View v, final int position) {

                final MaterialDialog dialog = new MaterialDialog(getActivity());
                dialog.setTitle("Delete Item?");
                dialog.setPositiveButton("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        adapter.removeItem(position);
                        dialog.dismiss();


                    }
                });
                dialog.setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();

                    }
                });
                dialog.show();

            }
        }));



        return view;
    }

    private void initSwipe(View view) {



    }

    private void initRefreshView() {
        mPullToRefresh.setOnRefreshListener(new FireworkyPullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsRefreshing = true;
                mPullToRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        list = realm.where(ClassTask.class).equalTo("done","1").findAll();
                        if (list.size()>0){
                            taskList.clear();
                            //   Collections.reverse(list);
                            for (int i=list.size()-1;i>=0;i--){
                                taskList.add(list.get(i));
                            }
                            adapter = new AdapterRecycler(taskList,getActivity(),"2");
                            recyclerView.setAdapter(adapter);
                        }else if (list.isEmpty()){
                            mPullToRefresh.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                        }

                        mPullToRefresh.setRefreshing(mIsRefreshing = false);
                    }
                }, REFRESH_DELAY);
            }
        });
    }




    public FragmentDone() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public interface ClickListener {

        void onCLick(View v, int position);

        void onLongClick(View v, int position);

    }

    static class RecyclerTOuchListener implements RecyclerView.OnItemTouchListener {

        GestureDetector gestureDetector;
        ClickListener clickListener;

        public RecyclerTOuchListener(Context context, final RecyclerView rv, final ClickListener clickListener) {

            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                    View child = rv.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, rv.getChildPosition(child));
                    }

                }
            });


        }


        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {

                clickListener.onCLick(child, rv.getChildPosition(child));

            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }




}

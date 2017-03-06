package techfie.razon.tasktodo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cleveroad.pulltorefresh.firework.FireworkyPullToRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmResults;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class FragmentToDoList extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.input_edit)
    EditText inputEdit;
    @BindView(R.id.input_text)
    TextView inputText;
    @BindView(R.id.input_layout)
    LinearLayout inputLayout;
    @BindView(R.id.root)
    LinearLayout root;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.pullToRefresh)
    FireworkyPullToRefreshLayout mPullToRefresh;
    private boolean mIsRefreshing;
    private static final int REFRESH_DELAY = 2000;

    LinearLayoutManager manager;
    AdapterTaskToDo adapter;

    ArrayList<ClassToDo> arrayList;
    Realm realm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_to_do_list, container, false);
        Realm.init(getActivity());
        realm = Realm.getDefaultInstance();
        unbinder = ButterKnife.bind(this, view);

        manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new LandingAnimator());
        arrayList = new ArrayList<ClassToDo>();
        RealmResults<ClassToDo> realmResults;
        if (!realm.where(ClassToDo.class).findAll().isEmpty()){
            realmResults = realm.where(ClassToDo.class).findAll();
            for (int i = 0;i<realmResults.size();i++){
                if (!realmResults.get(i).isDone()) {
                    arrayList.add(realmResults.get(i));
                }
            }
        }
        adapter = new AdapterTaskToDo(getActivity());
//        ArrayList<ClassToDo> list = new ArrayList<ClassToDo>();
//        list = arrayList;
        Collections.reverse(arrayList);
        adapter.setData(arrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.getItemAnimator().setAddDuration(500);


        initRefreshView();

        mPullToRefresh.post(new Runnable() {
            @Override
            public void run() {
                mPullToRefresh.setRefreshing(mIsRefreshing);
            }
        });

       onClick(view);

        return view;
    }

    private void initRefreshView() {
        mPullToRefresh.setOnRefreshListener(new FireworkyPullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsRefreshing = true;
                mPullToRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        arrayList.clear();
                        RealmResults<ClassToDo> realmResults;
                        if (!realm.where(ClassToDo.class).findAll().isEmpty()){
                            realmResults = realm.where(ClassToDo.class).findAll();
                            for (int i = 0;i<realmResults.size();i++){
                                if (!realmResults.get(i).isDone()) {
                                    arrayList.add(realmResults.get(i));
                                }
                            }
                        }
                        adapter = new AdapterTaskToDo(getActivity());
//        ArrayList<ClassToDo> list = new ArrayList<ClassToDo>();
//        list = arrayList;
                        Collections.reverse(arrayList);
                        adapter.setData(arrayList);
                        recyclerView.setAdapter(adapter);
                        mPullToRefresh.setRefreshing(mIsRefreshing = false);
                    }
                }, REFRESH_DELAY);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.input_edit, R.id.input_text, R.id.input_layout})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.input_edit:
                break;

            case R.id.input_text:
                if (!inputEdit.getText().toString().isEmpty())
                {

                    SharedPreferences preferences = SampleApplication.preferences;
                    long id = preferences.getLong("id",1);
                    String note = inputEdit.getText().toString();
                    final ClassToDo classToDo = new ClassToDo(note,"1",String.valueOf(id),false,false);
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            ClassToDo classToDo1 = realm.copyToRealm(classToDo);
                        }
                    });

                    arrayList.add(classToDo);
//                    ArrayList<ClassToDo> list = new ArrayList<ClassToDo>();
//                    list.addAll(arrayList);
                    Collections.reverse(arrayList);
                    adapter.setData(arrayList);
                    adapter.notifyDataSetChanged();
                    //    adapter.notifyItemInserted(0);

                    SharedPreferences.Editor editor = preferences.edit();
                    long id1 = id + 1;
                    editor.putLong("id", id1);
                    editor.commit();
                    editor.apply();


                }else {
                    Toast.makeText(getActivity(), "Please enter a Task", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.input_layout:
                Toast.makeText(getActivity(), "This is a message from the activity", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

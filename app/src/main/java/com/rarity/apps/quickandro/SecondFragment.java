package com.rarity.apps.quickandro;

/**
 * Created by Premang on 06-Aug-16.
 */
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v7.widget.DefaultItemAnimator;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import java.util.ArrayList;

/**
 * Created by meet on 8/6/2016.
 */
public class SecondFragment extends Fragment {

    private RecyclerView r_view;
    private Adapter adp;
    ArrayList<String> conversation;

    public SecondFragment() {}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        r_view = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        conversation =  ((MainActivity)getActivity()).conversation;


        adp = new Adapter(conversation);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setStackFromEnd(true);
        r_view.setLayoutManager(mLayoutManager);
        r_view.setItemAnimator(new DefaultItemAnimator());
        r_view.setAdapter(adp);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.two_frag,container,false);
    }

    public String updateListView(String message){
        conversation.add(message);
        adp = new Adapter(conversation);
        r_view.setAdapter(adp);

        return message;
    }
}

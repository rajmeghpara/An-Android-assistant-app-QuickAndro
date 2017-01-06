package com.rarity.apps.quickandro;

/**
 * Created by Premang on 06-Aug-16.
 */
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.view.KeyEvent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.TextView;

        import com.rarity.apps.quickandro.Modules.Call;

        import java.util.ArrayList;

/**
 * Created by meet on 8/6/2016.
 */
public class OneFragment extends Fragment {


    EditText command;
    private ListView suggest;
    private ArrayList<String> numberlist;

    public OneFragment() {}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        suggest = (ListView)getActivity().findViewById(R.id.suggestionList);

        command = (EditText)getActivity().findViewById(R.id.command);
        command.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == 6) {
                    ((MainActivity)getActivity()).btn.callOnClick();
                    handled = true;
                }
                return handled;
            }
        });

        command.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSuggest(new ArrayList<String>());
            }
        });

    }

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.one_frag,container,false);
    }

    public void setSuggest(ArrayList<String> list){

        final ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,list);
        suggest.setAdapter(adapter);

        final String[] reply = new String[1];

        suggest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Call call = new Call(getActivity());
                reply[0] = call.call(numberlist.get(i));
                ((MainActivity)getActivity()).updateLayout(reply[0] + " "+adapter.getItem(i).toString());
                suggest.setAdapter(null);
            }
        });
    }

    public void setnumberList(ArrayList<String> list){
        numberlist = list;
    }

}


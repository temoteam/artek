package org.artek.app.game;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.artek.app.ExceptionHandler;
import org.artek.app.R;
import org.artek.app.adapters.RecyclerAdapter;
import org.artek.app.RecyclerItemClickListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class VisitedFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerAdapter mAdapter;

    FragmentTransaction fTrans;
    DetailSpotFragment detailSpotFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof ExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        }
        return inflater.inflate(R.layout.fragment_visited, null);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Log.d("azaza", "click" + position);
                        Log.d("azaza", view.toString());
                        TextView tw = (TextView)view.findViewById(R.id.tv_recycler_item);
                        String yop = tw.getText().toString();
                        Log.d("yop", yop);
                        writeFile("currcardclick", yop);
                        fTrans = getFragmentManager().beginTransaction();
                        detailSpotFragment = new DetailSpotFragment();
                        fTrans.replace(R.id.frgmContGame,detailSpotFragment);
                        fTrans.addToBackStack(null);
                        fTrans.commit();


                    }

                    @Override public void onLongItemClick(View view, int position) {
                        Log.d("azaza", "Long Click" + position);
                    }
                })
        );
        ArrayList<String> myDataset = getDataSet();

        //HashMap<Integer, String> myDataset= new Map<Integer, String>();

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);


    }

    public ArrayList<String> getDataSet() {

        ArrayList<String> myDataSet = new ArrayList<>();

        String lala = readFile("visited");
       // String lalakek[] = lala.split("##");
        String kek[] = lala.split("#");

        Integer a = kek.length;
        Log.d("lala", a.toString());

        for (String v : kek) {
            String toplol = readFile(v);
            String topkek[] = toplol.split("#");
            myDataSet.add(topkek[0]);
            // Log.d("lalala", kek[i]);
        }

        return myDataSet;


    }

    public void writeFile(String FILENAME, String content) {
        try {

            // open stream to write data
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(getContext().openFileOutput(FILENAME, MODE_PRIVATE)));

            // write data
            bw.write(content);

            // close stream
            bw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String readFile(String FILENAME) {
        String content = "";
        String str = "";
        try {

            // open stream to read data
            BufferedReader br = new BufferedReader(new InputStreamReader(getContext().openFileInput(FILENAME)));

            // read data
            while ((str = br.readLine()) != null) {
                content = content + str;
            }

            // close stream
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }


}
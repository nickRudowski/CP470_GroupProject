package com.code.wlu.cp470_groupproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_notification, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EditText EditTextFrag = view.findViewById(R.id.EditTextFrag);

        ProgressBar progressBar;
        Button Submit = view.findViewById(R.id.Submit);
        progressBar = view.findViewById(R.id.ProgressBar);
        // this is the API endpoint to database
        String urlString = "https://cp470-groupproject-default-rtdb.firebaseio.com/Users/"+ getArguments().getString("user") +"/UserDetails.json";
        // this is class that updates the Database
        class NotificationQuery extends AsyncTask<String, Integer, String> {
            @Override
            protected String doInBackground(String... urls) {
                // PUT api HTTP logic is here to update DB
                try {
                    Random random = new Random();
                    Log.i("Here is URL",urls[1]);
                    URL url = new URL(urls[1]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("PUT");
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    String update = "{\"Notification Pref\":\""+EditTextFrag.getText().toString()+"\"}";
                    OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                    osw.write(String.format(update));
                    osw.flush();
                    osw.close();
                    publishProgress(10,20,30,40,50,60,70,80,90,100);
                    Log.i("EXECUTE",update);
                    Log.i("Response", connection.getResponseMessage());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
            // show progress bar updating
            @Override
            protected void onProgressUpdate(Integer... values) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(values[0]);
            }
            // hide progress bar when we have asynchronusly completed the update
            @Override
            protected void onPostExecute(String s) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if the user didnt enter a value to update then send a toast for asking valid input
                if(EditTextFrag.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(),"Please enter a Value to Change",Toast.LENGTH_LONG).show();
                } else {
                    // if there is a value entered run the asynchorus PUT query to the Database to update the value
                    Log.i("Notification", EditTextFrag.getText().toString());
                    new NotificationQuery().execute(EditTextFrag.getText().toString(), urlString);
                }
                // after we updated the database send the value and update in UI setting activity
                Bundle result = new Bundle();
                result.putString("updateUI", EditTextFrag.getText().toString());
                getParentFragmentManager().setFragmentResult("requestKey", result);
            }
        });
    }

}
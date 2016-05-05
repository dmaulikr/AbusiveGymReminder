package com.pipit.agc.agc.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pipit.agc.agc.R;
import com.pipit.agc.agc.model.Message;
import com.pipit.agc.agc.data.DBRecordsSource;

/**
 * Displays message
 */

public class MessageBodyFragment extends Fragment {
    public final static String TAG = "MessageBodyFragment";
    private static final String ARG_PARAM1 = "id";
    private DBRecordsSource datasource;
    private Message _msg;

    private String _id;

    public MessageBodyFragment() {
        // Required empty public constructor
    }

    public static MessageBodyFragment newInstance(String param1) {
        MessageBodyFragment fragment = new MessageBodyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _id = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message_body, container, false);
        RelativeLayout background = (RelativeLayout) rootView.findViewById(R.id.msgbackground);
        TextView header = (TextView) rootView.findViewById(R.id.header);
        TextView body = (TextView) rootView.findViewById(R.id.body);
        TextView date = (TextView) rootView.findViewById(R.id.date);
        TextView reason = (TextView) rootView.findViewById(R.id.reason);
        if (_id==null){
            return rootView;
        }
        datasource = DBRecordsSource.getInstance();
        datasource.openDatabase();
        Log.d(TAG, "getting frag id " + _id);
        _msg = datasource.getMessageById(_id);
        if (_msg==null){
            //Log.e(TAG, "Message ID " + _id + " was not found in database");
            header.setText("null message");
        }
        header.setText(_msg.getHeader());
        //Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Black.ttf");
        header.setTypeface(null, Typeface.BOLD);;
        header.setTextSize(48);
        body.setText(_msg.getBody());
        body.setTypeface(null, Typeface.BOLD);;
        body.setTextSize(36);
        date.setText(_msg.getIntelligentDateString());

        if (_msg.getReason()== Message.HIT_YESTERDAY) {
            reason.setText(getContext().getResources().getString(R.string.reason_hit_gym_yesterday));
        }
        if (_msg.getReason()== Message.MISSED_YESTERDAY) {
            reason.setText(getContext().getResources().getString(R.string.reason_missed_gym));
        }
        if (_msg.getReason()== Message.HIT_TODAY) {
            reason.setText(getContext().getResources().getString(R.string.reason_hit_gym));
        }
        if (_msg.getReason()==Message.WELCOME){
            reason.setText("Welcome");
        }

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        header.setTextColor(ContextCompat.getColor(getContext(), R.color.schemeone_mediumblue));
        //background.setBackgroundColor(getResources().getColor(R.color.schemeone_tan_two, getActivity().getTheme()));
        //body.setTextColor(getResources().getColor(R.color.basewhite, getActivity().getTheme()));

        /*Mark as read*/
        if (!_msg.getRead()){
            DBRecordsSource datasource = DBRecordsSource.getInstance();
            datasource.openDatabase();
            datasource.markMessageRead(_msg.getId(), true);
            datasource.closeDatabase();
            _msg.setRead(true);
        }
        return rootView;
    }


}
package com.example.trungnguyen.newsapp;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.trungnguyen.newsapp.adapter.CommentAdapter;
import com.example.trungnguyen.newsapp.fragment.FragmentCongNghe;
import com.example.trungnguyen.newsapp.fragment.FragmentTheGioi;
import com.example.trungnguyen.newsapp.model.Comment;
import com.thaonguyen.MongoDBConnectorClient.MongoDBConnectorClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung Nguyen on 3/9/2017.
 */
public class CommentDialog extends DialogFragment implements View.OnClickListener {


    private EditText etCmt;
    ProgressDialog dialog;
    MongoDBConnectorClient mClient;
    String mNewsId;
    static CommentAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(getContext());
        dialog.show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mReturnView = inflater.inflate(R.layout.comment_dialog, container, false);
        RecyclerView rvComment = (RecyclerView) mReturnView.findViewById(R.id.lvComments);
        etCmt = (EditText) mReturnView.findViewById(R.id.etComment);
        TextView tvLoginCmt = (TextView) mReturnView.findViewById(R.id.tvLoginCmt);
        ImageView btnSubmitCmt = (ImageView) mReturnView.findViewById(R.id.btn_submit_cmt);
        btnSubmitCmt.setOnClickListener(this);
        Bundle bundle = getArguments();
        mNewsId = bundle.getString(FragmentCongNghe.NEWS_ID, "NULL");
        if (!bundle.getBoolean(MainActivity.IS_LOGIN)) {
            etCmt.setVisibility(View.GONE);
            tvLoginCmt.setVisibility(View.VISIBLE);
        } else {
            etCmt.setVisibility(View.VISIBLE);
            tvLoginCmt.setVisibility(View.GONE);
        }
        // Setup dialog
        try {
            getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM); // todo set position of dialog fragment

            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // todo: check out below note

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Must set getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // to set dialog full of screen width and if keyboard is showing, the dialog will be collapsed
        // and one more function, the dialog will be have the radius attributes


//        List<Comment> comments = bundle.getParcelableArrayList(FragmentTheGioi.COMMENT);

        mAdapter = new CommentAdapter(getContext());

        rvComment.setAdapter(mAdapter);


        return mReturnView;
    }


    @Override
    public void onResume() {
        mClient = MainActivity.getClient();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        params.height = getResources().getDimensionPixelSize(R.dimen.comment_dialog_height);

        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        Log.d("DIALOG", "onResume");
        mClient.GetComment(mNewsId);
        dialog.dismiss();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit_cmt) {
            String content = etCmt.getText().toString();
            if (!content.isEmpty()) {
                mClient.AddComment(mNewsId, content);
                etCmt.requestFocus();
            }
        }
    }

    public static void pushData(ArrayList<Comment> listCmt) {
        mAdapter.addNewList(listCmt);
    }
}

package com.example.trungnguyen.newsapp;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.trungnguyen.newsapp.adapter.CommentAdapter;
import com.example.trungnguyen.newsapp.fragment.FragmentTheGioi;
import com.example.trungnguyen.newsapp.model.Comment;
import com.example.trungnguyen.newsapp.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung Nguyen on 3/9/2017.
 */
public class CommentDialog extends DialogFragment {


    int mNewsPosition;
    ListView lvComment;
    ProgressDialog dialog;

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
        lvComment = (ListView) mReturnView.findViewById(R.id.lvComments);
        // Setup dialog
        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM); // todo set position of dialog fragment

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // todo: check out below note

        // Must set getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // to set dialog full of screen width and if keyboard is showing, the dialog will be collapsed
        // and one more function, the dialog will be have the radius attributes


        List<Comment> comments = getArguments().getParcelableArrayList(FragmentTheGioi.COMMENT);

        CommentAdapter adapter = new CommentAdapter(getContext(), R.layout.comment_item, comments);

        lvComment.setAdapter(adapter);

        return mReturnView;
    }


    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        params.height = getResources().getDimensionPixelSize(R.dimen.comment_dialog_height);

        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        dialog.dismiss();
        super.onResume();
    }
}

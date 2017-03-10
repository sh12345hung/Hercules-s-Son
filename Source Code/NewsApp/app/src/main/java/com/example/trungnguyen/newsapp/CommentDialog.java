package com.example.trungnguyen.newsapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Trung Nguyen on 3/9/2017.
 */
public class CommentDialog extends DialogFragment {


    int mNewsPosition;
    ArrayList<String> mCommentList;
    ListView lvComment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mReturnView = inflater.inflate(R.layout.comment_dialog, container, false);
        lvComment = (ListView) mReturnView.findViewById(R.id.lvComments);
        ArrayList<String> comments = new ArrayList<>();

        comments.add("Input new comment=========");
        comments.add("Input new comment");
        comments.add("Input new comment");
        comments.add("Input new comment");
        comments.add("Input new comment");
        comments.add("Input new comment");
        comments.add("Input new comment");
        comments.add("Input new comment");
        comments.add("Input new comment");
        comments.add("Input new comment");
        comments.add("Input new comment");
        comments.add("Input new comment");
        comments.add("Input new comment");
        comments.add("Input new comment");
        comments.add("Input new comment");
        comments.add("Input new comment");
        comments.add("Input new comment");
        comments.add("Input new comment");
        comments.add("Input new comment");
        comments.add("Input new comment");

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, comments);

        lvComment.setAdapter(adapter);

        return mReturnView;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = getResources().getDimensionPixelSize(R.dimen.comment_dialog_height);
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        super.onResume();
    }
}

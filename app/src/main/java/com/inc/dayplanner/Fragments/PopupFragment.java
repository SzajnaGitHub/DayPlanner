package com.inc.dayplanner.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.inc.dayplanner.R;
import com.inc.dayplanner.ViewChange.DynamicViews;


public class PopupFragment extends AppCompatDialogFragment {


    private ActivityHandlerListener activityHandlerListener;
    public PopupFragment(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activityHandlerListener = (ActivityHandlerListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ "must implement ActivityHandlerListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.pop_up,null);

        Button delButton = v.findViewById(R.id.deleteDialogButton);
        Button editButton = v.findViewById(R.id.editDialogButton);
        Button cancelButton = v.findViewById(R.id.cancelDialogButton);

        delButton.setOnClickListener(v1 -> {
                    activityHandlerListener.onItemDeleted();
                    dismiss();

                });
        editButton.setOnClickListener(v2 -> {
            activityHandlerListener.onItemEdited();
            dismiss();

        });

        cancelButton.setOnClickListener(v3 -> {

            activityHandlerListener.onItemCanceled();
            dismiss();

        });





        return new AlertDialog.Builder(getActivity()).setView(v).create();
    }

    public interface ActivityHandlerListener{
        void onItemDeleted();
        void onItemEdited();
        void onItemCanceled();
    }
}


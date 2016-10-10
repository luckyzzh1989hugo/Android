package com.hugo.lucky.easyparking;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by lucky on 8/14/2016.
 */
public class MyDialogFragment extends DialogFragment {
    DialogClickListener dialogClickListener;
    public interface DialogClickListener{
        public void positiveClick();
        public void negativeClick();


    }

    public void setDialogClickListener(final DialogClickListener dialogClickListener)
    {
        this.dialogClickListener=dialogClickListener;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.update_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        dialogClickListener.positiveClick();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialogClickListener.negativeClick();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

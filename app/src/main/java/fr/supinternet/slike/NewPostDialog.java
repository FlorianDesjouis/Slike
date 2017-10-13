package fr.supinternet.slike;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

/**
 * Created by trump on 10/10/2017.
 */

public class NewPostDialog extends DialogFragment {

    private EditText etMessage;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setCancelable(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        etMessage = new EditText(getActivity());

        builder.setTitle(R.string.send).setView(etMessage).setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUtils.sendMessage(etMessage.getText().toString());
            }
        }).setNegativeButton(android.R.string.cancel, null);


        return builder.create();
    }


}

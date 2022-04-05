package com.cmput301w22t36.codehunters.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cmput301w22t36.codehunters.Data.DataMappers.UserMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.MainActivity;
import com.cmput301w22t36.codehunters.Data.DataMappers.CommentMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.Comment;
import com.cmput301w22t36.codehunters.QRCode;
import com.cmput301w22t36.codehunters.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Introductory Comments:
 *      This Java file is a fragment representing the geolocation, photo and comment
 *      dialog of our application. The fragment opens when a QR Code in the Codes fragment is
 *      clicked and shows the geolocation ad photo of the QR Code and QR Code Location respectively,
 *      as well as any comments that have been made by the User or Players. If location, photo or
 *      comments were not provided, this fragment will not show anything
 *
 */

public class Geolocation_PhotosFragment extends DialogFragment {

    private QRCode qrCodeClicked;
    private TextView geolocation;
    private ListView commentBox;
    private ImageView photo;
    private ArrayList<String> commentsList = new ArrayList<String>();
    private ArrayAdapter<String> commentsArrayAdapter;


    public Geolocation_PhotosFragment(QRCode qrCodeClicked) {
        this.qrCodeClicked = qrCodeClicked;
    }
    

    @NonNull
    @Override
    /**
     * Opens dialog box and sets TextView and ImageView to geolocation and Bitmap image
     * @return
     * returns the dialog box showing geolocation, photo and comments
     */
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.geolocation_photos, null);
        geolocation = (TextView) view.findViewById(R.id.geolocation);
        photo = (ImageView) view.findViewById(R.id.photo);
        commentBox = (ListView) view.findViewById(R.id.commentbox);
        final EditText editText = new EditText(getContext());


//Sets geolocation and image to their respective TextView and ImageView
        if (qrCodeClicked.getGeolocation() != null) {
            geolocation.setText(qrCodeClicked.getGeolocation().toString());
        } else {
            geolocation.setText("You did not record a location");
        }

        if (qrCodeClicked.getPhoto() != null) {
            photo.setImageBitmap(qrCodeClicked.getPhoto());
        } else {
            photo.setImageBitmap(null);
        }

        // Collect comments for the qrcode post:

        CommentMapper cm = new CommentMapper();
        cm.getCommentsForHash(qrCodeClicked.getHash(), cm.new CompletionHandler<ArrayList<Comment>>() {
            @Override
            public void handleSuccess(ArrayList<Comment> comments) {
                for (Comment c : comments) {
                    String uid = c.getUserRef().substring(7);
                    UserMapper um = new UserMapper();
                    um.get(uid, um.new CompletionHandler<User>() {
                        @Override
                        public void handleSuccess(User data) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
                            Date date = new Date(c.getTimestamp());
                            commentsList.add("[" + dateFormat.format(date) + "] " + data.getUsername() + ": " + c.getComment());
                            commentsArrayAdapter.notifyDataSetChanged();
                        }
                    });

                }
                commentsArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,commentsList){

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view =super.getView(position, convertView, parent);

                        TextView textView=(TextView) view.findViewById(android.R.id.text1);
                        //Changes color of text in the listView to Black
                        textView.setTextColor(Color.BLACK);

                        return view;
                    }
                };
                commentBox.setAdapter(commentsArrayAdapter);
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setPositiveButton("OK", null)
                .setNegativeButton("Add Comment", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Creates a dialog box to Add Comments
                AlertDialog.Builder addCommentBuilder = new AlertDialog.Builder(getContext());
                addCommentBuilder.setTitle("Add Your Comments");
                addCommentBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Updates database with the comment that the User enters
                        String userComment = editText.getText().toString();
                        Comment comment = new Comment();
                        comment.setUserRef("/users/" + MainActivity.mainActivity.loggedinUser.getId().toString());
                        comment.setHashRef(qrCodeClicked.getHash());
                        comment.setComment(userComment);

                        cm.create(comment, cm.new CompletionHandler<Comment>() {
                            @Override
                            public void handleSuccess(Comment comment) {

                            }

                        });
                    }
                });
                addCommentBuilder.setView(editText);
                addCommentBuilder.show();

            }
        }).create();

    }
}

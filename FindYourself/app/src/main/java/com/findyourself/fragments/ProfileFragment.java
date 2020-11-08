package com.findyourself.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.findyourself.R;
import com.findyourself.activities.AboutActivity;
import com.findyourself.activities.LoginActivity;
import com.findyourself.activities.RegisterActivity;
import com.findyourself.activities.ThisUser;
import com.findyourself.activities.TipsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {


    MaterialButton logout, create_room;
    AppCompatImageButton menu_btn;
    MaterialAlertDialogBuilder materialDialog;

    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
    final String uid = fUser.getUid();
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    final DatabaseReference ref = db.getReference("rooms");
    final DatabaseReference user_ref = db.getReference("users");

    MaterialTextView fullname, username, dateofbirth, gender;
    String un, fn, dob, gen;
    ThisUser user = ThisUser.instance;


    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);




    }

    public void showPopup(View v) {
        PopupMenu pop = new PopupMenu(getActivity(), v);
        pop.setOnMenuItemClickListener(this);
        pop.inflate(R.menu.menu_items);
        pop.show();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        fullname = view.findViewById(R.id.fullname_profile);
        fullname.setText(user.getFullname());
        username = view.findViewById(R.id.username_profile);
        username.setText(user.getUsername());
        dateofbirth = view.findViewById(R.id.date_profile);
        dateofbirth.setText(user.getBirthday());
        gender = view.findViewById(R.id.gender_profile);
        gender.setText(user.getGender());
        menu_btn = view.findViewById(R.id.menu_btn);
        logout = view.findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });
        create_room = view.findViewById(R.id.create_room);

        create_room.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {

                user_ref.child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final int present_room_count = Integer.parseInt(snapshot.child("created_rooms").getValue().toString());
                        if (present_room_count > 3) {
                            Toast.makeText(getContext(), "Reached Maximum Limit : 3", Toast.LENGTH_LONG).show();
                        } else {

                            final EditText grp_name = new EditText(view.getContext());
                            final AlertDialog.Builder create_grp_dialog = new AlertDialog.Builder(view.getContext());
                            create_grp_dialog.setTitle("Create New Room");
                            create_grp_dialog.setMessage("Enter Room Name");
                            create_grp_dialog.setView(grp_name);
                            create_grp_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialogInterface, int i) {

                                    String room_name = grp_name.getText().toString();

                                    final HashMap mem = new HashMap();
                                    mem.put(uid, true);
                                    HashMap map = new HashMap();
                                    map.put("room_name", room_name);
                                    map.put("creator", uid);
                                    map.put("members_count", 1);
                                    //map.put("members",mem.get(0));

                                    final String room_id = ref.push().getKey();
                                    ref.child(room_id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                ref.child(room_id).child("members").setValue(mem);
                                                user_ref.child(uid).child("created_rooms").setValue(present_room_count + 1);
                                                Toast.makeText(getContext(), "Room Successfully Created", Toast.LENGTH_LONG).show();
                                                dialogInterface.dismiss();
                                            } else {
                                                Log.i("cannot create room", task.getException().toString());
                                            }
                                        }
                                    });
                                }
                            });

                            create_grp_dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();

                                }
                            });
                            create_grp_dialog.create().show();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }


        });

        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        return view;


    }

    private void create_new_room(View view, final int present_room_count) {


    }


    public void putArgs(Bundle bundle) {
        un = bundle.getString("username");
        fn = bundle.getString("fullname");
        gen = bundle.getString("gender");
        dob = bundle.getString("dob");
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.item_tips:
                startActivity(new Intent(getActivity(), TipsActivity.class));
                break;
//            case R.id.item_delete:
//                //deleteUser();
//                break;
        }
        return true;
    }

    private void deleteUser() {

        final EditText email = new EditText(getContext());
        final EditText password = new EditText(getContext());
        final AlertDialog.Builder create_grp_dialog = new AlertDialog.Builder(getContext());
        create_grp_dialog.setTitle("Delete Account");
        email.setHint("Enter your Email");
        password.setHint("Enter your Password");
        create_grp_dialog.setView(email);
        create_grp_dialog.setView(password);

        create_grp_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {

                String email_ = email.getText().toString();
                String password_ = password.getText().toString();

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AuthCredential credential = EmailAuthProvider
                        .getCredential(email_, password_);

                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    DatabaseReference user_ref = db.getReference("users");
                                    user_ref.child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (!task.isSuccessful()) {
                                                Log.i("error delete from users", task.getException().toString());
                                            }
                                        }
                                    });

                                    final DatabaseReference room_ref = db.getReference("rooms");
                                    room_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (final DataSnapshot ds : snapshot.getChildren()) {
                                                Log.i("ds", ds.getValue().toString());
                                                final DatabaseReference m_ref = ds.child("members").getRef();
                                                m_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot mds : snapshot.getChildren()) {
                                                            Log.i("mds", mds.getKey().toString());
                                                            if (mds.getKey().equals(uid)) {
                                                                m_ref.child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (!task.isSuccessful()) {
                                                                            Log.i("error rooms remove", task.getException().toString());
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });


                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                    startActivity(new Intent(getActivity(), RegisterActivity.class));
                                    getActivity().finish();
                                } else {
                                    Log.i("error account delete", task.getException().toString());
                                }
                            }
                        });
                    }
                });
            }

        });

        create_grp_dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        create_grp_dialog.create().show();


    }
}
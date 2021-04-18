package com.ankita.dell.quiz_app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ankita.dell.quiz_app.BroadcastReceiver.AlarmReceiver;
import com.ankita.dell.quiz_app.Common.Common;
import com.ankita.dell.quiz_app.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    MaterialEditText edtNewUser,edtNewPassword,edtNewEmail; //for sign up
    MaterialEditText edtUser,edtPassword; //for sign in

    Button btnSignUp,btnSignIn;

    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerAlarm();


        //firebase
        database=FirebaseDatabase.getInstance();
        users=database.getReference("Users");

        edtUser=(MaterialEditText)findViewById(R.id.edtUser);
        edtPassword=(MaterialEditText)findViewById(R.id.edtPassword);

        btnSignIn=(Button)findViewById(R.id.btn_sign_in);
        btnSignUp=(Button)findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this,"Clicked !",Toast.LENGTH_SHORT).show();
                signIn(edtUser.getText().toString(),edtPassword.getText().toString());
            }
        });
    }

    private void registerAlarm() {
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,20); //9 hour
        calendar.set(Calendar.MINUTE,3); //
        calendar.set(Calendar.SECOND,0);

        Intent intent=new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(MainActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am=(AlarmManager)this.getSystemService(this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
    }

    private void signIn(final String user, final String pwd){

        Toast.makeText(MainActivity.this,"user "+ user,Toast.LENGTH_SHORT).show();


        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user).exists())
                {
                    //Toast.makeText(MainActivity.this,"Database!",Toast.LENGTH_SHORT).show();
                    if (!user.isEmpty())
                    {
                      //  Toast.makeText(MainActivity.this,"Done 1 !",Toast.LENGTH_SHORT).show();
                        User login=dataSnapshot.child(user).getValue(User.class);
                        if (login.getPassword().equals(pwd)){

                        //    Toast.makeText(MainActivity.this,"Done 2!",Toast.LENGTH_SHORT).show();
                            Intent homeActivity=new Intent(MainActivity.this,Home.class);
                            Common.currentUser1=login;
                            startActivity(homeActivity);
                            finish();
                        }
                        else{
                        Toast.makeText(MainActivity.this,"Wrong Password !",Toast.LENGTH_SHORT).show();
                    }

                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"Please enter your name",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this,"User is not exists !",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showSignUpDialog(){
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Please fill your information");

        LayoutInflater inflater=this.getLayoutInflater();
        View sign_up_layout=inflater.inflate(R.layout.sign_up_layout,null);

        edtNewUser=(MaterialEditText)sign_up_layout.findViewById(R.id.edtNewUserName);
        edtNewEmail=(MaterialEditText)sign_up_layout.findViewById(R.id.edtNewEmail);
        edtNewPassword=(MaterialEditText)sign_up_layout.findViewById(R.id.edtNewPassword);

        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final User user=new User(edtNewUser.getText().toString(),edtNewPassword.getText().toString(),edtNewEmail.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(user.getUserName()).exists())
                            Toast.makeText(MainActivity.this,"User already exists !",Toast.LENGTH_SHORT).show();
                        else
                        {
                            users.child(user.getUserName()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(MainActivity.this,"User registration success !",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(MainActivity.this,"There was a error!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }
}

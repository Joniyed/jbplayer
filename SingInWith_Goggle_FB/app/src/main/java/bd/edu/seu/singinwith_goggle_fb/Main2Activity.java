package bd.edu.seu.singinwith_goggle_fb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Main2Activity extends AppCompatActivity {

    private ImageView profilePic;
    private TextView emailId;
    private TextView id;
    private Button singout;
    private RelativeLayout relativeLayout;

    private GoogleSignInClient mgoogleSignInClient;
    private CallbackManager callbackManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        profilePic = findViewById(R.id.profilePhoto);
        emailId = findViewById(R.id.email_id);
        id = findViewById(R.id.id_no);
        singout = findViewById(R.id.sign_out_button);


        callbackManager = CallbackManager.Factory.create();

        if(AccessToken.getCurrentAccessToken()!=null){
            Intent intent = getIntent();
            String name = intent.getStringExtra("name");
            String profile = intent.getStringExtra("url");
            String email = intent.getStringExtra("email");


            Glide.with(this).load(profile).into(profilePic);
            if(profile==null){
                profilePic.setImageResource(R.drawable.btn_google_dark_disabled);
            }
            id.setText(name);
            emailId.setText(email);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        mgoogleSignInClient = GoogleSignIn.getClient(this,gso);

        singout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_out_button:
                        signOut();
                        LoginManager.getInstance().logOut();
                        break;
                }
            }
        });

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            emailId.setText(personEmail);
            id.setText(personName);
            Glide.with(this).load(String.valueOf(personPhoto)).into(profilePic);
            if(personPhoto==null){
                profilePic.setImageResource(R.drawable.btn_google_dark_disabled);
            }
        }
    }

    private void signOut() {
        mgoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Main2Activity.this, "Successfully SignOut", Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                });
    }
}

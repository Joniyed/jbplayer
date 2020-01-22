package bd.edu.seu.singinwith_goggle_fb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    GoogleSignInClient googleSignInClient;
    GoogleSignInButton signInButton1;
    int RC_SIGN_IN = 0;
    CallbackManager callbackManager;
    LoginButton loginButton;
    private EditText editText1,editText2;
    private Button LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInButton1 = findViewById(R.id.googleSignInButton);
        loginButton = findViewById(R.id.login_button);
        editText1 = findViewById(R.id.userNameFieldid);
        editText2 = findViewById(R.id.PasswordFieldID);
        LOGIN = findViewById(R.id.LogInButtonID);

        callbackManager = CallbackManager.Factory.create();

       loginButton.setReadPermissions(Arrays.asList("email","public_profile"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
               //facebookLogin(loginResult);
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Cancle", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Error",error.toString());
            }
        });


        signInButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.googleSignInButton:
                        signIn();
                        break;
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        googleSignInClient = GoogleSignIn.getClient(this,gso);
    }


    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(this);
        if(acc!=null){
            startActivity(new Intent(MainActivity.this,Main2Activity.class));
        }


        if(AccessToken.getCurrentAccessToken()!=null){
            //facebookLogin(new LoginResult(AccessToken.getCurrentAccessToken(),null,null));
        }

    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Intent intent = new Intent(MainActivity.this,Main2Activity.class);
            startActivity(intent);


        } catch (ApiException e) {
            Log.d("Error","LogInUnseccessFull");
        }
    }

//    private void facebookLogin(LoginResult loginResult){
//        Intent intent = new Intent(MainActivity.this,Main2Activity.class);
//        String id = loginResult.getAccessToken().getUserId();
//        intent.putExtra("id",id);
//        String profilePic = "https://graph.facebook.com/"+loginResult.getAccessToken().getUserId()+
//                "/picture?return_ssl_resource=1";
//        intent.putExtra("profile",profilePic);
//        startActivity(intent);
//    }



    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            try {
                if (accessTokenTracker == null) {
                    Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                    loadUserProfile(currentAccessToken);
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        }
    };


    private void loadUserProfile(AccessToken accessToken){

        try {
            GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    if (object != null) {
                        try {
                            String firstName = object.getString("first_name");
                            String lastname = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            Log.d("Json", response.toString());
                            String imageUrl = "https://graph.facebook.com/" + id + "/picture?type=normal";

                            Toast.makeText(MainActivity.this, firstName + " " + lastname + "\n" + email, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                            intent.putExtra("name",firstName+" "+lastname);
                            intent.putExtra("email",email);
                            intent.putExtra("url",imageUrl);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });


            Bundle bundle = new Bundle();
            bundle.putString("fields", "first_name,last_name,email,id");
            request.setParameters(bundle);
            request.executeAsync();
        }catch (Exception e){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

}

//package com.example.findher;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//
//import com.facebook.AccessToken;
//import com.facebook.AccessTokenTracker;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.GraphRequest;
//import com.facebook.GraphResponse;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
//import com.squareup.picasso.Picasso;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Set;
//
//
//public class FacebookActivity extends Activity
//{
//    private LoginButton loginButton;
//    private ImageView imageOfTheUser;
//    private TextView nameOfTheUser,emailOfUser;
//    private CallbackManager callbackManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.user_login_facebook);
//
//        loginButton = findViewById(R.id.login_button_facebook);
//        nameOfTheUser = findViewById(R.id.first_name_facebook);
//        emailOfUser = findViewById(R.id.email_facebook);
//        imageOfTheUser = findViewById(R.id.img_facebook);
//
//        //loginButton.setVisibility(View.INVISIBLE);
//        Set<String> listOfString = AccessToken.getCurrentAccessToken().getPermissions();
//
//        callbackManager = CallbackManager.Factory.create();
//        loginButton.setPermissions(Arrays.asList("email","public_profile"));
//        checkLoginStatus();
//
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult)
//            {
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//
//
//        });
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
//    {
//        callbackManager.onActivityResult(requestCode,resultCode,data);
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//
//    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
//        @Override
//        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
//        {
//            if(currentAccessToken==null)
//            {
//                nameOfTheUser.setText("");
//                emailOfUser.setText("");
//                Toast.makeText(FacebookActivity.this,"User Logged out",Toast.LENGTH_LONG).show();
//            }
//
//            else
//                loadUserProfile(currentAccessToken);
//        }
//    };
//
//    private void loadUserProfile(AccessToken newAccessToken)
//    {
//        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
//            @Override
//            public void onCompleted(JSONObject object, GraphResponse response)
//            {
//                try {
//                    String first_name = object.getString("first_name");
//                    String last_name = object.getString("last_name");
//                    String email = object.getString("email");
//                    String id = object.getString("id");
//                    TextView location = findViewById(R.id.location_facebook);
//                    String image_url = "https://graph.facebook.com/"+id+ "/picture?type=normal";
//
//                    Intent findHerActivity = new Intent(FacebookActivity.this,ZivugActivity.class);
//                    findHerActivity.putExtra("lastname",first_name);
//                    findHerActivity.putExtra("firstname",last_name);
//                    findHerActivity.putExtra("email",email);
//                    startActivity(findHerActivity);
//
//                    Picasso.get().load(image_url).into(imageOfTheUser);
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//
//        Bundle parameters = new Bundle();
//        parameters.putString("fields","first_name,last_name,email,id");
//        request.setParameters(parameters);
//        request.executeAsync();
//    }
//
//    private void checkLoginStatus()
//    {
//        if(AccessToken.getCurrentAccessToken()!=null)
//        {
//            loadUserProfile(AccessToken.getCurrentAccessToken());
//        }
//    }
//}

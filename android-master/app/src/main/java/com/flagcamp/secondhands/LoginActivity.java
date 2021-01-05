package com.flagcamp.secondhands;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.PrimaryKey;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.flagcamp.secondhands.model.NewUser;
import com.flagcamp.secondhands.model.User;
import com.flagcamp.secondhands.model.UserResponse;
import com.flagcamp.secondhands.network.Api;
import com.flagcamp.secondhands.network.RetrofitClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import java.security.PrivateKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private final static int RC_SIGN_IN = 123;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private GoogleSignInButton googleSignInButton;
    private GoogleSignInClient mGoogleSignInClient;

    //facebook//
    private CallbackManager mCallbackManager;
    private LoginButton fbLoginButton;
    private AccessTokenTracker accessTokenTracker;
    private static final String TAG = "FacebookAuthentication";
    private String idToken;
    private Api api;

    @Override
    protected void onStart() {
        super.onStart();

        api = RetrofitClient.newInstance(getApplicationContext()).create(Api.class);

        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            //Sign in go to Profile Page in project, now change to MainActivity for test;
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        createRequest();

        googleSignInButton = findViewById(R.id.google_signIn);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        //facebook//
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        //       setContentView(R.layout.activity_login);
        //       textViewUser = (TextView)findViewById(R.id.textViewUser);
        fbLoginButton = findViewById(R.id.facebook_login_button);
        fbLoginButton.setReadPermissions("email", "public_profile");
        fbLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "Facebook: onSuccess" + loginResult);
                handleFacebookToken(loginResult.getAccessToken());

                //textViewUser.setText("User ID: " + loginResult.getAccessToken().getUserId() + "\n" + "Auth Token: " + loginResult.getAccessToken().getToken());
                //Toast.makeText(LoginActivity.this, "Facebook Login Successfully.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Facebook: onCancel");
                //            textViewUser.setText("Facebook: onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "Facebook: onError" + error);
                //             textViewUser.setText("Facebook: onError");
            }
        });

        //setupFirebaseAuth;
        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    updateUI(user);

                    NewUser newUser = new NewUser();
                    newUser.userUID = user.getUid(); // TODO: For real
//                    newUser.userUID = "user2"; // For test
                    newUser.email = user.getEmail();
                    newUser.name = user.getDisplayName();
                    newUser.photoUrl = user.getPhotoUrl().toString();

                    // TODO: only for test
//                    CurrentUserSingleton currentUser = CurrentUserSingleton.getInstance();
//                    currentUser.setUserId(user.getUid());
//                    currentUser.setUserName(user.getDisplayName());
//                    currentUser.setEmail(user.getEmail());
//                    currentUser.setPhotoUrl(user.getPhotoUrl().toString());

                    FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                            .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                public void onComplete(@NonNull Task<GetTokenResult> task) {
                                    if (task.isSuccessful()) {
                                        idToken = task.getResult().getToken(); // String idToken;
                                        CurrentUserSingleton currentUser = CurrentUserSingleton.getInstance();
                                        currentUser.setIdToken(idToken); // TODO: only available for one hour
                                        Log.d("idToken", idToken);
                                        getLoginUser(idToken, newUser)
                                                .observe(
                                                        LoginActivity.this,
                                                        userResponse -> {
                                                            if (userResponse != null) {
                                                                User responseUser = userResponse.user;
                                                                currentUser.setUserId(responseUser.userUID);
                                                                currentUser.setUserName(responseUser.name);
                                                                currentUser.setEmail(responseUser.email);
                                                                currentUser.setPhotoUrl(responseUser.photoUrl);
                                                                currentUser.setRating(responseUser.rating);
                                                            }
                                                        }
                                                );
                                    } else {
                                        // Handle error -> task.getException();
                                    }
                                }
                            });

                }else{
                    updateUI(null);
                }
            }
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null){
                    mAuth.signOut();
                }
            }
        };

    }

    private LiveData<UserResponse> getLoginUser(String idToken, NewUser newUser) {
        MutableLiveData<UserResponse> loginUserLiveData = new MutableLiveData<>();
//        api.postUser("diHLnr1thkZ2nkuRG61BHcBoWQ63", newUser) // "user2" for test
        api.postUser(idToken, newUser) // TODO: For real
                .enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.isSuccessful()) {
                            loginUserLiveData.setValue(response.body());
                        } else {
                            loginUserLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        loginUserLiveData.setValue(null);
                    }
                });
        return loginUserLiveData;
    }

    private void handleFacebookToken(AccessToken token){
        Log.d(TAG, "handleFacebookToken" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Facebook Login with credential: successful");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }else{
                            Log.d(TAG, "Facebook Login with credential: failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }


    private void updateUI(FirebaseUser user){
        if(user != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
//            textViewUser.setText(user.getDisplayName());
//            //***get Facebook user photo if possible***//
//            if(user.getPhotoUrl() != null){
//                String photoUrl = user.getPhotoUrl().toString();
//                photoUrl = photoUrl + "?type = large";
//                Picasso.get().load(photoUrl).into(mLogo);
//            }
        }else{
//            textViewUser.setText("");
//            mLogo.setImageResource(R.drawable.logo);
        }
    }



    private void createRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Google//
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        //Facebook//
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
//                    FirebaseUser user = mAuth.getCurrentUser();
                    //Sign in go to Profile Page, now change to MainActivity for test;
                    //Intent intent = new Intent(getApplicationContext(), ContactsContract.Profile.class);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);


                } else {
                    Toast.makeText(LoginActivity.this, "Sorry auth failed.", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener != null){
            mAuth.removeAuthStateListener(authStateListener);
        }
    }


}

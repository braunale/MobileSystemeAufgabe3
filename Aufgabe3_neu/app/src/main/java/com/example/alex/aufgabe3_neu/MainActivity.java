package com.example.alex.aufgabe3_neu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.Task;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private final String ACTIVITY_NAME = MainActivity.class.getSimpleName();
    private final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in_button:
                signIn();
        }

    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInAccount(task);
        } else if (requestCode == 4711){
            mGoogleSignInClient.signOut();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed for the Google services.", Toast.LENGTH_LONG)
                .show();
    }

    private void handleSignInAccount(Task<GoogleSignInAccount> completedTask){
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d(ACTIVITY_NAME, "User successfully signed in, signed in user data:");
            Log.d(ACTIVITY_NAME, "Email: " + account.getEmail());
            Log.d(ACTIVITY_NAME, "Given name: " + account.getGivenName());
            Log.d(ACTIVITY_NAME, "Family name: " + account.getFamilyName());
            Log.d(ACTIVITY_NAME, "Account: " + account.getAccount());

            Intent intent = new Intent(this, LoggedInActivity.class);
            intent.putExtra("user", account);
            startActivityForResult(intent, 4711);

        } catch(ApiException e){
            Toast.makeText(this, "Error while getting the sign in result with status code " + e.getStatusCode(), Toast.LENGTH_LONG)
                    .show();
        }
    }
}

package com.sitadigi.go4lunch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import org.jetbrains.annotations.Nullable;

public class LoginActivity2 extends AppCompatActivity {

    private static final int GOOGLE_SIGN_IN_CODE = 10005;
    SignInButton btnLoginGoogle;
    private FirebaseAuth mAuth;
    GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        btnLoginGoogle = (SignInButton) findViewById(R.id.login_google2);
        mAuth = FirebaseAuth.getInstance();

       gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("838688106638-45mr19aj3cao0mrj6kp18353spg7hskh.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount == null) {
            Toast.makeText(this, "DEJA CONNECTE", Toast.LENGTH_SHORT).show();
        }

        /*mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();*/

        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signIntentGoogle = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signIntentGoogle, GOOGLE_SIGN_IN_CODE);
                //Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                //  startActivity(signIntent,);

            }
        });

    }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == GOOGLE_SIGN_IN_CODE){
                Log.e("TAG", "onActivityResult: IF" );
                Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                Log.e("TAG", "onActivityResult: 3" );

                try{
                    Log.e("TAG", "onActivityResult: 2" );

                    GoogleSignInAccount signInAcc = signInTask.getResult(ApiException.class);
                    Log.e("TAG", "onActivityResult: 1" );

                    AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAcc.getIdToken(),null);
                    mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(LoginActivity2.this, "ON COMPLETE", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "onComplete: " );
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity2.this, "ON FAILLURE", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "onFailure: ");
                        }
                    });
                    Log.e("TAG", "onActivityResult: " );
                }catch (ApiException e){
                    e.printStackTrace();

                }
            }
        }
}
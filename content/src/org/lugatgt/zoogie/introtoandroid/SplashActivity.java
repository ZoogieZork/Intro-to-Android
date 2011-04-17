/*
 * Copyright (C) 2011 Michael Imamura
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.lugatgt.zoogie.introtoandroid;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * Splash screen for preview builds.
 * @author Michael Imamura
 */
public class SplashActivity extends Activity {
    
    private static final String TAG = SplashActivity.class.getSimpleName();
    
    private EditText inviteCodeTxt;
    
    // LIFECYCLE ///////////////////////////////////////////////////////////////
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        inviteCodeTxt = (EditText)findViewById(R.id.inviteCode);
        inviteCodeTxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    onInviteCodeButtonClicked();
                    return true;
                }
                return false;
            }
        });
        
        Button inviteCodeBtn = (Button)findViewById(R.id.inviteCodeBtn);
        inviteCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInviteCodeButtonClicked();
            }
        });
    }
    
    // EVENT HANDLING //////////////////////////////////////////////////////////
    
    protected void onInviteCodeButtonClicked() {
        String inviteCode = inviteCodeTxt.getText().toString();
        String inviteHash = generateHash(inviteCode);
        
        Log.i(TAG, "Invite code hash was: " + inviteHash);
        
        if ("744ccdc86efe40e58b6bb54f76d8e35b97c7d9e3".equals(inviteHash)) {
            launchPresentation();
        } else if ("11f9578d05e6f7bb58a3cdd00107e9f4e3882671".equals(inviteHash)) {
            throw new SplashException();
        } else {
            inviteCodeTxt.setError("Invalid code");
            inviteCodeTxt.getText().clear();
            inviteCodeTxt.requestFocus();
        }
    }
    
    // ACTIONS /////////////////////////////////////////////////////////////////
    
    private void launchPresentation() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
    
    // UTILITIES ///////////////////////////////////////////////////////////////
    
    private static String generateHash(String s) {
        MessageDigest digester = null;
        try {
            digester = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        
        byte[] digest = digester.digest(s.getBytes());
        StringBuilder sb = new StringBuilder(digest.length * 2);
        for (byte b : digest) {
            String hexByte = Integer.toHexString(0xff & b);
            if (hexByte.length() == 1) sb.append('0');
            sb.append(hexByte);
        }
        return sb.toString();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // SplashException
    
    private static class SplashException extends RuntimeException {
        
        public SplashException() {
            super("A test error occurred in the splash screen.");
        }
        
    }
    
}

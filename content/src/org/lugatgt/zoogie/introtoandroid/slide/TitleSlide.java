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

package org.lugatgt.zoogie.introtoandroid.slide;

import org.lugatgt.zoogie.introtoandroid.AboutActivity;
import org.lugatgt.zoogie.introtoandroid.R;
import org.lugatgt.zoogie.present.ui.SlideFragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class TitleSlide extends SlideFragment {
    
    private static final String TAG = TitleSlide.class.getSimpleName();

    // CONTENT /////////////////////////////////////////////////////////////////
    
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slide_title, null);
        
        // Load the version string from our own package info.
        Context ctx = getActivity();
        String versionName = "Unknown";
        try {
            versionName = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
        } catch (NameNotFoundException ex) {
            Log.w(TAG, "Failed to retrieve our own version name", ex);
        }
        
        TextView versionLbl = (TextView)view.findViewById(R.id.versionLbl);
        versionLbl.setText("Version " + versionName);
        
        view.findViewById(R.id.aboutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAboutButtonClick();
            }
        });
        
        return view;
    }
    
    protected void onAboutButtonClick() {
        startActivity(new Intent(getActivity(), AboutActivity.class));
    }
    
}

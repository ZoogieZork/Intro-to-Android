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

import org.lugatgt.zoogie.introtoandroid.R;
import org.lugatgt.zoogie.present.ui.SlideFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Displays a URL and a large QR code for the URL.
 * <p>
 * The URL and the QR code image are clickable to launch the browser.
 * 
 * @author Michael Imamura
 */
public class ForkThisSlide extends SlideFragment {

    private Uri launchUri;
    
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slide_fork_this, null);
        
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchBrowser();
            }
        };
        
        TextView forkThisUrlTxt = (TextView)view.findViewById(R.id.forkThisUrl);
        forkThisUrlTxt.setOnClickListener(clickListener);
        
        view.findViewById(R.id.forkThisQrCode).setOnClickListener(clickListener);
        
        // The URL that will be launched is whatever is in the text.
        launchUri = Uri.parse(forkThisUrlTxt.getText().toString());
        
        return view;
    }
    
    protected void launchBrowser() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(launchUri);
        startActivity(intent);
    }

}

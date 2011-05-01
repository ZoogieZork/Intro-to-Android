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

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.lugatgt.zoogie.present.R;


/**
 * The "About" activity.
 * <p>
 * This is the content-specific realization of the abstract
 * {@link org.lugatgt.zoogie.present.ui.AboutActivity}.  Here we add our own
 * additional content.
 * 
 * @author Michael Imamura
 */
public class AboutActivity extends org.lugatgt.zoogie.present.ui.AboutActivity {

    // LIFECYCLE ///////////////////////////////////////////////////////////////
    
    @Override
    public void onBuildHeaders(List<Header> target) {
        // Add our own content-specific pages first, followed by the standard set.
        loadHeadersFromResource(R.xml.about_content_headers, target);
        super.onBuildHeaders(target);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // AboutFragment
    
    public static class AboutFragment extends Fragment {
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.about_content, null);
            
            TextView contentTxt = (TextView)view.findViewById(R.id.contentTxt);
            contentTxt.setMovementMethod(LinkMovementMethod.getInstance());
            
            return view;
        }
        
    }
    
}

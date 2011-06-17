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

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lugatgt.zoogie.introtoandroid.AboutActivity;
import org.lugatgt.zoogie.introtoandroid.R;
import org.lugatgt.zoogie.present.slide.TitleSlide;


/**
 * Customized title slide that adds an "About" button.
 * @author Michael Imamura
 */
public class MainTitleSlide extends TitleSlide {
    
    // CONTENT /////////////////////////////////////////////////////////////////
    
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setAuthor(getText(R.string.byline_presenter));
        setEvent(getText(R.string.byline_event));
        setDate(getText(R.string.byline_date));
        
        return super.onCreateContentView(inflater, container, savedInstanceState);
    }
    
    @Override
    protected View onCreateTitleSlideContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slide_title, null);
        
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

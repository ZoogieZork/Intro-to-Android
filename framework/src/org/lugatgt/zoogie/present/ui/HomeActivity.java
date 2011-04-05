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

package org.lugatgt.zoogie.present.ui;

import org.lugatgt.zoogie.present.Presentation;
import org.lugatgt.zoogie.present.R;
import org.lugatgt.zoogie.present.Slide;
import org.lugatgt.zoogie.present.SlideFragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;


/**
 * Main presentation activity.
 * @author Michael Imamura
 */
public abstract class HomeActivity extends Activity {
    
    private static final String TAG = HomeActivity.class.getSimpleName();
    
    private static final String CONTENT_FRAG_TAG = "contentSlide";
    
    private Presentation presentation;
    
    private Button prevBtn;
    private Button nextBtn;
    
    // CONSTRUCTORS ////////////////////////////////////////////////////////////
    
    public HomeActivity(Presentation presentation) {
        this.presentation = presentation;
    }
    
    // LIFECYCLE ///////////////////////////////////////////////////////////////
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.main);
        
        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(false);
        bar.setDisplayUseLogoEnabled(false);
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        
        bar.setListNavigationCallbacks(
            new SlideListSpinnerAdapter(presentation, this),
            new ActionBar.OnNavigationListener() {
                @Override
                public boolean onNavigationItemSelected(int pos, long id) {
                    jumpTo(pos);
                    return true;
                }
            });
        
        prevBtn = (Button)findViewById(R.id.prevBtn);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navPrevSlide();
            }
        });
        
        nextBtn = (Button)findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navNextSlide();
            }
        });
        
        if (savedInstanceState == null) {
            // Starting from scratch.
            jumpTo(0);
        } else {
            // The title and slide fragments will restore their own state;
            // we only need to restore the presentation internal state.
            presentation.onRestoreInstanceState(savedInstanceState);
            bar.setSelectedNavigationItem(presentation.getCurrentSlideIndex());
        }
        
        View v = findViewById(R.id.slideContainer);
        v.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        presentation.onSaveInstanceState(outState);
    }
    
    // NAVIGATION //////////////////////////////////////////////////////////////
    
    /**
     * Navigate to the previous slide (with transition).
     */
    protected void navPrevSlide() {
        if (presentation.isAtBeginning()) return;
        
        //TODO: Transition.
        jumpTo(presentation.getCurrentSlideIndex() - 1);
    }
    
    /**
     * Navigate to the next slide (with transition).
     */
    protected void navNextSlide() {
        if (presentation.isAtEnd()) return;

        //TODO: Transition.
        jumpTo(presentation.getCurrentSlideIndex() + 1);
    }
    
    /**
     * Switch directly to a slide by index.
     * <p>
     * No transition animation will be played, even if the slide is the next or
     * previous to the current slide.
     * @param idx The slide index.
     */
    protected void jumpTo(int idx) {
        FragmentManager fragMgr = getFragmentManager();
        
        getActionBar().setSelectedNavigationItem(idx);
        
        TitleFragment titleFrag = (TitleFragment)fragMgr.findFragmentById(R.id.titleFragment);
        Slide slide = presentation.jumpTo(idx);
        titleFrag.setSlide(slide);
        
        // Create the slide fragment.
        Class<? extends SlideFragment> slideFragClass = slide.getFragmentClass();
        SlideFragment slideFrag = null;
        try {
            slideFrag = slideFragClass.newInstance();
        } catch (Exception ex) {
            Log.e(TAG, "Unable to instantiate slide fragment: " + slideFragClass, ex);
            //TODO: Use a standard placeholder slide fragment.
            throw new RuntimeException(ex);
        }
        
        // Add or replace the slide fragment, depending if
        // there's a content fragment or not.
        FragmentTransaction ft = fragMgr.beginTransaction();
        if (fragMgr.findFragmentByTag(CONTENT_FRAG_TAG) != null) {
            ft.replace(R.id.slideContainer, slideFrag, CONTENT_FRAG_TAG);
        } else {
            ft.add(R.id.slideContainer, slideFrag, CONTENT_FRAG_TAG);
        }
        ft.commit();
        
        updateToolbarState();
    }
    
    // VIEW STATE //////////////////////////////////////////////////////////////
    
    /**
     * Update the state of the toolbar buttons.
     */
    protected void updateToolbarState() {
        int curSlideIndex = presentation.getCurrentSlideIndex();
        prevBtn.setEnabled(curSlideIndex > 0);
        nextBtn.setEnabled(curSlideIndex < presentation.getSlideCount() - 1);
    }
    
}

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

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import org.lugatgt.zoogie.present.Presentation;
import org.lugatgt.zoogie.present.R;
import org.lugatgt.zoogie.present.Slide;
import org.lugatgt.zoogie.present.SlideTransition;


/**
 * Main presentation activity.
 * @author Michael Imamura
 */
public abstract class PresentationActivity extends Activity implements Presentation.OnIndexChangedListener {
    
    private static final String TAG = PresentationActivity.class.getSimpleName();
    
    private static final String CONTENT_FRAG_TAG = "contentSlide";
    
    private Presentation presentation;
    
    private ImageButton prevBtn;
    private ImageButton nextBtn;
    
    // CONSTRUCTORS ////////////////////////////////////////////////////////////
    
    public PresentationActivity(Presentation presentation) {
        this.presentation = presentation;
        presentation.setOnIndexChangedListener(this);
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
        
        /*FIXME: Disabled until custom action bar is implemented.
        bar.setListNavigationCallbacks(
            new SlideListSpinnerAdapter(presentation, this),
            new ActionBar.OnNavigationListener() {
                @Override
                public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                    jumpTo(itemPosition);
                    return true;
                }
            });
        */
        
        prevBtn = (ImageButton)findViewById(R.id.prevBtn);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentation.prev();
            }
        });
        
        nextBtn = (ImageButton)findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentation.next();
            }
        });
        
        if (savedInstanceState == null) {
            // Starting from scratch.
            onAfterIndexChanged(null, 0, true);
        } else {
            // The title and slide fragments will restore their own state;
            // we only need to restore the presentation internal state.
            presentation.onRestoreInstanceState(savedInstanceState);
            bar.setSelectedNavigationItem(presentation.getCurrentSlideIndex());
            updateToolbarState(presentation.getCurrentSlide(), presentation.getCurrentSlideIndex());
        }
        
        View v = findViewById(R.id.slideContainer);
        v.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
        
        //FIXME: Hide the ActionBar for now, until we add custom controls.
        bar.hide();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        presentation.onSaveInstanceState(outState);
    }
    
    // EVENTS //////////////////////////////////////////////////////////////////
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        
        // These define the global navigation keys.
        // Since this is only called if a view on a slide doesn't handle the
        // keypress first, slides should avoid defining keyboard shortcuts
        // that interfere with the following keys.
        // (Of course, for some views it's unavoidable, e.g. EditText). 
        
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_SPACE:
                presentation.next();
                return true;
                
            case KeyEvent.KEYCODE_DPAD_LEFT:
                presentation.prev();
                return true;
        }
        
        return super.onKeyDown(keyCode, event);
    }
    
    // CONTENT FRAGMENT ////////////////////////////////////////////////////////
    
    /**
     * Prepare a transaction for transitioning to a new slide.
     * @param prevSlide The previous slide (may be null if no previous slide).
     * @param slide The new slide (may not be null).
     * @param animated true if the transition should be animated.
     * @return The initialized transaction (never null).
     *         It is up to the caller to amend and commit it.
     */
    protected FragmentTransaction createFragmentTransaction(Slide prevSlide, Slide slide, boolean animated) {
        FragmentManager fragMgr = getFragmentManager();
        
        // Create the slide fragment.
        SlideFragment slideFrag;
        try {
            slideFrag = slide.createFragment();
            if (slideFrag == null) {
                throw new RuntimeException("Slide fragment is null: " + slide);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Unable to instantiate slide fragment: " + slide, ex);
            //TODO: Use a standard placeholder slide fragment.
            throw new RuntimeException(ex);
        }
        
        // Add or replace the slide fragment, depending if
        // there's a content fragment or not.
        FragmentTransaction ft = fragMgr.beginTransaction();
        
        if (animated) {
            // The animations must be specified before the action (replace/add)
            // is specified, otherwise no animation will play.
            if (prevSlide != null) {
                SlideTransition transition = prevSlide.getTransition();
                if (transition != null) {
                    ft.setCustomAnimations(
                        transition.getInAnimationRes(),
                        transition.getOutAnimationRes());
                }
            }
        }
        
        // If there is an existing visible slide fragment, then detach it (mark
        // it as no longer the active slide) and replace it and mark the new
        // fragment as active.  Otherwise, just add the new fragment and mark it
        // as active.
        SlideFragment oldFragment = (SlideFragment)fragMgr.findFragmentByTag(CONTENT_FRAG_TAG);
        if (oldFragment != null) {
            oldFragment.onDetachFromPresentation();
            ft.replace(R.id.slideContainer, slideFrag, CONTENT_FRAG_TAG);
        } else {
            ft.add(R.id.slideContainer, slideFrag, CONTENT_FRAG_TAG);
        }
        slideFrag.onAttachToPresentation(presentation);
        
        return ft;
    }
    
    // VIEW STATE //////////////////////////////////////////////////////////////
    
    /**
     * Convenience function to update all UI elements on slide transition.
     * @param slide The current slide (may not be null).
     * @param idx The index of the current slide.
     * @param animate true to animate the transition, if possible.
     */
    private void updateUi(Slide slide, int idx, boolean animate) {
        updateTitle(slide, idx, animate);
        updateNavigation(slide, idx);
        updateToolbarState(slide, idx);
    }
    
    /**
     * Update the navigation controls in the action bar.
     * @param slide The current slide (may not be null).
     * @param idx The index of the current slide.
     */
    protected void updateNavigation(Slide slide, int idx) {
        /*FIXME: Disabled since it triggers onNavigationItemSelected().
        getActionBar().setSelectedNavigationItem(idx);
        */
    }
    
    /**
     * Update the title fragment.
     * @param slide The current slide (may not be null).
     * @param idx The index of the current slide.
     * @param animate true to animate the transition, if possible.
     */
    protected void updateTitle(Slide slide, int idx, boolean animate) {
        FragmentManager fragMgr = getFragmentManager();
        TitleFragment titleFrag = (TitleFragment)fragMgr.findFragmentById(R.id.titleFragment);
        titleFrag.setSlide(slide, idx, animate);
    }
    
    /**
     * Update the state of the toolbar buttons.
     * @param slide The current slide (may not be null).
     * @param idx The index of the current slide.
     */
    protected void updateToolbarState(Slide slide, int idx) {
        int curSlideIndex = presentation.getCurrentSlideIndex();
        prevBtn.setEnabled(curSlideIndex > 0);
        nextBtn.setEnabled(curSlideIndex < presentation.getSlideCount() - 1);
    }
    
    // Presentation.OnIndexChangedListener /////////////////////////////////////
    
    public void onAfterIndexChanged(Slide oldSlide, int oldIndex, boolean immediate) {
        Slide slide = presentation.getCurrentSlide();
        createFragmentTransaction(oldSlide, slide, !immediate).commit();
        updateUi(slide, presentation.getCurrentSlideIndex(), !immediate);
    }
    
}

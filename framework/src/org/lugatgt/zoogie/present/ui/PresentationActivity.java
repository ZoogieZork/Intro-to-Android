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

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.lugatgt.zoogie.present.Presentation;
import org.lugatgt.zoogie.present.PresentationInflater;
import org.lugatgt.zoogie.present.R;
import org.lugatgt.zoogie.present.Slide;
import org.lugatgt.zoogie.present.SlideTransition;


/**
 * Main presentation activity.
 * <p>
 * The activity can be in one of two states, based on the {@code tocVisible}
 * flag:
 * <ol>
 * <li>Normal ("presentation") mode: This is the initial mode.</li>
 * <li>Table of Contents ("TOC") mode: The slide view is minimized to a
 *     preview in the corner, allowing the user to jump quickly to any
 *     slide in the presentation.</li>
 * </ol>
 * 
 * @author Michael Imamura
 */
public abstract class PresentationActivity extends Activity implements Presentation.OnIndexChangedListener {
    
    private static final String TAG = PresentationActivity.class.getSimpleName();
    
    /** Time (in ms) until the action bar automatically fades out. */
    private static final int ACTIONBAR_AUTOHIDE_TIMEOUT = 3000;
    
    private static final String CONTENT_FRAG_TAG = "contentSlide";
    
    private static final String TOC_VISIBLE_KEY = "tocVisible";
    
    private Presentation presentation;
    private boolean tocVisible = false;
    
    /** Cache of the slide titles (we assume that they won't change). */
    private CharSequence[] slideTitles;
    
    private View rootView;
    
    private TextView actionbarSlideTitleLbl;
    
    private ViewGroup slideFrame;
    private ViewGroup mainToolbarFrame;
    private ViewGroup tocFrame;
    
    private ImageButton prevBtn;
    private ImageButton nextBtn;
    
    private ListView tocList;
    
    private Handler handler;
    private Runnable fadeoutRunnable;
    
    // CONSTRUCTORS ////////////////////////////////////////////////////////////
    
    public PresentationActivity() {
    }
    
    // FIELD ACCESS ////////////////////////////////////////////////////////////
    
    protected Presentation getPresentation() {
        return presentation;
    }
    
    // LIFECYCLE ///////////////////////////////////////////////////////////////
    
    /**
     * Called when the {@link Presentation} should be created.
     * <p>
     * The default behavior is to load from an XML resource (presentation.xml).
     * Subclasses can override this behavior to create their own presentations.
     * 
     * @param inflater A presentation inflater for loading from XML resources
     *                 (never null).
     * @param savedInstanceState Activity saved state (may be null), same as
     *                           passed to {@link #onCreate(Bundle)}.
     * @return The created {@link Presentation} (never null).
     */
    protected Presentation onCreatePresentation(PresentationInflater inflater, Bundle savedInstanceState) {
        //TODO: Use real resource.
        return inflater.inflate(0);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Check if we are in a debug build.
        boolean debugPackage = false;
        try {
            debugPackage = (getPackageManager().getPackageInfo(getPackageName(), 0).applicationInfo.flags &
                ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (NameNotFoundException ex) {
            // Shouldn't happen (in theory).
            Log.w(TAG, "Failed to retrieve our own debug status", ex);
        }
        
        if (debugPackage) {
            Log.i(TAG, "Initializing strict mode for debug build");
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().penaltyDeath().build());
        }
        
        // Create the presentation and register to listen for events.
        presentation = onCreatePresentation(new PresentationInflater(this), savedInstanceState);
        if (presentation == null) {
            throw new IllegalArgumentException("onCreatePresentation() must return a Presentation instance.");
        }
        presentation.setOnIndexChangedListener(this);
        
        slideTitles = presentation.getSlideTitles(this);
        // Since the first slide's title is the name of the presentation,
        // we replace it for listing purposes with just "Title".
        slideTitles[0] = getString(R.string.title_slide_title);
        
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.toc);
        
        rootView = findViewById(R.id.presenterRoot);
        
        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(false);
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowCustomEnabled(true);
        bar.setCustomView(R.layout.actionbar);
        bar.addOnMenuVisibilityListener(new ActionBar.OnMenuVisibilityListener() {
            @Override
            public void onMenuVisibilityChanged(boolean isVisible) {
                // Keep the action bar visible while the menu is shown.
                resetActionBarTimeout(isVisible);
            }
        });
        
        View actionbarView = bar.getCustomView();
        actionbarSlideTitleLbl = (TextView)actionbarView.findViewById(R.id.actionbar_slideTitle);
        
        slideFrame = (ViewGroup)findViewById(R.id.presentationFrame);
        mainToolbarFrame = (ViewGroup)findViewById(R.id.mainToolbarFrame);
        tocFrame = (ViewGroup)findViewById(R.id.tocFrame);
        
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
        
        // Tapping on the slide preview in TOC mode switches back to
        // presentation mode.
        findViewById(R.id.slidePreview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTableOfContents();
            }
        });
        
        // Tapping on the title frag makes the action bar visible and resets
        // the fade-out timer.
        getFragmentManager().findFragmentById(R.id.titleFragment).getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetActionBarTimeout(false);
            }
        });
        
        if (savedInstanceState == null) {
            // Starting from scratch.
            onAfterIndexChanged(null, 0, true);
        } else {
            // The title and slide fragments will restore their own state;
            // we only need to restore the presentation internal state.
            presentation.onRestoreInstanceState(savedInstanceState);
            
            Slide curSlide = presentation.getCurrentSlide();
            int curSlideIdx = presentation.getCurrentSlideIndex();
            updateNavigation(curSlide, curSlideIdx);
            updateToolbarState(curSlide, curSlideIdx);
            
            tocVisible = savedInstanceState.getBoolean(TOC_VISIBLE_KEY);
            setTocViewState(tocVisible ? 0.0f : 1.0f);
            if (tocVisible) {
                initToc();
            }
        }
        
        // Set up the callback for auto-hiding the action bar after a delay.
        handler = new Handler();
        fadeoutRunnable = new Runnable() {
            @Override
            public void run() {
                rootView.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
                getActionBar().hide();
            }
        };
        if (!tocVisible) {
            resetActionBarTimeout(false);
        }
    }
    
    @Override
    public void onDestroy() {
        if (presentation != null) {
            presentation.setOnIndexChangedListener(null);
            presentation = null;
        }
        
        super.onDestroy();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.presentation, menu);
        return true;
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        presentation.onSaveInstanceState(outState);
        outState.putBoolean(TOC_VISIBLE_KEY, tocVisible);
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
    
    @Override
    public void onBackPressed() {
        // Going back from TOC mode is normal mode.
        if (tocVisible) {
            toggleTableOfContents();
        } else {
            super.onBackPressed();
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_title:
                presentation.jumpTo(0);
                if (tocVisible) {
                    toggleTableOfContents();
                }
                return true;
                
            case R.id.menu_toc:
                toggleTableOfContents();
                return true;
                
            default:
                return super.onOptionsItemSelected(item);
        }
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
    
    // TABLE OF CONTENTS ///////////////////////////////////////////////////////
    
    /**
     * Toggle the visible state of the table of contents.
     */
    protected void toggleTableOfContents() {
        ObjectAnimator anim = null;
        if (tocVisible) {
            // Leaving TOC mode.
            anim = ObjectAnimator.ofFloat(this, "tocViewState", 0.0f, 1.0f);
            //TODO: Destroy the TOC views when animation is finished.
        } else {
            // Entering TOC mode.
            anim = ObjectAnimator.ofFloat(this, "tocViewState", 1.0f, 0.0f);
            initToc();
        }
        anim.setDuration(getResources().getInteger(R.integer.tocTransitionDuration));
        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();
        
        tocVisible = !tocVisible;
        
        // Keep the action bar visible while TOC mode is active.
        resetActionBarTimeout(tocVisible);
        
        updateNavigation(presentation.getCurrentSlide(), presentation.getCurrentSlideIndex());
    }
    
    /**
     * Set how far expanded the slide fragment container is.
     * @param amount 1.0f for fully-expanded (table of contents not visible) to
     *               0.0f for fully-shrunk (table of contents visible).
     */
    public void setTocViewState(float amount) {
        slideFrame.setX((1.0f - amount) * getResources().getDimension(R.dimen.toc_slide_shrink_translate_x));
        slideFrame.setY((1.0f - amount) * getResources().getDimension(R.dimen.toc_slide_shrink_translate_y));
        slideFrame.setScaleX((1.0f/3.0f) + (amount * (2.0f / 3.0f)));
        slideFrame.setScaleY((1.0f/3.0f) + (amount * (2.0f / 3.0f)));
        
        // Hide the prev/next toolbar since it doesn't work in TOC mode.
        mainToolbarFrame.setAlpha(amount);
        
        tocFrame.setVisibility((amount < 0.99f) ? View.VISIBLE : View.GONE);
        tocFrame.setScaleX((2.0f * amount) + 1.0f);
        tocFrame.setScaleY((2.0f * amount) + 1.0f);
        tocFrame.setAlpha(1.0f - amount);
    }
    
    /**
     * Set up the table of contents views.
     */
    protected void initToc() {
        // Lazily initialize the table of contents list.
        if (tocList == null) {
            tocList = (ListView)findViewById(R.id.tocList);
            tocList.setAdapter(new ArrayAdapter<CharSequence>(this, R.layout.toc_list_item, slideTitles));
            tocList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    getPresentation().jumpTo(position);
                }
            });
        }
        
        int pos = presentation.getCurrentSlideIndex();
        tocList.setSelection(pos);
        tocList.setItemChecked(pos, true);
    }
    
    // ACTION BAR //////////////////////////////////////////////////////////////
    
    /**
     * Show the action bar, optionally auto-hiding it after a delay.
     * @param keepVisible true to keep the action bar visible,
     *                    false to auto-hide it after a fixed interval.
     */
    protected void resetActionBarTimeout(boolean keepVisible) {
        rootView.setSystemUiVisibility(View.STATUS_BAR_VISIBLE);
        getActionBar().show();
        
        // Remove any pending auto-hide event, and if auto-hide is requested,
        // then set it again with the full timeout time.
        handler.removeCallbacks(fadeoutRunnable);
        if (!keepVisible) {
            handler.postDelayed(fadeoutRunnable, ACTIONBAR_AUTOHIDE_TIMEOUT);
        }
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
        if (tocVisible) {
            actionbarSlideTitleLbl.setText(R.string.menu_toc);
        } else {
            actionbarSlideTitleLbl.setText(slideTitles[idx]);
        }
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

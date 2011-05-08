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
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.lugatgt.zoogie.present.R;
import org.lugatgt.zoogie.present.Slide;


/**
 * Displays the title.
 * This fragment may expand, depending on the current slide.
 * @author Michael Imamura
 */
public class TitleFragment extends Fragment {

    private static final String INDEX_KEY = "index";
    private static final String TITLE_KEY = "title";
    private static final String SUBTITLE_KEY = "subtitle";
    
    private ForegroundColorSpan subtitleColorSpan;
    private int normalHeight;
    private int expandedHeight;
    private int slideTransitionDuration;
    
    private RelativeLayout contentView;
    private TextView titleLbl;
    private TextView titleAnimLbl;
    private View titleFrame;
    private View titleAnimFrame;
    
    private int index;
    private CharSequence title = "";
    private CharSequence subtitle = "";
    
    // LIFECYCLE ///////////////////////////////////////////////////////////////
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = (RelativeLayout)inflater.inflate(R.layout.title, null);
        
        // Load resources -- these may change depending on orientation, etc.,
        // so we need to load them on every onCreateView().
        Resources res = getResources();
        subtitleColorSpan = new ForegroundColorSpan(res.getColor(R.color.subtitle_fg));
        normalHeight = res.getDimensionPixelSize(R.dimen.title_frag_height);
        expandedHeight = res.getDimensionPixelSize(R.dimen.title_frag_expanded_height);
        slideTransitionDuration = res.getInteger(R.integer.slideTransitionDuration);
        
        index = 0;
        
        titleLbl = (TextView)contentView.findViewById(R.id.titleLbl);
        titleAnimLbl = (TextView)contentView.findViewById(R.id.titleAnimLbl);
        titleFrame = contentView.findViewById(R.id.titleFrame);
        titleAnimFrame = contentView.findViewById(R.id.titleAnimFrame);
        
        if (savedInstanceState != null) {
            index = savedInstanceState.getInt(INDEX_KEY);
            title = savedInstanceState.getCharSequence(TITLE_KEY);
            subtitle = savedInstanceState.getCharSequence(SUBTITLE_KEY);
            // Since updateUi() changes the layout, we can't call it here
            // because the view hasn't been attached to the activity's
            // layout yet.
        }
        
        return contentView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        if (savedInstanceState != null) {
            // We call updateUi() here since this is after our layout has been
            // attached to the activity's layout.
            updateUi(0, false);
        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        outState.putInt(INDEX_KEY, index);
        outState.putCharSequence(TITLE_KEY, title);
        outState.putCharSequence(SUBTITLE_KEY, subtitle);
    }
    
    // CONTENT /////////////////////////////////////////////////////////////////
    
    /**
     * Update the title to reflect a new slide.
     * @param slide The new slide (may not be null).
     * @param idx The zero-based index of the slide in the presentation.
     * @param animate true to animate between the previous title and the new title.
     */
    public void setSlide(Slide slide, int idx, boolean animate) {
        CharSequence newTitle = slide.getTitle(getActivity());
        if (newTitle == null) newTitle = "";
        
        CharSequence newSubtitle = slide.getSubtitle(getActivity());
        if (newSubtitle == null) newSubtitle = "";
        
        // Do nothing if the title and subtitle are unchanged.
        if (newTitle.equals(title) && newSubtitle.equals(subtitle)) return;
        
        int oldIndex = index;
        index = idx;
        title = newTitle;
        subtitle = newSubtitle;
        
        updateUi(oldIndex, animate);
    }
    
    /**
     * Update the UI elements to reflect the current state.
     * @param oldIndex The zero-based previous slide index.
     * @param animate true if moving to the new state should be animated.
     */
    private void updateUi(int oldIndex, boolean animate) {
        // Update our height, animating it if requested.
        if (oldIndex > 0 && index > 0) {
            // Do nothing, height is already correct.
        } else if (oldIndex == 0 && index > 0 && animate) {
            ObjectAnimator.ofInt(this, "contentHeight", expandedHeight, normalHeight).
                setDuration(slideTransitionDuration).start();
        } else {
            contentView.getLayoutParams().height =
                index == 0 ? expandedHeight : normalHeight;
        }
        
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(title);
        if (subtitle.length() > 0) {
            // Use a different foreground color for the subtitle.
            sb.append(": ").append(subtitle);
            sb.setSpan(subtitleColorSpan, title.length() + 2, sb.length(), 0);
        }
        
        if (animate) {
            titleAnimLbl.setText(titleLbl.getText());
            titleLbl.setText(sb);
            // We animate the frames instead of the TextViews because setting
            // the alpha on a TextView with a ForegroundColorSpan doesn't
            // quite work right (the span overrides the alpha).
            ObjectAnimator.ofFloat(titleFrame, "alpha", 0.0f, 1.0f).
                setDuration(slideTransitionDuration).start();
            ObjectAnimator.ofFloat(titleAnimFrame, "alpha", 1.0f, 0.0f).
                setDuration(slideTransitionDuration).start();
        } else {
            titleLbl.setText(sb);
        }
    }
    
    /**
     * <em>Do not call this method directly!</em>
     * <p>
     * This is called by the {@link ObjectAnimator} to update the layout height
     * since {@link LayoutParams} doesn't have a public {@code setHeight()}
     * method.
     * @param height The new height in pixels.
     */
    public void setContentHeight(int height) {
        contentView.getLayoutParams().height = height;
        contentView.requestLayout();
    }
    
}

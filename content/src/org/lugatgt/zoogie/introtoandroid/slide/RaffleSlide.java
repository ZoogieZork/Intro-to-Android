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

import java.util.Random;

import org.lugatgt.zoogie.introtoandroid.R;
import org.lugatgt.zoogie.present.ui.SlideFragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Picks a random number for the raffle drawing.
 * @author Michael Imamura
 */
public class RaffleSlide extends SlideFragment {
    
    private static final String TAG = RaffleSlide.class.getSimpleName();

    private Random random;
    private EditText raffleTxt;
    private TextView raffleOutputTxt;
    private Button goBtn;
    
    private int maxNum;
    
    private int lastStep;
    
    // LIFECYCLE ///////////////////////////////////////////////////////////////
    
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slide_raffle, null);
        
        raffleTxt = (EditText)view.findViewById(R.id.raffle_num);
        raffleOutputTxt = (TextView)view.findViewById(R.id.raffle_output);
        
        goBtn = (Button)view.findViewById(R.id.raffle_btn);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRaffleButtonClick();
            }
        });
        
        random = new Random();
        
        return view;
    }
    
    // EVENTS //////////////////////////////////////////////////////////////////
    
    protected void onRaffleButtonClick() {
        maxNum = Integer.parseInt(raffleTxt.getText().toString());
        
        ObjectAnimator animator = ObjectAnimator.ofInt(this, "newNum", 0, 100);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(5000);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
            
            @Override
            public void onAnimationEnd(Animator animation) {
                onRaffleAnimationEnd();
            }
            
            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });
        
        raffleOutputTxt.setAlpha(0.5f);
        raffleTxt.setEnabled(false);
        goBtn.setEnabled(false);
        animator.start();
        
        Log.i(TAG, "Started!");
    }
    
    protected void onRaffleAnimationEnd() {
        raffleOutputTxt.setAlpha(1.0f);
        raffleTxt.setEnabled(true);
        goBtn.setEnabled(true);
    }
    
    // ANIMATION CALLBACK //////////////////////////////////////////////////////
    
    public void setNewNum(int i) {
        if (lastStep != i) {
            lastStep = i;
            raffleOutputTxt.setText(Integer.toString(random.nextInt(maxNum) + 1));
        }
    }

}

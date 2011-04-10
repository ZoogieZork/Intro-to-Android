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

import android.content.Context;

import org.lugatgt.zoogie.introtoandroid.slide.*;
import org.lugatgt.zoogie.present.Presentation;
import org.lugatgt.zoogie.present.Slide;
import org.lugatgt.zoogie.present.SlideTransition;
import org.lugatgt.zoogie.present.Transitions;
import org.lugatgt.zoogie.present.ui.SlideFragment;


public class MainPresentation extends Presentation {
    
    private enum Slides implements Slide {
        
        TITLE(TitleSlide.class, "Intro to Android Development", Transitions.SLIDE_LEFT),
        //INTRO(TitleSlide.class, "Introduction"),
        PARTS(PartsSlide.class, "Anatomy of an Android App", Transitions.SLIDE_LEFT),
        API_HISTORY(ApiHistorySlide.class, "API History", Transitions.FADE),
        FORK_THIS(ForkThisSlide.class, "Fork This Presentation!", Transitions.SLIDE_LEFT);
        
        private Class<? extends SlideFragment> fragmentClass;
        private String title;
        private SlideTransition transition;
        
        private Slides(Class<?> fragmentClass, String title, SlideTransition transition) {
            this.fragmentClass = fragmentClass.asSubclass(SlideFragment.class);
            this.title = title;
            this.transition = transition;
        }
        
        @Override
        public String getName() {
            return toString();
        }
        
        @Override
        public String getTitle(Context ctx) {
            return title;
        }
        
        @Override
        public SlideFragment createFragment() throws InstantiationException, IllegalAccessException {
            return fragmentClass.newInstance();
        }
        
        @Override
        public SlideTransition getTransition() {
            return transition;
        }
        
    }
    
    // CONSTRUCTORS ////////////////////////////////////////////////////////////
    
    public MainPresentation() {
        super(Slides.values());
    }
    
}

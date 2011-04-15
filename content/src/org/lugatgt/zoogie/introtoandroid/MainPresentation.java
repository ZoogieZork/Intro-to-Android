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

import java.lang.reflect.InvocationTargetException;

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
        STARTING(BasicTextSlide.class, "Getting Started", Transitions.SLIDE_LEFT, R.array.starting_content),
        DALVIK(BasicTextSlide.class, "Dalvik VM", Transitions.SLIDE_UP, R.array.dalvik_content),
        DALVIK_JAVA(BasicTextSlide.class, "Dalvik VM", Transitions.SLIDE_LEFT, R.array.dalvik_java_content),
        PARTS(PartsSlide.class, "Anatomy of an Android App", Transitions.SLIDE_LEFT),
        LIFECYCLE(BasicTextSlide.class, "What happens to my app when it goes away?", Transitions.SLIDE_LEFT, R.array.lifecycle_content),
        API_HISTORY(ApiHistorySlide.class, "API History", Transitions.FADE),
        FORK_THIS(ForkThisSlide.class, "Fork This Presentation!", Transitions.SLIDE_LEFT);
        
        private Class<? extends SlideFragment> fragmentClass;
        private String title;
        private SlideTransition transition;
        private int[] contentResources;
        
        private Slides(Class<?> fragmentClass, String title, SlideTransition transition, int... contentResources) {
            this.fragmentClass = fragmentClass.asSubclass(SlideFragment.class);
            this.title = title;
            this.contentResources = contentResources;
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
        public SlideFragment createFragment() throws InstantiationException, IllegalAccessException, InvocationTargetException {
            if (contentResources == null || contentResources.length == 0) {
                return fragmentClass.newInstance();
            } else {
                // Use reflection to find a constructor that takes the same
                // number of int arguments as contentResources.
                Class<?>[] paramTypes = new Class<?>[contentResources.length];
                Object[] params = new Object[contentResources.length];
                for (int i = 0; i < paramTypes.length; i++) {
                    paramTypes[i] = Integer.TYPE;
                    params[i] = contentResources[i];
                }
                try {
                    return fragmentClass.getConstructor(paramTypes).newInstance(params);
                } catch (NoSuchMethodException e) {
                    throw new InstantiationException(
                        "Class " + fragmentClass + " does not have a constructor that " +
                        "takes " + contentResources.length + " int parameter(s).");
                }
            }
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

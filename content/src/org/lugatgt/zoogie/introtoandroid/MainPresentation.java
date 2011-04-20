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
        ANDROID(AndroidSlide.class, "About Android", Transitions.ZOOM_IN),
        INTERNALS(InternalsSlide.class, "About Android", "Under the Hood", Transitions.SLIDE_LEFT),
        DALVIK(BasicTextSlide.class, "Dalvik VM", Transitions.SLIDE_UP, R.array.dalvik_content),
        DALVIK_JAVA(BasicTextSlide.class, "Dalvik VM", "Where does Java fit in?", Transitions.SLIDE_LEFT, R.array.dalvik_java_content),
        API(BasicTextSlide.class, "What's included in the API?", Transitions.SLIDE_UP, R.array.api_content),
        API_GOOGLE(BasicTextSlide.class, "Other Google APIs", Transitions.SLIDE_LEFT, R.array.api_google_content),
        PARTS(PartsSlide.class, "Anatomy of an Android App", Transitions.SLIDE_LEFT),
        PARTS_OTHER(BasicTextSlide.class, "Anatomy of an Android App", "Other Parts", Transitions.SLIDE_LEFT, R.array.parts_other_content),
        APPS(BasicTextSlide.class, "Other Kinds of Apps", Transitions.SLIDE_LEFT, R.array.apps_content),
        LIFECYCLE(BasicTextSlide.class, "What happens to my app when it goes away?", Transitions.SLIDE_LEFT, R.array.lifecycle_content),
        STARTING(BasicTextSlide.class, "Getting Started", Transitions.SLIDE_LEFT, R.array.starting_content),
        API_HISTORY(ApiHistorySlide.class, "API History", Transitions.SLIDE_LEFT),
        RES(BasicTextSlide.class, "Resources", Transitions.SLIDE_UP, R.array.resources_content),
        RES_TYPES(BasicTextSlide.class, "Resources", "Types", Transitions.SLIDE_UP, R.array.resources_types_content),
        RES_DIRS(BasicTextSlide.class, "Resources", "Directories", Transitions.SLIDE_LEFT, R.array.resources_dirs_content),
        DEBUG(BasicTextSlide.class, "Debugging", Transitions.SLIDE_UP, R.array.debug_content),
        DEBUG_LOG(BasicTextSlide.class, "Debugging", "Logging", Transitions.SLIDE_LEFT, R.array.debug_log_content),
        MARKET(BasicTextSlide.class, "Android Market", Transitions.SLIDE_UP, R.array.market_content),
        MARKET_OTHER(BasicTextSlide.class, "Android Market", "Alternate Markets", Transitions.SLIDE_UP, R.array.market_alt_content),
        MARKET_PUBLISH(BasicTextSlide.class, "Android Market", "Publishing", Transitions.SLIDE_LEFT, R.array.market_publish_content),
        MAINTENANCE(BasicTextSlide.class, "Oh, no! Something went wrong!", Transitions.SLIDE_LEFT, R.array.maintenance_content),
        LINKS(BasicTextSlide.class, "Where to go for help?", Transitions.FADE, R.array.links_content),
        FORK_THIS(ForkThisSlide.class, "Fork This Presentation!", Transitions.ZOOM_IN),
        RAFFLE(RaffleSlide.class, "Raffle Time!", Transitions.SLIDE_LEFT);
        
        private Class<? extends SlideFragment> fragmentClass;
        private String title;
        private String subtitle;
        private SlideTransition transition;
        private int[] contentResources;
        
        private Slides(Class<?> fragmentClass, String title, SlideTransition transition, int... contentResources) {
            this(fragmentClass, title, null, transition, contentResources);
        }
        
        private Slides(Class<?> fragmentClass, String title, String subtitle, SlideTransition transition, int... contentResources) {
            this.fragmentClass = fragmentClass.asSubclass(SlideFragment.class);
            this.title = title;
            this.subtitle = subtitle;
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
        public String getSubtitle(Context ctx) {
            return subtitle;
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

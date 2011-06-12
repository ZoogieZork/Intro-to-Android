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

package org.lugatgt.zoogie.present;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.InflateException;


/**
 * Loads a presentation from an XML resource.
 * @author Michael Imamura
 */
public class PresentationInflater {

    private static final String TAG = PresentationInflater.class.getSimpleName();
    
    private Context ctx;
    
    // CONSTRUCTORS ////////////////////////////////////////////////////////////
    
    public PresentationInflater(Context ctx) {
        this.ctx = ctx;
    }
    
    // INFLATION ///////////////////////////////////////////////////////////////
    
    /**
     * Load a presentation from the specified XML resource.
     * @param resId The ID of an XML resource.
     */
    public Presentation inflate(int resId) {
        XmlResourceParser parser = null;
        long startTime = System.currentTimeMillis();
        try {
            parser = ctx.getResources().getXml(resId);
            AttributeSet attrs = Xml.asAttributeSet(parser);
            return parsePresentation(parser, attrs);
            
        } catch (XmlPullParserException ex) {
            throw new InflateException("Failed to load presententation XML.", ex);
            
        } catch (IOException ex) {
            throw new InflateException("Failed to load presententation XML.", ex);
            
        } finally {
            if (parser != null) parser.close();
            
            Log.d(TAG, "Inflated presentation from resource in " +
                (System.currentTimeMillis() - startTime) + "ms");
        }
    }
    
    /**
     * Parse the presentation from the XML resource.
     * @param parser The parser initialized to the XML resource (may not be null).
     * @param attrs The attribute set associated with the parser (may not be null).
     * @return A presentation (never null).
     * @throws InflateException
     * @throws XmlPullParserException
     * @throws IOException
     */
    private Presentation parsePresentation(XmlResourceParser parser, AttributeSet attrs)
        throws XmlPullParserException, IOException
    {
        
        int eventType = parser.getEventType();
        String presentationClassName = null;
        
        // Skip to the root tag.
        do {
            if (eventType == XmlPullParser.START_TAG) {
                presentationClassName = parser.getName();
                break;
            }
            eventType = parser.next();
        } while (eventType != XmlPullParser.END_DOCUMENT);
        
        // The root tag is either:
        //   - "presentation" for the standard Presentation class.
        //   - The fully-qualified name of a class that extends Presentation.
        // In either case, the class must have a constructor that takes a single
        // AttributeSet for initialization.
        Presentation presentation = null;
        if (presentationClassName == null) {
            throw new InflateException("Presentation XML does not contain a root element.");
        }
        if ("presentation".equals(presentationClassName)) {
            presentation = new Presentation(attrs);
        } else {
            presentation = instantiatePresentation(presentationClassName, attrs);
        }
        
        //TODO: Slides.
        
        return presentation;
    }
    
    /**
     * Create a new instance of the requested presentation class.
     * @param className The name of the class (may not be null).
     * @param attrs The attribute set to pass to the constructor (may not be null).
     * @return The created presentation (never null).
     */
    private static Presentation instantiatePresentation(String className, AttributeSet attrs) {
        Class<? extends Presentation> presentationClass = null;
        try {
            presentationClass = Class.forName(className).asSubclass(Presentation.class);
        } catch (ClassNotFoundException e) {
            throw new InflateException("Presentation class does not exist: " + className);
        }
        
        try {
            return presentationClass.getConstructor(AttributeSet.class).newInstance(attrs);
            
        } catch (NoSuchMethodException ex) {
            throw new InflateException("Presentation class " + className +
                " does not have a public constructor that takes an AttributeSet.");
            
        } catch (InstantiationException ex) {
            throw new InflateException("Unable to create " + className, ex);
            
        } catch (IllegalAccessException ex) {
            throw new InflateException("Presentation class " + className +
                " does not have a public constructor that takes an AttributeSet.");
            
        } catch (InvocationTargetException ex) {
            throw new InflateException("Constructor for " + className + " threw an exception.", ex);
        }
    }
    
}

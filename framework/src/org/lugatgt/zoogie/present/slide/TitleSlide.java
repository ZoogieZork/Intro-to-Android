package org.lugatgt.zoogie.present.slide;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.lugatgt.zoogie.present.R;
import org.lugatgt.zoogie.present.ui.SlideFragment;


/**
 * The title slide of a presentation.
 * <p>
 * The title slide has built-in fields for displaying the author, date, and
 * event of the presentation.
 * 
 * @author Michael Imamura
 */
public class TitleSlide extends SlideFragment {

    private static final String TAG = TitleSlide.class.getSimpleName();
    
    private CharSequence author;
    private CharSequence event;
    private CharSequence date;
    
    // CONSTRUCTORS ////////////////////////////////////////////////////////////
    
    public TitleSlide() {
    }
    
    public TitleSlide(CharSequence author, CharSequence event, CharSequence date) {
        this.author = author;
        this.event = event;
        this.date = date;
    }
    
    // FIELD ACCESS ////////////////////////////////////////////////////////////
    
    public CharSequence getAuthor() {
        return author;
    }

    protected void setAuthor(CharSequence author) {
        this.author = author;
    }

    public CharSequence getEvent() {
        return event;
    }

    protected void setEvent(CharSequence event) {
        this.event = event;
    }

    public CharSequence getDate() {
        return date;
    }

    protected void setDate(CharSequence date) {
        this.date = date;
    }

    // CONTENT /////////////////////////////////////////////////////////////////
    
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slide_internal_title, null);
        
        // Load the version string from our own package info.
        Context ctx = getActivity();
        String versionName = "Unknown";
        try {
            versionName = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
        } catch (NameNotFoundException ex) {
            Log.w(TAG, "Failed to retrieve our own version name", ex);
        }
        
        TextView versionLbl = (TextView)view.findViewById(R.id.titleSlide_versionLbl);
        versionLbl.setText("Version " + versionName);
        
        // Create the content view.
        ViewGroup contentFrame = (ViewGroup)view.findViewById(R.id.titleSlide_contentFrame);
        View contentView = onCreateTitleSlideContentView(inflater, contentFrame, savedInstanceState);
        if (contentView != null) {
            contentFrame.addView(contentView);
        }
        
        if (author != null) {
            ((TextView)view.findViewById(R.id.titleSlide_presenter)).setText(author);
        }
        
        if (event != null) {
            ((TextView)view.findViewById(R.id.titleSlide_event)).setText(event);
        }
        
        if (date != null) {
            ((TextView)view.findViewById(R.id.titleSlide_date)).setText(date);
        }
        
        return view;
    }
    
    /**
     * Create the content view (the space between the byline and the bottom of
     * the slide).
     * <p>
     * Subclasses can override this method to return a {@link View} that will be
     * inserted into the content area.
     * 
     * @param inflater Layout inflater (never null).
     * @param container The content frame that will be the parent view of the
     *                  returned view (never null).
     * @param savedInstanceState The instance state (may be null).
     * @return A view, or null if there is nothing to see here (move along).
     */
    protected View onCreateTitleSlideContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

}

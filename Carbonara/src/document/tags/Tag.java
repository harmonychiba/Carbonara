/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package document.tags;

import cafe.control.TagDataChangeEvent;
import cafe.control.TagDataChangeListener;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author chibayuuki
 */
public abstract  class Tag {
    public static final String HEADER = "TAG_HEADER";
    public static final String FOOTER = "TAG_FOOTER";
    public static final String CONTENT = "TAG_CONTENT";
    public static final String TITLE = "TAG_TITLE";
    public static final String PARAGRAPH = "TAG_PARAGRAPH";

    protected final Tag self;
    private String tag_name;
    private String class_name;
    private String id_name;
    private HashMap<String, String> parameters;
    private String text;
    protected TagDataChangeListener listener;
    
    private boolean containable;
    
    private ArrayList<Tag> children;
    private ArrayList<Float> heights_of_children_preview;
    
    protected Pane preview_pane;
    protected Pane child_preview;
    protected VBox children_preview;
    protected Tag temp_tag;
    
    protected boolean content_selected;
    protected int focussedTag;
    protected int indexToAddTag;
    protected Timeline expand_anim;

    public Tag(String tag) {
        self = this;
        self.tag_name = tag;
        self.children = new ArrayList<>();
        self.heights_of_children_preview = new ArrayList<>();
        self.parameters = new HashMap<>();

        self.containable = false;
        
        self.text = "";
    }

    public void setParameter(String name, String value) {
        self.pushEvent();
        parameters.put(name, value);
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }

    public Set<String> getParameterNames() {
        return parameters.keySet();
    }

    public void setId(String id_name) {
        self.id_name = id_name;
        self.pushEvent();
    }

    public void setClassName(String class_name) {
        self.class_name = class_name;
        self.pushEvent();
    }

    public String getIdName() {
        return self.getIdName();
    }

    public String getClassName() {
        return self.getClassName();
    }

    public void setListener(TagDataChangeListener listener) {
        self.listener = listener;
    }

    protected void pushEvent() {
        if(self.listener != null){
            self.listener.handle(new TagDataChangeEvent(this, Event.NULL_SOURCE_TARGET, TagDataChangeEvent.TAG_DATA_CHANGE_EVENT));
        }
    }
    public String getTagName(){
        return self.tag_name;
    }
    public ArrayList<Tag> getChildren(){
        return self.children;
    }

    public boolean isContainable() {
        return containable;
    }

    public void setContainable(boolean containable) {
        this.containable = containable;
    }
    
    public abstract  Pane generateView(float parentWidth,float parentHeight);

    public abstract void dragOverAtPoint(double x, double y, String id,boolean is_continue) ;

    public abstract void dragExit();

    public abstract void dragDrop(String id);
    
    protected void replayExpandAnimation() {
        self.expand_anim.stop();
        self.child_preview.setScaleX(0.0f);
        self.child_preview.setOpacity(0.0f);
        self.expand_anim.play();
    }
}

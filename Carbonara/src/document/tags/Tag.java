/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package document.tags;

import cafe.Carbonara;
import cafe.control.MainController;
import cafe.control.TagDataChangeEvent;
import cafe.control.TagDataChangeListener;
import document.Page;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author chibayuuki
 */
public abstract class Tag {

    public static final String HEADER = "TAG_HEADER";
    public static final String FOOTER = "TAG_FOOTER";
    public static final String CONTENT = "TAG_CONTENT";
    public static final String TITLE = "TAG_TITLE";
    public static final String PARAGRAPH = "TAG_PARAGRAPH";
    public static final String BUTTON = "TAG_BUTTON";
    public static final String COUPON = "TAG_COUPON";
    public static final String SHARE = "TAG_SHARE";
    public static final String TIMELINE = "TAG_TIMELINE";
    public static final String TWEET = "TAG_TWEET";
    public static final String MAP = "TAG_MAP";
    public static final String DMM = "TAG_DMM";
    public static final String KAIHEI = "TAG_KAIHEI";
    protected final Tag self;
    private String tag_name;
    protected String class_name;
    protected String id_name;
    private HashMap<String, String> parameters;
    private String text;
    protected TagDataChangeListener listener;
    private boolean containable;
    protected ArrayList<Tag> children;
    private ArrayList<Float> heights_of_children_preview;
    protected Pane preview_pane;
    protected Pane child_preview;
    protected VBox children_preview;
    protected Tag temp_tag;
    protected boolean content_selected;
    protected int focussedTag;
    protected int indexToAddTag;
    protected Timeline expand_anim;
    private Page page;
    private Tag parent;

    public Tag(String tag) {
        self = this;
        self.tag_name = tag;
        self.children = new ArrayList<>();
        self.heights_of_children_preview = new ArrayList<>();
        self.parameters = new HashMap<>();

        self.containable = false;
        
        self.class_name = "";
        self.id_name = "";

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
        if (self.listener != null) {
            self.listener.handle(new TagDataChangeEvent(this, Event.NULL_SOURCE_TARGET, TagDataChangeEvent.TAG_DATA_CHANGE_EVENT));
        }
    }

    public String getTagName() {
        return self.tag_name;
    }

    public ArrayList<Tag> getChildren() {
        return self.children;
    }

    public boolean isContainable() {
        return containable;
    }

    public void setContainable(boolean containable) {
        this.containable = containable;
    }

    public abstract Pane generateView(float parentWidth, float parentHeight);

    public abstract void dragOverAtPoint(double x, double y, String id, boolean is_continue);

    public abstract void dragExit();

    public abstract void dragDrop(String id);
    
    public abstract void getParameterEditor(VBox box,String param);

    protected void replayExpandAnimation() {
        self.expand_anim.stop();
        self.child_preview.setScaleX(0.0f);
        self.child_preview.setOpacity(0.0f);
        self.expand_anim.play();
    }

    public void setPage(Page page) {
        self.page = page;
    }

    void addChildAtIndex(int indexToAddTag, Tag new_tag) {
        self.getChildren().add(indexToAddTag, new_tag);
        new_tag.setParent(self);
    }

    void addChildAtLast(Tag new_tag) {
        self.getChildren().add(new_tag);
        new_tag.setParent(self);
    }

    private void setParent(Tag tag) {
        self.parent = tag;
    }

    protected void initPreviewPane() {
        if (self.preview_pane != null) {
            self.preview_pane.setId("ID:"+self.tag_name);
            self.preview_pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    MainController controller = Carbonara.AppController();

                    controller.onPreviewPaneClicked(self);
                    
                    boolean consumed = false;
                    float clicked_x = (float) t.getX();
                    float clicked_y = (float) t.getY();
                    for(Tag child : self.children){
                        if(child.preview_pane != null){
                            Bounds layout = child.preview_pane.getBoundsInParent();
                            System.out.println("("+layout.getMinX()+","+layout.getMaxX()+","+layout.getMinY()+","+layout.getMaxY()+")");
                            if(clicked_x >= layout.getMinX() && clicked_x <= layout.getMaxX() && clicked_y >= layout.getMinY() && clicked_y <= layout.getMaxY()){
                                child.preview_pane.onMouseClickedProperty().getValue().handle(t);
                                consumed = true;
                                break;
                            }
                        }
                    }
                    if(!consumed){
                        controller.onPreviewPaneClicked(self);
                    }
                }
            });
            self.preview_pane.setOnDragDetected(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    Pane source = (Pane) t.getSource();
                    Dragboard db = source.startDragAndDrop(TransferMode.LINK);

                    ClipboardContent content = new ClipboardContent();
                    content.putString(source.getId());
                    db.setContent(content);
                    
                    if(self.page == null){
                        self.parent.children.remove(self);
                        Carbonara.AppController().setExistingMode(true);
                        Carbonara.Renderer().setPreview_tag(self.preview_pane);
                    }
                    else{
                        self.page.removeTag(self);
                        Carbonara.AppController().setExistingMode(true);
                        Carbonara.Renderer().setPreview_tag(self.preview_pane);
                    }

                    t.consume();
                }
            });
            self.preview_pane.setOnDragDone(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    Pane source = (Pane) event.getSource();
                    if (event.getTransferMode() == TransferMode.LINK) {
                        System.out.println("detect that drag are successfully done!!!!");
                    }
                    event.consume();
                }
            });
        }
    }
    public abstract String generateHTML();
}

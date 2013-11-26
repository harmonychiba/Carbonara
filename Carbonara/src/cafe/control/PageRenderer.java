/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cafe.control;

import cafe.view.ContentPane;
import document.Page;
import document.tags.ButtonTag;
import document.tags.ContentTag;
import document.tags.FooterTag;
import document.tags.HeaderTag;
import document.tags.ParagraphTag;
import document.tags.Tag;
import document.tags.TestTag;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 *
 * @author chibayuuki
 */
public class PageRenderer implements TagDataChangeListener {

    public static final double DEFAULT_CONTENT_SPACE = 10.0;
    public static final double EXPAND_ANIMATION_DURATION = 500.0;
    public static final int TO_ADD_LAST_OF_LIST = -100;
    public final PageRenderer self;
    private Page page;
    private ContentPane view;
    private AnchorPane page_preview;
    private VBox linear_box;
    private MainController controller;
    private Timeline expand_anim;
    private Pane preview_tag;
    private boolean content_selected;
    private int indexToAddTag;
    private int focussedTag;
    
    public PageRenderer() {
        self = this;
        self.page_preview = new AnchorPane();
        page_preview.setPrefWidth(720.0f);
        page_preview.setPrefHeight(1280.0f);
        page_preview.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                AnchorPane source = (AnchorPane) event.getSource();
                if (event.getGestureSource() != source && event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.LINK);

                    boolean detected = false;

                    if (linear_box.getChildren().contains(self.preview_tag)) {
                        linear_box.getChildren().remove(self.preview_tag);
                    }

                    for (int i = 0; i < linear_box.getChildren().size(); i++) {
                        Node node = linear_box.getChildren().get(i);

                        Bounds content_bounds = node.getBoundsInParent();
                        double judge_height = content_bounds.getHeight();
                        double height_margin = 0.0f;
                        if (!self.content_selected) {
                            height_margin = judge_height / 4.0f;
                        }

                        if (content_bounds.getMinX() <= event.getY() && content_bounds.getMinY() + height_margin > event.getY()) {
                            linear_box.getChildren().add(i, self.preview_tag);
                            
                            if(self.content_selected){
                                self.page.getTags().get(self.focussedTag).dragExit();
                                self.focussedTag = -1;
                            }
                            
                            self.content_selected = false;
                            detected = true;
                            if (self.indexToAddTag != i) {
                                self.replayExpandAnimation();
                            }
                            self.indexToAddTag = i;
                            break;
                        } else if (content_bounds.getMaxY() - height_margin <= event.getY() && content_bounds.getMaxY() > event.getY()) {
                            linear_box.getChildren().add(i + 1, self.preview_tag);
                            
                            if(self.content_selected){
                                self.page.getTags().get(self.focussedTag).dragExit();
                                self.focussedTag = -1;
                            }
                            
                            self.content_selected = false;
                            detected = true;
                            if (self.indexToAddTag != i + 1) {
                                self.replayExpandAnimation();
                            }
                            self.indexToAddTag = i + 1;
                            break;
                        } else if (content_bounds.getMinY() + height_margin <= event.getY() && content_bounds.getMaxY() - height_margin >= event.getY()) {
                            boolean is_continue = (i==self.focussedTag);
                            if(!is_continue && self.focussedTag != -1){
                                self.page.getTags().get(self.focussedTag).dragExit();
                            }
                            self.page.getTags().get(i).dragOverAtPoint(event.getX() - content_bounds.getMinX(), event.getY() - content_bounds.getMinY(), event.getDragboard().getString(),is_continue);
                            detected = true;
                            self.content_selected = true;
                            self.indexToAddTag = -1;
                            self.focussedTag = i;
                            break;
                        }
                    }

                    if (!detected) {
                        if (linear_box.getChildren().contains(self.preview_tag)) {
                            linear_box.getChildren().remove(self.preview_tag);
                        }
                        linear_box.getChildren().add(self.preview_tag);
                        
                        if(self.content_selected){
                            self.page.getTags().get(self.focussedTag).dragExit();
                            self.focussedTag = -1;
                        }
                        
                        self.content_selected = false;

                        if (self.indexToAddTag != PageRenderer.TO_ADD_LAST_OF_LIST) {
                            self.replayExpandAnimation();
                        }

                        self.indexToAddTag = PageRenderer.TO_ADD_LAST_OF_LIST;
                    }

                }
                event.consume();
            }
        });
        page_preview.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                AnchorPane source = (AnchorPane) event.getSource();
                if (event.getGestureSource() != source && event.getDragboard().hasString()) {

                    String tag_type = event.getDragboard().getString().split(":")[1];
                    Tag temp_tag = null;
                    switch (tag_type) {
                        case Tag.HEADER:
                            temp_tag = new HeaderTag();
                            break;
                        case Tag.FOOTER:
                            temp_tag = new FooterTag();
                            break;
                        case Tag.CONTENT:
                            temp_tag = new ContentTag();
                            break;
                        case Tag.BUTTON:
                            temp_tag = new ButtonTag();
                            break;
                        case Tag.PARAGRAPH:
                            temp_tag = new ParagraphTag();
                            break;
                        case Tag.TEST:
                            temp_tag = new TestTag();
                            break;
                    }
                    if (temp_tag != null) {
                        self.preview_tag = temp_tag.generateView((float) self.page_preview.getPrefWidth(), (float) self.page_preview.getPrefHeight());
                        self.preview_tag.setScaleX(0.0f);
                        self.preview_tag.setOpacity(0.0f);
                        self.expand_anim = new Timeline();
                        KeyValue width_value = new KeyValue(self.preview_tag.scaleXProperty(), 1.0f);
                        KeyValue opacity_value = new KeyValue(self.preview_tag.opacityProperty(), 0.75f);
                        KeyFrame width_frame = new KeyFrame(new Duration(PageRenderer.EXPAND_ANIMATION_DURATION), width_value);
                        KeyFrame opacity_frame = new KeyFrame(new Duration(PageRenderer.EXPAND_ANIMATION_DURATION), opacity_value);
                        self.expand_anim.getKeyFrames().addAll(width_frame, opacity_frame);
                        self.expand_anim.play();
                    }
                    self.focussedTag = -1;
                }

                event.consume();
            }
        });
        page_preview.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                AnchorPane source = (AnchorPane) event.getSource();
                linear_box.getChildren().remove(self.preview_tag);

                event.consume();
                
                if(self.content_selected){
                    self.page.getTags().get(self.focussedTag).dragExit();
                }
                self.content_selected = false;
            }
        });
        page_preview.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                AnchorPane source = (AnchorPane) event.getSource();
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    String id = db.getString().split(":")[1];

                    if (self.indexToAddTag != -1) {
                        self.controller.itemDropped(id, self.indexToAddTag);
                    }
                    else{
                        self.page.getTags().get(self.focussedTag).dragDrop(id);
                        self.page.getTags().get(self.focussedTag).generateView((float)self.page_preview.getWidth(), (float)self.page_preview.getHeight());
                    }

                    success = true;
                }
                event.setDropCompleted(success);

                event.consume();
                
                if(self.content_selected){
                    self.page.getTags().get(self.focussedTag).dragExit();
                }
                self.content_selected = false;
            }
        });

        self.render();
    }

    public void setPage(Page page) {
        self.page = page;
        self.view.setPreview(self.page_preview);
        self.render();
        self.snapshotPagePreview();
    }

    public void render() {
        page_preview.getChildren().clear();

        if (self.page != null) {
            if (linear_box != null) {
                self.linear_box.getChildren().clear();
                self.linear_box = null;
            }

            linear_box = new VBox(PageRenderer.DEFAULT_CONTENT_SPACE);
            linear_box.setPrefSize(self.page_preview.getPrefWidth(), self.page_preview.getPrefHeight());

            for (Tag tags : self.page.getTags()) {
                linear_box.getChildren().add(tags.generateView((float) linear_box.getPrefWidth(), (float) linear_box.getPrefHeight()));
            }
            self.page_preview.getChildren().add(linear_box);
        }
    }
    private void snapshotPagePreview(){
        if(self.page!=null){
            try{
                if(self.page.getPreviewImage() == null){
                    self.page.initPreviewImage(self.page_preview.snapshot(new SnapshotParameters(), null));
                }
                else{
                    self.page_preview.snapshot(new SnapshotParameters(), self.page.getPreviewImage());
                }
            }
            catch(Exception e){
                System.err.println("Error occuered when application render page preview image.");
                e.printStackTrace();
            }
        }
    }
    @Override
    public void handle(TagDataChangeEvent t) {
        self.render();
        self.snapshotPagePreview();
    }

    public void setView(ContentPane root) {
        self.view = root;
        self.view.setPreview(self.page_preview);
        self.render();
    }

    public void setHandler(MainController controller) {
        self.controller = controller;
    }

    private void replayExpandAnimation() {
        self.expand_anim.stop();
        self.preview_tag.setScaleX(0.0f);
        self.preview_tag.setOpacity(0.0f);
        self.expand_anim.play();
    }

    public void setPreview_tag(Pane preview_tag) {
        this.preview_tag = preview_tag;
        self.render();
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package document.tags;

import cafe.Carbonara;
import cafe.control.PageRenderer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

/**
 *
 * @author chibayuuki
 */
public class ContentTag extends Tag {

    public static final float DEFAULT_CONTENT_HEIGHT = 200.0f;
    public static final float DEFAULT_CONTENT_WORD_SIZE = 15.0f;
    public static final float DEFAULT_CHILDREN_SPACE = 10.0f;
    public static final float DEFAULT_MARGIN_BOTTOM = 30.0f;

    public ContentTag() {
        super(Tag.CONTENT);
        initializeParameters();
        self.setContainable(true);
        self.preview_pane = new AnchorPane();
        self.children_preview = new VBox(DEFAULT_CHILDREN_SPACE);
        self.initPreviewPane();
    }

    private void initializeParameters() {
        self.setParameter("fix", "false");
        self.setParameter("image", "null");
        self.setParameter("width", "match_parent");
        self.setParameter("height", "wrap_content");
    }

    @Override
    public Pane generateView(float parentWidth, float parentHeight) {
        Pane pane = self.preview_pane;

        self.children_preview.getChildren().clear();

        for (Tag child : self.getChildren()) {
            Node node = child.generateView((float) self.preview_pane.getWidth(), (float) self.preview_pane.getHeight());
            self.children_preview.getChildren().add(node);
            node.setTranslateY(ContentTag.DEFAULT_CHILDREN_SPACE);
        }

        pane.getChildren().clear();

        Rectangle background = new Rectangle();

        if (self.getParameter("width").equals("match_parent")) {
            background.setWidth(parentWidth);
            self.children_preview.setPrefWidth(parentWidth);
        } else if (self.getParameter("width").equals("wrap_content")) {
            background.setWidth(self.children_preview.getPrefWidth());
        } else if (self.getParameter("width").endsWith("px")) {
            background.setWidth(Double.parseDouble(self.getParameter("width").split("px")[0]));
            self.children_preview.setPrefWidth(background.getWidth());
        }

        if (self.getParameter("height").equals("match_parent")) {
            background.setHeight(parentHeight);
            self.children_preview.setPrefHeight(parentHeight);
        } else if (self.getParameter("height").equals("wrap_content")) {
            if (self.children_preview.getHeight() < ContentTag.DEFAULT_CONTENT_HEIGHT) {
                self.children_preview.setPrefHeight(ContentTag.DEFAULT_CONTENT_HEIGHT);
                background.setHeight(self.children_preview.getPrefHeight() * 5.0f / 4.0f + ContentTag.DEFAULT_MARGIN_BOTTOM);
            } else {
                background.setHeight(self.children_preview.getHeight() * 5.0f / 4.0f + ContentTag.DEFAULT_MARGIN_BOTTOM);
            }
        } else if (self.getParameter("height").endsWith("px")) {
            background.setHeight(Double.parseDouble(self.getParameter("height").split("px")[0]));
            self.children_preview.setPrefHeight(background.getHeight());
        }
        background.setFill(new Color(0.8f, 0.9f, 1.0f, 0.2f));
        background.setStrokeWidth(2.5);
        background.getStrokeDashArray().addAll(5.0, 10.0);
        background.setStrokeLineJoin(StrokeLineJoin.ROUND);
        background.setStroke(Color.GRAY);
        background.setStrokeType(StrokeType.INSIDE);

        pane.getChildren().add(background);
        pane.getChildren().add(self.children_preview);

        self.preview_pane = pane;

        return pane;
    }

    @Override
    public void dragOverAtPoint(double x, double y, String id, boolean is_continue) {
        if (self.isContainable()) {
            self.children_preview.getChildren().remove(self.child_preview);
            if (!is_continue) {
                String tag_type = id.split(":")[1];
                self.temp_tag = null;
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
                if (self.temp_tag != null) {
                    self.child_preview = self.temp_tag.generateView((float) self.preview_pane.getWidth(), (float) self.preview_pane.getHeight());
                    self.child_preview.setTranslateY(ContentTag.DEFAULT_CHILDREN_SPACE);

                    self.child_preview.setScaleX(0.0f);
                    self.child_preview.setOpacity(0.0f);
                    self.expand_anim = new Timeline();
                    KeyValue width_value = new KeyValue(self.child_preview.scaleXProperty(), 1.0f);
                    KeyValue opacity_value = new KeyValue(self.child_preview.opacityProperty(), 0.75f);
                    KeyFrame width_frame = new KeyFrame(new Duration(PageRenderer.EXPAND_ANIMATION_DURATION), width_value);
                    KeyFrame opacity_frame = new KeyFrame(new Duration(PageRenderer.EXPAND_ANIMATION_DURATION), opacity_value);
                    self.expand_anim.getKeyFrames().addAll(width_frame, opacity_frame);
                    self.expand_anim.play();
                }
                self.focussedTag = -1;
            }

            boolean detected = false;

            for (int i = 0; i < self.getChildren().size(); i++) {
                Tag child = self.getChildren().get(i);
                Bounds childBounds = child.preview_pane.getBoundsInParent();

                double judge_height = childBounds.getHeight();
                double height_margin = 0.0;
                if (!self.content_selected) {
                    height_margin = judge_height / 4.0f;
                }

                if (childBounds.getMinX() <= y && childBounds.getMinY() + height_margin > y) {
                    self.children_preview.getChildren().add(i, self.child_preview);

                    if (self.content_selected) {
                        self.getChildren().get(self.focussedTag).dragExit();
                        self.focussedTag = -1;
                    }

                    self.content_selected = false;
                    detected = true;
                    if (self.indexToAddTag != i) {
                        self.replayExpandAnimation();
                    }
                    self.indexToAddTag = i;
                    break;
                } else if (childBounds.getMaxY() - height_margin <= y && childBounds.getMaxY() > y) {
                    children_preview.getChildren().add(i + 1, self.child_preview);

                    if (self.content_selected) {
                        self.getChildren().get(self.focussedTag).dragExit();
                        self.focussedTag = -1;
                    }

                    self.content_selected = false;
                    detected = true;
                    if (self.indexToAddTag != i + 1) {
                        self.replayExpandAnimation();
                    }
                    self.indexToAddTag = i + 1;
                    break;
                } else if (childBounds.getMinY() + height_margin <= y && childBounds.getMaxY() - height_margin >= y) {
                    boolean is_continue2 = (i == self.focussedTag);
                    if (!is_continue2 && self.focussedTag != -1) {
                        self.getChildren().get(self.focussedTag).dragExit();
                    }
                    self.getChildren().get(i).dragOverAtPoint(x - childBounds.getMinX(), y - childBounds.getMinY(), id, is_continue2);
                    detected = true;
                    self.content_selected = true;
                    self.indexToAddTag = -1;
                    self.focussedTag = i;
                    break;
                }
            }

            if (!detected) {
                if (self.children_preview.getChildren().contains(self.child_preview)) {
                    self.children_preview.getChildren().remove(self.child_preview);
                }
                self.children_preview.getChildren().add(self.child_preview);

                if (self.content_selected) {
                    self.getChildren().get(self.focussedTag).dragExit();
                    self.focussedTag = -1;
                }

                self.content_selected = false;

                if (self.indexToAddTag != PageRenderer.TO_ADD_LAST_OF_LIST) {
                    self.replayExpandAnimation();
                }

                self.indexToAddTag = PageRenderer.TO_ADD_LAST_OF_LIST;
            }


        }
    }

    @Override
    public void dragExit() {
        self.children_preview.getChildren().remove(self.child_preview);

        if (self.content_selected) {
            self.getChildren().get(self.focussedTag).dragExit();
        }
        self.content_selected = false;
    }

    @Override
    public void dragDrop(String id) {
        if (self.indexToAddTag != -1) {
            Tag new_tag = null;
            String tag_type = id;
            switch (tag_type) {
                case Tag.HEADER:
                    new_tag = new HeaderTag();
                    break;
                case Tag.FOOTER:
                    new_tag = new FooterTag();
                    break;
                case Tag.CONTENT:
                    new_tag = new ContentTag();
                    break;
                case Tag.BUTTON:
                    new_tag = new ButtonTag();
                    break;
                case Tag.PARAGRAPH:
                    new_tag = new ParagraphTag();
                    break;
                case Tag.TEST:
                    new_tag = new TestTag();
                    break;
            }
            if (new_tag != null) {
                new_tag.setListener(self.listener);
                if (self.indexToAddTag != PageRenderer.TO_ADD_LAST_OF_LIST) {
                    self.addChildAtIndex(self.indexToAddTag, new_tag);
                } else {
                    self.addChildAtLast(new_tag);
                }
            }
            //self.controller.itemDropped(id, self.indexToAddTag);
        } else {
            self.getChildren().get(self.focussedTag).dragDrop(id);
            self.getChildren().get(self.focussedTag).generateView(DEFAULT_CONTENT_HEIGHT, DEFAULT_CONTENT_HEIGHT);
        }
        System.out.println(self.listener);
        self.pushEvent();
    }

    @Override
    public void getParameterEditor(VBox box, String param) {
        double width = box.getPrefWidth();
        switch (param) {
            case "fix":
                Label fix_label = new Label("固定");
                ComboBox<String> fix_selector = new ComboBox<>(FXCollections.observableArrayList("On", "Off"));
                fix_label.setPrefWidth(width);
                fix_selector.setPrefWidth(width);
                fix_selector.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> ov, String old_v, String new_v) {
                        switch (old_v) {
                            case "On":
                                self.setParameter("fix", "true");
                                break;
                            case "Off":
                                self.setParameter("fix", "false");
                                break;
                        }
                    }
                });
                box.getChildren().addAll(fix_label, fix_selector);
                break;
            case "width":
                Label width_label = new Label("横幅");
                CheckBox width_match_parent = new CheckBox("外側に合わせる");
                CheckBox width_wrap_content = new CheckBox("内側に合わせる");
                TextField width_px_editor = new TextField("ピクセルを指定する");
                width_label.setPrefWidth(width);
                width_wrap_content.setPrefWidth(width);
                width_match_parent.setPrefWidth(width);
                width_px_editor.setPrefWidth(width);

                width_wrap_content.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean o, Boolean n) {
                        if (n) {
                            self.setParameter("width", "wrap_content");
                            Carbonara.Renderer().render();
                        }
                    }
                });
                width_match_parent.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean o, Boolean n) {
                        if (n) {
                            self.setParameter("width", "match_parent");
                            Carbonara.Renderer().render();
                        }
                    }
                });
                width_px_editor.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> ov, String o, String n) {
                        try {
                            int width = Integer.parseInt(n);
                            self.setParameter("width", width + "px");
                            Carbonara.Renderer().render();
                        } catch (Exception e) {
                        }
                    }
                });

                box.getChildren().addAll(width_label, width_match_parent, width_wrap_content, width_px_editor);
                break;
            case "height":
                Label height_label = new Label("高さ");
                CheckBox height_match_parent = new CheckBox("外側に合わせる");
                CheckBox height_wrap_content = new CheckBox("内側に合わせる");
                TextField height_px_editor = new TextField("ピクセルを指定する");
                height_label.setPrefWidth(width);
                height_wrap_content.setPrefWidth(width);
                height_match_parent.setPrefWidth(width);
                height_px_editor.setPrefWidth(width);

                height_wrap_content.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean o, Boolean n) {
                        if (n) {
                            self.setParameter("height", "wrap_content");
                            Carbonara.Renderer().render();
                        }
                    }
                });
                height_match_parent.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean o, Boolean n) {
                        if (n) {
                            self.setParameter("height", "match_parent");
                            Carbonara.Renderer().render();
                        }
                    }
                });
                height_px_editor.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> ov, String o, String n) {
                        try {
                            int width = Integer.parseInt(n);
                            self.setParameter("height", width + "px");
                            Carbonara.Renderer().render();
                        } catch (Exception e) {
                        }
                    }
                });

                box.getChildren().addAll(height_label, height_match_parent, height_wrap_content, height_px_editor);
                break;
        }
    }

    @Override
    public String generateHTML() {
        String html = "<div data-role=\"content\" ";
        if(self.class_name.equalsIgnoreCase("")){
            html += "class=\""+self.class_name+"\" ";
        }
        if(self.id_name.equalsIgnoreCase("")){
            html += "class=\""+self.id_name+"\" ";
        }
        html+=">\n";
            for(Tag child:self.children){
                html+=child.generateHTML();
            }
        html+="</div>\n";
        
        return html;
    }
}

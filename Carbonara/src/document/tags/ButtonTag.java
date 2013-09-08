/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package document.tags;

import cafe.Carbonara;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 *
 * @author chibayuuki
 */
public class ButtonTag extends Tag {

    public static final float DEFAULT_HEADER_HEIGHT = 50.0f;
    public static final float DEFAULT_HEADER_WORD_SIZE = 32.0f;

    public ButtonTag() {
        super(Tag.BUTTON);
        initializeParameters();
        self.preview_pane = new AnchorPane();
        self.initPreviewPane();
    }

    private void initializeParameters() {
        self.setParameter("word", "BUTTON");
        self.setParameter("link","#");
        self.setParameter("image", "null");
        self.setParameter("width", "match_parent");
        self.setParameter("height", "wrap_content");
    }

    @Override
    public Pane generateView(float parentWidth, float parentHeight) {
        Pane pane = self.preview_pane;
        pane.getChildren().clear();

        Label text = new Label(self.getParameter("word"));
        Font font = new Font(ButtonTag.DEFAULT_HEADER_WORD_SIZE);
        text.setFont(font);
        text.setTextFill(Color.WHITE);

        Rectangle background = new Rectangle();

        if (self.getParameter("width").equals("match_parent")) {
            background.setWidth(parentWidth);
            text.setPrefWidth(parentWidth);
            text.setAlignment(Pos.CENTER);
        } else if (self.getParameter("width").equals("wrap_content")) {
            text.setWrapText(true);
            background.setWidth(text.getWidth());
        } else if (self.getParameter("width").endsWith("px")) {
            background.setWidth(Double.parseDouble(self.getParameter("width").split("px")[0]));
            text.setPrefWidth(background.getWidth());
        }

        if (self.getParameter("height").equals("match_parent")) {
            background.setHeight(parentHeight);
            text.setPrefHeight(parentHeight);
        } else if (self.getParameter("height").equals("wrap_content")) {
            if (text.getHeight() < ButtonTag.DEFAULT_HEADER_HEIGHT) {
                text.setPrefHeight(ButtonTag.DEFAULT_HEADER_HEIGHT);
            }
            background.setHeight(text.getPrefHeight());
        } else if (self.getParameter("height").endsWith("px")) {
            background.setHeight(Double.parseDouble(self.getParameter("height").split("px")[0]));
            text.setPrefHeight(background.getHeight());
        }
        background.setFill(Color.LIGHTGRAY);

        background.setArcHeight(background.getHeight() / 2.0f);
        background.setArcWidth(background.getHeight() / 2.0f);

        pane.getChildren().add(background);
        pane.getChildren().add(text);

        return pane;
    }

    @Override
    public void dragOverAtPoint(double x, double y, String id, boolean is_continue) {
        if (self.isContainable() == false) {
            ColorAdjust effect = new ColorAdjust();
            effect.setBrightness(0.1);
            effect.setContrast(0.25);
            effect.setHue(0.0);
            effect.setSaturation(0.5);
            self.preview_pane.setEffect(effect);
        }
    }

    @Override
    public void dragExit() {
        ColorAdjust effect = new ColorAdjust();
        effect.setBrightness(0.0);
        effect.setContrast(0.0);
        effect.setHue(0.0);
        effect.setSaturation(0.0);
        self.preview_pane.setEffect(effect);
    }

    @Override
    public void dragDrop(String id) {
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
            case "word":
                Label word_label = new Label("文字");
                TextField word_editor = new TextField("文字を指定する");
                word_editor.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> ov, String o, String n) {
                        self.setParameter("word", n);
                        Carbonara.Renderer().render();
                    }
                });
                box.getChildren().addAll(word_label, word_editor);
                break;
            case "link":
                Label link_label = new Label("文字");
                TextField link_editor = new TextField("文字を指定する");
                link_editor.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> ov, String o, String n) {
                        self.setParameter("link", n);
                        Carbonara.Renderer().render();
                    }
                });
                box.getChildren().addAll(link_label, link_editor);
                break;
        }
    }

    @Override
    public String generateHTML() {
        String html = "<a data-role=\"button\" href=\""+self.getParameter("link")+"\" ";
        if (self.class_name.equalsIgnoreCase("")) {
            html += "class=\"" + self.class_name + "\" ";
        }
        if (self.id_name.equalsIgnoreCase("")) {
            html += "class=\"" + self.id_name + "\" ";
        }
        html += ">\n";

        html += "<h4>" + self.getParameter("word") + "</h4>";

        html += "</div>\n";

        return html;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * @author Takafumi
 */
public class DMMTag extends Tag{
    
    //名付け親：渡邊寛謙
    
    public static final float DEFAULT_HEADER_HEIGHT = 50.0f;
    public static final float DEFAULT_HEADER_WORD_SIZE = 32.0f;
    
    
    public DMMTag(){
        super(Tag.DMM);
        initializeParameters();
        self.preview_pane = new AnchorPane();
        self.initPreviewPane();
    }
    
    private void initializeParameters(){
        self.setParameter("word", "Youtube");
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
        background.setFill(Color.RED);//色の選択

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
            
            case "link":
                Label word_label = new Label("URL");
                TextField word_editor = new TextField("YoutubeのURLを入力");
                //TextField word_editor = new TextField(System.getProperty("user.dir"));
                word_editor.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> ov, String o, String n) {
                        int index = n.indexOf("http://www.youtube.com/watch?v=");
                        self.setParameter("link", n.substring(+31));
                        Carbonara.Renderer().render();
                    }
                });
                box.getChildren().addAll(word_label, word_editor);
                break;
        }
    }

    @Override
    public String generateHTML() {
        String html = "<div data-role=\"header\" ";
        if(self.class_name.equalsIgnoreCase("")){
            html += "class=\""+self.class_name+"\" ";
        }
        if(self.id_name.equalsIgnoreCase("")){
            html += "class=\""+self.id_name+"\" ";
        }
        html+=">\n";
        html += "<object width=\"425\" height=\"344\">\n<param name=\"movie\" value=\"http://www.youtube.com/v/"+self.getParameter("link")+"&hl=ja&fs=1\">\n</param><param name=\"allowFullScreen\" value=\"true\">\n</param>\n<param name=\"allowscriptaccess\" value=\"always\">\n</param><embed src=\"http://www.youtube.com/v/"+self.getParameter("link")+"&hl=ja&fs=1\" type=\"application/x-shockwave-flash\" allowscriptaccess=\"always\" allowfullscreen=\"true\" width=\"425\" height=\"344\">\n</embed>\n</object>\n";
        html+="</div>\n";
        return html;
    }
    
}

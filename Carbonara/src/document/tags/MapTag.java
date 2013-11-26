/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package document.tags;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
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
public class MapTag extends Tag{
    public MapTag(){
        super(Tag.MAP);
        initializeParameters();
        self.preview_pane = new AnchorPane();
        self.initPreviewPane();
    }
    private void initializeParameters(){
        self.setParameter("word", "TIMELINE");
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void getParameterEditor(VBox box, String param) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String generateHTML() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

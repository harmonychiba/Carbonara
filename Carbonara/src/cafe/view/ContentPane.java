/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cafe.view;

import java.util.Set;
import java.util.Iterator;
import cafe.control.MainController;
import document.Document;
import document.tags.Tag;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

/**
 *
 * @author chibayuuki
 */
public class ContentPane extends AnchorPane {

    public static final float MENU_ICON_SPACE = 25.0f;
    public static final float MENU_ICON_CONTAINER_HEIGHT = 80.0f;
    public static final float MENU_ICON_BUTTON_SIDE = 50.0f;
    public static final float TAG_ICON_SPACE = 30.0f;
    public static final float TAG_ICON_CONTAINER_HEIGHT = 140.0f;
    public static final float TAG_ICON_BUTTON_WIDTH = 80.0f;
    public static final float TAG_ICON_BUTTON_HEIGHT = 80.0f;
    public static final float PAGES_ICON_SPACE = 20.0f;
    public static final float PAGES_ICON_CONTAINER_WIDTH = 220.0f;
    public static final float PAGES_ICON_CONTAINER_HORIZONTAL_SPACE = 10.0f;
    public static final float PAGES_ICON_CONTAINER_VERTICAL_SPACE = 10.0f;
    public static final float PAGES_ICON_BUTTON_WIDTH = 60.0f;
    public static final float PAGES_ICON_BUTTON_HEIGHT = 100.0f;
    public static final int PAGES_ICON_BUTTON_COLUMN_NUM = 3;
    public static final float INSPECTOR_WIDTH = 250.0f;
    public static final float INSPECTOR_HORIZONTAL_SPACE = 20.0f;
    public static final float INSPECTOR_VERTICAL_SPACE = 10.0f;
    private static final float INSPECTOR_BOX_SPACE = 8.0f;
    public static final String MENU_ICON_NEW_ID = "ID:MENU_ICON_NEW";
    public static final String MENU_ICON_OPEN_ID = "ID:MENU_ICON_OPEN";
    public static final String MENU_ICON_SAVE_ID = "ID:MENU_ICON_SAVE";
    public static final String MENU_ICON_SAVE_AS_ID = "ID:MENU_ICON_SAVE_AS";
    public static final String MENU_ICON_COMMIT_ID = "ID:MENU_ICON_COMMIT";
    
    public static final String PAGE_ICON_BUTTON_ID = "ID:PAGE_ICON_BUTTON_NO.";
    public static final String PAGE_ICON_BUTTON_NEW_ID = "ID:PAGE_ICON_BUTTON_NEW";
    
    public static final String TAG_HEADER_ID = "ID:TAG_HEADER";
    public static final String TAG_FOOTER_ID = "ID:TAG_FOOTER";
    public static final String TAG_CONTENT_ID = "ID:TAG_CONTENT";
    //public static final String TAG_TITLE_ID = "ID:TAG_TITLE";
    public static final String TAG_PARAGRAPH_ID = "ID:TAG_PARAGRAPH";
    public static final String TAG_BUTTON_ID = "ID:TAG_BUTTON";
    public static final String TAG_COUPON_ID = "ID:TAG_COUPON";
    public static final String TAG_SHARE_ID = "ID:TAG_SHARE";
    public static final String TAG_TIMELINE_ID = "ID:TAG_TIMELINE";
    public static final String TAG_TWEET_ID = "ID:TAG_TWEET";
    //public static final String TAG_MOVIE_ID = "ID:TAG_MOVIE";
    public static final String TAG_MAP_ID = "ID:TAG_MAP";
    public static final String TAG_DMM_ID = "ID:TAG_DMM";
    public static final String TAG_KAIHEI_ID = "ID:TAG_KAIHEI";
    
    private MainController controller;
    private ContentPane self;
    private Stage window;
    private ArrayList<Button> menu_icon_buttons;
    private HBox menu_icon_container;
    private ArrayList<Button> tag_icon_buttons;
    private HBox tag_icon_container;
    private ArrayList<Button> page_icon_buttons;
    private ScrollPane pages_container;
    private VBox page_icon_row_container;
    private ArrayList<HBox> page_icon_rows;
    private ScrollPane inspector;
    private VBox inspector_box;
    private ScrollPane editor;
    private AnchorPane page_preview;
    private Document document;

    public ContentPane(MainController controller) {
        self = this;
        self.controller = controller;
        self.initViewContents();
    }

    public void setWindow(Stage stage) {
        self.window = stage;
        self.window.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object prev_value, Object next_value) {
                self.updateWindowSize();
            }
        });
        self.window.heightProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object prev_value, Object next_value) {
                self.updateWindowSize();
            }
        });
    }

    public void updateWindowSize() {
        self.resize(self.window.getScene().getWidth(), self.window.getScene().getHeight());

        self.menu_icon_container.setPrefSize(self.getWidth(), ContentPane.MENU_ICON_CONTAINER_HEIGHT);
        self.tag_icon_container.setTranslateX(0.0f);
        self.tag_icon_container.setTranslateY(0.0f);

        for (Button menu_icon_button : self.menu_icon_buttons) {
            menu_icon_button.setPrefHeight(ContentPane.MENU_ICON_BUTTON_SIDE);
            menu_icon_button.setPrefWidth(ContentPane.MENU_ICON_BUTTON_SIDE);
            menu_icon_button.setTranslateX(ContentPane.MENU_ICON_SPACE / 2.0f);
            menu_icon_button.setTranslateY((ContentPane.MENU_ICON_CONTAINER_HEIGHT - ContentPane.MENU_ICON_BUTTON_SIDE) / 2.0f);
        }

        self.tag_icon_container.setPrefSize(self.getWidth(), ContentPane.TAG_ICON_CONTAINER_HEIGHT);
        self.tag_icon_container.setTranslateX(0.0f);
        self.tag_icon_container.setTranslateY(self.getHeight() - ContentPane.TAG_ICON_CONTAINER_HEIGHT);

        for (Button tag_icon_button : self.tag_icon_buttons) {
            tag_icon_button.setPrefWidth(ContentPane.TAG_ICON_BUTTON_WIDTH);
            tag_icon_button.setPrefHeight(ContentPane.TAG_ICON_BUTTON_HEIGHT);
            tag_icon_button.setTranslateX(ContentPane.TAG_ICON_SPACE / 2.0f);
            tag_icon_button.setTranslateY((ContentPane.TAG_ICON_CONTAINER_HEIGHT - ContentPane.TAG_ICON_BUTTON_HEIGHT) / 2.0f);
        }

        self.pages_container.setPrefSize(ContentPane.PAGES_ICON_CONTAINER_WIDTH, self.getHeight() - ContentPane.MENU_ICON_CONTAINER_HEIGHT - ContentPane.TAG_ICON_CONTAINER_HEIGHT - 2.0f * ContentPane.PAGES_ICON_CONTAINER_VERTICAL_SPACE);
        self.pages_container.setTranslateX(ContentPane.PAGES_ICON_CONTAINER_HORIZONTAL_SPACE);
        self.pages_container.setTranslateY(ContentPane.PAGES_ICON_CONTAINER_VERTICAL_SPACE + ContentPane.MENU_ICON_CONTAINER_HEIGHT);

        self.inspector.setPrefSize(ContentPane.INSPECTOR_WIDTH, self.getHeight() - ContentPane.MENU_ICON_CONTAINER_HEIGHT - ContentPane.TAG_ICON_CONTAINER_HEIGHT - 2.0f * ContentPane.INSPECTOR_VERTICAL_SPACE);
        self.inspector.setTranslateX(self.getWidth() - (ContentPane.INSPECTOR_WIDTH + ContentPane.INSPECTOR_HORIZONTAL_SPACE));
        self.inspector.setTranslateY(ContentPane.INSPECTOR_VERTICAL_SPACE + ContentPane.MENU_ICON_CONTAINER_HEIGHT);

        self.editor.setPrefSize(self.getWidth() - ContentPane.PAGES_ICON_CONTAINER_WIDTH - 2.0f * ContentPane.PAGES_ICON_CONTAINER_HORIZONTAL_SPACE - ContentPane.INSPECTOR_WIDTH - 2.0f * ContentPane.INSPECTOR_HORIZONTAL_SPACE, self.getHeight() - ContentPane.MENU_ICON_CONTAINER_HEIGHT - ContentPane.TAG_ICON_CONTAINER_HEIGHT - 2.0f * ContentPane.PAGES_ICON_CONTAINER_VERTICAL_SPACE);
        self.editor.setTranslateX(ContentPane.PAGES_ICON_CONTAINER_WIDTH + 2.0f * ContentPane.PAGES_ICON_CONTAINER_HORIZONTAL_SPACE);
        self.editor.setTranslateY(ContentPane.INSPECTOR_VERTICAL_SPACE + ContentPane.MENU_ICON_CONTAINER_HEIGHT);
    }

    private void initViewContents() {
        self.menu_icon_container = new HBox(ContentPane.MENU_ICON_SPACE);
        self.menu_icon_container.setStyle("-fx-background-color: #afafaf;-fx-background-radius:5px;-fx-border-color:#5f5f5f;-fx-border-width:1px;-fx-border-radius:5px;");

        self.menu_icon_buttons = new ArrayList<>();
        
        Image img = new Image("icon_new.png");
        ImageView icon_new = new  ImageView(img);
        icon_new.setFitHeight(40f);
        icon_new.setFitWidth(40f);
        Button menu_icon_new = new Button();
        menu_icon_new.setGraphic(icon_new);
        Button menu_icon_open = new Button("開く");
        img = new Image("icon_save.png");
        ImageView icon_save = new ImageView(img);
        icon_save.setFitHeight(40f);
        icon_save.setFitWidth(40f);
        Button menu_icon_save = new Button();
        menu_icon_save.setGraphic(icon_save);
        Button menu_icon_save_as = new Button("名前を付けて保存する");
        Button menu_icon_commit = new Button("Webサイトを更新する");

        menu_icon_new.setId(MENU_ICON_NEW_ID);
        menu_icon_open.setId(MENU_ICON_OPEN_ID);
        menu_icon_save.setId(MENU_ICON_SAVE_ID);
        menu_icon_save_as.setId(MENU_ICON_SAVE_AS_ID);
        menu_icon_commit.setId(MENU_ICON_COMMIT_ID);

        self.menu_icon_buttons.add(menu_icon_new);
        self.menu_icon_buttons.add(menu_icon_open);
        self.menu_icon_buttons.add(menu_icon_save);
        self.menu_icon_buttons.add(menu_icon_save_as);
        self.menu_icon_buttons.add(menu_icon_commit);

        self.menu_icon_container.getChildren().addAll(menu_icon_new, menu_icon_open, menu_icon_save, menu_icon_save_as, menu_icon_commit);

        for (Button menu_icon_button : self.menu_icon_buttons) {
            menu_icon_button.setOnAction(self.controller);

            menu_icon_button.setEffect(new Reflection(0, ((ContentPane.MENU_ICON_CONTAINER_HEIGHT - ContentPane.MENU_ICON_BUTTON_SIDE) / 2.0f) / ContentPane.MENU_ICON_BUTTON_SIDE, 0.3f, 0.0f));
        }

        self.getChildren().add(menu_icon_container);

        self.tag_icon_container = new HBox(ContentPane.TAG_ICON_SPACE);
        self.tag_icon_container.setStyle("-fx-background-color: #efefef;-fx-background-radius:5px;-fx-border-color:#afafaf;-fx-border-width:1px;-fx-border-radius:5px;");

        self.tag_icon_buttons = new ArrayList<>();
        
        img = new Image("icon_header.png");
        ImageView icon_header = new ImageView(img);
        icon_header.setFitHeight(60f);
        icon_header.setFitWidth(60f);
        Button tag_icon_header = new Button();
        tag_icon_header.setStyle("-fx-base: #ffffffff");
        tag_icon_header.setGraphic(icon_header);
        img = new Image("icon_footer.png");
        ImageView icon_footer = new ImageView(img);
        icon_footer.setFitHeight(60f);
        icon_footer.setFitWidth(60f);
        Button tag_icon_footer = new Button();
        tag_icon_footer.setStyle("-fx-base: #ffffffff");
        tag_icon_footer.setGraphic(icon_footer);
        Button tag_icon_content = new Button("Content");
        Button tag_icon_button = new Button("Button");
        Button tag_icon_title = new Button("Title");
         img = new Image("icon_paragraph.png");
         ImageView icon_paragraph = new ImageView(img);
         icon_paragraph.setFitHeight(60f);
         icon_paragraph.setFitWidth(60f);
        Button tag_icon_paragraph = new Button();
        tag_icon_paragraph.setStyle("-fx-base: #a5e280ff;");
        tag_icon_paragraph.setGraphic(icon_paragraph);
        Button tag_icon_coupon = new Button("Coupon");
        img = new Image("icon_facebook.png");
        ImageView icon_facebook = new ImageView(img);
        icon_facebook.setFitHeight(60f);
        icon_facebook.setFitWidth(60f);
        Button tag_icon_share = new Button();
        tag_icon_share.setStyle("-fx-base: #000080ff;");
        tag_icon_share.setGraphic(icon_facebook);
        Button tag_icon_timeline = new Button("Timeline");
        img = new Image("icon_tweet.png");
        ImageView icon_tweet = new ImageView(img);
        icon_tweet.setFitHeight(60f);
        icon_tweet.setFitWidth(60f);
        Button tag_icon_tweet = new Button();
        tag_icon_tweet.setStyle("-fx-base: #67a8ffff;");
        tag_icon_tweet.setGraphic(icon_tweet);
        //Button tag_icon_movie = new Button("Movie");
        img = new Image("icon_map.png");
        ImageView icon_map = new ImageView(img);
        icon_map.setFitHeight(60f);
        icon_map.setFitWidth(60f);
        Button tag_icon_map = new Button();
        tag_icon_map.setStyle("-fx-base: #c7c05eff;");
        tag_icon_map.setGraphic(icon_map);
        img = new Image("icon_youtube.png");
        ImageView icon_dmm = new ImageView(img);
        icon_dmm.setFitWidth(60f);
        icon_dmm.setFitHeight(60f);
        Button tag_icon_dmm = new Button();
        tag_icon_dmm.setStyle("-fx-base: #e99d91;");
        tag_icon_dmm.setGraphic(icon_dmm);
        img = new Image("icon_kaihei.png");
        ImageView icon_kaihei = new ImageView(img);
        icon_kaihei.setFitWidth(60f);
        icon_kaihei.setFitHeight(60f);
        Button tag_icon_kaihei = new Button();
        tag_icon_kaihei.setStyle("-fx-base: #fbb763ff;");
        tag_icon_kaihei.setGraphic(icon_kaihei);

        tag_icon_header.setId(TAG_HEADER_ID);
        tag_icon_footer.setId(TAG_FOOTER_ID);
        tag_icon_content.setId(TAG_CONTENT_ID);
        tag_icon_button.setId(TAG_BUTTON_ID);
        //tag_icon_title.setId(TAG_TITLE_ID);
        tag_icon_paragraph.setId(TAG_PARAGRAPH_ID);
        tag_icon_coupon.setId(TAG_COUPON_ID);
        tag_icon_share.setId(TAG_SHARE_ID);
        tag_icon_timeline.setId(TAG_TIMELINE_ID);
        tag_icon_tweet.setId(TAG_TWEET_ID);
        //tag_icon_movie.setId(TAG_MOVIE_ID);
        tag_icon_map.setId(TAG_MAP_ID);
        tag_icon_dmm.setId(TAG_DMM_ID);
        tag_icon_kaihei.setId(TAG_KAIHEI_ID);

        self.tag_icon_buttons.add(tag_icon_header);
        self.tag_icon_buttons.add(tag_icon_footer);
        self.tag_icon_buttons.add(tag_icon_content);
        self.tag_icon_buttons.add(tag_icon_button);
        //self.tag_icon_buttons.add(tag_icon_title);
        self.tag_icon_buttons.add(tag_icon_paragraph);
        self.tag_icon_buttons.add(tag_icon_coupon);
        self.tag_icon_buttons.add(tag_icon_share);
        self.tag_icon_buttons.add(tag_icon_timeline);
        self.tag_icon_buttons.add(tag_icon_tweet);
        //self.tag_icon_buttons.add(tag_icon_movie);
        self.tag_icon_buttons.add(tag_icon_map);
        self.tag_icon_buttons.add(tag_icon_dmm);
        
        self.tag_icon_buttons.add(tag_icon_kaihei);

        for (Button tag_icon : self.tag_icon_buttons) {
            tag_icon.setOnDragDetected(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    Button source = (Button) t.getSource();
                    Dragboard db = source.startDragAndDrop(TransferMode.LINK);

                    ClipboardContent content = new ClipboardContent();
                    content.putString(source.getId());
                    db.setContent(content);

                    t.consume();
                }
            });
            tag_icon.setOnDragDone(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    Button source = (Button) event.getSource();
                    if (event.getTransferMode() == TransferMode.LINK) {
                        System.out.println("detect that drag are successfully done!!!!");
                    }
                    event.consume();
                }
            });
        }

        self.tag_icon_container.getChildren().addAll(tag_icon_header, tag_icon_footer, tag_icon_paragraph,tag_icon_tweet,tag_icon_share,tag_icon_map,tag_icon_dmm,tag_icon_kaihei);
        //self.tag_icon_container.getChildren().addAll(tag_icon_dmm);
        
        for (Button _tag_icon_button : self.tag_icon_buttons) {
            _tag_icon_button.setOnAction(self.controller);

            _tag_icon_button.setEffect(new Reflection(0, ((ContentPane.TAG_ICON_CONTAINER_HEIGHT - ContentPane.TAG_ICON_BUTTON_HEIGHT) / 2.0f) / ContentPane.TAG_ICON_BUTTON_HEIGHT, 0.3f, 0.0f));
        }

        self.getChildren().add(self.tag_icon_container);

        self.pages_container = new ScrollPane();
        self.pages_container.setStyle("-fx-background-color: #efdf9f;-fx-background-radius:5px;-fx-border-color:#af9f4f;-fx-border-width:1px;-fx-border-radius:5px;");

        self.page_icon_row_container = new VBox(PAGES_ICON_CONTAINER_VERTICAL_SPACE);
        self.pages_container.setContent(self.page_icon_row_container);

        self.page_icon_buttons = new ArrayList<>();
        self.page_icon_rows = new ArrayList<>();

        self.getChildren().add(self.pages_container);

        self.inspector = new ScrollPane();
        self.inspector.setStyle("-fx-background-color: #efdf9f;-fx-background-radius:5px;-fx-border-color:#af9f4f;-fx-border-width:1px;-fx-border-radius:5px;");

        self.getChildren().add(self.inspector);
        
        self.inspector_box = new VBox(ContentPane.INSPECTOR_BOX_SPACE);
        
        self.inspector.setContent(self.inspector_box);

        self.editor = new ScrollPane();
        self.editor.setStyle("-fx-background-color: #efefef;-fx-background-radius:5px;-fx-border-color:#afafaf;-fx-border-width:1px;-fx-border-radius:5px;");

        self.getChildren().add(self.editor);
    }

    public void setPreview(AnchorPane page_preview) {
        self.page_preview = page_preview;
        self.editor.setContent(page_preview);
    }

    public void setDocument(Document document) {
        self.document = document;
        self.documentChanged();
    }

    private void documentChanged() {
        self.page_icon_buttons.clear();

        for (HBox row : self.page_icon_rows) {
            row.getChildren().clear();
        }

        self.page_icon_rows.clear();

        self.page_icon_row_container.getChildren().clear();

        int row_num = self.document.getPageCount() / ContentPane.PAGES_ICON_BUTTON_COLUMN_NUM;
        for (int i = 0; i < row_num; i++) {
            HBox page_icon_row = new HBox(ContentPane.PAGES_ICON_CONTAINER_HORIZONTAL_SPACE);
            for (int j = 0; j < ContentPane.PAGES_ICON_BUTTON_COLUMN_NUM; j++) {
                ImageView img_view = new ImageView(self.document.getPageAtIndex(ContentPane.PAGES_ICON_BUTTON_COLUMN_NUM * i + j).getPreviewImage());
                img_view.setPreserveRatio(false);
                img_view.setSmooth(true);
                img_view.setFitWidth(ContentPane.PAGES_ICON_BUTTON_WIDTH);
                img_view.setFitHeight(ContentPane.PAGES_ICON_BUTTON_HEIGHT);
                
                Button page_icon = new Button("", img_view);
                page_icon.setStyle("-fx-background-color: #333333;-fx-background-radius: 0;-fx-background-insets: 0;");
                page_icon.setId(ContentPane.PAGE_ICON_BUTTON_ID+ (ContentPane.PAGES_ICON_BUTTON_COLUMN_NUM * i + j));
                page_icon.setOnAction(self.controller);                
                page_icon.setMaxSize(ContentPane.PAGES_ICON_BUTTON_WIDTH, ContentPane.PAGES_ICON_BUTTON_HEIGHT);
                page_icon.setMinSize(ContentPane.PAGES_ICON_BUTTON_WIDTH, ContentPane.PAGES_ICON_BUTTON_HEIGHT);
                page_icon_row.getChildren().add(page_icon);
                self.page_icon_buttons.add(page_icon);
            }
            self.page_icon_row_container.getChildren().add(page_icon_row);
            self.page_icon_rows.add(page_icon_row);
        }

        HBox page_icon_row = new HBox(ContentPane.PAGES_ICON_CONTAINER_HORIZONTAL_SPACE);
        for (int j = 0; j < self.document.getPageCount() % ContentPane.PAGES_ICON_BUTTON_COLUMN_NUM; j++) {
            ImageView img_view = new ImageView(self.document.getPageAtIndex(ContentPane.PAGES_ICON_BUTTON_COLUMN_NUM * row_num + j).getPreviewImage());
            img_view.setPreserveRatio(false);
            img_view.setSmooth(true);
            img_view.setFitWidth(ContentPane.PAGES_ICON_BUTTON_WIDTH);
            img_view.setFitHeight(ContentPane.PAGES_ICON_BUTTON_HEIGHT);
            
            Button page_icon = new Button("", img_view);
            page_icon.setId(ContentPane.PAGE_ICON_BUTTON_ID+ (ContentPane.PAGES_ICON_BUTTON_COLUMN_NUM * row_num + j));
            page_icon.setOnAction(self.controller);
            page_icon.setMaxSize(ContentPane.PAGES_ICON_BUTTON_WIDTH, ContentPane.PAGES_ICON_BUTTON_HEIGHT);
            page_icon.setMinSize(ContentPane.PAGES_ICON_BUTTON_WIDTH, ContentPane.PAGES_ICON_BUTTON_HEIGHT);

            page_icon_row.getChildren().add(page_icon);
            self.page_icon_buttons.add(page_icon);
        }
        Button new_page_icon = new Button("New");
        new_page_icon.setId(ContentPane.PAGE_ICON_BUTTON_NEW_ID);
        new_page_icon.setOnAction(self.controller);
        new_page_icon.setMaxSize(ContentPane.PAGES_ICON_BUTTON_WIDTH, ContentPane.PAGES_ICON_BUTTON_HEIGHT);
        new_page_icon.setMinSize(ContentPane.PAGES_ICON_BUTTON_WIDTH, ContentPane.PAGES_ICON_BUTTON_HEIGHT);

        page_icon_row.getChildren().add(new_page_icon);
        self.page_icon_buttons.add(new_page_icon);

        self.page_icon_row_container.getChildren().add(page_icon_row);
        self.page_icon_rows.add(page_icon_row);
    }

    public void onPreviewPaneClicked(Tag event_source) {
        self.inspector_box.getChildren().clear();
        
        Set<String> param_names = event_source.getParameterNames();
        Iterator<String> iterator = param_names.iterator();
        
        while(iterator.hasNext()){
            String name = iterator.next();
            VBox param_box = new VBox();
            param_box.setPrefWidth(self.inspector_box.getPrefWidth());
            event_source.getParameterEditor(param_box, name);
            self.inspector_box.getChildren().add(param_box);
        }
    }
}

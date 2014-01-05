/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cafe.control;

import java.util.HashMap;
import cafe.view.ContentPane;
import com.sun.javafx.scene.web.skin.ColorPicker;
import document.Document;
import document.Page;
import document.tags.ButtonTag;
import document.tags.ContentTag;
import document.tags.CouponTag;
import document.tags.DMMTag;
import document.tags.FooterTag;
import document.tags.HeaderTag;
import document.tags.KaiheiTag;
import document.tags.MapTag;
import document.tags.ParagraphTag;
import document.tags.ShareTag;
import document.tags.Tag;
import document.tags.TimelineTag;
import document.tags.TweetTag;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import static javafx.scene.input.DataFormat.URL;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author chibayuuki
 */
public class MainController implements EventHandler {

    private ContentPane root;
    private MainController self;
    private PageRenderer renderer;
    private Document document;
    private int currentPageIndex;
    private boolean existingTagMode;

    private String service_name;
    private String email;
    private final String page_register_url = "http://localhost:8080/Cappuccino/register_page";
    private final String register_url = "http://localhost:8080/Cappuccino/register";
    private final String image_register_url = "http://localhost:8080/Cappuccino/register_image";

    public MainController() {
        this.self = this;

        self.document = new Document();

        self.existingTagMode = false;
        
        if(service_name == null){
            this.register();
        }
    }

    public void setContentPane(ContentPane root) {
        self.root = root;
    }

    @Override
    public void handle(Event t) {
        EventType type = t.getEventType();
        if (type.equals(ActionEvent.ACTION)) {
            self.handleActionEvent((ActionEvent) t);
        }
    }

    public void setRenderer(PageRenderer renderer) {
        self.renderer = renderer;
        self.renderer.setHandler(self);
        self.document.setListener(self.renderer);
    }

    private void handleActionEvent(ActionEvent actionEvent) {
        Control source = (Control) actionEvent.getSource();
        String id = source.getId();

        if (id.startsWith(ContentPane.PAGE_ICON_BUTTON_ID)) {
            int num = Integer.parseInt(id.substring(ContentPane.PAGE_ICON_BUTTON_ID.length(), id.length()));
            self.changeCurrentPageIndex(num);
        } else {
            switch (id) {
                case ContentPane.MENU_ICON_NEW_ID:
                    break;
                case ContentPane.MENU_ICON_OPEN_ID:
                    break;
                case ContentPane.MENU_ICON_SAVE_ID:
                    break;
                case ContentPane.MENU_ICON_SAVE_AS_ID:
                    break;
                case ContentPane.MENU_ICON_COMMIT_ID:
                    self.commit();
                    break;
                case ContentPane.PAGE_ICON_BUTTON_NEW_ID:
                    int index = self.addNewPage("untitled");
                    self.changeCurrentPageIndex(index);
                    self.root.setDocument(self.document);
                    break;
            }
        }
    }

    public void itemDropped(String id, int index) {
        Tag newTag = null;
        switch (id) {
            case Tag.HEADER:
                newTag = new HeaderTag();
                if (index == PageRenderer.TO_ADD_LAST_OF_LIST) {
                    self.document.getPageAtIndex(self.currentPageIndex).addTag(newTag);
                } else {
                    self.document.getPageAtIndex(self.currentPageIndex).insertTag(newTag, index);
                }
                break;
            case Tag.FOOTER:
                newTag = new FooterTag();
                if (index == PageRenderer.TO_ADD_LAST_OF_LIST) {
                    self.document.getPageAtIndex(self.currentPageIndex).addTag(newTag);
                } else {
                    self.document.getPageAtIndex(self.currentPageIndex).insertTag(newTag, index);
                }
                break;
            case Tag.CONTENT:
                newTag = new ContentTag();
                if (index == PageRenderer.TO_ADD_LAST_OF_LIST) {
                    self.document.getPageAtIndex(self.currentPageIndex).addTag(newTag);
                } else {
                    self.document.getPageAtIndex(self.currentPageIndex).insertTag(newTag, index);
                }
                break;
            case Tag.PARAGRAPH:
                newTag = new ParagraphTag();
                if (index == PageRenderer.TO_ADD_LAST_OF_LIST) {
                    self.document.getPageAtIndex(self.currentPageIndex).addTag(newTag);
                } else {
                    self.document.getPageAtIndex(self.currentPageIndex).insertTag(newTag, index);
                }
                break;
            case Tag.BUTTON:
                newTag = new ButtonTag();
                if (index == PageRenderer.TO_ADD_LAST_OF_LIST) {
                    self.document.getPageAtIndex(self.currentPageIndex).addTag(newTag);
                } else {
                    self.document.getPageAtIndex(self.currentPageIndex).insertTag(newTag, index);
                }
                break;
            case Tag.COUPON:
                newTag = new CouponTag();
                if (index == PageRenderer.TO_ADD_LAST_OF_LIST) {
                    self.document.getPageAtIndex(self.currentPageIndex).addTag(newTag);
                } else {
                    self.document.getPageAtIndex(self.currentPageIndex).insertTag(newTag, index);
                }
                break;
            case Tag.DMM:
                newTag = new DMMTag();
                if (index == PageRenderer.TO_ADD_LAST_OF_LIST) {
                    self.document.getPageAtIndex(self.currentPageIndex).addTag(newTag);
                } else {
                    self.document.getPageAtIndex(self.currentPageIndex).insertTag(newTag, index);
                }
                break;
            case Tag.SHARE:
                newTag = new ShareTag();
                if (index == PageRenderer.TO_ADD_LAST_OF_LIST) {
                    self.document.getPageAtIndex(self.currentPageIndex).addTag(newTag);
                } else {
                    self.document.getPageAtIndex(self.currentPageIndex).insertTag(newTag, index);
                }
                break;
            case Tag.TIMELINE:
                newTag = new TimelineTag();
                if (index == PageRenderer.TO_ADD_LAST_OF_LIST) {
                    self.document.getPageAtIndex(self.currentPageIndex).addTag(newTag);
                } else {
                    self.document.getPageAtIndex(self.currentPageIndex).insertTag(newTag, index);
                }
                break;
            case Tag.TWEET:
                newTag = new TweetTag();
                if (index == PageRenderer.TO_ADD_LAST_OF_LIST) {
                    self.document.getPageAtIndex(self.currentPageIndex).addTag(newTag);
                } else {
                    self.document.getPageAtIndex(self.currentPageIndex).insertTag(newTag, index);
                }
                break;
            case Tag.MAP:
                newTag = new MapTag();
                if (index == PageRenderer.TO_ADD_LAST_OF_LIST) {
                    self.document.getPageAtIndex(self.currentPageIndex).addTag(newTag);
                } else {
                    self.document.getPageAtIndex(self.currentPageIndex).insertTag(newTag, index);
                }
                break;
            case Tag.KAIHEI:
                newTag = new KaiheiTag();
                if (index == PageRenderer.TO_ADD_LAST_OF_LIST) {
                    self.document.getPageAtIndex(self.currentPageIndex).addTag(newTag);
                } else {
                    self.document.getPageAtIndex(self.currentPageIndex).insertTag(newTag, index);
                }
                break;

        }
    }

    public void start() {
        self.addNewPage("top");

        self.changeCurrentPageIndex(0);

        self.root.setDocument(self.document);
    }

    private void changeCurrentPageIndex(int num) {
        self.currentPageIndex = num;

        self.renderer.setPage(self.document.getPageAtIndex(self.currentPageIndex));
    }

    private int addNewPage(String title) {
        Page page = new Page();
        page.setName(title);
        document.addPage(page);
        Tag tag_header = new ContentTag();
        page.addTag(tag_header);

        return document.getPageCount() - 1;
    }

    public void onPreviewPaneClicked(Tag event_source) {
        self.root.onPreviewPaneClicked(event_source);
    }

    public void setExistingMode(boolean mode) {
        self.existingTagMode = mode;
    }

    private void commit() {
        HashMap<String, String> htmls = self.document.generateHTMLs();
        for (String name : htmls.keySet()) {
            String html = htmls.get(name);
            System.out.println(html);
            sendHTML(name, html);
        }
        for (String html : htmls.values()) {
            System.out.println("\n----------------------html-------------------------\n\n" + html + "\n\n\n");
        }

    }

    private void sendHTML(String name, String html) {
        
        HttpClient client = new DefaultHttpClient();

        try {
            System.out.println("try to connect using http protocol");
            ArrayList<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("service_name", service_name));
            pairs.add(new BasicNameValuePair("email", email));
            pairs.add(new BasicNameValuePair("html", html));
            pairs.add(new BasicNameValuePair("name", name));

            HttpPost post = new HttpPost(new URL(page_register_url).toURI());
            post.setEntity(new UrlEncodedFormEntity(pairs));
            //post data and get response
            HttpResponse response = client.execute(post);
            int status = response.getStatusLine().getStatusCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            while((line = reader.readLine())!=null){
                System.out.println(line);
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void register() {
        Stage registerStage = new Stage();
        StackPane pane = new StackPane();
        VBox box = new VBox(10);
        Label email_label = new Label("メールアドレス");
        final TextField email_field = new TextField();
        Label name_label = new Label("ウェブサイト名");
        final TextField name_field = new TextField();
        Button submit = new Button("登録");
        submit.setId("submit");
        
        box.getChildren().addAll(email_label, email_field, name_label, name_field, submit);
        pane.getChildren().add(box);
        
        Scene scene = new Scene(pane, 200, 200);
        registerStage.setScene(scene);
        registerStage.show();
        submit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                HttpClient client = new DefaultHttpClient();
        try {
            System.out.println("try to connect using http protocol");

            ArrayList<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("service_name", name_field.getText()));
            pairs.add(new BasicNameValuePair("email", email_field.getText()));
            service_name = name_field.getText();
            email = email_field.getText();

            HttpPost post = new HttpPost(new URL(register_url).toURI());
            post.setEntity(new UrlEncodedFormEntity(pairs));
            //post data and get response
            HttpResponse response = client.execute(post);
            int status = response.getStatusLine().getStatusCode();
            System.out.println(status);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            while((line = reader.readLine())!=null){
                System.out.println(line);
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
            }
        });
        
        
    }

}

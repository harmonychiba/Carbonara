/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cafe.control;

import java.util.HashMap;
import cafe.view.ContentPane;
import document.Document;
import document.Page;
import document.tags.ButtonTag;
import document.tags.ContentTag;
import document.tags.CouponTag;
import document.tags.FooterTag;
import document.tags.HeaderTag;
import document.tags.ParagraphTag;
import document.tags.Tag;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Control;

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

    public MainController() {
        this.self = this;

        self.document = new Document();
        
        self.existingTagMode = false;
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
                if (index == PageRenderer.TO_ADD_LAST_OF_LIST){
                    self.document.getPageAtIndex(self.currentPageIndex).addTag(newTag);
                } else {
                    self.document.getPageAtIndex(self.currentPageIndex).insertTag(newTag, index);
                }
                break;
            case Tag.BUTTON:
                newTag = new ButtonTag();
                if (index == PageRenderer.TO_ADD_LAST_OF_LIST){
                    self.document.getPageAtIndex(self.currentPageIndex).addTag(newTag);
                } else {
                    self.document.getPageAtIndex(self.currentPageIndex).insertTag(newTag, index);
                }
                break;
            case Tag.COUPON:
                newTag = new CouponTag();
                if (index == PageRenderer.TO_ADD_LAST_OF_LIST){
                    self.document.getPageAtIndex(self.currentPageIndex).addTag(newTag);
                } else {
                    self.document.getPageAtIndex(self.currentPageIndex).insertTag(newTag, index);
                }
                break;
            case Tag.SHARE:
                newTag = new CouponTag();
                if (index == PageRenderer.TO_ADD_LAST_OF_LIST){
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
    public void setExistingMode(boolean mode){
        self.existingTagMode = mode;
    }

    private void commit() {
        HashMap<String,String> htmls = self.document.generateHTMLs();
        for(String html:htmls.values()){
            System.out.println("\n----------------------html-------------------------\n\n"+html+"\n\n\n");
        }
    }
}

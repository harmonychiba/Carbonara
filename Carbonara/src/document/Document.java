/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package document;

import java.util.HashMap;
import cafe.control.TagDataChangeEvent;
import cafe.control.TagDataChangeListener;
import java.util.ArrayList;
import javafx.event.Event;

/**
 *
 * @author chibayuuki
 */
public class Document {
    public final Document self;
    ArrayList<Page> pages;
    
    private TagDataChangeListener listener;
    
    public Document(){
        self = this;
        self.pages = new ArrayList<>();
        
    }
    public void addPage(Page page){
        self.pushEvent();
        page.setListener(self.listener);
        self.pages.add(page);
    }
    
    public void insertPageAtIndex(Page page,int index){
        self.pushEvent();
        page.setListener(self.listener);
        self.pages.add(index, page);
    }
    
    public void setPageAtIndex(Page page,int index){
        self.pushEvent();
        page.setListener(self.listener);
        self.pages.set(index, page);
    }
    
    public Page getPageAtIndex(int index){
        return self.pages.get(index);
    }
    public void setListener(TagDataChangeListener listener){
        self.listener = listener;
    }

    private void pushEvent() {
        if(self.listener != null){
            self.listener.handle(new TagDataChangeEvent(this, Event.NULL_SOURCE_TARGET, TagDataChangeEvent.TAG_DATA_CHANGE_EVENT));
        }
    }

    public int getPageCount() {
        return self.pages.size();
    }
    
    public HashMap<String,String> generateHTMLs(){
        HashMap<String,String> htmls = new HashMap<>();
        for(Page page:self.pages){
            htmls.put(page.getName(), page.generateHTML());
            System.out.println(page.generateHTML());
        }
        return htmls;
    }
}

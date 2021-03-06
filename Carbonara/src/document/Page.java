/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package document;

import cafe.control.TagDataChangeEvent;
import cafe.control.TagDataChangeListener;
import document.tags.Tag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import javafx.event.Event;
import javafx.scene.image.WritableImage;

/**
 *
 * @author chibayuuki
 */
public class Page {
    public final Page self;
    private ArrayList<Tag> tags;
    
    private String name;
    
    private TagDataChangeListener listener;
    
    private HashMap<String, String> parameters;
    private WritableImage preview;
    
    public Page(){
        self = this;
        self.tags = new ArrayList<>();
        
        self.parameters = new HashMap<>();
    }
    
    public void setName(String name){
        self.name = name;
    }
    
    public String getName(){
        return self.name;
    }
    
    public void addTag(Tag tag){
        self.tags.add(tag);
        tag.setPage(self);
        tag.setListener(self.listener);
        self.pushEvent();
    }
    public void insertTag(Tag tag,int index){
        self.tags.add(index, tag);
        tag.setPage(self);
        tag.setListener(self.listener);
        self.pushEvent();
    }
    public void removeTag(Tag tag){
        self.tags.remove(tag);
    }
    public ArrayList<Tag> getTags(){
        return self.tags;
    }
    public void setParameter(String name, String value) {
        parameters.put(name, value);
        self.pushEvent();
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }

    public Set<String> getParameterNames() {
        return parameters.keySet();
    }
    
    public void setListener(TagDataChangeListener listener) {
        self.listener = listener;
    }

    private void pushEvent() {
        if(self.listener != null){
            self.listener.handle(new TagDataChangeEvent(this, Event.NULL_SOURCE_TARGET, TagDataChangeEvent.TAG_DATA_CHANGE_EVENT));
        }
    }

    public WritableImage getPreviewImage() {
        return self.preview;
    }

    public void initPreviewImage(WritableImage snapshot) {
        self.preview = snapshot;
        System.out.println("page_preview is initialized");
    }
    public String generateHTML(){
        String html = "<!DOCTYPE html>\n" +
                                "<html>\n" +
                                    "<head>\n" +
                                        "<title>"+self.name+"</title>\n" +
                                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                                        "<link rel=\"stylesheet\" href=\"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css\" />\n" +
                                        "<script src=\"http://code.jquery.com/jquery-1.9.1.min.js\"></script>\n" +
                                        "<script src=\"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js\"></script>\n" +
                                    "</head>\n" +
                                    "<body>\n" +
                                        "<div data-role=\"page\" id = "+self.name+">\n";
        
        for(Tag tag:self.tags){
            html+=tag.generateHTML();
        }
        
        html+="</div><!-- /page -->\n" +
                "</body>\n" +
            "</html>";
        return html;
    }
}

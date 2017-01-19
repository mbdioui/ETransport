package Beans;

/**
 * Created by mohamed salah on 19/01/2017.
 */

public class Image {
    private String description;
    private String link;

    public Image(String link) {
        this.link = link;
    }

    public Image(String description, String link) {

        this.description = description;
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

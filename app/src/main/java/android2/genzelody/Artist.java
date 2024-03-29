package android2.genzelody;

public class Artist {
    private String id;
    private String name;
    private String image;


    private String followers;

    public Artist(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Artist(String id, String name, String image, String followers) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.followers = followers;
    }

    public Artist(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }
}

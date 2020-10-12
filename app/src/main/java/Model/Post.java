package Model;

public class Post {

    private String imageUrl;
    private String postId;
    private String publisher;
    private String title;
    private String article;

    public Post() {
    }

    public Post(String imageUrl, String postId, String publisher, String title, String article) {
        this.imageUrl = imageUrl;
        this.postId = postId;
        this.publisher = publisher;
        this.title = title;
        this.article = article;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }
}

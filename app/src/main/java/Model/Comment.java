package Model;

public class Comment {
    private String publisher;
    private String comment;

    public Comment() {
    }

    public Comment(String publisher, String comment) {
        this.publisher = publisher;
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

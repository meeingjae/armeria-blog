package dto;

import com.linecorp.armeria.server.annotation.Default;
import com.linecorp.armeria.server.annotation.Param;

public final class BlogPost {
    @Param("id")
    private final int id;
    @Param("title")
    @Default("Default Title")
    private final String title;
    @Param("content")
    @Default("Default content")
    private final String content;
    @Param("createdAt")
    private final long createdAt;
    @Param("modifiedAt")
    private final long modifiedAt;

    public BlogPost(int id, String title, String content) {
        this(id, title, content, System.currentTimeMillis());
    }

    public BlogPost(int id, String title, String content, long createdAt) {
        this(id, title, content, createdAt, createdAt);
    }

    public BlogPost(int id, String title, String content, long createdAt, long modifiedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public int getId() { return id; }

    public String getTitle() { return title; }

    public String getContent() { return content; }

    public long getCreatedAt() { return createdAt; }

    public long getModifiedAt() { return modifiedAt; }
}

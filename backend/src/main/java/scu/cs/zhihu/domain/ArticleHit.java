package scu.cs.zhihu.domain;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class ArticleHit {
    private Long articleId;
    private String title;
    private String urlToken;
    private String authorId;
    private String authorName;
    private String excerpt;
    private Integer voteupCount;
    private Integer commentCount;
    private Integer versionNo;
    private OffsetDateTime capturedAt;
    private String tags;
    private Double score;
}

package scu.cs.zhihu.domain;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class ArticleDetail {
    private Long articleId;
    private String title;
    private String urlToken;
    private String authorId;
    private String authorName;
    private String authorHeadline;
    private OffsetDateTime publishTime;
    private OffsetDateTime firstSeenAt;
    private OffsetDateTime lastSeenAt;
    private Integer latestVersionNo;
    private Integer voteupCount;
    private Integer commentCount;
    private OffsetDateTime capturedAt;
    private String excerpt;
    private String content;
    private String contentHtml;
    private Integer contentLen;
    private String tags;
}

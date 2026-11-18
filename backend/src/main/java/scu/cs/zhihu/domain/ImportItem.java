package scu.cs.zhihu.domain;

import lombok.Data;

@Data
public class ImportItem {
    private String urlToken;
    private String authorId;
    private String authorName;
    private String authorHeadline;
    private String title;
    private String excerpt;
    private String content;
    private String contentHtml;
    private Integer voteupCount;
    private Integer commentCount;
    private String capturedAt;
}

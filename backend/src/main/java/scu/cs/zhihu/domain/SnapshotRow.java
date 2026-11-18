package scu.cs.zhihu.domain;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class SnapshotRow {
    private Long articleId;
    private Integer versionNo;
    private OffsetDateTime capturedAt;
    private String title;
    private String excerpt;
    private String content;
    private String contentHtml;
    private Integer voteupCount;
    private Integer commentCount;
}

package scu.cs.zhihu.domain;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class VersionRow {
    private Integer versionNo;
    private OffsetDateTime capturedAt;
    private Integer voteupCount;
    private Integer voteupDelta;
    private Integer commentCount;
    private String excerpt;
}

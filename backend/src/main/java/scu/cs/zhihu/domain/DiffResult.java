package scu.cs.zhihu.domain;

import lombok.Data;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class DiffResult {
    private Integer fromVersion;
    private Integer toVersion;
    private OffsetDateTime fromCapturedAt;
    private OffsetDateTime toCapturedAt;
    private Integer voteupDelta;
    private Integer commentDelta;
    private int addedLines;
    private int removedLines;
    private List<DiffSegment> segments;
}

package scu.cs.zhihu.domain;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class BatchRow {
    private Long batchId;
    private String sourceNote;
    private OffsetDateTime startedAt;
    private OffsetDateTime finishedAt;
    private String status;
    private Integer fetchedCnt;
    private Integer insertedCnt;
    private Integer updatedCnt;
    private Integer snapshotCnt;
}

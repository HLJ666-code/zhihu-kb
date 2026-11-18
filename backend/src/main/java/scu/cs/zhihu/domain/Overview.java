package scu.cs.zhihu.domain;

import lombok.Data;

@Data
public class Overview {
    private int tableCount;
    private int viewCount;
    private int triggerCount;
    private int indexCount;
    private int articleCount;
    private int snapshotCount;
    private int authorCount;
    private int batchCount;
}

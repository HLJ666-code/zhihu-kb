package scu.cs.zhihu.domain;

import lombok.Data;

@Data
public class GapRow {
    private String keyword;
    private String executedAt;
    private int resultCnt;
}

package scu.cs.zhihu.domain;

import lombok.Data;

@Data
public class ImportResult {
    private int received;
    private int inserted;
    private int skipped;
    private int failed;
    private Long batchId;
}

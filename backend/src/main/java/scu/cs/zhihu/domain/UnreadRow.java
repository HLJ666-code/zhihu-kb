package scu.cs.zhihu.domain;

import lombok.Data;

@Data
public class UnreadRow {
    private Long articleId;
    private String title;
    private String authorName;
}

package scu.cs.zhihu.common;

import lombok.Data;
import scu.cs.zhihu.domain.ArticleHit;
import java.util.List;

@Data
public class SearchResponse {
    private long taskId;
    private String keyword;
    private int tookMs;
    private int count;
    private boolean gap;
    private List<ArticleHit> hits;
}

package scu.cs.zhihu.domain;

import lombok.Data;

@Data
public class RankRow {
    private String authorId;
    private String name;
    private Integer articleCnt;
    private Long totalVoteup;
    private Double avgVoteup;
    private Long voteupRank;
}

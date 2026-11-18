package scu.cs.zhihu.domain;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AuthorSeries {
    private LocalDate statDate;
    private Integer followerCnt;
    private Integer totalVoteup;
}

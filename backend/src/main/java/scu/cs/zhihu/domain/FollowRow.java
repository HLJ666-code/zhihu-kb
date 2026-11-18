package scu.cs.zhihu.domain;

import lombok.Data;

@Data
public class FollowRow {
    private String authorId;
    private String authorName;
    private String headline;
    private String followAt;
    private String unfollowAt;
}

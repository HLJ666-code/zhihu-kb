package scu.cs.zhihu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import scu.cs.zhihu.domain.*;
import java.util.List;

@Mapper
public interface AuthorMapper {
    List<RankRow> rank();
    List<AuthorSeries> series(@Param("authorId") String authorId);
    List<ArticleHit> articles(@Param("authorId") String authorId);
    List<FollowRow> follows(@Param("userId") long userId);
    void follow(@Param("userId") long userId, @Param("authorId") String authorId);
    void unfollow(@Param("userId") long userId, @Param("authorId") String authorId);
}

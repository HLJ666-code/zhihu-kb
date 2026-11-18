package scu.cs.zhihu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import scu.cs.zhihu.domain.*;
import java.util.List;

@Mapper
public interface UserMapper {
    List<NoteRow> notes(@Param("userId") long userId);
    void addNote(@Param("userId") long userId, @Param("articleId") Long articleId, @Param("noteText") String noteText);
    List<UnreadRow> unread(@Param("userId") long userId);
    List<GapRow> searchHistory(@Param("userId") long userId, @Param("onlyGap") boolean onlyGap);
    Long insertSearchTask(@Param("userId") long userId, @Param("keyword") String keyword, @Param("durationMs") int durationMs, @Param("resultCnt") int resultCnt);
    void insertSearchResult(@Param("taskId") Long taskId, @Param("articleId") Long articleId, @Param("rankNo") int rankNo, @Param("score") double score);
}

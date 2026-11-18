package scu.cs.zhihu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import scu.cs.zhihu.domain.*;
import java.util.List;

@Mapper
public interface ArticleMapper {
    List<ArticleHit> search(@Param("keyword") String keyword, @Param("tagId") Integer tagId);
    ArticleDetail detail(@Param("articleId") Long articleId);
    List<VersionRow> versions(@Param("articleId") Long articleId);
    SnapshotRow versionSnapshot(@Param("articleId") Long articleId, @Param("versionNo") int versionNo);
    int countSearchHit(@Param("articleId") Long articleId);
}

package scu.cs.zhihu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import scu.cs.zhihu.domain.ImportItem;

@Mapper
public interface ImportMapper {
    Long createBatch(@Param("sourceNote") String sourceNote);
    int insertAuthorIfNotExists(@Param("item") ImportItem item);
    int insertArticleIfNotExists(@Param("item") ImportItem item);
    Long countArticleByToken(@Param("urlToken") String urlToken);
    Long insertSnapshot(@Param("item") ImportItem item, @Param("batchId") Long batchId, @Param("versionNo") int versionNo);
    int countSnapshotByArticle(@Param("articleId") Long articleId);
    void closeBatchSuccess(@Param("batchId") Long batchId);
    void closeBatchFailed(@Param("batchId") Long batchId);
}

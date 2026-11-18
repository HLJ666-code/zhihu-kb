package scu.cs.zhihu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scu.cs.zhihu.domain.ImportItem;
import scu.cs.zhihu.domain.ImportResult;
import scu.cs.zhihu.mapper.ImportMapper;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final ImportMapper importMapper;

    @Transactional
    public ImportResult importItems(List<ImportItem> items, String sourceNote) {
        Long batchId = importMapper.createBatch(sourceNote);
        int inserted = 0, skipped = 0, failed = 0;
        try {
            for (ImportItem item : items) {
                try {
                    importMapper.insertAuthorIfNotExists(item);
                    importMapper.insertArticleIfNotExists(item);
                    Long articleId = importMapper.countArticleByToken(item.getUrlToken());
                    int versionNo = importMapper.countSnapshotByArticle(articleId) + 1;
                    importMapper.insertSnapshot(item, batchId, versionNo);
                    inserted++;
                } catch (Exception e) {
                    skipped++;
                }
            }
            importMapper.closeBatchSuccess(batchId);
        } catch (Exception e) {
            importMapper.closeBatchFailed(batchId);
            throw e;
        }
        ImportResult r = new ImportResult();
        r.setReceived(items.size());
        r.setInserted(inserted);
        r.setSkipped(skipped);
        r.setFailed(failed);
        r.setBatchId(batchId);
        return r;
    }
}

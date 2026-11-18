package scu.cs.zhihu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import scu.cs.zhihu.common.SearchResponse;
import scu.cs.zhihu.domain.*;
import scu.cs.zhihu.mapper.ArticleMapper;
import scu.cs.zhihu.mapper.UserMapper;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleMapper articleMapper;
    private final UserMapper userMapper;

    public SearchResponse search(String keyword, Integer tagId, long userId) {
        long start = System.currentTimeMillis();
        List<ArticleHit> hits = articleMapper.search(keyword, tagId);
        long took = System.currentTimeMillis() - start;
        Long taskId = userMapper.insertSearchTask(userId, keyword, (int) took, hits.size());
        for (int i = 0; i < hits.size(); i++) {
            userMapper.insertSearchResult(taskId, hits.get(i).getArticleId(), i + 1, hits.get(i).getScore() == null ? 0.9 : hits.get(i).getScore());
        }
        SearchResponse resp = new SearchResponse();
        resp.setTaskId(taskId);
        resp.setKeyword(keyword);
        resp.setTookMs((int) took);
        resp.setCount(hits.size());
        resp.setGap(hits.isEmpty());
        resp.setHits(hits);
        return resp;
    }

    public ArticleDetail detail(long id) { return articleMapper.detail(id); }
    public List<VersionRow> versions(long id) { return articleMapper.versions(id); }
    public SnapshotRow versionSnapshot(long id, int versionNo) { return articleMapper.versionSnapshot(id, versionNo); }

    public DiffResult diff(long id, int from, int to) {
        SnapshotRow old = articleMapper.versionSnapshot(id, from);
        SnapshotRow now = articleMapper.versionSnapshot(id, to);
        DiffResult r = new DiffResult();
        r.setFromVersion(from);
        r.setToVersion(to);
        r.setFromCapturedAt(old.getCapturedAt());
        r.setToCapturedAt(now.getCapturedAt());
        r.setVoteupDelta(now.getVoteupCount() - old.getVoteupCount());
        r.setCommentDelta(now.getCommentCount() - old.getCommentCount());
        List<DiffSegment> segs = new ArrayList<>();
        String[] oldLines = old.getContent() == null ? new String[0] : old.getContent().split("\n");
        String[] newLines = now.getContent() == null ? new String[0] : now.getContent().split("\n");
        Set<String> oldSet = new HashSet<>(Arrays.asList(oldLines));
        Set<String> newSet = new HashSet<>(Arrays.asList(newLines));
        int added = 0, removed = 0;
        for (String l : newLines) { if (!oldSet.contains(l)) { segs.add(seg("add", l)); added++; } }
        for (String l : oldLines) { if (!newSet.contains(l)) { segs.add(seg("del", l)); removed++; } }
        r.setAddedLines(added);
        r.setRemovedLines(removed);
        r.setSegments(segs);
        return r;
    }

    private DiffSegment seg(String type, String text) {
        DiffSegment s = new DiffSegment();
        s.setType(type);
        s.setText(text);
        return s;
    }
}

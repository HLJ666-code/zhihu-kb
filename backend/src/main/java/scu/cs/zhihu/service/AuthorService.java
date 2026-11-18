package scu.cs.zhihu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import scu.cs.zhihu.domain.*;
import scu.cs.zhihu.mapper.AuthorMapper;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorMapper authorMapper;
    public List<RankRow> rank() { return authorMapper.rank(); }
    public List<AuthorSeries> series(String authorId) { return authorMapper.series(authorId); }
    public List<ArticleHit> articles(String authorId) { return authorMapper.articles(authorId); }
    public List<FollowRow> follows(long userId) { return authorMapper.follows(userId); }
    public void follow(long userId, String authorId) { authorMapper.follow(userId, authorId); }
    public void unfollow(long userId, String authorId) { authorMapper.unfollow(userId, authorId); }
}

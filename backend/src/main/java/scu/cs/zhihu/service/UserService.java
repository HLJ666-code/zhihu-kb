package scu.cs.zhihu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import scu.cs.zhihu.domain.*;
import scu.cs.zhihu.mapper.UserMapper;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    public List<NoteRow> notes(long userId) { return userMapper.notes(userId); }
    public void addNote(long userId, Long articleId, String noteText) { userMapper.addNote(userId, articleId, noteText); }
    public List<UnreadRow> unread(long userId) { return userMapper.unread(userId); }
    public List<GapRow> searchHistory(long userId, boolean onlyGap) { return userMapper.searchHistory(userId, onlyGap); }
    public void follow(long userId, String authorId) { userMapper.follow(userId, authorId); }
    public void unfollow(long userId, String authorId) { userMapper.unfollow(userId, authorId); }
}

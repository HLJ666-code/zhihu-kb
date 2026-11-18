package scu.cs.zhihu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import scu.cs.zhihu.common.ApiResponse;
import scu.cs.zhihu.common.NoteRequest;
import scu.cs.zhihu.domain.GapRow;
import scu.cs.zhihu.domain.NoteRow;
import scu.cs.zhihu.domain.UnreadRow;
import scu.cs.zhihu.service.UserService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/notes")
    public ApiResponse<List<NoteRow>> notes(@RequestParam(defaultValue = "1") long userId) { return ApiResponse.ok(userService.notes(userId)); }
    @PostMapping("/notes")
    public ApiResponse<Map<String, Object>> addNote(@RequestBody NoteRequest req) {
        userService.addNote(req.getUserId() == null ? 1L : req.getUserId(), req.getArticleId(), req.getNoteText());
        return ApiResponse.ok(Map.of("saved", true));
    }
    @GetMapping("/unread")
    public ApiResponse<List<UnreadRow>> unread(@RequestParam(defaultValue = "1") long userId) { return ApiResponse.ok(userService.unread(userId)); }
    @GetMapping("/search-history")
    public ApiResponse<List<GapRow>> history(@RequestParam(defaultValue = "1") long userId, @RequestParam(defaultValue = "false") boolean onlyGap) { return ApiResponse.ok(userService.searchHistory(userId, onlyGap)); }
    @PostMapping("/follows/{authorId}")
    public ApiResponse<Map<String, Object>> follow(@PathVariable String authorId, @RequestParam(defaultValue = "1") long userId) {
        userService.follow(userId, authorId);
        return ApiResponse.ok(Map.of("following", true, "authorId", authorId));
    }
    @DeleteMapping("/follows/{authorId}")
    public ApiResponse<Map<String, Object>> unfollow(@PathVariable String authorId, @RequestParam(defaultValue = "1") long userId) {
        userService.unfollow(userId, authorId);
        return ApiResponse.ok(Map.of("following", false, "authorId", authorId));
    }
}

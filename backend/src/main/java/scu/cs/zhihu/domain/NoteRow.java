package scu.cs.zhihu.domain;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class NoteRow {
    private Long articleId;
    private String title;
    private String noteText;
    private OffsetDateTime createdAt;
}

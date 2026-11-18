package scu.cs.zhihu.common;

import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> onConstraint(DataIntegrityViolationException e) {
        String detail = rootMessage(e);
        String friendly;
        if (detail.contains("uq_snap_hash")) {
            friendly = "内容未变化，这份快照被唯一约束 uq_snap_hash 拒绝（幂等去重）";
        } else if (detail.contains("exclusion constraint")) {
            friendly = "关注区间与已有记录重叠，被排他约束拒绝（时态数据不许自相矛盾）";
        } else if (detail.contains("uq_snap_batch_article")) {
            friendly = "同一采集批次内该文章已有快照，违反部分唯一索引";
        } else if (detail.contains("chk_batch_finish")) {
            friendly = "批次状态与结束时间不匹配，违反表内 CHECK 约束";
        } else {
            friendly = "数据库约束拒绝了本次写入：" + detail;
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.fail(friendly + " | 原始错误：" + detail));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> onOther(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(e.getMessage()));
    }

    private String rootMessage(Throwable e) {
        Throwable cur = e;
        while (cur.getCause() != null) cur = cur.getCause();
        if (cur instanceof PSQLException psql && psql.getServerErrorMessage() != null) {
            String msg = psql.getServerErrorMessage().getMessage();
            String detail = psql.getServerErrorMessage().getDetail();
            return detail == null ? msg : msg + " | " + detail;
        }
        return cur.getMessage() == null ? e.getMessage() : cur.getMessage();
    }
}

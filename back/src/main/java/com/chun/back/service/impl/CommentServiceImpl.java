package com.chun.back.service.impl;

import com.chun.back.mapper.CommentMapper;
import com.chun.back.mapper.PostMapper;
import com.chun.back.pojo.Comment;
import com.chun.back.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private PostMapper postMapper;

    @Override
    public List<Comment> listTree(Long postId) {
        List<Comment> all = commentMapper.selectByPostId(postId);

        // 处理删除占位（保留树结构）
        for (Comment c : all) {
            if (c.getIsDeleted() != null && c.getIsDeleted() == 1) {
                c.setContent("[该评论已删除]");
                c.setAuthor("已删除");
                c.setAuthorPic(null);
            }
            c.setReplies(new ArrayList<>());
        }

        Map<Long, Comment> map = new HashMap<>();
        for (Comment c : all) map.put(c.getId(), c);

        List<Comment> roots = new ArrayList<>();
        for (Comment c : all) {
            if (c.getParentId() == null || c.getParentId() == 0) {
                roots.add(c);
            } else {
                Comment parent = map.get(c.getParentId());
                if (parent != null) {
                    parent.getReplies().add(c);
                } else {
                    // 父评论不存在，兜底当作一级
                    roots.add(c);
                }
            }
        }
        return roots;
    }

    @Override
    public Long create(Long postId, Long userId, String content, Long parentId, Long replyUserId) {
        long pid = parentId == null ? 0L : parentId;

        Comment c = new Comment();
        c.setPostId(postId);
        c.setUserId(userId);
        c.setParentId(pid);
        c.setReplyUserId(replyUserId);

        long rootId = 0L;
        if (pid != 0L) {
            Comment parent = commentMapper.selectById(pid);
            if (parent != null) {
                if (parent.getRootId() != null && parent.getRootId() != 0L) {
                    rootId = parent.getRootId();
                } else {
                    rootId = parent.getId();
                }
                // 默认回复父评论作者
                if (replyUserId == null) {
                    c.setReplyUserId(parent.getUserId());
                }
            }
        }
        c.setRootId(rootId);
        c.setContent(content);

        commentMapper.insert(c);

        // 顶层评论 root_id = 0（按 schema 默认）
        postMapper.incReplyCount(postId);

        return c.getId();
    }

    @Override
    public boolean delete(Long commentId, Long userId) {
        // 先查 post_id，用于更新 reply_count
        Comment c = commentMapper.selectById(commentId);
        if (c == null) return false;

        int ok = commentMapper.softDelete(commentId, userId);
        if (ok > 0) {
            postMapper.decReplyCount(c.getPostId());
            return true;
        }
        return false;
    }
}

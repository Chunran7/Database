<template>
  <div class="post-detail-page container">
    <el-card v-if="post" class="post-card">
      <template #header>
        <div class="post-header">
          <h2>{{ post.title }}</h2>
          <div class="post-meta">
            <span class="author">作者: {{ post.author || '匿名用户' }}</span>
            <span class="time">发布时间: {{ formatTime(post.createTime) }}</span>
          </div>
        </div>
      </template>
      <div class="post-content">
        {{ post.content }}
      </div>
    </el-card>

    <!-- 评论区域 -->
    <div class="comments-section">
      <h3>评论 ({{ comments.length }})</h3>
      
      <!-- 回复表单 -->
      <el-card class="reply-form-card">
        <div class="reply-form-header">
          <h4>发表回复</h4>
        </div>
        <el-form :model="replyForm" ref="replyFormRef">
          <el-form-item prop="content">
            <el-input
              v-model="replyForm.content"
              type="textarea"
              :rows="4"
              placeholder="请输入您的回复..."
            />
          </el-form-item>
          <el-form-item>
            <el-button 
              type="primary" 
              @click="submitReply"
              :loading="replyLoading"
            >
              发表回复
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 评论列表 -->
      <div class="comments-list">
        <el-card 
          v-for="comment in comments" 
          :key="comment.id" 
          class="comment-card"
        >
          <div class="comment-header">
            <span class="comment-author">{{ comment.author || '匿名用户' }}</span>
            <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
          </div>
          <div class="comment-content">
            {{ comment.content }}
          </div>
          
          <!-- 回复特定评论的表单 -->
          <div class="reply-to-comment">
            <el-button 
              size="small" 
              type="primary" 
              link
              @click="showReplyToForm(comment)"
            >
              回复
            </el-button>
            
            <div v-if="replyToCommentId === comment.id" class="nested-reply-form">
              <el-form :model="nestedReplyForm" @submit.prevent="submitNestedReply(comment)">
                <el-form-item prop="content">
                  <el-input
                    v-model="nestedReplyForm.content"
                    type="textarea"
                    :rows="2"
                    placeholder="请输入您的回复..."
                  />
                </el-form-item>
                <el-form-item>
                  <el-button 
                    type="primary" 
                    size="small"
                    @click="submitNestedReply(comment)"
                    :loading="replyLoading"
                  >
                    提交回复
                  </el-button>
                  <el-button 
                    size="small" 
                    @click="cancelNestedReply"
                  >
                    取消
                  </el-button>
                </el-form-item>
              </el-form>
            </div>
          </div>

          <!-- 子评论 -->
          <div v-if="comment.replies && comment.replies.length" class="sub-comments">
            <el-card 
              v-for="subComment in comment.replies" 
              :key="subComment.id" 
              class="sub-comment-card"
            >
              <div class="sub-comment-header">
                <span class="sub-comment-author">{{ subComment.author || '匿名用户' }}</span>
                <span class="sub-comment-time">{{ formatTime(subComment.createTime) }}</span>
              </div>
              <div class="sub-comment-content">
                {{ subComment.content }}
              </div>
            </el-card>
          </div>
        </el-card>
        
        <div v-if="comments.length === 0" class="no-comments">
          暂无评论，快来发表第一条评论吧！
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

// 模拟数据
const route = useRoute()
const post = ref(null)
const comments = ref([])
const replyToCommentId = ref(null)

// 表单相关
const replyFormRef = ref(null)
const replyLoading = ref(false)

const replyForm = reactive({
  content: ''
})

const nestedReplyForm = reactive({
  content: ''
})

// 获取帖子详情
const loadPostDetail = async () => {
  try {
    // 模拟API调用
    // 实际应该使用类似 postService.getPostById(route.params.id) 的方法
    
    // 模拟数据
    post.value = {
      id: route.params.id,
      title: '示例帖子标题',
      content: '这是帖子的内容，用户可以在这里查看帖子的详细内容。这是一个示例帖子，用于演示如何展示帖子详情。',
      author: '张三',
      createTime: new Date(Date.now() - 24 * 60 * 60 * 1000).toISOString() // 24小时前
    }
    
    // 加载评论
    loadComments()
  } catch (error) {
    console.error('获取帖子详情失败:', error)
    ElMessage.error('获取帖子详情失败')
  }
}

// 获取评论列表
const loadComments = async () => {
  try {
    // 模拟API调用
    // 实际应该使用类似 postService.getCommentsByPostId(route.params.id) 的方法
    
    // 模拟数据
    comments.value = [
      {
        id: 1,
        content: '这是一个很好的观点，我同意你的看法。',
        author: '李四',
        createTime: new Date(Date.now() - 12 * 60 * 60 * 1000).toISOString(), // 12小时前
        replies: [
          {
            id: 11,
            content: '谢谢你的赞同，我也觉得这个想法很不错。',
            author: '张三',
            createTime: new Date(Date.now() - 11 * 60 * 60 * 1000).toISOString() // 11小时前
          }
        ]
      },
      {
        id: 2,
        content: '我对这个问题有一些不同的看法，我觉得...',
        author: '王五',
        createTime: new Date(Date.now() - 6 * 60 * 60 * 1000).toISOString() // 6小时前
      }
    ]
  } catch (error) {
    console.error('获取评论失败:', error)
    ElMessage.error('获取评论失败')
  }
}

// 发表回复（主贴）
const submitReply = async () => {
  if (!replyForm.content.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }

  try {
    replyLoading.value = true
    
    // 模拟API调用
    // 实际应该使用类似 postService.addComment({postId: route.params.id, content: replyForm.content}) 的方法
    
    // 模拟添加评论成功
    const newComment = {
      id: Date.now(), // 简单生成唯一ID
      content: replyForm.content,
      author: '当前用户',
      createTime: new Date().toISOString()
    }
    
    comments.value.unshift(newComment)
    replyForm.content = ''
    ElMessage.success('回复发表成功')
  } catch (error) {
    console.error('发表回复失败:', error)
    ElMessage.error('发表回复失败')
  } finally {
    replyLoading.value = false
  }
}

// 显示回复特定评论的表单
const showReplyToForm = (comment) => {
  replyToCommentId.value = comment.id
  nestedReplyForm.content = ''
}

// 取消回复特定评论
const cancelNestedReply = () => {
  replyToCommentId.value = null
  nestedReplyForm.content = ''
}

// 回复特定评论
const submitNestedReply = async (parentComment) => {
  if (!nestedReplyForm.content.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }

  try {
    replyLoading.value = true
    
    // 模拟API调用
    // 实际应该使用类似 postService.addComment({
    //   postId: route.params.id, 
    //   content: nestedReplyForm.content, 
    //   parentId: parentComment.id
    // }) 的方法
    
    // 模拟添加子评论成功
    const newSubComment = {
      id: Date.now(),
      content: nestedReplyForm.content,
      author: '当前用户',
      createTime: new Date().toISOString()
    }
    
    // 将子评论添加到对应的父评论中
    if (!parentComment.replies) {
      parentComment.replies = []
    }
    parentComment.replies.push(newSubComment)
    
    nestedReplyForm.content = ''
    replyToCommentId.value = null
    ElMessage.success('回复发表成功')
  } catch (error) {
    console.error('发表回复失败:', error)
    ElMessage.error('发表回复失败')
  } finally {
    replyLoading.value = false
  }
}

// 格式化时间显示
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date

  // 如果是当天发布的，显示小时数
  if (diff < 24 * 60 * 60 * 1000 && now.getDate() === date.getDate()) {
    const hours = Math.floor(diff / (60 * 60 * 1000))
    if (hours > 0) {
      return `${hours}小时前`
    } else {
      const minutes = Math.floor(diff / (60 * 1000))
      return `${minutes}分钟前`
    }
  }

  // 如果是当年发布的，显示月日
  if (now.getFullYear() === date.getFullYear()) {
    return `${date.getMonth() + 1}-${date.getDate()}`
  }

  // 跨年的显示年月日
  return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`
}

onMounted(() => {
  loadPostDetail()
})
</script>

<style lang="scss" scoped>
.post-detail-page {
  padding: 24px 40px;
  
  .post-card {
    margin-bottom: 24px;
    
    .post-header {
      h2 {
        margin: 0 0 12px 0;
        color: #333;
      }
      
      .post-meta {
        display: flex;
        gap: 20px;
        color: #666;
        font-size: 14px;
      }
    }
    
    .post-content {
      white-space: pre-wrap;
      line-height: 1.6;
      color: #444;
    }
  }
  
  .comments-section {
    h3 {
      margin: 0 0 16px 0;
      color: #333;
    }
    
    .reply-form-card {
      margin-bottom: 24px;
      
      .reply-form-header {
        margin-bottom: 16px;
        
        h4 {
          margin: 0;
          color: #333;
        }
      }
    }
    
    .comment-card {
      margin-bottom: 16px;
      
      .comment-header {
        display: flex;
        justify-content: space-between;
        margin-bottom: 12px;
        font-size: 14px;
        
        .comment-author {
          font-weight: bold;
          color: #333;
        }
        
        .comment-time {
          color: #999;
        }
      }
      
      .comment-content {
        margin-bottom: 12px;
        line-height: 1.5;
        color: #555;
      }
      
      .reply-to-comment {
        text-align: right;
        
        .nested-reply-form {
          margin-top: 12px;
        }
      }
      
      .sub-comments {
        margin-top: 16px;
        padding-left: 20px;
        
        .sub-comment-card {
          margin-bottom: 12px;
          
          .sub-comment-header {
            display: flex;
            justify-content: space-between;
            margin-bottom: 8px;
            font-size: 13px;
            
            .sub-comment-author {
              font-weight: bold;
              color: #555;
            }
            
            .sub-comment-time {
              color: #aaa;
            }
          }
          
          .sub-comment-content {
            font-size: 13px;
            color: #666;
            line-height: 1.4;
          }
        }
      }
    }
    
    .no-comments {
      text-align: center;
      color: #999;
      padding: 40px 0;
    }
  }
}
</style>
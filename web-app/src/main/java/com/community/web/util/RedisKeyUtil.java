package com.community.web.util;

// 用于生成Redis数据库储存的key
public class RedisKeyUtil {

    // key的分割符
    private static final String SPLIT = ":";
    // 实体赞的前缀
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    // 关注者
    private static final String PREFIX_FOLLOWEE = "followee";
    // 被关注者
    private static final String PREFIX_FOLLOWER = "follower";

    private static final String PREFIX_MYPOST = "mypost";
    private static final String PREFIX_MYREPLY = "myreply";

    private static final String PREFIX_KAPTCHA = "kaptcha";
    private static final String PREFIX_TICKET = "ticket";
    private static final String PREFIX_USER = "user";

    private static final String PREFIX_UV = "uv";
    private static final String PREFIX_DAU = "dau";

    private static final String PREFIX_POST = "post";


    // 某个实体的赞，通过这个key得到的value是set，内涵点赞者的信息
    // like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    // 某个用户的赞的数量
    // like:user:userId -> int
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    // 某个用户关注的实体
    // 使用关注时间排序
    // followee:userId:entityType -> zset(entityId,now)
    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    // 某个实体拥有的粉丝
    // 存储关注者id和关注时间
    // follower:entityType:entityId -> zset(userId,now)
    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }


    // 某个实体发布的帖子
    // 存储发布帖子的 id 和发布时间
    // myPost:userId:entityType -> zset(entityId,now)
    public static String getMyPostKey(int userId, int entityType) {
        return PREFIX_MYPOST + SPLIT + userId + SPLIT + entityType;
    }

    // 某个实体发布的帖子
    // 存储发布帖子的 id 和发布时间
    // myPost:userId:entityType -> zset(entityId,now)
    public static String getMyReplyKey(int userId, int entityType) {
        return PREFIX_MYREPLY + SPLIT + userId + SPLIT + entityType;
    }


    // 登录验证码
    // 访问登录页面时临时生成的凭证
    public static String getKaptchaKey(String owner) {
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    // 登录的凭证
    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    // 用户
    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
    }


    // 单日UV
    public static String getUVKey(String date) {
        return PREFIX_UV + SPLIT + date;
    }

    // 区间UV
    public static String getUVKey(String startDate, String endDate) {
        return PREFIX_UV + SPLIT + startDate + SPLIT + endDate;
    }

    // 单日活跃用户
    public static String getDAUKey(String date) {
        return PREFIX_DAU + SPLIT + date;
    }

    // 区间活跃用户
    public static String getDAUKey(String startDate, String endDate) {
        return PREFIX_DAU + SPLIT + startDate + SPLIT + endDate;
    }

    // 帖子分数
    public static String getPostScoreKey() {
        return PREFIX_POST + SPLIT + "score";
    }

}

package com.example.service;

import com.example.common.model.User;
import com.example.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ユーザー管理を行うサービスクラス
 */
@Service
public class UserService {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public UserService() {
        // 初期データを作成
        createUser("山田太郎", "yamada@example.com");
        createUser("佐藤花子", "sato@example.com");
        createUser("田中一郎", "tanaka@example.com");
    }

    /**
     * 新しいユーザーを作成
     */
    public User createUser(String name, String email) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("ユーザー名は必須です");
        }
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("メールアドレスは必須です");
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("メールアドレスの形式が正しくありません");
        }

        Long id = idGenerator.getAndIncrement();
        User user = new User(id, name.trim(), email.trim(), LocalDateTime.now(), LocalDateTime.now());
        users.put(id, user);

        return user;
    }

    /**
     * 全ユーザーを取得
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    /**
     * IDでユーザーを取得
     */
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    /**
     * 名前でユーザーを検索（部分一致）
     */
    public List<User> searchUsersByName(String name) {
        if (StringUtils.isBlank(name)) {
            return getAllUsers();
        }

        String searchTerm = name.toLowerCase().trim();
        return users.values().stream()
                .filter(user -> user.getName().toLowerCase().contains(searchTerm))
                .sorted(Comparator.comparing(User::getName))
                .toList();
    }

    /**
     * ユーザー情報を更新
     */
    public User updateUser(Long id, String name, String email) {
        User user = users.get(id);
        if (user == null) {
            throw new IllegalArgumentException("ユーザーが見つかりません: " + id);
        }

        if (!StringUtils.isBlank(name)) {
            user.setName(name.trim());
        }
        if (!StringUtils.isBlank(email)) {
            if (!isValidEmail(email)) {
                throw new IllegalArgumentException("メールアドレスの形式が正しくありません");
            }
            user.setEmail(email.trim());
        }

        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    /**
     * ユーザーを削除
     */
    public boolean deleteUser(Long id) {
        return users.remove(id) != null;
    }

    /**
     * ユーザー数を取得
     */
    public int getUserCount() {
        return users.size();
    }

    /**
     * 簡単なメールアドレス形式チェック
     */
    private boolean isValidEmail(String email) {
        return email != null &&
                email.contains("@") &&
                email.indexOf("@") > 0 &&
                email.indexOf("@") < email.length() - 1;
    }

    /**
     * ユーザー統計情報を取得
     */
    public Map<String, Object> getUserStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", getUserCount());
        stats.put("domains", getEmailDomains());
        return stats;
    }

    private Map<String, Integer> getEmailDomains() {
        Map<String, Integer> domains = new HashMap<>();
        users.values().forEach(user -> {
            String domain = user.getEmail().substring(user.getEmail().indexOf("@") + 1);
            domains.merge(domain, 1, Integer::sum);
        });
        return domains;
    }
}
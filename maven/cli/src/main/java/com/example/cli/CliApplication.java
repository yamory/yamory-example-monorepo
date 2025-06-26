package com.example.cli;

import com.example.common.model.User;
import com.example.common.util.StringUtils;
import com.example.service.UserService;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * CLIアプリケーションのメインクラス
 */
@Command(name = "user-cli", mixinStandardHelpOptions = true, version = "1.0.0", description = "ユーザー管理CLI アプリケーション")
public class CliApplication implements Callable<Integer> {

    private final UserService userService = new UserService();

    @Command(name = "list", description = "ユーザー一覧を表示")
    static class ListCommand implements Callable<Integer> {
        @Option(names = { "-s", "--search" }, description = "検索キーワード")
        private String search;

        private final UserService userService = new UserService();

        @Override
        public Integer call() {
            List<User> users;
            if (!StringUtils.isBlank(search)) {
                users = userService.searchUsersByName(search);
                System.out.println("検索結果 ('" + search + "'): " + users.size() + "件");
            } else {
                users = userService.getAllUsers();
                System.out.println("全ユーザー: " + users.size() + "件");
            }

            System.out.println();
            System.out.printf("%-4s %-20s %-30s %-20s%n", "ID", "名前", "メールアドレス", "作成日時");
            System.out.println("-".repeat(80));

            for (User user : users) {
                System.out.printf("%-4d %-20s %-30s %-20s%n",
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getCreatedAt().toLocalDate().toString());
            }

            return 0;
        }
    }

    @Command(name = "show", description = "ユーザー詳細を表示")
    static class ShowCommand implements Callable<Integer> {
        @Parameters(index = "0", description = "ユーザーID")
        private Long id;

        private final UserService userService = new UserService();

        @Override
        public Integer call() {
            Optional<User> userOpt = userService.getUserById(id);
            if (userOpt.isEmpty()) {
                System.err.println("ユーザーが見つかりません: " + id);
                return 1;
            }

            User user = userOpt.get();
            System.out.println("ユーザー詳細:");
            System.out.println("  ID: " + user.getId());
            System.out.println("  名前: " + user.getName());
            System.out.println("  メールアドレス: " + user.getEmail());
            System.out.println("  作成日時: " + user.getCreatedAt());
            System.out.println("  更新日時: " + user.getUpdatedAt());

            return 0;
        }
    }

    @Command(name = "create", description = "新しいユーザーを作成")
    static class CreateCommand implements Callable<Integer> {
        @Option(names = { "-n", "--name" }, required = true, description = "ユーザー名")
        private String name;

        @Option(names = { "-e", "--email" }, required = true, description = "メールアドレス")
        private String email;

        private final UserService userService = new UserService();

        @Override
        public Integer call() {
            try {
                User user = userService.createUser(name, email);
                System.out.println("ユーザーを作成しました:");
                System.out.println("  ID: " + user.getId());
                System.out.println("  名前: " + user.getName());
                System.out.println("  メールアドレス: " + user.getEmail());
                return 0;
            } catch (Exception e) {
                System.err.println("エラー: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "delete", description = "ユーザーを削除")
    static class DeleteCommand implements Callable<Integer> {
        @Parameters(index = "0", description = "ユーザーID")
        private Long id;

        private final UserService userService = new UserService();

        @Override
        public Integer call() {
            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                System.out.println("ユーザーを削除しました: " + id);
                return 0;
            } else {
                System.err.println("ユーザーが見つかりません: " + id);
                return 1;
            }
        }
    }

    @Command(name = "stats", description = "ユーザー統計を表示")
    static class StatsCommand implements Callable<Integer> {
        private final UserService userService = new UserService();

        @Override
        @SuppressWarnings("unchecked")
        public Integer call() {
            Map<String, Object> stats = userService.getUserStatistics();
            System.out.println("ユーザー統計:");
            System.out.println("  総ユーザー数: " + stats.get("totalUsers"));
            System.out.println("  ドメイン別ユーザー数:");

            Map<String, Integer> domains = (Map<String, Integer>) stats.get("domains");
            domains.forEach((domain, count) -> System.out.println("    " + domain + ": " + count + "人"));

            return 0;
        }
    }

    @Override
    public Integer call() {
        System.out.println("ユーザー管理CLI アプリケーション");
        System.out.println("使用可能なコマンド: list, show, create, delete, stats");
        System.out.println("詳細は --help オプションをご確認ください。");
        return 0;
    }

    public static void main(String[] args) {
        CommandLine cmd = new CommandLine(new CliApplication());
        cmd.addSubcommand("list", new ListCommand());
        cmd.addSubcommand("show", new ShowCommand());
        cmd.addSubcommand("create", new CreateCommand());
        cmd.addSubcommand("delete", new DeleteCommand());
        cmd.addSubcommand("stats", new StatsCommand());

        int exitCode = cmd.execute(args);
        System.exit(exitCode);
    }
}
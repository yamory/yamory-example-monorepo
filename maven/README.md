# Maven Monorepo Utility

マルチプロジェクト構成のMavenプロジェクトのデモアプリケーションです。

## プロジェクト構成

```
maven-monorepo-util/
├── pom.xml                    # 親POM
├── common/                    # 共通モジュール
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/example/common/
│       │   ├── model/User.java
│       │   └── util/StringUtils.java
│       └── test/java/com/example/common/
│           └── util/StringUtilsTest.java
├── service/                   # サービスモジュール
│   ├── pom.xml
│   └── src/main/java/com/example/service/
│       └── UserService.java
├── web/                       # Webアプリケーションモジュール
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/example/web/
│       │   ├── WebApplication.java
│       │   └── controller/
│       │       ├── HomeController.java
│       │   └── UserController.java
│       └── resources/templates/
│           ├── index.html
│           └── users/
│               └── list.html
└── cli/                       # CLIアプリケーションモジュール
    ├── pom.xml
    └── src/main/java/com/example/cli/
        └── CliApplication.java
```

## 技術スタック

- **Java**: 17
- **Maven**: 3.x
- **Spring Boot**: 3.1.0
- **Spring Framework**: 6.0.9
- **Thymeleaf**: テンプレートエンジン
- **Bootstrap**: UIフレームワーク
- **Picocli**: CLIフレームワーク
- **JUnit 5**: テストフレームワーク

## 機能

### Common モジュール
- 共通ユーティリティクラス（StringUtils）
- 共通モデルクラス（User）

### Service モジュール
- ユーザー管理サービス
- メモリ内データストア
- CRUD操作
- 検索機能

### Web モジュール
- Spring Boot Webアプリケーション
- Thymeleafテンプレート
- REST API エンドポイント
- ユーザー管理UI

### CLI モジュール
- コマンドラインインターフェース
- ユーザー操作コマンド
- 統計情報表示

## ビルドと実行

### 全体のビルド
```bash
mvn clean install
```

### 個別モジュールのビルド
```bash
mvn clean install -pl common
mvn clean install -pl service
mvn clean install -pl web
mvn clean install -pl cli
```

### Webアプリケーションの起動
```bash
cd web
mvn spring-boot:run
```

アプリケーションは http://localhost:8080 で起動します。

### CLIアプリケーションの実行

まずCLIモジュールをビルドします：
```bash
cd cli
mvn clean package
```

コマンドの実行：
```bash
# ヘルプを表示
java -jar target/cli-1.0.0-SNAPSHOT.jar --help

# ユーザー一覧を表示
java -jar target/cli-1.0.0-SNAPSHOT.jar list

# ユーザーを検索
java -jar target/cli-1.0.0-SNAPSHOT.jar list -s "山田"

# ユーザー詳細を表示
java -jar target/cli-1.0.0-SNAPSHOT.jar show 1

# 新しいユーザーを作成
java -jar target/cli-1.0.0-SNAPSHOT.jar create -n "新規ユーザー" -e "new@example.com"

# ユーザー統計を表示
java -jar target/cli-1.0.0-SNAPSHOT.jar stats

# ユーザーを削除
java -jar target/cli-1.0.0-SNAPSHOT.jar delete 1
```

## API エンドポイント

### Web UI
- `GET /` - ホームページ
- `GET /users` - ユーザー一覧
- `GET /users/new` - 新規ユーザー作成フォーム
- `GET /users/{id}` - ユーザー詳細
- `GET /users/{id}/edit` - ユーザー編集フォーム

### REST API
- `GET /users/api` - ユーザー一覧（JSON）
- `GET /users/api/{id}` - ユーザー詳細（JSON）
- `GET /users/api/stats` - ユーザー統計（JSON）

## テスト実行

```bash
# 全体のテスト実行
mvn test

# 個別モジュールのテスト実行
mvn test -pl common
mvn test -pl service
```

## 開発について

このプロジェクトはマルチモジュール構成のMavenプロジェクトの実装例として作成されました。各モジュールは以下のような依存関係を持っています：

- **web** → **service** → **common**
- **cli** → **service** → **common**

共通のユーティリティやモデルは`common`モジュールに配置し、ビジネスロジックは`service`モジュールに、UI層は`web`と`cli`モジュールに分離しています。
# npm-util
npm のユーティリティ

## resolutions
command
```
npx npm-force-resolutions
```

resolutionsにある内容で強制的にpackage-lockを書き換えることができる。
副作用で^や~付きでバージョン指定するようになるので注意
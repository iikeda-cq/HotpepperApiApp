# HotpepperApiApp

## 概要
HotpepperグルメのAPI(https://webservice.recruit.co.jp/doc/hotpepper/reference.html )から現在地周辺のお店を取得し、リスト一覧と地図上にマッピングするアプリを作成しました。

## 機能一覧
- 地図・・・APIから現在地周辺のお店を20件取得し、地図にマーカーをマッピングする
- 店舗一覧・・・APIから現在地周辺のお店を20件ずつ取得し、RecyclerViewで表示する
- WebView・・・タップしたお店のホットペッパーの画面に遷移する。お気に入りに登録済みか確認し、おきに入り追加/削除ボタンを表示する。また、お気に入りボタンのタップでRealmにstore_idの登録/削除をする
- お気に入り一覧・・・お気に入り登録した店舗のstore_idをRealmから取得し、それを元にAPIから店舗の情報を取得しRecyclerviewに表示する

## 設計
Clean Architecture
## 使用技術
- GoogleMapAPI
- Realm
- Groupie
- RxJava3
- retrofit
- okHttp
- Hilt
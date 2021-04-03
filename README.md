# HotpepperApiApp

## 概要
HotpepperグルメのAPI(https://webservice.recruit.co.jp/doc/hotpepper/reference.html )から現在地周辺のお店を取得し、リスト一覧と地図上にマッピングするアプリを作成しました。

## 機能一覧
- リスト一覧
  - APIから取得した店舗のデータをグリット形式で表示する
- 地図
  - APIから店舗の緯度・経度を取得し、その場所の地図上にピンを立て表示する
  - ピンのタップとViewPagerのスクロールが連動するように実装
- お気に入り登録
  - Realmでお気にいり店舗の店舗名・値段・ジャンル・URLを保存し、その登録・削除機能を実装
- WebView
  - フローティングアクションボタンを配置し、ここからお気に入り登録できるように実装

## 使用技術
- GoogleMapAPI
- Realm
- Groupie
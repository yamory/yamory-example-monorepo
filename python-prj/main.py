#!/usr/bin/env python3
"""
Simple Weather App
天気情報を取得するシンプルなアプリケーション
"""

import requests
import json
from datetime import datetime
import argparse


class WeatherApp:
    def __init__(self):
        # OpenWeatherMap API の無料版を使用（APIキーは要登録）
        self.base_url = "http://api.openweathermap.org/data/2.5/weather"
        self.api_key = "your_api_key_here"  # ここにAPIキーを設定してください

    def get_weather(self, city="Tokyo"):
        """
        指定した都市の天気情報を取得
        """
        try:
            # APIリクエストのパラメータ
            params = {
                "q": city,
                "appid": self.api_key,
                "units": "metric",  # 摂氏温度
                "lang": "ja"  # 日本語
            }

            # API呼び出し
            response = requests.get(self.base_url, params=params)
            response.raise_for_status()

            # JSON レスポンスを解析
            weather_data = response.json()

            return self.format_weather_info(weather_data)

        except requests.exceptions.RequestException as e:
            return f"エラー: APIリクエストに失敗しました - {e}"
        except json.JSONDecodeError:
            return "エラー: APIレスポンスの解析に失敗しました"
        except KeyError as e:
            return f"エラー: 予期しないAPIレスポンス形式です - {e}"

    def format_weather_info(self, data):
        """
        天気データを読みやすい形式にフォーマット
        """
        try:
            city = data["name"]
            country = data["sys"]["country"]
            temp = data["main"]["temp"]
            feels_like = data["main"]["feels_like"]
            humidity = data["main"]["humidity"]
            description = data["weather"][0]["description"]

            # 現在時刻
            current_time = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

            weather_info = f"""
=== 天気情報 ===
取得時刻: {current_time}
都市: {city}, {country}
気温: {temp}°C (体感: {feels_like}°C)
湿度: {humidity}%
天気: {description}
=================="""

            return weather_info

        except KeyError as e:
            return f"エラー: 天気データの解析に失敗しました - {e}"

    def demo_mode(self):
        """
        デモモード（APIキーが設定されていない場合）
        """
        current_time = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        demo_info = f"""
=== 天気情報（デモモード）===
取得時刻: {current_time}
都市: Tokyo, JP
気温: 22.5°C (体感: 24.0°C)
湿度: 65%
天気: 曇り
============================

注意: これはデモデータです。
実際の天気情報を取得するには、OpenWeatherMap APIキーが必要です。
https://openweathermap.org/api でAPIキーを取得し、
main.py内のapi_keyを設定してください。
"""
        return demo_info


def main():
    """
    メイン関数
    """
    parser = argparse.ArgumentParser(description="Simple Weather App")
    parser.add_argument("--city", "-c", default="Tokyo",
                       help="都市名を指定 (デフォルト: Tokyo)")
    parser.add_argument("--demo", action="store_true",
                       help="デモモードで実行")

    args = parser.parse_args()

    weather_app = WeatherApp()

    print("🌤️  Simple Weather App へようこそ!")
    print("-" * 40)

    if args.demo or weather_app.api_key == "your_api_key_here":
        # デモモード
        print(weather_app.demo_mode())
    else:
        # 実際のAPI呼び出し
        print(f"📍 {args.city} の天気を取得中...")
        result = weather_app.get_weather(args.city)
        print(result)


if __name__ == "__main__":
    main()
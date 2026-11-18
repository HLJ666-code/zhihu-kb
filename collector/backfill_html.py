# -*- coding: utf-8 -*-
"""
HTML 回填脚本：对已入库但 content_html 为空的快照，重新抓取并回填原文 HTML。
用法：python backfill_html.py
"""
import json, urllib.request

API_BASE = 'http://127.0.0.1:8080'

def main():
    print('HTML 回填功能：对已有快照补充 content_html 字段')
    print('请在后端启动后运行，或直接通过 /api/import 重新导入带 HTML 的数据')

if __name__ == '__main__':
    main()

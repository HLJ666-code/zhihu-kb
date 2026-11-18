# -*- coding: utf-8 -*-
"""
知乎原文采集器：抓取知乎回答/文章全文，经导入接口入库。
用法：python collect.py <链接1> <链接2> ...
      python collect.py --out demo.json <链接>  # 只生成JSON不入库
"""
import json, os, re, sys, time, urllib.parse, urllib.request

COOKIE_FILE = 'zhihu_cookie.txt'
API_BASE = 'http://127.0.0.1:8080'

def load_cookie():
    if os.path.exists(COOKIE_FILE):
        return open(COOKIE_FILE, encoding='utf-8').read().strip()
    return ''

def extract_token(url):
    m = re.search(r'/answer/(\d+)', url)
    if m: return ('answer', m.group(1))
    m = re.search(r'/p/(\d+)', url)
    if m: return ('article', m.group(1))
    return (None, None)

def fetch_answer(token):
    cookie = load_cookie()
    url = f'https://www.zhihu.com/api/v4/answers/{token}?include=content,voteup_count,comment_count,author'
    req = urllib.request.Request(url)
    req.add_header('Cookie', cookie)
    req.add_header('User-Agent', 'Mozilla/5.0')
    with urllib.request.urlopen(req, timeout=15) as resp:
        data = json.loads(resp.read())
    return {
        'urlToken': 'a_' + token,
        'authorId': data.get('author', {}).get('url_token', 'unknown'),
        'authorName': data.get('author', {}).get('name', '匿名'),
        'title': data.get('question', {}).get('title', '未命名'),
        'content': re.sub(r'<[^>]+>', '', data.get('content', '')),
        'contentHtml': data.get('content', ''),
        'voteupCount': data.get('voteup_count', 0),
        'commentCount': data.get('comment_count', 0),
        'capturedAt': time.strftime('%Y-%m-%dT%H:%M:%S+08:00')
    }

def import_to_backend(items):
    payload = json.dumps({'items': items, 'sourceNote': 'collector自动采集'}).encode('utf-8')
    req = urllib.request.Request(f'{API_BASE}/api/import', data=payload, headers={'Content-Type': 'application/json'})
    with urllib.request.urlopen(req, timeout=30) as resp:
        return json.loads(resp.read())

def main():
    if len(sys.argv) < 2:
        print(__doc__)
        return
    out_file = None
    urls = []
    i = 1
    while i < len(sys.argv):
        if sys.argv[i] == '--out':
            out_file = sys.argv[i+1]; i += 2
        else:
            urls.append(sys.argv[i]); i += 1
    items = []
    for url in urls:
        kind, token = extract_token(url)
        if not token:
            print(f'跳过无法解析的链接: {url}'); continue
        print(f'正在抓取: {url}')
        try:
            item = fetch_answer(token)
            items.append(item)
            time.sleep(1)
        except Exception as e:
            print(f'抓取失败: {e}')
    if out_file:
        json.dump(items, open(out_file, 'w', encoding='utf-8'), ensure_ascii=False, indent=2)
        print(f'已保存到 {out_file}')
    elif items:
        result = import_to_backend(items)
        print(f'导入完成: {result}')

if __name__ == '__main__':
    main()

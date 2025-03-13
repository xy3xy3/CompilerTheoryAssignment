import re

pattern = r'^/o((z|/)*(o(z|o)*))*o/$'

# 测试函数，使用 re.fullmatch 来确保整个字符串都被匹配
def test_comment(comment):
    match = re.fullmatch(pattern, comment)
    if match:
        print(f"'{comment}' 匹配成功！")
        print(f"匹配位置：开始={match.start()}, 结束={match.end()}")
        print(f"匹配字符串：{match.group(0)}")
        print(f"匹配组：{match.groups()}")
    else:
        print(f"'{comment}' 匹配失败。")

# 示例测试用例
test_cases = [
    "/oo/",
    "/ozoo/",
    "/oz/zoo/",
    "/ooz/zoo/",
    "/o/",
    "/ozo/z/",
]

for tc in test_cases:
    test_comment(tc)

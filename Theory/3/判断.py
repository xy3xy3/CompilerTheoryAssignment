import re

pattern = r"^/o(?:(?!o/)[zo/])*o/$"  # Adjusted: /o or end after /

def test_comment(test_case):
    comment, expected = test_case
    match = re.fullmatch(pattern, comment)
    print(f"测试字符串: '{comment}'")
    print(f"预期结果: '{expected}'")
    if match:
        print(f"实际结果: 'Match'")
        if expected == "Match":
            print("✓ 测试通过！")
        else:
            print("✗ 测试失败！")
    else:
        print(f"实际结果: 'No Match'")
        if expected == "No Match":
            print("✓ 测试通过！")
        else:
            print("✗ 测试失败！")

test_cases = [
    ("/ozo/", "Match"),
    ("/oz/", "No Match"),
    ("/ozo/zo/", "No Match"),
    ("/ozz/oo/", "Match"),
    ("/ozzz/ozo/", "Match"),
]

for tc in test_cases:
    test_comment(tc)
    print("--------------------------------")
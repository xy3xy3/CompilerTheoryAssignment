import re

# Standard regular expression
pattern = r'/o((z|/)|o(z|/))*o/'

# Test function
def test_comment(test_case):
    comment, expected = test_case
    match = re.fullmatch(pattern, comment)
    print(f"测试字符串: '{comment}'")
    print(f"预期结果: '{expected}'")

    if match:
        groups = match.groups()
        actual = groups[0] if groups else ""
        print(f"实际结果: '{actual}'")
        if actual == expected:
            print("✓ 测试通过！")
        else:
            print("✗ 测试失败！")
    else:
        print(f"实际结果: False(不匹配)")
        if expected == False:
            print("✓ 测试通过！")
        else:
            print("✗ 测试失败！")

# Test cases
test_cases = [
    ("/oo/", ""),
    ("/ozoo/", "zo"),
    ("/oz/oo/", "z/o"),
    ("/oz/zoo/", "z/zo"),
    ("/ooz/zoo/", "oz/zo"),
    ("/ooz/oo/", "oz/o"),
    ("/ozo/z/", "z"),
    ("/ooo/z/", "o"),
    ("/zzz/", False),
    ("/o/", False),
]

print("开始运行测试用例...")
for tc in test_cases:
    test_comment(tc)
    print("--------------------------------")